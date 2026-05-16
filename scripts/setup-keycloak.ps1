# ============================================================
# SETUP KEYCLOAK 26.6.1 - Newsvi API
# Execute depois do Keycloak estar rodando em http://localhost:8180
# ============================================================

$ErrorActionPreference = "Stop"

$KeycloakUrl = "http://localhost:8180"
$AdminUser = "admin"
$AdminPass = "admin"

$Realm = "newsvi"
$ClientId = "newsvi-api"

$ClienteUser = "cliente@teste.com"
$ClientePass = "SenhaForte123!"

$AdminAppUser = "admin@teste.com"
$AdminAppPass = "AdminForte123!"

function Get-JsonHeaders {
  param([Parameter(Mandatory = $true)][string]$Token)

  return @{
    Authorization = "Bearer $Token"
  }
}

function Invoke-Keycloak {
  param(
    [Parameter(Mandatory = $true)][string]$Method,
    [Parameter(Mandatory = $true)][string]$Uri,
    [string]$Token,
    [object]$Body
  )

  $params = @{
    Method = $Method
    Uri = $Uri
  }

  if ($Token) {
    $params.Headers = Get-JsonHeaders -Token $Token
  }

  if ($null -ne $Body) {
    if ($Body -is [string]) {
      $params.Body = $Body
    } else {
      $params.Body = ($Body | ConvertTo-Json -Depth 20)
    }

    $params.ContentType = "application/json"
  }

  return Invoke-RestMethod @params
}

function Test-KeycloakExists {
  param(
    [Parameter(Mandatory = $true)][string]$Uri,
    [Parameter(Mandatory = $true)][string]$Token
  )

  try {
    Invoke-Keycloak -Method "GET" -Uri $Uri -Token $Token | Out-Null
    return $true
  } catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 404) {
      return $false
    }

    throw
  }
}

function Find-ByProperty {
  param(
    [object]$Items,
    [Parameter(Mandatory = $true)][string]$Property,
    [Parameter(Mandatory = $true)][string]$Value
  )

  foreach ($Item in $Items) {
    if ($Item -is [array]) {
      foreach ($NestedItem in $Item) {
        if ($NestedItem.$Property -eq $Value) {
          return $NestedItem
        }
      }
    } elseif ($Item.$Property -eq $Value) {
      return $Item
    }
  }

  return $null
}

Write-Host "Obtendo token admin..." -ForegroundColor Green

$AdminTokenResponse = Invoke-RestMethod `
  -Method POST `
  -Uri "$KeycloakUrl/realms/master/protocol/openid-connect/token" `
  -ContentType "application/x-www-form-urlencoded" `
  -Body @{
    username = $AdminUser
    password = $AdminPass
    grant_type = "password"
    client_id = "admin-cli"
  }

$AdminToken = $AdminTokenResponse.access_token

if (-not $AdminToken) {
  Write-Host "Erro: nao consegui obter token admin. Confira usuario/senha e se o Keycloak esta rodando." -ForegroundColor Red
  exit 1
}

Write-Host "Token admin obtido." -ForegroundColor Cyan

Write-Host "Verificando realm '$Realm'..." -ForegroundColor Green
$RealmExists = Test-KeycloakExists -Uri "$KeycloakUrl/admin/realms/$Realm" -Token $AdminToken

if (-not $RealmExists) {
  Invoke-Keycloak -Method "POST" -Uri "$KeycloakUrl/admin/realms" -Token $AdminToken -Body @{
    realm = $Realm
    enabled = $true
  } | Out-Null

  Write-Host "Realm criado." -ForegroundColor Cyan
} else {
  Write-Host "Realm ja existe." -ForegroundColor Cyan
}

Write-Host "Configurando tempo de expiracao dos tokens do realm..." -ForegroundColor Green
$RealmConfig = Invoke-Keycloak -Method "GET" -Uri "$KeycloakUrl/admin/realms/$Realm" -Token $AdminToken
$RealmConfig.accessTokenLifespan = 1800
$RealmConfig.ssoSessionIdleTimeout = 14400
$RealmConfig.ssoSessionMaxLifespan = 28800
Invoke-Keycloak -Method "PUT" -Uri "$KeycloakUrl/admin/realms/$Realm" -Token $AdminToken -Body $RealmConfig | Out-Null
Write-Host "Access token configurado para 30 minutos." -ForegroundColor Cyan

