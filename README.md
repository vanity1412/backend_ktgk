# 🍵 UTE TEA BACKEND API, VŨ VĂN THÔNG 23162098

> Backend API cho ứng dụng đặt trà sữa UTE Tea - Đồ án Lập trình Di động

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---
## 🧪 Testing

### Test với Swagger UI

1. Mở http://localhost:8080/swagger-ui.html
2. Login để lấy token
3. Click **Authorize**, nhập: `Bearer <token>`
4. Test các endpoints

## 📋 Mục lục

1. [Giới thiệu](#-giới-thiệu)
2. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
3. [Kiến trúc Project](#-kiến-trúc-project)
4. [Cấu trúc Database](#-cấu-trúc-database)
5. [Cài đặt & Chạy](#-cài-đặt--chạy)
6. [API Endpoints](#-api-endpoints)
7. [Tính năng](#-tính-năng)
8. [Tài liệu](#-tài-liệu)
9. [Team](#-team)

---

## 🎯 Giới thiệu

**UTE Tea Backend** là REST API server cho ứng dụng đặt trà sữa trực tuyến, phục vụ sinh viên và cộng đồng UTE. Hệ thống hỗ trợ:

- 🔐 Xác thực người dùng với JWT
- 🥤 Quản lý menu 16+ món nước với 4 categories
- 🛒 Đặt hàng online (Delivery/Pickup)
- 🎟️ Hệ thống mã giảm giá
- 👨‍💼 Dashboard quản lý cho Manager
- 📊 Thống kê doanh thu và đơn hàng

---

## 🚀 Công nghệ sử dụng

### Backend Framework
- **Java 17** - Programming language
- **Spring Boot 3.5.7** - Application framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database ORM
- **Hibernate** - JPA implementation

### Database
- **MySQL 8.0** - Relational database
- **Aiven Cloud MySQL** - Cloud database (production)

### Security & Authentication
- **JWT (JSON Web Token)** - Stateless authentication
- **BCrypt** - Password hashing

### Documentation
- **Swagger/OpenAPI 3.0** - API documentation
- **SpringDoc** - Swagger integration

### Build & Dependencies
- **Maven** - Build tool & dependency management
- **Lombok** - Reduce boilerplate code

### Other Libraries
- **Jakarta Validation** - Input validation
- **Jackson** - JSON serialization

---

## 🏗️ Kiến trúc Project

### Layered Architecture (MVC Pattern)

```
┌─────────────────────────────────────────┐
│         Controller Layer                │  ← REST API Endpoints
│  (AuthController, DrinkController, ...) │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          Service Layer                  │  ← Business Logic
│  (AuthService, DrinkService, ...)      │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│        Repository Layer                 │  ← Data Access
│  (UserRepository, DrinkRepository, ...) │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Database (MySQL)              │  ← Data Storage
└─────────────────────────────────────────┘
```

### Cấu trúc thư mục

```
src/main/java/com/utetea/backend/
├── 📁 config/              # Cấu hình (Security, Swagger, CORS)
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── WebConfig.java
│
├── 📁 controller/          # REST API Controllers
│   ├── AuthController.java          # Đăng ký, đăng nhập
│   ├── DrinkController.java         # Xem menu
│   ├── DrinkCategoryController.java # Xem categories
│   ├── OrderController.java         # Đặt hàng
│   ├── StoreController.java         # Xem cửa hàng
│   ├── PromotionController.java     # Mã giảm giá
│   ├── ManagerController.java       # Dashboard manager
│   ├── AdminDrinkController.java    # Quản lý món
│   └── AdminCategoryController.java # Quản lý categories
│
├── 📁 service/             # Business Logic
│   ├── AuthService.java
│   ├── DrinkService.java
│   ├── OrderService.java
│   ├── ManagerService.java
│   └── ...
│
├── 📁 repository/          # Data Access Layer (JPA)
│   ├── UserRepository.java
│   ├── DrinkRepository.java
│   ├── OrderRepository.java
│   └── ...
│
├── 📁 model/               # Entity Models (Database Tables)
│   ├── User.java
│   ├── Drink.java
│   ├── DrinkCategory.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── ...
│
├── 📁 dto/                 # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── DrinkDto.java
│   ├── OrderDto.java
│   └── ...
│
├── 📁 security/            # Security & JWT
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   ├── SecurityConfig.java
│   └── CustomUserDetailsService.java
│
├── 📁 exception/           # Exception Handling
│   ├── GlobalExceptionHandler.java
│   ├── BusinessException.java
│   └── ResourceNotFoundException.java
│
└── 📁 mapper/              # Entity ↔ DTO Mappers
    ├── DrinkMapper.java
    ├── OrderMapper.java
    └── ...
```

---

## 💾 Cấu trúc Database

### Entity Relationship Diagram (ERD)

```
┌──────────────┐
│    users     │
└──────┬───────┘
       │ 1
       │
       │ N
┌──────▼───────┐      N ┌──────────────┐
│   orders     │◄───────┤  promotions  │
└──────┬───────┘        └──────────────┘
       │ 1
       │        N ┌──────────────┐
       │ N      ┌─┤    stores    │
┌──────▼───────┐│ └──────────────┘
│ order_items  ││
└──────┬───────┘│
       │ 1      │
       │        │
       │ N      │
┌──────▼────────▼──┐
│ order_item_      │
│   toppings       │
└──────────────────┘

┌──────────────────┐
│ drink_categories │
└────────┬─────────┘
         │ 1
         │
         │ N
    ┌────▼─────┐
    │  drinks  │
    └────┬─────┘
         │ 1
    ┌────┴─────┬────────────┐
    │ N        │ N          │
┌───▼──────┐ ┌─▼──────────┐│
│drink_    │ │drink_      ││
│sizes     │ │toppings    ││
└──────────┘ └────────────┘│
                           │
                           └─► order_items
```

### Các bảng chính

| Bảng | Mô tả | Số records |
|------|-------|------------|
| `users` | Người dùng (USER, MANAGER) | 4 |
| `drink_categories` | Loại đồ uống | 4 |
| `drinks` | Món nước | 16 |
| `drink_sizes` | Size món (M, L, Jumbo) | ~40 |
| `drink_toppings` | Topping | ~10 |
| `stores` | Cửa hàng | 2 |
| `promotions` | Mã giảm giá | 3 |
| `orders` | Đơn hàng | Dynamic |
| `order_items` | Chi tiết đơn | Dynamic |
| `order_item_toppings` | Topping trong đơn | Dynamic |

### Dữ liệu mẫu

#### Users (Tài khoản test)
```
Username: manager_ute     | Password: 123456 | Role: MANAGER
Username: ute_student_01  | Password: 123456 | Role: USER (BRONZE)
Username: ute_student_02  | Password: 123456 | Role: USER (SILVER)
Username: ute_student_03  | Password: 123456 | Role: USER (GOLD)
```

#### Categories
1. **Milk Tea** - Trà sữa Houjicha (4 món)
2. **Fruit Tea** - Trà trái cây (5 món)
3. **Macchiato** - Trà kem cheese (3 món)
4. **Special** - Đồ uống đặc biệt (4 món)

#### Promotions
- **STUDENT20**: Giảm 20% (đơn tối thiểu 50,000đ)
- **FREESHIPUTE**: Giảm 15,000đ ship (đơn tối thiểu 60,000đ)
- **COMBO4UTE**: Giảm 30,000đ (đơn tối thiểu 120,000đ)

---

## ⚙️ Cài đặt & Chạy

### Yêu cầu hệ thống

- ✅ Java JDK 17+
- ✅ Maven 3.6+ (hoặc dùng Maven Wrapper)
- ✅ MySQL 8.0+ (hoặc dùng cloud database)
- ✅ IDE: IntelliJ IDEA / Eclipse / VS Code (tùy chọn)

### Bước 1: Clone project

```bash
git clone <repository-url>
cd backend_utetea
```

### Bước 2: Cấu hình database

**Option A: Dùng Cloud Database (Đã setup sẵn)**

File `application.properties` đã cấu hình sẵn:
```properties
spring.datasource.url=jdbc:mysql://mysql-16b47c6b-phongtran080809-7c70.c.aivencloud.com:26260/LTDD_Thong
spring.datasource.username=avnadmin
spring.datasource.password=AVNS_Ix83Fzpvp1FUIgDMvry
```

✅ Không cần làm gì thêm!

**Option B: Dùng MySQL Local**

1. Tạo database:
```sql
CREATE DATABASE LTDD_Thongtesst CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Cập nhật `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/LTDD_Thongtesst
spring.datasource.username=root
spring.datasource.password=your_password
```

3. Import dữ liệu:
```bash
mysql -u root -p LTDD_Thongtesst < src/main/resources/data-ltdd.sql
```

### Bước 3: Build project

```bash
# Windows
.\mvnw.cmd clean install

# Mac/Linux
./mvnw clean install
```

### Bước 4: Chạy application

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

### Bước 5: Kiểm tra

Server chạy tại: **http://localhost:8080**

Test API:
- Browser: http://localhost:8080/api/drinks
- Swagger UI: http://localhost:8080/swagger-ui.html

---

## 📡 API Endpoints

### 🔓 Public Endpoints (Không cần authentication)

#### Authentication
```http
POST   /api/auth/register      # Đăng ký tài khoản
POST   /api/auth/login         # Đăng nhập
GET    /api/auth/health        # Health check
```

#### Drinks & Menu
```http
GET    /api/drinks             # Lấy tất cả món
GET    /api/drinks/{id}        # Chi tiết món
GET    /api/drinks/search      # Tìm kiếm món
GET    /api/drinks/page        # Phân trang
```

#### Categories
```http
GET    /api/categories         # Lấy tất cả categories
GET    /api/categories/{id}    # Chi tiết category
```

#### Stores
```http
GET    /api/stores             # Lấy tất cả cửa hàng
GET    /api/stores/{id}        # Chi tiết cửa hàng
GET    /api/stores/search      # Tìm kiếm cửa hàng
```

#### Promotions
```http
GET    /api/promotions         # Lấy tất cả mã giảm giá
GET    /api/promotions/validate # Kiểm tra mã hợp lệ
```

---

### 🔐 User Endpoints (Cần JWT token)

#### Orders
```http
POST   /api/orders             # Tạo đơn hàng mới
GET    /api/orders/user/{userId}         # Lịch sử đơn
GET    /api/orders/user/{userId}/current # Đơn hiện tại
GET    /api/orders/{orderId}   # Chi tiết đơn
```

---

### 👨‍💼 Manager Endpoints (Chỉ MANAGER)

#### Dashboard
```http
GET    /api/manager/summary    # Thống kê tổng quan
GET    /api/manager/orders     # Xem tất cả đơn hàng
GET    /api/manager/orders/{id} # Chi tiết đơn
PUT    /api/manager/orders/{id}/status # Cập nhật trạng thái
```

#### Quản lý Categories
```http
GET    /api/admin/categories   # Xem tất cả (bao gồm ẩn)
POST   /api/admin/categories   # Thêm category mới
PUT    /api/admin/categories/{id} # Sửa category
DELETE /api/admin/categories/{id} # Ẩn category
```

#### Quản lý Drinks
```http
POST   /api/admin/drinks       # Thêm món mới
PUT    /api/admin/drinks/{id}  # Sửa món
DELETE /api/admin/drinks/{id}  # Ẩn món
```

---

### 📝 Request/Response Examples

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrPhone": "ute_student_01",
  "password": "123456"
}
```

Response:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "id": 2,
    "username": "ute_student_01",
    "fullName": "Nguyen Thi A",
    "phone": "0909000001",
    "role": "USER",
    "memberTier": "BRONZE",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

#### Create Order
```http
POST /api/orders
Content-Type: application/json
Authorization: Bearer <token>

{
  "userId": 2,
  "storeId": 1,
  "type": "DELIVERY",
  "address": "KTX khu A, UTE",
  "paymentMethod": "COD",
  "promotionCode": "STUDENT20",
  "items": [
    {
      "drinkId": 1,
      "sizeName": "L",
      "quantity": 2,
      "note": "Ít đường",
      "toppings": [
        { "toppingName": "Trân châu đen" }
      ]
    }
  ]
}
```

---

## ✨ Tính năng

### 👤 User Features

- ✅ **Authentication**
  - Đăng ký tài khoản mới
  - Đăng nhập với username/phone
  - JWT token authentication
  - Password hashing với BCrypt

- ✅ **Menu & Drinks**
  - Xem 16 món nước theo 4 categories
  - Tìm kiếm món theo tên
  - Xem chi tiết món (giá, mô tả, ảnh)
  - Chọn size (M, L, Jumbo)
  - Chọn topping (6+ loại)

- ✅ **Ordering**
  - Đặt hàng online
  - Chọn loại: Delivery hoặc Pickup
  - Áp dụng mã giảm giá
  - Xem lịch sử đơn hàng
  - Theo dõi trạng thái đơn

- ✅ **Stores**
  - Xem 2 cửa hàng UTE
  - Xem địa chỉ, giờ mở cửa
  - Tìm kiếm cửa hàng gần nhất

- ✅ **Promotions**
  - Xem mã giảm giá có sẵn
  - Kiểm tra mã hợp lệ
  - Tự động tính discount

### 👨‍💼 Manager Features

- ✅ **Dashboard**
  - Thống kê tổng quan (doanh thu, đơn hàng)
  - Xem tất cả đơn hàng
  - Lọc theo trạng thái, ngày
  - Cập nhật trạng thái đơn

- ✅ **Menu Management**
  - Thêm/sửa/xóa món nước
  - Quản lý categories
  - Cập nhật giá, mô tả
  - Ẩn/hiện món

- ✅ **Order Management**
  - Xem chi tiết đơn hàng
  - Cập nhật trạng thái:
    - PENDING → MAKING → SHIPPING → DONE
    - Hoặc CANCELED

---

## 🔒 Security

### Authentication Flow

```
1. User login → POST /api/auth/login
2. Server validates credentials
3. Server generates JWT token
4. Client stores token
5. Client sends token in header: Authorization: Bearer <token>
6. Server validates token for protected endpoints
```

### Password Security

- Passwords được hash bằng **BCrypt** (cost factor: 10)
- Không lưu plain text password
- Validation: minimum 6 characters

### JWT Configuration

```properties
jwt.secret=utetea-secret-key-for-jwt-token-generation-minimum-256-bits
jwt.expiration=86400000  # 24 hours
```

### CORS Configuration

- Cho phép tất cả origins (`*`) - Development only
- Production nên giới hạn specific domains

---

## 📚 Tài liệu

### Tài liệu chi tiết

1. **HUONG-DAN-CHAY-API.md** - Hướng dẫn chạy API từng bước
2. **DATABASE-GUIDE.md** - Hướng dẫn database chi tiết
3. **API-DOCUMENTATION.md** - Tài liệu API đầy đủ
4. **SETUP-INSTRUCTIONS.md** - Hướng dẫn setup
5. **QUICK-START.txt** - Quick start guide

### Swagger UI

Truy cập: **http://localhost:8080/swagger-ui.html**

- Xem tất cả API endpoints
- Test API trực tiếp trên browser
- Xem request/response schema
- Authorize với JWT token

### Postman Collection

Import OpenAPI spec từ: **http://localhost:8080/v3/api-docs**

---


### Test với cURL

```bash
# Health check
curl http://localhost:8080/api/auth/health

# Get drinks
curl http://localhost:8080/api/drinks

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrPhone":"ute_student_01","password":"123456"}'
```

### Test từ Android

```java
// Base URL
String BASE_URL = "http://192.168.1.100:8080/";

// Retrofit setup
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build();
```

**Lưu ý:** Thay `192.168.1.100` bằng IP máy tính của bạn

---

## 🐛 Troubleshooting

### Port 8080 đã được sử dụng

```properties
# Đổi port trong application.properties
server.port=8081
```

### Không kết nối được MySQL

- Kiểm tra MySQL service đã chạy
- Kiểm tra username/password
- Dùng cloud database (đã setup sẵn)

### Java version không đúng

```bash
java -version  # Phải >= 17
```

Cài Java 17: https://adoptium.net/

### Maven command not found

Dùng Maven Wrapper:
```bash
.\mvnw.cmd spring-boot:run  # Windows
./mvnw spring-boot:run      # Mac/Linux
```

---

## 📂 Assets & Images

### Cấu trúc folder ảnh

```
assets/drinks/
├── milk_tea/          # 4 món trà sữa
├── fruit_tea/         # 5 món trà trái cây
├── macchiato/         # 3 món macchiato
└── special/           # 4 món đặc biệt
```

### Truy cập ảnh

```
http://localhost:8080/assets/drinks/milk_tea/ute_houjicha_classic.png
http://localhost:8080/assets/drinks/fruit_tea/dao_hong_ute.png
```

---

## 🚀 Deployment

### Build JAR file

```bash
.\mvnw.cmd clean package
```

File JAR: `target/backend-0.0.1-SNAPSHOT.jar`

### Chạy JAR

```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Environment Variables

```bash
# Database
export DB_URL=jdbc:mysql://host:port/database
export DB_USERNAME=username
export DB_PASSWORD=password

# JWT
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000
```

---

## 👥 Team

**Đồ án Lập trình Di động - UTE Tea**

- Backend API: Spring Boot + MySQL
- Android App: Java/Kotlin
- Database: MySQL 8.0

---

## 📄 License

MIT License - Xem file [LICENSE](LICENSE) để biết thêm chi tiết

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- MySQL Documentation
- JWT.io
- Swagger/OpenAPI

---

## 📞 Contact & Support

Nếu gặp vấn đề hoặc có câu hỏi:

1. Đọc tài liệu trong folder `docs/`
2. Kiểm tra Troubleshooting section
3. Xem Swagger UI để test API
4. Kiểm tra logs trong console

---

**Happy Coding! 🎉**

*Last updated: November 27, 2025*
