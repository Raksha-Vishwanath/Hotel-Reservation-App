package com.example.myrak.ui.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myrak.R
import com.example.myrak.SignupModel
import com.example.myrak.databinding.FragmentReviewBinding
import com.example.myrak.reviewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private lateinit var dbref: DatabaseReference
    private lateinit var rating: TextView
    private lateinit var feedback: EditText

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val nameofrate = binding.nameofrate
        val feedback = binding.feedback
        val rating = binding.nameofrate

        val stars = binding.stars
        stars.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            // Convert the float rating to an integer
            val ratingName = when (rating.toInt()) {
                0 -> "Very Dissatisfied" // If rating is 0, display "Very Dissatisfied"
                1 -> "Dissatisfied" // If rating is 1, display "Dissatisfied"
                2 -> "Ok" // If rating is 2, display "Ok"
                3 -> "Good"
                4 -> "Satisfied" // If rating is 4, display "Satisfied"
                5 -> "Very Satisfied" // If rating is 5, display "Very Satisfied"
                else -> {
                    ""
                }
            }
            // Set the rating name to the ratingNameTextView
            nameofrate.text = ratingName
        }
        dbref= FirebaseDatabase.getInstance().getReference("rating")
        binding.button1.setOnClickListener {
            // Display a Toast message when button1 is clicked
            val feed=feedback.text.toString()
            val rate=rating.text.toString()
            val Id= dbref.push().key!!
            val sign = reviewModel(Id, feed, rate)
            dbref.child(Id).setValue(sign)
                .addOnCompleteListener {
                    feedback.text.clear()
                    Toast.makeText(context, "Thank You for your Feedback!!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }


        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}