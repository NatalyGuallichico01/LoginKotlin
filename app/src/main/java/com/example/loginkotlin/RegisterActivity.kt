package com.example.loginkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    //private lateinit var binding: ActivityRegistrationBindding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val regitertext: TextView =findViewById(R.id.textView_login_now)

        regitertext.setOnClickListener{
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button= findViewById(R.id.button_register)

        registerButton.setOnClickListener {
            performSignUp()
        }

        //lets get email and password from the user
        performSignUp()
    }

    private fun performSignUp() {
        val email=findViewById<EditText>(R.id.editText_email_register)
        val password=findViewById<EditText>(R.id.editText_password_register)
        val name=findViewById<EditText>(R.id.editText_name_register)

        if (email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Por favor, igrese todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val inputEmail= email.text.toString()
        val inputPassword=password.text.toString()
        val inputName=name.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)

            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, let move to the next activity  i.e MainActivity
                    val  intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    val user=auth.currentUser
                    val uid=user!!.uid

                    Toast.makeText(baseContext, "Ingreso Satisfactorio.",
                        Toast.LENGTH_SHORT).show()
                    val map= hashMapOf(
                        "nombre" to inputName
                    )

                    val db= Firebase.firestore

                    db.collection("users").document(uid).set(map).addOnSuccessListener {
                        //infoUser()
                        Toast.makeText(baseContext, "Datos ingresados correctamente.",
                            Toast.LENGTH_SHORT).show()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Ourrio un error ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}