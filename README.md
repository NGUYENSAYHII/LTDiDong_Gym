📱 GoGym – Ứng Dụng Theo Dõi Sức Khỏe

GoGym là ứng dụng di động về sức khỏe được xây dựng bằng Kotlin + Jetpack Compose dành cho Android.
Ứng dụng cho phép theo dõi bài tập, bữa ăn, lượng nước và tiến độ mục tiêu sức khỏe, tích hợp Firebase Authentication và Firestore để lưu dữ liệu thời gian thực.

🚀 Mô Tả

GoGym cung cấp các tính năng:

Ghi nhật ký bài tập, bữa ăn và lượng nước

Xem tổng quan calo đốt – nạp – còn lại

Truy cập danh mục bài tập có hình ảnh

Bài tập tự động theo mục tiêu

Tạo kế hoạch luyện tập

Quản lý hồ sơ cá nhân

Tự động đồng bộ dữ liệu Firebase

Ứng dụng hỗ trợ:

Chủ đề sáng/tối

Màu động (Android 12+)

Bố cục responsive

✨ Tính Năng Chính
🔐 Xác Thực

Đăng ký / Đăng nhập email – mật khẩu

Đăng nhập Google

Quên mật khẩu

🏠 Bảng Điều Khiển Trang Chủ

Lượng calo tiêu thụ

Calo nạp

Calo còn lại

Lượng nước

Phân loại bữa ăn (sáng/trưa/tối/snack)

💪 Theo Dõi Bài Tập

7 nhóm bài tập: Bụng, Tay, Ngực, Chân, Lưng–Vai, Văn phòng, Kickboxing

Chi tiết bài tập: hình ảnh, mô tả, timer, lượng calo

Gợi ý bài tập theo mục tiêu

Lịch tập luyện theo tuần

🍽️ Ghi Nhật Ký Bữa Ăn

Thêm món ăn

Tính calo & protein

Tìm kiếm + danh sách yêu thích

💧 Theo Dõi Nước

Ghi nước theo ml/lít

👤 Hồ Sơ & Cài Đặt

Ngôn ngữ: VI / EN

Tùy chọn đơn vị (kg/lbs – cm/inch)

Đổi thông tin cá nhân

🧰 Công Nghệ Sử Dụng

Ngôn ngữ: Kotlin

UI: Jetpack Compose + Material 3

Navigation: Navigation Compose

State Management: ViewModel, LiveData, StateFlow

Backend: Firebase Auth + Firestore

Libraries:

Coil (load ảnh)

Google Sign-In

Kiến trúc: MVVM + Repository

📁 Cấu Trúc Dự Án
com.example.gogym/
│
├── ui/theme/              
├── model/                
├── pages/                
│   ├── Auth
│   ├── Home
│   ├── Workout
│   ├── Profile
│   └── Navigation
└── data/

🔧 Cài Đặt
Yêu Cầu

Android Studio mới nhất

Kotlin 1.9+

Firebase Project

File google-services.json

Thiết Lập
git clone <repo>


Sau đó:

Mở Android Studio

Kết nối Firebase Auth + Firestore

Thêm google-services.json vào app/

Sync Gradle → Run
