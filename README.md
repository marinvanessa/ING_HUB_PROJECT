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
