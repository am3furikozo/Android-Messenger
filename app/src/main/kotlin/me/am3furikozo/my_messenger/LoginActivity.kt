package me.am3furikozo.my_messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.am3furikozo.my_messenger.utils.isValidEmail

class LoginActivity : AppCompatActivity() {
  private lateinit var edtEmail: EditText
  private lateinit var edtPassword: EditText
  private lateinit var btnLogin: Button
  private lateinit var btnSignUp: Button
  private lateinit var auth: FirebaseAuth

  companion object {
    private val TAG = LoginActivity::class.java.simpleName
  }

  private fun login(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          finish()
          startActivity(Intent(this, MainActivity::class.java))
        }
        else {
          Log.w(TAG, "signInWithEmail:failure", task.exception)
          Toast
            .makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT)
            .show()
        }
      }

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    supportActionBar?.hide()

    auth = Firebase.auth

    edtEmail = findViewById(R.id.edt_email)
    edtPassword = findViewById(R.id.edt_password)
    btnLogin = findViewById(R.id.btn_login)
    btnSignUp = findViewById(R.id.btn_sign_up)

    btnSignUp.setOnClickListener {
      startActivity(Intent(this, SignUpActivity::class.java))
    }

    btnLogin.setOnClickListener {
      val email = edtEmail.text.toString().trim()
      val password = edtPassword.text.toString().trim()

      when {
        !email.isValidEmail() -> Toast
          .makeText(this, "Email is not valid", Toast.LENGTH_SHORT)
          .show()
        password.length < 8 -> Toast
          .makeText(this, "Password too short", Toast.LENGTH_SHORT)
          .show()
        else -> login(email, password)
      }
    }
  }
}