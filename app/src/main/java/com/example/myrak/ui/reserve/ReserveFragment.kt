package com.example.myrak.ui.reserve

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myrak.R
import com.example.myrak.databinding.FragmentReserveBinding
import com.example.myrak.login

class ReserveFragment : Fragment() {
    private var _binding: FragmentReserveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReserveBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val explicitBtn: Button = root.findViewById(R.id.b1)
        explicitBtn.setOnClickListener {
            val intent=Intent(requireContext(), login::class.java)
            startActivity(intent)
        }
        val explicitBtn2: Button = root.findViewById(R.id.b2)
        explicitBtn2.setOnClickListener {
            val intent = Intent(requireContext(), login::class.java)
            startActivity(intent)
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}