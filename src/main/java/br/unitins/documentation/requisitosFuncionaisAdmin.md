# DOCUMENTO DE REQUISITOS FUNCIONAIS
## Sistema de Cinema - Parte Administrativa

| **Versão** | **Data** | **Autor** |
|------------|----------|-----------|
| 1.0 | Abril/2026 | Bernardo dos Santos Graça |

---

## 1. INTRODUÇÃO

### 1.1 Objetivo

Este documento tem como objetivo descrever os requisitos funcionais para a parte administrativa de um sistema de cinema, abrangendo o gerenciamento de filmes, atores, gêneros, salas, poltronas, sessões e prêmios.

### 1.2 Escopo

O sistema permitirá que um administrador realize operações de CRUD (Create, Read, Update, Delete) e buscas em todos os módulos listados.

### 1.3 Definições e Siglas

| Sigla | Significado |
|-------|-------------|
| RF | Requisito Funcional |
| CRUD | Create, Read, Update, Delete |

---

## 2. REQUISITOS FUNCIONAIS

### 2.1 Módulo: Filmes

| ID | Descrição |
|----|-----------|
| RF01 | Cadastrar filme com nome, duração, sinopse, idioma original, ano de lançamento, classificação indicativa, URL do poster e URL do trailer |
| RF02 | Associar um ou mais gêneros a um filme |
| RF03 | Associar um ou mais atores a um filme |
| RF04 | Associar um ou mais prêmios a um filme |
| RF05 | Editar os dados de um filme |
| RF06 | Excluir um filme |
| RF07 | Listar todos os filmes |
| RF08 | Buscar filme por ID |
| RF09 | Buscar filmes por nome |
| RF10 | Buscar filmes por gênero |
| RF11 | Buscar filmes por ator |
| RF12 | Buscar filmes por faixa de duração |
| RF13 | Converter automaticamente a duração (String) para minutos ao salvar |

---

### 2.2 Módulo: Atores

| ID | Descrição |
|----|-----------|
| RF14 | Cadastrar ator com nome |
| RF15 | Associar um ou mais prêmios a um ator |
| RF16 | Editar os dados de um ator |
| RF17 | Excluir um ator |
| RF18 | Listar todos os atores |
| RF19 | Buscar ator por ID |
| RF20 | Buscar atores por nome |
| RF21 | Buscar atores por prêmio |

---

### 2.3 Módulo: Gêneros

| ID | Descrição |
|----|-----------|
| RF22 | Cadastrar gênero com nome |
| RF23 | Editar os dados de um gênero |
| RF24 | Excluir um gênero |
| RF25 | Listar todos os gêneros |
| RF26 | Buscar gênero por ID |
| RF27 | Buscar gêneros por nome |

---

### 2.4 Módulo: Salas

| ID | Descrição |
|----|-----------|
| RF28 | Cadastrar sala com capacidade total |
| RF29 | Associar uma ou mais poltronas a uma sala |
| RF30 | Editar os dados de uma sala |
| RF31 | Excluir uma sala |
| RF32 | Listar todas as salas |
| RF33 | Buscar sala por ID |

---

### 2.5 Módulo: Poltronas

| ID | Descrição |
|----|-----------|
| RF34 | Cadastrar poltrona com código e disponibilidade |
| RF35 | Editar os dados de uma poltrona |
| RF36 | Excluir uma poltrona |
| RF37 | Listar todas as poltronas |
| RF38 | Buscar poltrona por ID |
| RF39 | Buscar poltronas por código |
| RF40 | Buscar poltronas por disponibilidade |

---

### 2.6 Módulo: Sessões

| ID | Descrição |
|----|-----------|
| RF41 | Cadastrar sessão com horário de início, horário de fim, preço, capacidade total, capacidade disponível, filme, tipo e status |
| RF42 | Associar uma ou mais salas a uma sessão |
| RF43 | Editar os dados de uma sessão |
| RF44 | Excluir uma sessão |
| RF45 | Listar todas as sessões |
| RF46 | Buscar sessão por ID |
| RF47 | Definir o status da sessão |

---

### 2.7 Módulo: Prêmios

| ID | Descrição |
|----|-----------|
| RF48 | Cadastrar prêmio com nome, ano e categoria |
| RF49 | Editar os dados de um prêmio |
| RF50 | Excluir um prêmio |
| RF51 | Listar todos os prêmios |
| RF52 | Buscar prêmio por ID |
| RF53 | Buscar prêmios por nome |
| RF54 | Buscar prêmios por categoria |

---

## 3. RESUMO GERAL

| Módulo | Quantidade de RFs |
|--------|-------------------|
| Filmes | 13 |
| Atores | 8 |
| Gêneros | 6 |
| Salas | 6 |
| Poltronas | 7 |
| Sessões | 7 |
| Prêmios | 7 |
| **TOTAL** | **54** |

---

## 4. CONSIDERAÇÕES FINAIS

### 4.1 Fora do Escopo

Os seguintes itens NÃO fazem parte deste documento (parte administrativa):

- Cadastro de usuários/clientes
- Autenticação e login
- Venda de ingressos
- Processamento de pagamento
- Geração de QR Code para ingressos
- Histórico de compras do usuário
- Validações (ex: ano não futuro, preço maior que zero) - estes são Requisitos Não Funcionais

---

## 5. HISTÓRICO DE REVISÕES

| Versão | Data | Autor | Descrição |
|--------|------|-------|------------|
| 1.0 | Abril/2026 | Bernardo dos Santos Graça | Versão inicial dos requisitos funcionais da parte administrativa |

---

**Fim do documento**