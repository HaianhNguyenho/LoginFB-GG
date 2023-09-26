package com.example.loginbasic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.util.Arrays

class MainActivity : AppCompatActivity() {

    private lateinit var signIn: LoginButton
    private lateinit var callbackManager: CallbackManager
    private lateinit var gSignIn: SignInButton

    private lateinit var gso: GoogleSignInOptions

    private lateinit var gsc: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các thành phần giao diện
        signIn = findViewById(R.id.sign_in)
        callbackManager = CallbackManager.Factory.create()

        gSignIn = findViewById(R.id.g_sign_in)

        // Cấu hình Google Sign-In
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)


        // Kiểm tra nếu người dùng đã đăng nhập bằng Facebook
        val aToken = AccessToken.getCurrentAccessToken()

        if (aToken != null && !aToken.isExpired) {
            startActivity(Intent(this, FacebookLogin::class.java))
            finish()
        } else {
            // Nếu chưa đăng nhập bằng Facebook, thiết lập callback cho việc đăng nhập
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        // Handle cancellation
                        // Xử lý khi người dùng hủy đăng nhập Facebook
                    }

                    override fun onError(error: FacebookException) {
                        // Handle error
                        // Xử lý khi có lỗi xảy ra trong quá trình đăng nhập Facebook
                    }

                    override fun onSuccess(result: LoginResult) {
                        // Chuyển hướng đến màn hình FacebookLogin khi đăng nhập thành công
                        startActivity(Intent(this@MainActivity, FacebookLogin::class.java))
                        finish()
                    }
                })
            // Xử lý sự kiện khi người dùng nhấn nút đăng nhập Facebook
            signIn.setOnClickListener {
                // Yêu cầu quyền truy cập public_profile từ người dùng
                LoginManager.getInstance()
                    .logInWithReadPermissions(this, Arrays.asList("public_profile"))

            }
        }
        // Kiểm tra nếu người dùng đã đăng nhập bằng Google
        val account: GoogleSignInAccount?= GoogleSignIn
            .getLastSignedInAccount(this)
        if(account!=null){
            goHome()
        }

        // Xử lý sự kiện khi người dùng nhấn nút đăng nhập Google
        gSignIn.setOnClickListener{
            goSignIn()
        }
    }
    // Xử lý kết quả của việc đăng nhập Facebook và Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(resultCode, requestCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1000){
            val task: Task<GoogleSignInAccount> = GoogleSignIn
                .getSignedInAccountFromIntent(data)

            try{
                task.getResult(ApiException::class.java)
                goHome()
            }catch (e:java.lang.Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Chuyển đến màn hình đăng nhập Google
    private fun goSignIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }
    // Chuyển đến màn hình GoogleLogin
    private fun goHome() {
        val intent = Intent(this, GoogleLogin::class.java)

        startActivity(intent)

        finish()
    }
}