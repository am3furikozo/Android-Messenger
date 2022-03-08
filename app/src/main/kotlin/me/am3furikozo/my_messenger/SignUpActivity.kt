package me.am3furikozo.my_messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import me.am3furikozo.my_messenger.models.User
import me.am3furikozo.my_messenger.utils.isValidEmail

class SignUpActivity : AppCompatActivity() {
  private lateinit var edtName: EditText
  private lateinit var edtEmail: EditText
  private lateinit var edtPassword: EditText
  private lateinit var btnSignUp: Button
  private lateinit var auth: FirebaseAuth
  private lateinit var dbRef: DatabaseReference

  companion object {
    private val TAG = SignUpActivity::class.java.simpleName
  }

  private fun addUserToDatabase(name: String, email: String, uid: String) {
    dbRef = FirebaseDatabase.getInstance().reference
    dbRef.child("user").child(uid).setValue(User(name, email, uid))
  }

  private fun signUp(name: String, email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        when {
          task.isSuccessful -> {
            addUserToDatabase(name, email, auth.currentUser?.uid!!)
            finish()
            startActivity(Intent(this, MainActivity::class.java))
          }
          task.exception is FirebaseAuthUserCollisionException -> {
            Toast.makeText(this, "Same user already exists", Toast.LENGTH_SHORT).show()
          }
          else -> {
            Log.w(TAG, "signUpWithEmail:failure", task.exception)
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
          }
        }
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up)

    supportActionBar?.hide()

    auth = Firebase.auth

    edtName = findViewById(R.id.edt_name)
    edtEmail = findViewById(R.id.edt_email)
    edtPassword = findViewById(R.id.edt_password)
    btnSignUp = findViewById(R.id.btn_sign_up)

    btnSignUp.setOnClickListener {
      val name = edtName.text.toString().trim()
      val email = edtEmail.text.toString().trim()
      val password = edtPassword.text.toString().trim()

      when {
        !email.isValidEmail() -> Toast
          .makeText(this, "Email is not valid", Toast.LENGTH_SHORT)
          .show()
        password.length < 8 -> Toast
          .makeText(this, "Password too short", Toast.LENGTH_SHORT)
          .show()
        else -> signUp(name, email, password)
      }
    }
  }
}