Write-Host "Criando roles se necessario..." -ForegroundColor Green

foreach ($RoleName in @("CLIENTE", "ADMIN")) {
  $RoleExists = Test-KeycloakExists -Uri "$KeycloakUrl/admin/realms/$Realm/roles/$RoleName" -Token $AdminToken

  if (-not $RoleExists) {
    Invoke-Keycloak -Method "POST" -Uri "$KeycloakUrl/admin/realms/$Realm/roles" -Token $AdminToken -Body @{
      name = $RoleName
    } | Out-Null

    Write-Host "Role $RoleName criada." -ForegroundColor Cyan
  } else {
    Write-Host "Role $RoleName ja existe." -ForegroundColor Cyan
  }
}

Write-Host "Verificando client '$ClientId'..." -ForegroundColor Green
$AllClients = Invoke-Keycloak -Method "GET" -Uri "$KeycloakUrl/admin/realms/$Realm/clients" -Token $AdminToken
$Client = Find-ByProperty -Items $AllClients -Property "clientId" -Value $ClientId

$ClientBody = @{
  clientId = $ClientId
  enabled = $true
  publicClient = $false
  clientAuthenticatorType = "client-secret"
  directAccessGrantsEnabled = $true
  serviceAccountsEnabled = $true
  standardFlowEnabled = $true
  protocol = "openid-connect"
  redirectUris = @("http://localhost:8080/*")
  webOrigins = @("http://localhost:8080")
}

if (-not $Client) {
  Invoke-Keycloak -Method "POST" -Uri "$KeycloakUrl/admin/realms/$Realm/clients" -Token $AdminToken -Body $ClientBody | Out-Null
  Write-Host "Client criado." -ForegroundColor Cyan
} else {
  Write-Host "Client ja existe." -ForegroundColor Cyan
}

$AllClients = Invoke-Keycloak -Method "GET" -Uri "$KeycloakUrl/admin/realms/$Realm/clients" -Token $AdminToken
$Client = Find-ByProperty -Items $AllClients -Property "clientId" -Value $ClientId

if (-not $Client) {
  Write-Host "Erro: o client '$ClientId' nao foi encontrado depois da criacao." -ForegroundColor Red
  exit 1
}

$ClientUuid = $Client.id

if (-not $ClientUuid) {
  Write-Host "Erro: nao consegui obter o UUID do client '$ClientId'." -ForegroundColor Red
  exit 1
}

Write-Host "Client UUID: $ClientUuid" -ForegroundColor Cyan

Write-Host "Verificando audience mapper..." -ForegroundColor Green
$MapperName = "$ClientId-audience"
$Mappers = Invoke-Keycloak -Method "GET" -Uri "$KeycloakUrl/admin/realms/$Realm/clients/$ClientUuid/protocol-mappers/models" -Token $AdminToken
$MapperExists = Find-ByProperty -Items $Mappers -Property "name" -Value $MapperName

if (-not $MapperExists) {
  Invoke-Keycloak -Method "POST" -Uri "$KeycloakUrl/admin/realms/$Realm/clients/$ClientUuid/protocol-mappers/models" -Token $AdminToken -Body @{
    name = $MapperName
    protocol = "openid-connect"
    protocolMapper = "oidc-audience-mapper"
    config = @{
      "included.client.audience" = $ClientId
      "access.token.claim" = "true"
      "id.token.claim" = "false"
    }
  } | Out-Null

  Write-Host "Audience mapper criado." -ForegroundColor Cyan
} else {
  Write-Host "Audience mapper ja existe." -ForegroundColor Cyan
}

Write-Host "Obtendo client secret..." -ForegroundColor Green
$ClientSecret = $null

try {
  $ClientSecret = (Invoke-Keycloak -Method "GET" -Uri "$KeycloakUrl/admin/realms/$Realm/clients/$ClientUuid/client-secret" -Token $AdminToken).value
} catch {
  $ClientSecret = $null
}

