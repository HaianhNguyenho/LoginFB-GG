package com.example.loginbasic

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.login.LoginManager

class FacebookLogin : AppCompatActivity() {

    // Khai báo các thành phần giao diện

    private lateinit var fbImage: ImageView

    private lateinit var fbName: TextView

    private lateinit var fbId: TextView


    private lateinit var fbLogout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_login)
        // Ánh xạ các thành phần giao diện với biến tương ứng
        fbImage = findViewById(R.id.fb_image)

        fbName = findViewById(R.id.fb_name)

        fbId = findViewById(R.id.fb_id)


        fbLogout = findViewById(R.id.fb_log_out)

        // Lấy thông tin AccessToken hiện tại của người dùng đã đăng nhập Facebook

        val aToken = AccessToken.getCurrentAccessToken()
        // Tạo một GraphRequest để lấy thông tin cá nhân từ Facebook
        val request = GraphRequest.newMeRequest(aToken) { `object`, response ->
            // Your code to handle the response goes here

            val id = `object`?.getString("id")


            val fullName = `object`?.getString("name")

            val profileUrl =
                `object`?.getJSONObject("picture")?.getJSONObject("data")?.getString("url")

            // Hiển thị thông tin lên giao diện
            fbName.text = fullName

            fbId.text = id


            // Sử dụng thư viện Glide để tải và hiển thị ảnh đại diện của người dùng
            Glide.with(applicationContext).load(profileUrl).into(fbImage)

        }

        val parameters = Bundle()

        // Đặt các tham số cho GraphRequest để yêu cầu thông tin cụ thể
        parameters.putString("fields", "id,name,link,picture.type(large)")


        request.parameters = parameters
        // Thực hiện GraphRequest để lấy thông tin và cập nhật giao diện
        request.executeAsync()

        // Xử lý sự kiện khi người dùng nhấn nút đăng xuất
        fbLogout.setOnClickListener {
            // Đăng xuất khỏi tài khoản Facebook
            LoginManager.getInstance().logOut()
            // Chuyển về màn hình MainActivity (hoặc màn hình đăng nhập khác nếu cần)
            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }


    }
}