# 🔐 Setup Keycloak para Autenticação

## 📋 Pré-requisitos

- Java 17+ instalado
- Docker (opcional, para rodas Keycloak em container)
- Servidor Quarkus rodando em `http://localhost:8080`

---

## 🚀 Passo 1: Instalar e Rodar Keycloak (porta 8180)

### Opção A: Standalone (Recomendado para desenvolvimento)

```bash
# 1. Download Keycloak
# Acesse: https://www.keycloak.org/downloads
# Baixe a versão "Standalone"

# 2. Extraia o arquivo
unzip keycloak-XX.X.X.zip
cd keycloak-XX.X.X

# 3. Inicie o servidor (Linux/Mac)
bin/kc.sh start-dev --http-port=8180

# 3. Inicie o servidor (Windows)
bin/kc.bat start-dev --http-port=8180
```

**Acesse:** `http://localhost:8180` (você verá a página de admin)

---

### Opção B: Docker

```bash
docker run -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest \
  start-dev
```

---

## 🛠️ Passo 2: Criar Realm "newsvi"

1. Acesse: `http://localhost:8180/admin`
2. Login com credenciais padrão:
   - **Username:** `admin`
   - **Password:** `admin`

3. No canto superior esquerdo, clique em "Master" → **"Create Realm"**
4. Preencha:
   - **Realm name:** `newsvi`
   - Clique **"Create"**

---

## 👥 Passo 3: Criar Usuários

1. No realm `newsvi`, vá para **"Users"** → **"Create new user"**
2. Crie pelo menos 2 usuários:

### Usuário 1 (Cliente)
- **Username:** `cliente@teste.com`
- **Email:** `cliente@teste.com`
- **First name:** `Cliente`
- **Last name:** `Teste`
- Clique **"Create"**
- Vá para aba **"Credentials"** → **"Set password"** → `SenhaForte123!`
- **User Details** → **"Add role mapping"** → Selecione `CLIENTE`

### Usuário 2 (Admin)
- **Username:** `admin@teste.com`
- **Email:** `admin@teste.com`
- **First name:** `Admin`
- **Last name:** `Teste`
- Clique **"Create"**
- Vá para aba **"Credentials"** → **"Set password"** → `AdminForte123!`
- **User Details** → **"Add role mapping"** → Selecione `ADMIN`

---

## 🔑 Passo 4: Criar Cliente OIDC

1. No realm `newsvi`, vá para **"Clients"** → **"Create client"**
2. Preencha:
   - **Client ID:** `newsvi-api`
   - Clique **"Next"**

3. Em **"Capability config":**
   - ✅ **Client authentication:** ON
   - ✅ **Authorization:** ON
   - Clique **"Next"**

4. Em **"Login settings":**
   - **Valid redirect URIs:** `http://localhost:8080/*`
   - **Web origins:** `http://localhost:8080`
   - Clique **"Save"**

5. Vá para aba **"Credentials"**:
   - Copie o **Client secret** (ex: `abcd1234efgh5678`)

---

## 🎯 Passo 5: Criar Roles

1. No realm `newsvi`, vá para **"Roles"** → **"Create role"**
2. Crie 2 roles:

### Role 1
- **Role name:** `CLIENTE`
- Clique **"Create"**

### Role 2
- **Role name:** `ADMIN`
- Clique **"Create"**

---

## ⚙️ Passo 6: Atualizar `application.properties`

No seu servidor Quarkus, atualize:

```properties
quarkus.oidc.auth-server-url=http://localhost:8180/auth/realms/newsvi
quarkus.oidc.client-id=newsvi-api
quarkus.oidc.credentials.secret=<COLE_O_CLIENT_SECRET_AQUI>
```

**Exemplo:**
```properties
quarkus.oidc.credentials.secret=abcd1234efgh5678ijkl9012mnop3456
```

---

## 🚀 Passo 7: Rodar o Servidor Quarkus

```bash
./mvnw compile:quarkus:dev
```

O servidor estará em: `http://localhost:8080`

---

## 🔗 Novo Fluxo de Autenticação

### 1️⃣ **Obter Token do Keycloak**

```bash
curl -X POST http://localhost:8180/auth/realms/newsvi/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=newsvi-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=password" \
  -d "username=cliente@teste.com" \
  -d "password=SenhaForte123!"
```

✅ **Resposta:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "token_type": "Bearer",
  ...
}
```

---

### 2️⃣ **Usar Token no Servidor Quarkus**

Copie o `access_token` e use em suas requisições:

```bash
# Obter perfil do usuário
curl -X GET http://localhost:8080/usuarios/me \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."

# Listar meus pedidos
curl -X GET http://localhost:8080/pedidos/meus \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."

# Adicionar aos desejos
curl -X POST http://localhost:8080/desejos \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{"filmeId": 1}'
```

---

## 🧪 Endpoints Testáveis

### Sem autenticação
- `POST /auth/register` - Registrar novo usuário
- `POST /auth/register/completo` - Registro com endereço

### Com autenticação (CLIENTE)
- `GET /auth/me` - Perfil do usuário
- `GET /usuarios/me` - Dados do usuário
- `GET /pedidos/meus` - Seus pedidos
- `POST /desejos` - Adicionar aos desejos
- `GET /desejos` - Listar desejos

### Com autenticação (ADMIN)
- `GET /pedidos` - Listar todos os pedidos
- `PUT /pedidos/{id}/status` - Alterar status do pedido

---

## 🐛 Troubleshooting

### Erro: "Unable to connect to Keycloak"
- ✅ Keycloak está rodando em `http://localhost:8180`?
- ✅ URL em `application.properties` está correta?

### Erro: "Invalid client secret"
- ✅ Você atualizou `application.properties` com o secret correto?
- ✅ Copiou exatamente do Keycloak (Admin → Clients → newsvi-api → Credentials)?

### Token expirado
- Obtenha um novo token usando o fluxo acima
- Ou use `refresh_token` para renovar:

```bash
curl -X POST http://localhost:8180/auth/realms/newsvi/protocol/openid-connect/token \
  -d "client_id=newsvi-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=refresh_token" \
  -d "refresh_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 📚 Referências

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Quarkus OIDC Guide](https://quarkus.io/guides/security-oidc-code-flow-authentication)
- [OpenID Connect Flow](https://openid.net/connect/)

