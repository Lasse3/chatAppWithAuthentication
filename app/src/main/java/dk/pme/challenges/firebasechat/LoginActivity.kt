package dk.pme.challenges.firebasechat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            performLogin()
        }

        back_to_register_textview.setOnClickListener{
            finish()
        }
    }

    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    //Log.d("Login", "Successfully logged in: ${it.result.user.uid}")
                }
                else{
                    val intent = Intent(this, ChatActivity::class.java)
                    intent.putExtra("User", email)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}