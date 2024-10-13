package com.example.mlplayground

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mlplayground.databinding.FragmentMLImageBinding

class MLImageFragment : Fragment() {
   private lateinit var binding: FragmentMLImageBinding
private lateinit var objectImage: ImageView
private lateinit var labelText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMLImageBinding.inflate(layoutInflater)
        return binding.root
     //   binding.objectImage.setImageBitmap(imageBitmap)

    }

}