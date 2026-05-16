
Write-Host "1. Obtendo token admin..." -ForegroundColor Green

$ADMIN_TOKEN = curl -s -X POST "http://localhost:8180/auth/realms/master/protocol/openid-connect/token" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "username=admin" `
  -d "password=admin" `
  -d "grant_type=password" `
  -d "client_id=admin-cli" | ConvertFrom-Json | Select-Object -ExpandProperty access_token

Write-Host "Token obtido: $($ADMIN_TOKEN.Substring(0, 20))..." -ForegroundColor Cyan
Write-Host "`n2. Criando realm 'newsvi'..." -ForegroundColor Green

curl -s -X POST "http://localhost:8180/auth/admin/realms" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{"realm":"newsvi","enabled":true}' | Out-Null

Write-Host "✓ Realm criado" -ForegroundColor Cyan
Write-Host "`n3. Criando roles..." -ForegroundColor Green

curl -s -X POST "http://localhost:8180/auth/admin/realms/newsvi/roles" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{"name":"CLIENTE"}' | Out-Null

Write-Host "✓ Role CLIENTE criada" -ForegroundColor Cyan

curl -s -X POST "http://localhost:8180/auth/admin/realms/newsvi/roles" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{"name":"ADMIN"}' | Out-Null

Write-Host "✓ Role ADMIN criada" -ForegroundColor Cyan
Write-Host "`n4. Criando cliente 'newsvi-api'..." -ForegroundColor Green

curl -s -X POST "http://localhost:8180/auth/admin/realms/newsvi/clients" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{
    "clientId":"newsvi-api",
    "enabled":true,
    "publicClient":false,
    "directAccessGrantsEnabled":true,
    "serviceAccountsEnabled":true,
    "protocol":"openid-connect",
    "redirectUris":["http://localhost:8080/*"],
    "webOrigins":["http://localhost:8080"]
  }' | Out-Null

Write-Host "✓ Cliente criado" -ForegroundColor Cyan
Write-Host "`n5. Obtendo ID do cliente..." -ForegroundColor Green

$CLIENT_ID = curl -s -X GET "http://localhost:8180/auth/admin/realms/newsvi/clients?clientId=newsvi-api" `
  -H "Authorization: Bearer $ADMIN_TOKEN" | ConvertFrom-Json | Select-Object -ExpandProperty id

Write-Host "Client ID: $CLIENT_ID" -ForegroundColor Cyan
Write-Host "`n6. Obtendo client secret..." -ForegroundColor Green

$CLIENT_SECRET_RESPONSE = curl -s -X GET "http://localhost:8180/auth/admin/realms/newsvi/clients/$CLIENT_ID/client-secret" `
  -H "Authorization: Bearer $ADMIN_TOKEN" | ConvertFrom-Json

$CLIENT_SECRET = $CLIENT_SECRET_RESPONSE.value

Write-Host "✓ Client Secret obtido" -ForegroundColor Cyan
Write-Host "SECRET: $CLIENT_SECRET" -ForegroundColor Yellow
Write-Host "`n7. Criando usuário 'cliente@teste.com'..." -ForegroundColor Green

curl -s -X POST "http://localhost:8180/auth/admin/realms/newsvi/users" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{"username":"cliente@teste.com","email":"cliente@teste.com","enabled":true}' | Out-Null

Write-Host "✓ Usuário cliente criado" -ForegroundColor Cyan
Write-Host "`n8. Criando usuário 'admin@teste.com'..." -ForegroundColor Green

curl -s -X POST "http://localhost:8180/auth/admin/realms/newsvi/users" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{"username":"admin@teste.com","email":"admin@teste.com","enabled":true}' | Out-Null

Write-Host "✓ Usuário admin criado" -ForegroundColor Cyan
Write-Host "`n9. Definindo senha do usuário cliente..." -ForegroundColor Green

$USER_CLIENTE = curl -s -X GET "http://localhost:8180/auth/admin/realms/newsvi/users?username=cliente@teste.com" `
  -H "Authorization: Bearer $ADMIN_TOKEN" | ConvertFrom-Json | Select-Object -ExpandProperty id

curl -s -X PUT "http://localhost:8180/auth/admin/realms/newsvi/users/$USER_CLIENTE/reset-password" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{"type":"password","value":"SenhaForte123!","temporary":false}' | Out-Null

Write-Host "✓ Senha definida: SenhaForte123!" -ForegroundColor Cyan
Write-Host "`n10. Definindo senha do usuário admin..." -ForegroundColor Green

$USER_ADMIN = curl -s -X GET "http://localhost:8180/auth/admin/realms/newsvi/users?username=admin@teste.com" `
  -H "Authorization: Bearer $ADMIN_TOKEN" | ConvertFrom-Json | Select-Object -ExpandProperty id

curl -s -X PUT "http://localhost:8180/auth/admin/realms/newsvi/users/$USER_ADMIN/reset-password" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{"type":"password","value":"AdminForte123!","temporary":false}' | Out-Null

Write-Host "✓ Senha definida: AdminForte123!" -ForegroundColor Cyan
Write-Host "`n11. Atribuindo role CLIENTE..." -ForegroundColor Green

$ROLE_CLIENTE = curl -s -X GET "http://localhost:8180/auth/admin/realms/newsvi/roles/CLIENTE" `
  -H "Authorization: Bearer $ADMIN_TOKEN" | ConvertFrom-Json

curl -s -X POST "http://localhost:8180/auth/admin/realms/newsvi/users/$USER_CLIENTE/role-mappings/realm" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d "[$($ROLE_CLIENTE | ConvertTo-Json)]" | Out-Null

Write-Host "✓ Role CLIENTE atribuída" -ForegroundColor Cyan
Write-Host "`n12. Atribuindo role ADMIN..." -ForegroundColor Green

$ROLE_ADMIN = curl -s -X GET "http://localhost:8180/auth/admin/realms/newsvi/roles/ADMIN" `
  -H "Authorization: Bearer $ADMIN_TOKEN" | ConvertFrom-Json

curl -s -X POST "http://localhost:8180/auth/admin/realms/newsvi/users/$USER_ADMIN/role-mappings/realm" `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d "[$($ROLE_ADMIN | ConvertTo-Json)]" | Out-Null

Write-Host "✓ Role ADMIN atribuída" -ForegroundColor Cyan
Write-Host "`n`n========== SETUP CONCLUÍDO ==========`n" -ForegroundColor Green

Write-Host "Credenciais para autenticação:" -ForegroundColor Yellow
Write-Host "
Usuário 1 (CLIENTE):
  - Login: cliente@teste.com
  - Senha: SenhaForte123!

Usuário 2 (ADMIN):
  - Login: admin@teste.com
  - Senha: AdminForte123!

Cliente OIDC:
  - ID: newsvi-api
  - Secret: $CLIENT_SECRET

Próximo passo:
  1. Atualize em application.properties:
     quarkus.oidc.credentials.secret=$CLIENT_SECRET
  
  2. Reinicie o Quarkus (Ctrl+C e execute novamente)
  
  3. Obtenha um token com:
     curl -X POST 'http://localhost:8180/auth/realms/newsvi/protocol/openid-connect/token' \
       -H 'Content-Type: application/x-www-form-urlencoded' \
       -d 'client_id=newsvi-api' \
       -d 'client_secret=$CLIENT_SECRET' \
       -d 'grant_type=password' \
       -d 'username=cliente@teste.com' \
       -d 'password=SenhaForte123!'
"
