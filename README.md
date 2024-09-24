# Mobile Banking API

## Introduction

Welcome to the Mobile Banking API! This project provides a robust API for managing users, accounts, and cards in a mobile banking application. The API is designed to facilitate financial transactions securely and efficiently, ensuring a smooth user experience.

## Features

- **User Management**: Create, update, retrieve, and delete user accounts.
- **Account Management**: Manage user accounts, including updating aliases and hiding accounts.
- **Card Management**: Handle card operations such as freezing and unfreezing cards.

## API Endpoints

### User Controller

- **Create User**
  - `POST /api/v1/users`
  
- **Get All Users**
  - `GET /api/v1/users`
  
- **Get User by ID**
  - `GET /api/v1/users/userId/{id}`
  
- **Get User by Email**
  - `GET /api/v1/users/email/{email}`
  
- **Update User Name**
  - `PUT /api/v1/users/update-name`
  
- **Delete User by Phone Number**
  - `DELETE /api/v1/users/phoneNumber/{phoneNumber}`

### Card Controller

- **Create Card**
  - `POST /api/v1/cards`
  
- **Get All Cards**
  - `GET /api/v1/cards`
  
- **Get Card by Number**
  - `GET /api/v1/cards/cardNumber/{cardNumber}`
  
- **Freeze Card**
  - `PUT /api/v1/cards/freeze-card/{cardNumber}`
  
- **Unfreeze Card**
  - `PUT /api/v1/cards/unfreeze-card/{cardNumber}`
  
- **Delete Card**
  - `DELETE /api/v1/cards/delete-card/{cardNumber}`

### Account Controller

- **Create Account**
  - `POST /api/v1/accounts`
  
- **Get All Accounts**
  - `GET /api/v1/accounts`
  
- **Get Account by Number**
  - `GET /api/v1/accounts/actNo/{actNo}`
  
- **Get Account by Phone Number**
  - `GET /api/v1/accounts/phoneNumber/{phoneNumber}`
  
- **Update Account Alias**
  - `PUT /api/v1/accounts/update-alias`
  
- **Hide Account**
  - `PUT /api/v1/accounts/hide-account/{actNo}`
  
- **Delete Account**
  - `DELETE /api/v1/accounts/delete-account/{actNo}`

## Getting Started

### Prerequisites

- JDK 21
- Gradle
- PostgreSQL
- docker for database server and deployment on local and cloud

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/mobile-banking-api.git
2. runt the project
3. Test on post or on swagger
4. swagger link
   ```bash
   http://localhost:8080/swagger-ui/index.html
