# Employee Management System

Aplikasi CRUD untuk pendataan karyawan dengan sistem penempatan cabang menggunakan Spring Boot Java 11.

## Fitur Utama

- **Manajemen Cabang (Branch)**
  - Create, Read, Update, Delete operasi untuk cabang
  - Pencarian cabang berdasarkan nama
  - Validasi untuk mencegah duplikasi kode cabang
  - Informasi jumlah karyawan per cabang

- **Manajemen Karyawan (Employee)**
  - Create, Read, Update, Delete operasi untuk karyawan
  - Penempatan karyawan ke cabang
  - Pencarian karyawan berdasarkan nama dan posisi
  - Validasi untuk mencegah duplikasi kode karyawan dan email
  - Informasi lengkap karyawan dengan detail cabang

## Teknologi yang Digunakan

- **Java 11**
- **Spring Boot 2.x**
- **Spring Data JPA**
- **H2 Database** (In-Memory untuk development)
- **Maven** sebagai build tool
- **Bean Validation** untuk validasi input
- **SLF4J** untuk logging

## Struktur Database

### Tabel Branches
- `id` (Primary Key)
- `code` (Unique, Required)
- `name` (Required)
- `address`
- `phone_number`
- `created_at`
- `updated_at`

### Tabel Employees
- `id` (Primary Key)
- `employee_code` (Unique, Required)
- `first_name` (Required)
- `last_name` (Required)
- `email` (Unique)
- `phone_number`
- `hire_date`
- `position`
- `address`
- `branch_id` (Foreign Key to Branches)
- `created_at`
- `updated_at`

## API Endpoints

### Branch APIs
- `GET /api/branches` - Mendapatkan semua cabang
- `GET /api/branches/{id}` - Mendapatkan cabang berdasarkan ID
- `GET /api/branches/code/{code}` - Mendapatkan cabang berdasarkan kode
- `POST /api/branches` - Membuat cabang baru
- `PUT /api/branches/{id}` - Update cabang
- `DELETE /api/branches/{id}` - Menghapus cabang
- `GET /api/branches/search?name={name}` - Pencarian cabang berdasarkan nama

### Employee APIs
- `GET /api/employees` - Mendapatkan semua karyawan
- `GET /api/employees/{id}` - Mendapatkan karyawan berdasarkan ID
- `GET /api/employees/code/{code}` - Mendapatkan karyawan berdasarkan kode
- `GET /api/employees/branch/{branchId}` - Mendapatkan karyawan berdasarkan cabang
- `POST /api/employees` - Membuat karyawan baru
- `PUT /api/employees/{id}` - Update karyawan
- `DELETE /api/employees/{id}` - Menghapus karyawan
- `GET /api/employees/search/name?name={name}` - Pencarian karyawan berdasarkan nama
- `GET /api/employees/search/position?position={position}` - Pencarian karyawan berdasarkan posisi

## Cara Menjalankan Aplikasi

### Prasyarat
- Java 11 atau lebih tinggi
- Maven 3.6 atau lebih tinggi

### Langkah-langkah
1. Clone repository ini
2. Masuk ke direktori project
3. Jalankan perintah: `mvn spring-boot:run`
4. Aplikasi akan berjalan di `http://localhost:8080`

### Mengakses H2 Database Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: `password`

## Contoh Data Sample

Aplikasi dilengkapi dengan data sample yang akan dimuat otomatis saat startup:

### Cabang Sample
- HO (Head Office)
- JKT01 (Jakarta Branch 1)
- BDG01 (Bandung Branch 1)
- SBY01 (Surabaya Branch 1)

### Karyawan Sample
- 8 karyawan dengan berbagai posisi dan penempatan cabang

## Testing API

Anda dapat menggunakan tools seperti Postman atau curl untuk testing API endpoints.

### Contoh Request

#### Membuat Cabang Baru
```bash
curl -X POST http://localhost:8080/api/branches \
  -H "Content-Type: application/json" \
  -d '{
    "code": "YGY01",
    "name": "Yogyakarta Branch 1",
    "address": "Jl. Malioboro No. 100, Yogyakarta",
    "phoneNumber": "0274-12345678"
  }'
```

#### Membuat Karyawan Baru
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP009",
    "firstName": "Muhammad",
    "lastName": "Rahman",
    "email": "muhammad.rahman@company.com",
    "phoneNumber": "081234567899",
    "hireDate": "2024-01-15",
    "position": "Software Engineer",
    "address": "Jl. Sudirman No. 999, Jakarta",
    "branchId": 1
  }'
```

## Logging

Aplikasi menggunakan SLF4J untuk logging dengan level INFO untuk aplikasi dan DEBUG untuk SQL queries.

## Error Handling

Aplikasi memiliki global exception handler yang menangani:
- Resource Not Found (404)
- Data Conflict (409)
- Validation Errors (400)
- Internal Server Error (500)

## Arsitektur

Aplikasi menggunakan layered architecture:
- **Controller Layer**: REST API endpoints
- **Service Layer**: Business logic
- **Repository Layer**: Data access
- **Entity Layer**: Database entities
- **DTO Layer**: Data transfer objects
