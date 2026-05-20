# 🏪 CoreMarket

Sistema SaaS para gerenciamento de contas pendentes em mini mercados.

O CoreMarket foi criado para substituir o controle manual em cadernos, permitindo que mini mercados registrem clientes, compras, pagamentos e acompanhem saldos devedores de forma organizada e moderna.

---

# 🚀 Objetivo

Facilitar o controle de clientes que compram e pagam posteriormente, oferecendo uma solução simples, prática e acessível para pequenos mercados.

---

# ✨ Funcionalidades

## 👥 Clientes

* Cadastro de clientes
* Edição de clientes
* Exclusão de clientes
* Busca de clientes

---

## 🛒 Compras

* Registro de compras
* Adição de múltiplos itens
* Cálculo automático do valor total
* Histórico de compras

---

## 💰 Pagamentos

* Registro de pagamentos
* Atualização automática do saldo devedor
* Histórico de pagamentos

---

## 🏢 Multiempresa

* Cada mercado possui seus próprios dados
* Isolamento de clientes, compras e pagamentos
* Estrutura preparada para múltiplos usuários

---

# 🛠️ Tecnologias

## Backend

* Java
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Maven

---

## Frontend

* React
* TailwindCSS
* Axios
* React Router DOM

---

# 📂 Estrutura do Projeto

```text
src/main/java/com/natan/coremarket
 ├── application
 │    ├── dtos
 │    └── services
 │
 ├── domain
 │    ├── entities
 │    └── enums
 │
 ├── infrastructure
 │    ├── controllers
 │    └── repositories
 │
 └── config
```

---

# 🏗️ Entidades Principais

* Empresa
* Usuario
* Cliente
* Compra
* ItemCompra
* Pagamento

---

# 🎯 Objetivo do MVP

A primeira versão do sistema terá foco em:

* Cadastro de clientes
* Registro de compras
* Registro de pagamentos
* Controle de saldo devedor
* Histórico básico

---

# 📌 Status do Projeto

🚧 Em desenvolvimento

---

# 📖 Futuras Funcionalidades

* Autenticação JWT
* Dashboard financeiro
* Relatórios
* Integração com WhatsApp
* Controle de estoque
* Notificações
* Aplicativo mobile

---

# 👨‍💻 Autor

Desenvolvido por Natanael Rodrigues.
