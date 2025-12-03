GoGym - Ứng Dụng Theo Dõi Sức Khỏe
GoGym là một ứng dụng di động về sức khỏe được xây dựng bằng Kotlin và Jetpack Compose dành cho Android. Ứng dụng giúp người dùng theo dõi bài tập, bữa ăn, lượng nước uống và tiến độ đạt mục tiêu sức khỏe. Ứng dụng tích hợp Firebase để xác thực (bao gồm Đăng nhập bằng Google) và lưu trữ dữ liệu (Firestore để lưu nhật ký). Nó có giao diện hiện đại với chủ đề, điều hướng và các thành phần tương tác như bộ đếm thời gian cho bài tập.
Mô Tả
GoGym cung cấp một nền tảng toàn diện cho người dùng để:

Ghi nhật ký bài tập, bữa ăn và lượng nước.
Xem tóm tắt hàng ngày về calo đốt cháy, calo nạp vào và calo còn lại.
Truy cập các bài tập được phân loại (ví dụ: bụng, tay, ngực) với chi tiết và bộ đếm thời gian.
Tùy chỉnh kế hoạch tập luyện, đặt mục tiêu và tạo báo cáo.
Quản lý hồ sơ với cài đặt ngôn ngữ, đơn vị đo và thông tin cá nhân.

Ứng dụng hỗ trợ chủ đề sáng/tối, màu sắc động (Android 12+), và bố cục responsive. Dữ liệu được đồng bộ qua Firebase, đảm bảo cập nhật thời gian thực.
Tính Năng

Xác Thực: Đăng ký/đăng nhập bằng email/mật khẩu, Đăng nhập bằng Google, đặt lại mật khẩu.
Bảng Điều Khiển Trang Chủ: Hiển thị mục tiêu calo hàng ngày, calo đốt cháy/nạp vào, lượng nước, và phân tích bữa ăn (bữa sáng, trưa, tối, đồ ăn vặt).
Theo Dõi Bài Tập:
Bài tập được phân loại (ví dụ: Bụng, Tay, Ngực, Chân, Lưng & Vai, Văn Phòng, Kickboxing).
Chi tiết bài tập với mô tả, hình ảnh, bộ đếm thời gian và ước tính calo.
Bài tập tự động tạo dựa trên khu vực mục tiêu và thời lượng.
Kế hoạch tập luyện tùy chỉnh theo ngày trong tuần.

Ghi Nhật Ký Bữa Ăn: Thêm thực phẩm vào bữa ăn, theo dõi calo và protein, với tìm kiếm và yêu thích.
Theo Dõi Nước: Ghi lượng nước uống theo lít.
Quản Lý Hồ Sơ: Chỉnh sửa tuổi, chiều cao, cân nặng, mục tiêu, đơn vị (kg/lbs, cm/inch), ngôn ngữ (VI/EN).
Báo Cáo: Báo cáo bài tập với xem lịch.
Cài Đặt: Cài đặt ứng dụng, báo cáo vấn đề, chia sẻ ứng dụng.
Hỗ Trợ Ngoại Tuyến: Chức năng cơ bản với bộ nhớ đệm Firebase.
Tính Năng Cao Cấp: Placeholder cho bài tập cao cấp (ví dụ: nội dung bị khóa).

Công Nghệ Sử Dụng

Ngôn Ngữ: Kotlin
Khung Giao Diện: Jetpack Compose
Điều Hướng: Jetpack Navigation Compose
Quản Lý Trạng Thái: ViewModel, LiveData, Coroutines, StateFlow
Backend: Firebase Authentication, Firestore
Thư Viện Khác:
Coil để tải hình ảnh
Google Sign-In API
Material3 cho thành phần giao diện

Kiến Trúc: MVVM với mẫu Repository

Cấu Trúc Dự Án
Dự án được tổ chức dưới com.example.gogym:

ui/theme: Định nghĩa chủ đề (màu sắc, kiểu chữ, chủ đề sáng/tối).
Color.kt, Type.kt, Theme.kt, GoGymTheme.kt

model: Lớp dữ liệu cho các thực thể.
MealType.kt, MealModels.kt, WorkoutAndWaterModels.kt

pages: Màn hình Composable và thành phần giao diện.
Xác Thực: LoginPage.kt, SignupPage.kt
Trang Chủ: HomePage.kt, AddFoodPage.kt
Bài Tập: WorkoutPage.kt, WorkoutCategoryDetailPage.kt, ExerciseDetailPage.kt, OfficeWorkoutPage.kt, AutoWorkoutPage.kt, CustomPlanDetailPage.kt, WorkoutReportPage.kt
Hồ Sơ & Cài Đặt: ProfilePage.kt, SettingsPage.kt, CustomizePage.kt
Điều Hướng: MainTabsPage.kt, MyAppNavigation.kt, NavHelpers.kt

data: Repository cho các hoạt động dữ liệu.
HomeRepository.kt (xử lý tương tác Firestore cho nhật ký và mục tiêu)


Điểm vào chính: MyAppNavigation.kt thiết lập biểu đồ điều hướng.
Thiết Lập
Yêu Cầu

Android Studio (phiên bản ổn định mới nhất)
Kotlin 1.9+
Thiết lập dự án Firebase (cho Auth và Firestore)
Tệp JSON Dịch Vụ Google trong thư mục app/

Các Bước

Sao chép kho lưu trữ:textgit clone <repository-url>
Mở dự án trong Android Studio.
Thêm cấu hình Firebase:
Tạo dự án Firebase tại console.firebase.google.com.
Bật Xác Thực (Email/Mật Khẩu và Google).
Bật Firestore.
Tải xuống google-services.json và đặt vào thư mục app/.
Cập nhật R.string.default_web_client_id trong strings.xml với ID Client Web Firebase của bạn.

Đồng bộ Gradle và xây dựng dự án.
Chạy trên trình giả lập hoặc thiết bị thực (API 21+).

Quy Tắc Firebase
Để bảo mật cơ bản, thiết lập quy tắc Firestore để cho phép đọc/ghi đã xác thực:
textrules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
Sử Dụng

Khởi chạy ứng dụng: Bắt đầu tại đăng nhập nếu chưa xác thực.
Đăng Ký/Đăng Nhập: Sử dụng email hoặc Google.
Trang Chủ: Xem thống kê hàng ngày; thêm bữa ăn/nước/bài tập.
Bài Tập: Duyệt danh mục, bắt đầu bài tập với bộ đếm thời gian.
Hồ Sơ: Chỉnh sửa thông tin cá nhân và cài đặt.
