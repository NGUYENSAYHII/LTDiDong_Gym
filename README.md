# GoGym – Ứng Dụng Theo Dõi Sức Khỏe

GoGym là ứng dụng di động về sức khỏe được xây dựng bằng Kotlin và Jetpack Compose.  
Ứng dụng hỗ trợ người dùng theo dõi bài tập, bữa ăn, lượng nước uống và tiến độ mục tiêu sức khỏe.  
Firebase được tích hợp để xác thực (bao gồm Đăng nhập bằng Google) và lưu trữ dữ liệu (Firestore).  
Ứng dụng hỗ trợ chủ đề sáng/tối, màu động (Android 12+) và bố cục responsive.

---
## [Link Fima](https://www.figma.com/design/G6wcqTWKfg53O3UEePpGsQ/ThietKeDiDong_Gym?node-id=0-1&t=Z4FfSD2pk4C87bQc-1)


---
## 1. Mô Tả Chung

GoGym cho phép người dùng:

- Ghi nhật ký bài tập, bữa ăn và lượng nước.
- Xem tóm tắt hàng ngày: calo đốt cháy, calo nạp vào, calo còn lại.
- Duyệt các bài tập theo danh mục với mô tả, hình minh họa và bộ đếm thời gian.
- Tạo kế hoạch tập luyện tùy chỉnh.
- Quản lý hồ sơ cá nhân và cài đặt.
- Đồng bộ dữ liệu theo thời gian thực thông qua Firebase.

---

## 2. Tính Năng

###  Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email & Password
- Đăng nhập bằng Google
- Quên mật khẩu

###  Bảng điều khiển trang chủ
- Mục tiêu calo hằng ngày
- Calo tiêu thụ / nạp vào
- Lượng nước uống
- Phân tích bữa ăn (sáng – trưa – tối – snack)

###  Theo dõi bài tập
- Danh mục: Bụng, Tay, Ngực, Chân, Lưng & Vai, Văn Phòng, Kickboxing
- Chi tiết bài tập: mô tả + ảnh + bộ đếm thời gian + calo
- Tạo bài tập tự động theo khu vực & thời lượng
- Lập kế hoạch tập luyện theo ngày trong tuần

###  Ghi nhật ký bữa ăn
- Thêm món ăn
- Tính calo & protein
- Tìm kiếm & đánh dấu yêu thích

###  Theo dõi nước uống
- Ghi nhận theo lít

###  Quản lý hồ sơ cá nhân
- Tuổi, cân nặng, chiều cao, mục tiêu
- Đổi ngôn ngữ (VI/EN)
- Đổi đơn vị đo (kg/lbs, cm/inch)

###  Báo cáo
- Lịch tập luyện
- Báo cáo theo ngày / tuần

---

## 3. Công Nghệ Sử Dụng

- **Kotlin**
- **Jetpack Compose (Material 3)**
- **Navigation Compose**
- **ViewModel, Coroutines, LiveData, StateFlow**
- **Firebase Authentication, Firestore**
- **Coil**
- **Google Sign-In API**
- **Kiến trúc: MVVM + Repository Pattern**

---

## 4. Cấu Trúc Dự Án

```
com.example.gogym
│
├── ui/theme
│   ├── Color.kt
│   ├── Type.kt
│   ├── Theme.kt
│   └── GoGymTheme.kt
│
├── model
│   ├── MealType.kt
│   ├── MealModels.kt
│   └── WorkoutAndWaterModels.kt
│
├── pages
│   ├── LoginPage.kt
│   ├── SignupPage.kt
│   ├── HomePage.kt
│   ├── AddFoodPage.kt
│   ├── WorkoutPage.kt
│   ├── WorkoutCategoryDetailPage.kt
│   ├── ExerciseDetailPage.kt
│   ├── OfficeWorkoutPage.kt
│   ├── AutoWorkoutPage.kt
│   ├── CustomPlanDetailPage.kt
│   ├── WorkoutReportPage.kt
│   ├── ProfilePage.kt
│   ├── SettingsPage.kt
│   ├── CustomizePage.kt
│   ├── MainTabsPage.kt
│   ├── MyAppNavigation.kt
│   └── NavHelpers.kt
│
└── data
    └── HomeRepository.kt
```

**Điểm vào chính:** `MyAppNavigation.kt`

---

## 5. Thiết Lập và Cài Đặt

### Yêu cầu
- Android Studio mới nhất
- Kotlin 1.9+
- Tạo Firebase Project
- File `google-services.json` trong thư mục **app/**

### Cách cài đặt

```
git clone <repository-url>
```

1. Mở bằng Android Studio  
2. Kết nối Firebase  
3. Bật Authentication (Email/Password & Google)  
4. Bật Firestore  
5. Đặt `google-services.json` vào `/app`  
6. Cập nhật `default_web_client_id` trong `strings.xml`  
7. Sync Gradle & Build  

---

## 6. Quy Tắc Firebase

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

---

## 7. Sử Dụng

- Mở app → chuyển đến màn hình đăng nhập  
- Đăng nhập email hoặc Google  
- Trang chủ: xem thống kê, thêm đồ ăn / nước uống / bài tập  
- Mục bài tập: chọn danh mục, bắt đầu với bộ đếm thời gian  
- Hồ sơ: chỉnh sửa thông tin và cài đặt  


