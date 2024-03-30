package com.example.myrak

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.myrak.ui.reserve.ReserveFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class login: AppCompatActivity() {
    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var submit:Button

    private lateinit var textView: TextView

    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val backButton: ImageView = findViewById(R.id.backbutton)
        backButton.setOnClickListener {
            val fragment= ReserveFragment()
            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).commit()
        }

        textView = findViewById(R.id.textview)
        username=findViewById(R.id.uname)
        password=findViewById(R.id.pword)
        submit=findViewById(R.id.b1)

        submit.setOnClickListener {
            verify()
        }

        makeSignUpClickable()
    }

    private fun makeSignUpClickable() {
        val spannableString = SpannableString(textView.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@login, signup::class.java)
                startActivity(intent)
            }
        }
        // Set the clickable span only for the "SIGN UP" portion of the text
        val startIndex = textView.text.indexOf("SIGN UP")
        spannableString.setSpan(clickableSpan, startIndex, startIndex + "SIGN UP".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set the modified text with clickable span to the TextView
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun verify(){
        val uname=username.text.toString()
        val pword=password.text.toString()
        if(uname.isEmpty()){
            username.error="Please Enter Username"
        }
        else if(pword.isEmpty()){
            username.error="Please Enter Password"
        }
        else
        {
            dbref=FirebaseDatabase.getInstance().getReference("login")
            dbref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){ //snapshot means data.. so if data exists
                        for(loginSnap in snapshot.children){ //.children is the data under the login
                            val logindata = loginSnap.getValue(SignupModel::class.java)
                            logindata?.let {
                                if (it.uname == uname && it.pword == pword) {
                                    // Username and password match
                                    // Proceed with your logic here
                                    val intent = Intent(this@login, form::class.java)
                                    startActivity(intent)
                                    return // Exit the loop and function as the user is authenticated
                                }
                            }
                        }
                        Toast.makeText(this@login, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@login, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}