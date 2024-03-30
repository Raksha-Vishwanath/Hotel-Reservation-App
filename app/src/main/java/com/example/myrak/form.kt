package com.example.myrak

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myrak.ui.reserve.ReserveFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//FOR SUPERIOR DOUBLE ROOM

class form: AppCompatActivity() {
    private lateinit var dbref: DatabaseReference //to check availability
    private lateinit var dbref1: DatabaseReference //to add to bookings

    private lateinit var checkindate:EditText
    private lateinit var numofroom:EditText
    private lateinit var fname:EditText
    private lateinit var lname:EditText
    private lateinit var email:EditText
    private lateinit var phone:EditText
    private lateinit var numofguest:EditText

    private lateinit var dateEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form)

        checkindate = findViewById(R.id.dateEditText)
        numofroom = findViewById(R.id.numofroom)
        fname= findViewById(R.id.e1)
        lname= findViewById(R.id.e2)
        email= findViewById(R.id.email)
        phone= findViewById(R.id.phone)
        numofguest=findViewById(R.id.numofguest)

        dbref1= FirebaseDatabase.getInstance().getReference("booking")

        val mSpinner = findViewById<Spinner>(R.id.roomTypeSpinner)
        val mArrayAdapter = ArrayAdapter.createFromResource(
                this,
        R.array.room_types,
        R.layout.spinner_list
        )

        // Specify the layout to use when the list of choices appears
        mArrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        // Set the adapter to the spinner
        mSpinner.adapter = mArrayAdapter

        val explicitBtn: Button = findViewById(R.id.form_btn1)
        explicitBtn.setOnClickListener {
            wichroom()
        }
        dateEditText = findViewById(R.id.dateEditText)
    }

    private fun wichroom()
    {
        val roomTypeSpinner: Spinner= findViewById(R.id.roomTypeSpinner)
        val selectedRoomType = roomTypeSpinner.selectedItem.toString()
        val fname=fname.text.toString()
        val email=email.text.toString()
        val phone=phone.text.toString()
        val checkindate=checkindate.text.toString()
        val numofroom=numofroom.text.toString()

        if(fname.isEmpty())
        {
            Toast.makeText(this@form, "Please enter First Name", Toast.LENGTH_SHORT).show()
        }
        else if(email.isEmpty()){
            Toast.makeText(this@form, "Please enter Email", Toast.LENGTH_SHORT).show()
        }
        else if(phone.isEmpty()){
        Toast.makeText(this@form, "Please enter Phone Number", Toast.LENGTH_SHORT).show()
        }
        else if(checkindate.isEmpty()){
            Toast.makeText(this@form, "Please enter Checkin Date", Toast.LENGTH_SHORT).show()
        }
        else if(numofroom.isEmpty()){
            Toast.makeText(this@form, "Please enter Number of rooms", Toast.LENGTH_SHORT).show()
        }
        else
        if(selectedRoomType=="Room Type: Superior Room")
        {
            superior_check()
        }
        else if(selectedRoomType=="Room Type: Deluxe Room")
        {
            dulex_check()
        }
        else
        {
            Toast.makeText(this@form, "Error! Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun superior_check(){
        //check if available
        val checkinndate=checkindate.text.toString()
        val numofroom=numofroom.text.toString()
        val IntNumofroom =numofroom.toInt()

        val fname=fname.text.toString()
        val lname=lname.text.toString()
        val email=email.text.toString()
        val phone=phone.text.toString()
        val roomTypeSpinner: Spinner= findViewById(R.id.roomTypeSpinner)
        val selectedRoomType = roomTypeSpinner.selectedItem.toString()
        val date=checkindate.text.toString()
        val numguest=numofguest.text.toString()

        dbref=FirebaseDatabase.getInstance().getReference("superior")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){ //snapshot means data.. so if data exists
                    var stoploop=false
                    for(dataSnap in snapshot.children){ //.children is the data under the superior
                        if(stoploop)
                        {
                            break
                        }
                        val data = dataSnap.getValue(LoginModel::class.java)
                        data?.let {
                            val numAsInt = it.num?.toIntOrNull() ?: 0 // Convert string to int or default to 0
                            val decrementedNum = (numAsInt - IntNumofroom) // Ensure the decremented value is at least 0

                            if (it.date == checkinndate && decrementedNum>=0) {
                                //update value of num
                                val decrementedNumString = decrementedNum.toString() // Convert int back to string
                                val Id = dataSnap.key // Assuming the key is the Id you need
                                Id?.let { id ->
                                    val numRef = dbref.child(id).child("num")
                                    numRef.setValue(decrementedNumString)
                                    //add booking to the database
                                    val Id= dbref1.push().key!!
                                    val book = BookingModel(Id, fname,lname,email,phone,selectedRoomType,date,numofroom,numguest)
                                    dbref1.child(Id).setValue(book)
                                        .addOnCompleteListener {
                                            Toast.makeText(this@form, "Booking Confirmed!", Toast.LENGTH_SHORT).show()
                                        }.addOnFailureListener { err ->
                                            Toast.makeText(this@form, "Error ${err.message}", Toast.LENGTH_LONG).show()
                                        }
                                    //intent to move to next page
                                    val intent = Intent(this@form, thank::class.java)
                                    intent.putExtra("roomtype", selectedRoomType)
                                    intent.putExtra("numofroom", numofroom)
                                    intent.putExtra("date", date)
                                    startActivity(intent)
                                    stoploop=true
                                }
                            }
                        }
                    }
                    if(!stoploop)
                    {
                        Toast.makeText(this@form, "Sorry! No rooms available for that date", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@form, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun dulex_check(){
        //check if available
        val checkinndate=checkindate.text.toString()
        val numofroom=numofroom.text.toString()
        val IntNumofroom =numofroom.toInt()

        val fname=fname.text.toString()
        val lname=lname.text.toString()
        val email=email.text.toString()
        val phone=phone.text.toString()
        val roomTypeSpinner: Spinner= findViewById(R.id.roomTypeSpinner)
        val selectedRoomType = roomTypeSpinner.selectedItem.toString()
        val date=checkindate.text.toString()
        val numguest=numofguest.text.toString()

        dbref=FirebaseDatabase.getInstance().getReference("dulex")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){ //snapshot means data.. so if data exists
                    var stoploop=false
                    for(dataSnap in snapshot.children){ //.children is the data under the login
                        if(stoploop)
                        {
                            break
                        }
                        val data = dataSnap.getValue(LoginModel::class.java)
                        data?.let {
                            val numAsInt = it.num?.toIntOrNull() ?: 0 // Convert string to int or default to 0
                            val decrementedNum = (numAsInt - IntNumofroom) // Ensure the decremented value is at least 0

                            if (it.date == checkinndate && decrementedNum>=0) {
                                //update value of num
                                val decrementedNumString = decrementedNum.toString() // Convert int back to string
                                val Id = dataSnap.key // Assuming the key is the Id you need
                                Id?.let { id ->
                                    val numRef = dbref.child(id).child("num")
                                    numRef.setValue(decrementedNumString)
                                    //add booking to the database
                                    val Id= dbref1.push().key!!
                                    val book = BookingModel(Id, fname,lname,email,phone,selectedRoomType,date,numofroom,numguest)
                                    dbref1.child(Id).setValue(book)
                                        .addOnCompleteListener {
                                            Toast.makeText(this@form, "Booking Confirmed!", Toast.LENGTH_SHORT).show()
                                        }.addOnFailureListener { err ->
                                            Toast.makeText(this@form, "Error ${err.message}", Toast.LENGTH_LONG).show()
                                        }
                                    //intent to move to next page
                                    val intent = Intent(this@form, thank::class.java)
                                    intent.putExtra("roomtype", selectedRoomType)
                                    intent.putExtra("numofroom", numofroom)
                                    intent.putExtra("date", date)
                                    startActivity(intent)
                                    stoploop=true
                                }
                            }
                        }
                    }
                    if(!stoploop)
                    {
                        Toast.makeText(this@form, "Sorry! No rooms available for that date", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@form, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

        fun showDatePickerDialog(view: View) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    dateEditText.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
}
