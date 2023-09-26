package com.example.loginbasic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleLogin : AppCompatActivity() {
    private lateinit var gImage: ImageView

    private lateinit var gName: TextView

    private lateinit var gEmail: TextView

    private lateinit var gID: TextView

    private lateinit var gSignOut: Button

    private lateinit var gso: GoogleSignInOptions

    private lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_login)
        // Ánh xạ các thành phần giao diện với biến tương ứng
        gImage = findViewById(R.id.g_image)

        gName = findViewById(R.id.g_name)

        gImage = findViewById(R.id.g_image)

        gID = findViewById(R.id.g_id)

        gEmail = findViewById(R.id.g_email)

        gSignOut = findViewById(R.id.g_sign_out)

        // Cấu hình GoogleSignInOptions để yêu cầu quyền truy cập email

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Tạo GoogleSignInClient dựa trên cấu hình gso
        gsc = GoogleSignIn.getClient(this, gso)
        // Kiểm tra xem người dùng đã đăng nhập bằng Google chưa
        val account: GoogleSignInAccount? = GoogleSignIn
            .getLastSignedInAccount(this)

        if (account != null) {
            // Hiển thị thông tin người dùng lên giao diện
            gName.text = account.displayName

            gEmail.text = account.email

            gID.text = account.id

            // Sử dụng thư viện Glide để tải và hiển thị ảnh đại diện của người dùng
            Glide.with(applicationContext).load(account.photoUrl).into(gImage)

// Nếu không có tài khoản Google đăng nhập, thực hiện đăng xuất
        } else {
            goOut()
        }
        // Xử lý sự kiện khi người dùng nhấn nút đăng xuất
        gSignOut.setOnClickListener {

            goOut()
        }


    }
    // Hàm thực hiện đăng xuất

    private fun goOut() {
        // Sử dụng GoogleSignInClient để đăng xuất tài khoản
        gsc.signOut().addOnSuccessListener {
            // Sau khi đăng xuất thành công, chuyển về màn hình MainActivity (hoặc màn hình đăng nhập khác nếu cần)
            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }

    }
}