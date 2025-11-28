# 📱 UTE TEA API DOCUMENTATION
## Tài liệu API cho Android App

**Base URL:** `http://localhost:8080` (development)  
**Base URL:** `http://YOUR_IP:8080` (test trên điện thoại)

---

## 🔐 1. AUTHENTICATION

### 1.1. Đăng ký
```
POST /api/auth/register
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "student123",
  "phone": "0909123456",
  "password": "123456",
  "fullName": "Nguyen Van A",
  "address": "KTX UTE, Thu Duc"
}
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "userId": 5,
    "username": "student123",
    "fullName": "Nguyen Van A",
    "phone": "0909123456",
    "role": "USER",
    "memberTier": "BRONZE"
  }
}
```

---

### 1.2. Đăng nhập
```
POST /api/auth/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "usernameOrPhone": "ute_student_01",
  "password": "123456"
}
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "userId": 2,
    "username": "ute_student_01",
    "fullName": "Nguyen Thi A",
    "phone": "0909000001",
    "role": "USER",
    "memberTier": "BRONZE"
  }
}
```

**Response Error (400):**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "data": null
}
```

---

## 🍵 2. CATEGORIES (Loại đồ uống)

### 2.1. Lấy danh sách categories
```
GET /api/categories
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Milk Tea",
      "description": "Trà sữa Houjicha đặc trưng của UTE Tea",
      "displayOrder": 1,
      "isActive": true
    },
    {
      "id": 2,
      "name": "Fruit Tea",
      "description": "Trà trái cây tươi mát, giàu vitamin",
      "displayOrder": 2,
      "isActive": true
    },
    {
      "id": 3,
      "name": "Macchiato",
      "description": "Trà kết hợp lớp kem cheese/macchiato béo mịn",
      "displayOrder": 3,
      "isActive": true
    },
    {
      "id": 4,
      "name": "Special",
      "description": "Đồ uống đặc biệt, đá xay, sống ảo",
      "displayOrder": 4,
      "isActive": true
    }
  ]
}
```

---

### 2.2. Lấy chi tiết category
```
GET /api/categories/{id}
```

**Example:** `GET /api/categories/1`

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "name": "Milk Tea",
    "description": "Trà sữa Houjicha đặc trưng của UTE Tea",
    "displayOrder": 1,
    "isActive": true
  }
}
```

---

## 🥤 3. DRINKS (Món nước)

### 3.1. Lấy tất cả món đang bán
```
GET /api/drinks
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "UTE Houjicha Classic",
      "description": "Trà sữa Houjicha đậm vị, thơm nhẹ, uống kèm trân châu hoặc topping tùy chọn.",
      "imageUrl": "/assets/drinks/milk_tea/ute_houjicha_classic.png",
      "basePrice": 29000.00,
      "isActive": true,
      "categoryId": 1,
      "categoryName": "Milk Tea",
      "sizes": [
        {
          "id": 1,
          "sizeName": "M",
          "extraPrice": 0.00
        },
        {
          "id": 2,
          "sizeName": "L",
          "extraPrice": 5000.00
        },
        {
          "id": 3,
          "sizeName": "Jumbo",
          "extraPrice": 10000.00
        }
      ],
      "toppings": [
        {
          "id": 1,
          "toppingName": "Trân châu đen",
          "price": 7000.00,
          "isActive": true
        },
        {
          "id": 2,
          "toppingName": "Trân châu trắng",
          "price": 8000.00,
          "isActive": true
        }
      ]
    }
  ]
}
```

---

### 3.2. Lấy món theo phân trang
```
GET /api/drinks/page?page=0&size=10
```

**Query Parameters:**
- `page`: Số trang (bắt đầu từ 0)
- `size`: Số món mỗi trang

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "content": [...],
    "totalElements": 16,
    "totalPages": 2,
    "size": 10,
    "number": 0
  }
}
```

---

### 3.3. Lấy chi tiết món
```
GET /api/drinks/{id}
```

**Example:** `GET /api/drinks/1`

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "name": "UTE Houjicha Classic",
    "description": "Trà sữa Houjicha đậm vị...",
    "imageUrl": "/assets/drinks/milk_tea/ute_houjicha_classic.png",
    "basePrice": 29000.00,
    "isActive": true,
    "categoryId": 1,
    "categoryName": "Milk Tea",
    "sizes": [...],
    "toppings": [...]
  }
}
```

---

