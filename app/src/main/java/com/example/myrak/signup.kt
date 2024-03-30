package com.example.myrak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signup: AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var password2: EditText
    private lateinit var submit: Button

    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        val backButton: ImageView = findViewById(R.id.backbutton)
        backButton.setOnClickListener {
            val intent = Intent(this@signup, login::class.java)
            startActivity(intent)
        }

        username=findViewById(R.id.uname)
        password=findViewById(R.id.pword)
        password2=findViewById(R.id.pword2)
        submit=findViewById(R.id.b1)

        dbref= FirebaseDatabase.getInstance().getReference("login")

        submit.setOnClickListener {
            register()
        }
    }
    private fun register(){
        //getting values from form
        val uname=username.text.toString()
        val pword=password.text.toString()
        val pword2=password2.text.toString()

        //validation if form is empty
        if(uname.isEmpty()){
            username.error="Please Enter Username"
        }
        else if(pword.isEmpty()){
            username.error="Please Enter Password"
        }
        else if(pword2.isEmpty()){
            username.error="Please ReEnter Password"
        }
        else if(pword != pword2){
            username.error="Passwords do not Match"
        }
        else
        {
            val Id= dbref.push().key!!
            val sign = SignupModel(Id, uname, pword)
            dbref.child(Id).setValue(sign)
                .addOnCompleteListener {
                    Toast.makeText(this, "Registration successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
            val intent = Intent(this@signup, login::class.java)
            startActivity(intent)
        }
    }
}