# API Documentation

## Endpoint-uri pentru Utilizator

### Înregistrare Utilizator
- **Endpoint:** `POST /api/auth/register`
- **Descriere:** Creează un nou utilizator în sistem
- **Roluri:** Acces public

### Autentificare Utilizator
- **Endpoint:** `POST /api/auth/login`
- **Descriere:** Autentifică un utilizator și generează un token JWT.
- **Roluri:** Acces public

### Adăugare Produs
- **Endpoint:** `POST /api/admin/products`
- **Descriere:** Adaugă un nou produs
- **Roluri:** ADMIN.

### Ștergere Produs după ID
- **Endpoint:** `DELETE /api/admin/products/{id}`
- **Descriere:** Șterge un produs existent după ID
- **Roluri:** ADMIN

### Ștergere Toate Produsele
- **Endpoint:** `DELETE /api/admin/products`
- **Descriere:** Șterge toate produsele din sistem
- **Roluri:** ADMIN

### Obținere Detalii Produs
- **Endpoint:** `GET /api/products/{id}`
- **Descriere:** Obține detalii despre un produs după ID
- **Roluri:** USER, ADMIN

### Listare Produse
- **Endpoint:** `GET /api/products`
- **Descriere:** Obține toate produsele
- **Roluri:** USER, ADMIN

- ## Endpoint-uri pentru Comandă

### Create Comandă
- **Endpoint:** `POST /api/orders`
- **Descriere:** Plasează o nouă comandă
- **Roluri:** USER

### Obținere Detalii Comandă
- **Endpoint:** `GET /api/orders/{id}`
- **Descriere:** Obține detalii despre o comandă după ID
- **Roluri:** USER, ADMIN (utilizatorul care a creat comanda sau un administrator)

### Listare Comenzi ale Utilizatorului
- **Endpoint:** `GET /api/orders/user/{userId}`
- **Descriere:** Obține lista comenzilor unui utilizator
- **Roluri:** USER, ADMIN (utilizatorul care a creat comanda sau un administrator)

### Listare Toate Comenzile
- **Endpoint:** `GET /api/orders`
- **Descriere:** Obține lista tuturor comenzilor
- **Roluri:** ADMIN

### Ștergere Comandă după ID
- **Endpoint:** `DELETE /api/orders/{id}`
- **Descriere:** Șterge o comandă existentă după ID
- **Roluri:** ADMIN, USER (utilizatorul care a creat comanda sau un administrator)

## Endpoint-uri pentru Articol

### Adăugare Articol în Comandă
- **Endpoint:** `POST /api/orders/{orderId}/items`
- **Descriere:** Adaugă un articol într-o comandă existentă
- **Roluri:** USER, ADMIN

### Actualizare Articol în Comandă
- **Endpoint:** `PUT /api/orders/{orderId}/items/{itemId}`
- **Descriere:** Actualizează un articol dintr-o comandă existentă
- **Roluri:** USER, ADMIN

### Ștergere Articol din Comandă
- **Endpoint:** `DELETE /api/orders/{orderId}/items/{itemId}`
- **Descriere:** Șterge un articol dintr-o comandă existentă
- **Roluri:** USER, ADMIN

### Obținere Detalii Articol în Comandă
- **Endpoint:** `GET /api/orders/{orderId}/items/{itemId}`
- **Descriere:** Obține detalii despre un articol specific într-o comandă
- **Roluri:** USER, ADMIN