### 3.4. Tìm kiếm món
```
GET /api/drinks/search?keyword=houjicha
```

**Query Parameters:**
- `keyword`: Từ khóa tìm kiếm

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "UTE Houjicha Classic",
      ...
    },
    {
      "id": 2,
      "name": "Houjicha Kem Cheese",
      ...
    }
  ]
}
```

---

## 🏪 4. STORES (Cửa hàng)

### 4.1. Lấy danh sách cửa hàng
```
GET /api/stores
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "storeName": "UTE Tea - Cơ sở 1",
      "address": "Số 1 Võ Văn Ngân, Thủ Đức, TP.HCM",
      "latitude": 10.8512345,
      "longitude": 106.7543210,
      "openTime": "08:00:00",
      "closeTime": "22:00:00",
      "phone": "0901 234 567"
    },
    {
      "id": 2,
      "storeName": "UTE Tea - Cơ sở 2",
      "address": "Khu KTX UTE, Linh Trung, Thủ Đức, TP.HCM",
      "latitude": 10.8723456,
      "longitude": 106.7732100,
      "openTime": "08:00:00",
      "closeTime": "22:30:00",
      "phone": "0902 345 678"
    }
  ]
}
```

---

### 4.2. Lấy chi tiết cửa hàng
```
GET /api/stores/{id}
```

**Example:** `GET /api/stores/1`

---

## 🎟️ 5. PROMOTIONS (Mã giảm giá)

### 5.1. Lấy danh sách mã giảm giá
```
GET /api/promotions
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "code": "STUDENT20",
      "description": "Giảm 20% cho sinh viên UTE",
      "discountType": "PERCENT",
      "discountValue": 20.00,
      "startDate": "2025-01-01T00:00:00",
      "endDate": "2025-12-31T23:59:59",
      "minOrderValue": 50000.00,
      "isActive": true
    }
  ]
}
```

---

### 5.2. Kiểm tra mã giảm giá
```
GET /api/promotions/validate?code=STUDENT20
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Promotion is valid",
  "data": {
    "id": 1,
    "code": "STUDENT20",
    "discountType": "PERCENT",
    "discountValue": 20.00,
    "minOrderValue": 50000.00
  }
}
```

---

## 🛒 6. ORDERS (Đơn hàng)

### 6.1. Tạo đơn hàng mới
```
POST /api/orders
Content-Type: application/json
```

**Request Body:**
```json
{
  "userId": 2,
  "storeId": 1,
  "type": "DELIVERY",
  "address": "KTX khu A, UTE, Thủ Đức",
  "pickupTime": null,
  "paymentMethod": "COD",
  "promotionCode": "STUDENT20",
  "items": [
    {
      "drinkId": 1,
      "sizeName": "L",
      "quantity": 2,
      "note": "Ít đường",
      "toppings": [
        {
          "toppingName": "Trân châu đen"
        }
      ]
    },
    {
      "drinkId": 5,
      "sizeName": "M",
      "quantity": 1,
      "note": "",
      "toppings": []
    }
  ]
}
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {
    "orderId": 1,
    "userId": 2,
    "storeId": 1,
    "type": "DELIVERY",
    "address": "KTX khu A, UTE, Thủ Đức",
    "status": "PENDING",
    "totalPrice": 114000.00,
    "discount": 22800.00,
    "finalPrice": 91200.00,
    "paymentMethod": "COD",
    "createdAt": "2025-11-27T18:30:00"
  }
}
```

---

### 6.2. Lấy lịch sử đơn hàng của user
```
GET /api/orders/user/{userId}
```

**Example:** `GET /api/orders/user/2`

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "orderId": 1,
      "storeName": "UTE Tea - Cơ sở 1",
      "type": "DELIVERY",
      "status": "DONE",
      "totalPrice": 114000.00,
      "discount": 22800.00,
      "finalPrice": 91200.00,
      "createdAt": "2025-11-25T10:30:00",
      "items": [
        {
          "drinkName": "UTE Houjicha Classic",
          "sizeName": "L",
          "quantity": 2,
          "itemPrice": 68000.00,
          "toppings": ["Trân châu đen"]
        }
      ]
    }
  ]
}
```

---

### 6.3. Lấy đơn hàng hiện tại (đang xử lý)
```
GET /api/orders/user/{userId}/current
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "orderId": 2,
    "status": "MAKING",
    "estimatedTime": "15 phút",
    ...
  }
}
```

