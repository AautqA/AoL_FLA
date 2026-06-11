# DineReserve Backend

Backend Java untuk aplikasi DineReserve. Implementasi ini menggunakan Java standar dengan `HttpServer` bawaan JDK, sehingga tidak butuh Maven atau dependency eksternal.

## Arsitektur

- Presentation Layer: `controller/`
- Business Layer: `service/`
- Data Access Layer: `repository/`
- Domain Model: `domain/`
- Utilities: `util/`
- Design Patterns:
  - Singleton: `InMemoryDatabase`
  - State: `service/state/ReservationState*`

## Menjalankan Backend

```bash
cd /Users/delzaabigailsuryono/FLA/backend
mkdir -p out
find src/main/java -name "*.java" | xargs javac -d out
java -cp out com.dinereserve.DineReserveApplication
```

Backend berjalan di `http://localhost:5001`.

Root endpoint `/` juga mengembalikan respons JSON sederhana supaya membuka host backend langsung di browser tidak terlihat sebagai 404.

## Endpoint

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/tables`
- `GET /api/tables/all`
- `POST /api/tables`
- `PUT /api/tables/{id}`
- `DELETE /api/tables/{id}`
- `GET /api/tables/{id}/availability`
- `POST /api/reservations`
- `PUT /api/reservations/{id}/cancel`
- `GET /api/reservations/user/{userId}`
- `GET /api/reservations`
- `GET /api/reservations/{id}`

## Dummy Data

Seed data sudah disiapkan di `InMemoryDatabase`:
- 2 users
- 6 tables
- sample reservations

### Kredensial Dummy

- Customer: `customer@example.com` / `password123`
- Admin: `admin@example.com` / `admin123`

## Note Design Pattern

- Singleton dipakai untuk in-memory data store.
- State pattern dipakai untuk status reservation.
- Layered architecture mengikuti PDF.
