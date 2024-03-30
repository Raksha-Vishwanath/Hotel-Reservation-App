package com.example.myrak.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myrak.R
import com.example.myrak.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val instagramButton: ImageButton = binding.insta
        instagramButton.setOnClickListener {
            openLink("https://www.instagram.com/hotel/")
        }

        val youtubeButton: ImageButton = binding.yt
        youtubeButton.setOnClickListener {
            openLink("https://youtu.be/G1FoU1p1YPI?si=BskfDcBBUranFuD_")
        }

        val playStoreButton: ImageButton = binding.playstore
        playStoreButton.setOnClickListener {
            openLink("https://play.google.com/store/apps/details?id=com.booking")
        }

        return root
    }

    private fun openLink(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}