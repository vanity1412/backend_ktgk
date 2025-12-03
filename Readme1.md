# Hướng dẫn nhanh cho nhóm (Readme1)

Mục tiêu file này: mô tả ngắn các yêu cầu UI (1–6) và giải thích chi tiết, dễ hiểu cho phần backend (số 7) — luồng hoạt động của code, các lớp chính, ví dụ request/response để các bạn nhóm khác dễ triển khai UI và thuyết trình.

## Yêu cầu UI (tóm tắt)
1. Khi mở app, nút "Bắt đầu" trên trang Intro:
   - Nếu user đã login (lưu JWT/flag trên client) → chuyển tới trang Main.
   - Nếu chưa login → chuyển tới trang Login.
   - Nếu chưa có tài khoản → đưa người dùng tới màn hình Register.

2. UI Đăng ký với OTP (6 chữ số):
   - Form nhập: username, phone, password, fullName (tùy chọn), address (tùy chọn), email (để nhận OTP).
   - Gửi POST `/api/auth/register-with-otp` với payload RegisterRequest.
   - Sau khi server gửi OTP tới email/phone, hiển thị màn hình nhập OTP (6 chữ số).
   - Gửi POST `/api/auth/otp-verify` để xác thực; nếu thành công, chuyển về màn hình Login.

3. UI Đăng nhập:
   - Form nhập usernameOrPhone và password.
   - Gọi POST `/api/auth/login`; server trả về `LoginResponse` có `token` (JWT).
   - Lưu token và thông tin user vào local storage/session của client.

4. UI Trang Main:
   - Khi vào Main, gọi API lấy thông tin user nếu cần (nếu JWT trong client, decode hoặc gọi endpoint user/profile nếu có) để hiển thị tên, vai trò, tier.

5. UI Hiển thị Categories (chéo ngang):
   - Gọi GET `/api/categories` để lấy danh sách category active.
   - Hiển thị theo hàng ngang (carousel hoặc danh sách ngang), thứ tự dựa trên `displayOrder`.

6. UI Hiển thị products theo category (grid):
   - Khi chọn category, hiển thị danh sách sản phẩm của category đó theo dạng grid.
   - Sắp xếp tăng dần theo giá bán (basePrice) trước khi hiển thị.
   - Nếu backend hiện chưa có endpoint lọc theo category thì UI có thể gọi GET `/api/drinks` rồi filter client-side bằng `categoryId` (hiện `DrinkDto` đã có `categoryId` và `categoryName`). Tuy nhiên tốt nhất là backend cung cấp API lọc/sort (mô tả cách thêm ở phần 7).

---

## Giải thích chi tiết cho API (số 7) — Luồng hoạt động và các lớp liên quan
Tôi sẽ mô tả theo bước thực tế (controller → service → repository → model), dễ đọc để các bạn trình bày trước nhóm.

### Các file / lớp chính mà bạn đã có (tham chiếu)
- Controller
  - `src/main/java/com/utetea/backend/controller/AuthController.java` — chứa các endpoint: `/api/auth/register`, `/api/auth/register-with-otp`, `/api/auth/otp-verify`, `/api/auth/resend-otp`, `/api/auth/login`.
  - `src/main/java/com/utetea/backend/controller/DrinkCategoryController.java` — GET `/api/categories`, GET `/api/categories/{id}`.
  - `src/main/java/com/utetea/backend/controller/DrinkController.java` — GET `/api/drinks`, `/api/drinks/page`, `/api/drinks/{id}`, `/api/drinks/search`.

- DTOs
  - `RegisterRequest`, `LoginRequest`, `LoginResponse`, `OtpRequest`, `DrinkDto`, `DrinkCategoryDto`, v.v.

- Service
  - `AuthService` (service xử lý register/login/OTP) — controller gọi service này để thực hiện logic.
  - `DrinkService`, `DrinkCategoryService` — xử lý truy vấn DB và mapping sang DTO.

- Repository & Model
  - `User` (model), `Drink`, `DrinkCategory`, các repository tương ứng (Spring Data JPA) — thao tác với DB.

- Cấu hình Email/Security
  - `EmailConfig.java` — cấu hình gửi email (JavaMailSender) để gửi OTP.
  - `security` package — có cấu hình liên quan tới JWT (xác thực/Authorization).

---

### Luồng chi tiết: Đăng ký với OTP (register-with-otp)
1. Client (UI) gửi POST `/api/auth/register-with-otp` với body kiểu `RegisterRequest`:
   {
     "username":"hung",
     "phone":"0123456789",
     "password":"123456",
     "email":"user@example.com"
   }