---

### 6.4. Hủy đơn hàng
```
PUT /api/orders/{orderId}/cancel
```

**Example:** `PUT /api/orders/2/cancel`

**Response Success (200):**
```json
{
  "success": true,
  "message": "Order canceled successfully",
  "data": null
}
```

---

## 👨‍💼 7. ADMIN APIs (Chỉ MANAGER)

### 7.1. Quản lý Categories

#### Lấy tất cả categories (bao gồm ẩn)
```
GET /api/admin/categories
```

#### Thêm category mới
```
POST /api/admin/categories
Content-Type: application/json

{
  "name": "Smoothie",
  "description": "Sinh tố trái cây",
  "displayOrder": 5,
  "isActive": true
}
```

#### Sửa category
```
PUT /api/admin/categories/{id}
Content-Type: application/json

{
  "name": "Smoothie & Juice",
  "description": "Sinh tố và nước ép",
  "displayOrder": 5,
  "isActive": true
}
```

#### Ẩn category
```
DELETE /api/admin/categories/{id}
```

---

### 7.2. Quản lý Drinks

#### Lấy tất cả món (bao gồm ẩn)
```
GET /api/admin/drinks
```

#### Thêm món mới
```
POST /api/admin/drinks
Content-Type: application/json

{
  "name": "Trà Sữa Dâu Tây",
  "description": "Trà sữa vị dâu tây tươi",
  "imageUrl": "/assets/drinks/milk_tea/tra_sua_dau_tay.png",
  "basePrice": 48000,
  "categoryId": 1,
  "isActive": true,
  "sizes": [
    {"sizeName": "M", "extraPrice": 0},
    {"sizeName": "L", "extraPrice": 5000}
  ]
}
```

#### Sửa món
```
PUT /api/admin/drinks/{id}
Content-Type: application/json
```

#### Ẩn món
```
DELETE /api/admin/drinks/{id}
```

---

### 7.3. Quản lý Orders

#### Xem tất cả đơn hàng
```
GET /api/admin/orders
```

#### Xem đơn theo trạng thái
```
GET /api/admin/orders?status=PENDING
```

#### Cập nhật trạng thái đơn
```
PUT /api/orders/{orderId}/status
Content-Type: application/json

{
  "status": "MAKING"
}
```

**Trạng thái:** `PENDING` → `MAKING` → `SHIPPING` → `DONE` hoặc `CANCELED`

---

## 🖼️ 8. IMAGES (Ảnh)

### Lấy ảnh món
```
GET /assets/drinks/{category}/{filename}
```

**Examples:**
```
GET /assets/drinks/milk_tea/ute_houjicha_classic.png
GET /assets/drinks/fruit_tea/dao_hong_ute.png
GET /assets/drinks/macchiato/hong_tra_macchiato.png
GET /assets/drinks/special/ute_galaxy_tea.png
```

---

## 📊 9. RESPONSE FORMAT

### Success Response
```json
{
  "success": true,
  "message": "Success message",
  "data": { ... }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error message",
  "data": null
}
```

---

## 🔧 10. TESTING

### Test trên máy tính
```
Base URL: http://localhost:8080
```

### Test trên điện thoại (cùng WiFi)
```
1. Tìm IP máy tính: ipconfig (Windows) hoặc ifconfig (Mac/Linux)
2. Base URL: http://192.168.x.x:8080
3. Đảm bảo firewall cho phép port 8080
```

---

## 📝 11. SAMPLE DATA

### Users
```
username: ute_student_01, password: 123456, role: USER
username: ute_student_02, password: 123456, role: USER
username: ute_student_03, password: 123456, role: USER
username: manager_ute, password: 123456, role: MANAGER
```

### Promotions
```
STUDENT20 - Giảm 20% (min 50k)
FREESHIPUTE - Giảm 15k ship (min 60k)
COMBO4UTE - Giảm 30k (min 120k)
```

---

## 🚀 12. QUICK START

1. **Chạy backend:**
   ```bash
   .\mvnw.cmd spring-boot:run
   ```

2. **Test API:**
   ```bash
   curl http://localhost:8080/api/drinks
   ```

3. **Connect từ Android:**
   - Sử dụng Retrofit hoặc Volley
   - Base URL: `http://YOUR_IP:8080`
   - Thêm Internet permission trong AndroidManifest.xml

---

**UTE Tea Backend API** - Ready for Android App! 🎉