if (-not $ClientSecret) {
  Write-Host "Client secret vazio. Gerando novo secret..." -ForegroundColor Yellow
  $ClientSecret = (Invoke-Keycloak -Method "POST" -Uri "$KeycloakUrl/admin/realms/$Realm/clients/$ClientUuid/client-secret" -Token $AdminToken).value
}

if (-not $ClientSecret) {
  Write-Host "Erro: nao consegui obter o client secret." -ForegroundColor Red
  Write-Host "No Admin Console, abra Clients > newsvi-api > Settings e confira se Client authentication esta ON." -ForegroundColor Red
  exit 1
}

Write-Host "Criando usuarios se necessario..." -ForegroundColor Green

$UsersToCreate = @(
  @{
    Username = $ClienteUser
    Password = $ClientePass
    FirstName = "Cliente"
    LastName = "Teste"
    Role = "CLIENTE"
  },
  @{
    Username = $AdminAppUser
    Password = $AdminAppPass
    FirstName = "Admin"
    LastName = "Teste"
    Role = "ADMIN"
  }
)

foreach ($UserData in $UsersToCreate) {
  $Username = $UserData.Username
  $ExistingUsers = Invoke-Keycloak -Method "GET" -Uri "$KeycloakUrl/admin/realms/$Realm/users?username=$Username" -Token $AdminToken
  $ExistingUser = Find-ByProperty -Items $ExistingUsers -Property "username" -Value $Username

  if (-not $ExistingUser) {
    Invoke-Keycloak -Method "POST" -Uri "$KeycloakUrl/admin/realms/$Realm/users" -Token $AdminToken -Body @{
      username = $Username
      email = $Username
      firstName = $UserData.FirstName
      lastName = $UserData.LastName
      enabled = $true
      emailVerified = $true
    } | Out-Null

    Write-Host "Usuario $Username criado." -ForegroundColor Cyan
    $ExistingUsers = Invoke-Keycloak -Method "GET" -Uri "$KeycloakUrl/admin/realms/$Realm/users?username=$Username" -Token $AdminToken
    $ExistingUser = Find-ByProperty -Items $ExistingUsers -Property "username" -Value $Username
  } else {
    Write-Host "Usuario $Username ja existe." -ForegroundColor Cyan
  }

  $UserUuid = $ExistingUser.id

  Invoke-Keycloak -Method "PUT" -Uri "$KeycloakUrl/admin/realms/$Realm/users/$UserUuid/reset-password" -Token $AdminToken -Body @{
    type = "password"
    value = $UserData.Password
    temporary = $false
  } | Out-Null

  $Role = Invoke-Keycloak -Method "GET" -Uri "$KeycloakUrl/admin/realms/$Realm/roles/$($UserData.Role)" -Token $AdminToken
  $RoleMappingJson = "[{`"id`":`"$($Role.id)`",`"name`":`"$($Role.name)`"}]"

  Invoke-Keycloak -Method "POST" -Uri "$KeycloakUrl/admin/realms/$Realm/users/$UserUuid/role-mappings/realm" -Token $AdminToken -Body $RoleMappingJson | Out-Null
  Write-Host "Senha definida e role $($UserData.Role) atribuida para $Username." -ForegroundColor Cyan
}

Write-Host ""
Write-Host "========== SETUP CONCLUIDO ==========" -ForegroundColor Green
Write-Host ""
Write-Host "Usuario CLIENTE:"
Write-Host "  Login: $ClienteUser"
Write-Host "  Senha: $ClientePass"
Write-Host ""
Write-Host "Usuario ADMIN:"
Write-Host "  Login: $AdminAppUser"
Write-Host "  Senha: $AdminAppPass"
Write-Host ""
Write-Host "Cliente OIDC:"
Write-Host "  ID: $ClientId"
Write-Host "  Secret: $ClientSecret" -ForegroundColor Yellow
Write-Host ""
Write-Host "Atualize em src/main/resources/application.properties:"
Write-Host "quarkus.oidc.credentials.secret=$ClientSecret" -ForegroundColor Yellow