2. `AuthController.registerWithOtp(request)` nhận request và gọi `authService.registerWithOtp(request)`.

3. `AuthService.registerWithOtp()` (luồng chuẩn, thường thực hiện):
   - Kiểm tra dữ liệu hợp lệ (username/phone/email không trùng).
   - Tạo một entity User mới với trạng thái chưa active (enabled = false) và mã hóa password (BCryptPasswordEncoder).
   - Lưu user vào DB bằng `userRepository.save(user)`.
   - Sinh mã OTP 6 chữ số, tạo entity `Otp` (chứa phone/email, otp, expiry timestamp, retry count...) và lưu vào `otpRepository`.
   - Gọi `mailService.sendOtp(email, otp)` hoặc `smsService.sendOtp(phone, otp)` (dùng `EmailConfig` + JavaMailSender để gửi email). Log các bước để debug.
   - Trả về void hoặc thông báo success.

4. Client nhận response "OTP đã được gửi thành công", hiển thị màn hình nhập OTP.

5. Khi user nhập OTP, UI gửi POST `/api/auth/otp-verify` với `OtpRequest`:
   {
     "email":"user@example.com",
     "otp":"123456"
   }

6. `AuthController.verifyOtp()` gọi `authService.verifyOtpAndActivateByEmail(email, otp)` (hoặc theo phone):
   - Service tìm bản ghi `Otp` theo email/phone.
   - Kiểm tra: mã OTP khớp && chưa expired.
   - Nếu ok: set `user.setEnabled(true)` (kích hoạt account), xóa hoặc đánh dấu OTP đã dùng.
   - Nếu không ok: ném BusinessException hoặc trả lỗi rõ ràng (expired/invalid).
   - Trả về thông báo success. Client chuyển về màn hình Login.

Edge cases bạn nên nêu khi giải thích:
- OTP hết hạn: trả lỗi rõ ràng, UI nên hiển thị "OTP hết hạn, gửi lại".
- OTP nhập sai nhiều lần: giới hạn lượt thử, block tạm thời.
- Nếu email/phone trùng tài khoản khác: trả lỗi ngay khi register.

---

### Luồng chi tiết: Đăng nhập (login)
1. Client gửi POST `/api/auth/login` với `LoginRequest`:
   { "usernameOrPhone":"hung", "password":"123456" }

2. `AuthController.login()` gọi `authService.login(request)`.

3. `AuthService.login()` làm những bước sau:
   - Dùng `AuthenticationManager` (Spring Security) hoặc tự kiểm tra user/password bằng `userRepository.findByUsernameOrPhone(...)` + `passwordEncoder.matches()`.
   - Nếu xác thực thành công: tạo JWT token (thời hạn, claims như userId, role) bằng component tạo token (ví dụ `JwtTokenProvider` hoặc `JwtUtil`).
   - Build `LoginResponse` chứa: id, username, phone, fullName, role, memberTier, token.
   - Trả về `LoginResponse`.

4. Controller trả `ApiResponse.success("Login successful", LoginResponse)` — Client lưu `token` và sử dụng Authorization: Bearer <token> cho các API yêu cầu.

Security notes (để giải thích):
- Password lưu dưới dạng hash (BCrypt) — KHÔNG lưu password thuần.
- JWT lưu trên client; server dùng filter bảo vệ endpoint (check header Authorization).

---

### Luồng: Lấy Categories & Drinks (các API hiện có)
- Categories:
  - GET `/api/categories` → `DrinkCategoryController.getAllActiveCategories()` → gọi `DrinkCategoryService.getAllActiveCategories()` → repo `findByIsActiveTrueOrderByDisplayOrderAsc()` → map entity -> `DrinkCategoryDto`.
  - Kết quả trả về là danh sách category đã active, theo `displayOrder`.

- Drinks:
  - GET `/api/drinks` → `DrinkController.getAllDrinks()` → `drinkService.getAllActiveDrinks()` → `drinkRepository.findByIsActiveTrue()` → map -> `DrinkDto`.
  - `DrinkDto` đã chứa `categoryId` và `categoryName` (xem `DrinkService.mapToDto`), nên client có thể lọc theo category Id client-side.
  - Ngoài ra có endpoint paging `/api/drinks/page` và `/api/drinks/search?keyword=...`.

Cách mở rộng (để UI #6 hoạt động tốt):
- Tốt nhất backend thêm endpoint: GET `/api/drinks` hỗ trợ query params: `categoryId`, `sort=price,asc`.
  - Implementation: trong `DrinkRepository` thêm method `List<Drink> findByCategoryIdAndIsActiveTrueOrderByBasePriceAsc(Long categoryId);`
  - Trong `DrinkService` thêm method `getDrinksByCategorySorted(Long categoryId)` gọi repo trên và map sang DTO.
  - Trong `DrinkController` thêm handler `@GetMapping(params = "categoryId")` hoặc `@GetMapping("/by-category")` để trả về danh sách.

Ví dụ response (grid): mỗi item `DrinkDto` có: id, name, imageUrl, basePrice, categoryId, categoryName.

---

### Ví dụ request / response (gọn để đọc nhanh)
- Register-with-OTP (POST /api/auth/register-with-otp)
  Request (JSON):
  {
    "username":"hung",
    "phone":"0123456789",
    "password":"123456",
    "email":"user@example.com"
  }
  Response (200):
  {
    "success": true,
    "message": "OTP đã được gửi thành công!",
    "data": "Vui lòng kiểm tra email: user@example.com"
  }

- Verify OTP (POST /api/auth/otp-verify)
  Request:
  { "email":"user@example.com", "otp":"123456" }
  Response (200): success message → UI quay về Login.

- Login (POST /api/auth/login)
  Request: { "usernameOrPhone":"hung", "password":"123456" }
  Response data (LoginResponse):
  {
    "id": 12,
    "username":"hung",
    "phone":"0123456789",
    "fullName":"Nguyễn Hùng",
    "address":"...",
    "role": "USER",
    "memberTier": "BRONZE",
    "token": "eyJhbGciOi..."
  }

- Get categories (GET /api/categories) → trả list `DrinkCategoryDto`.
- Get drinks by category (nếu backend đã mở rộng): GET `/api/drinks?categoryId=3&sort=price,asc` → trả list `DrinkDto` sắp xếp tăng dần theo basePrice.

---

### Lưu ý để giải thích khi thuyết trình (ngắn gọn, dễ nhớ)
- Mô hình dòng dữ liệu: Controller (REST endpoint) → Service (business logic, validation, transaction) → Repository (DB) → Entity/DTO → Controller trả DTO cho client.
- OTP: tách thành entity riêng (OTP + expiry), không lưu OTP trên user. Sau xác thực, mới bật enabled cho user.
- Bảo mật: password hash + JWT token. JWT cấp 1 lần cho client để gọi API.
- Error handling: controller/service ném exception cụ thể (ResourceNotFoundException, BusinessException) → trả ApiResponse.error(msg).
- Tính mở rộng: nếu UI cần filter/sort lớn, tạo các query/repository method hoặc dùng Specification/Criteria.

---

## Kết luận / Ghi chú triển khai nhanh
- Bạn đã làm xong phần API (7) — file `AuthController`, `DrinkController`, `DrinkCategoryController`, `DrinkService` đều có sẵn và có thể dùng ngay cho UI 1–6.
- Điểm cần bổ sung nếu muốn hoàn thiện UI 6 (sản phẩm theo category sắp tăng dần theo giá): thêm endpoint filter/sort hoặc dùng dữ liệu hiện có và filter client-side (khuyến nghị: backend thêm phương thức repo `findByCategoryIdAndIsActiveTrueOrderByBasePriceAsc`).

Nếu bạn muốn, tôi có thể:
- 1) tạo mẫu API call (cURL / Postman collection) cho register/otp/login/categories/drinks;
- 2) hoặc bổ sung endpoint backend `GET /api/drinks?categoryId=...&sort=price,asc` (tạo repo + service + controller method) và test nhanh.

---

Files tham khảo trong project (để mở khi giải thích):
- `src/main/java/com/utetea/backend/controller/AuthController.java`
- `src/main/java/com/utetea/backend/dto/RegisterRequest.java`
- `src/main/java/com/utetea/backend/dto/OtpRequest.java`
- `src/main/java/com/utetea/backend/dto/LoginRequest.java`
- `src/main/java/com/utetea/backend/dto/LoginResponse.java`
- `src/main/java/com/utetea/backend/controller/DrinkCategoryController.java`
- `src/main/java/com/utetea/backend/controller/DrinkController.java`
- `src/main/java/com/utetea/backend/service/DrinkService.java`
- `src/main/java/com/utetea/backend/config/EmailConfig.java`

Chúc nhóm thuyết trình tốt! Nếu cần, tôi sẽ bổ sung tài liệu ngắn dạng slide hoặc các ví dụ HTTP cụ thể để trình bày trong lớp.
