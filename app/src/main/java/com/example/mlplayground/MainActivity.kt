package com.example.mlplayground

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mlplayground.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding
  //  private lateinit var objectImage: ImageView
   // private lateinit var labelText: TextView
private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageLabeler: ImageLabeler
    private lateinit var captureImage: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkCameraPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)


        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == Activity.RESULT_OK){
                val extras = result.data?.extras
                val imageBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    extras?.getParcelable("data", Bitmap::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    extras?.getParcelable("data")
                }
                if (imageBitmap != null){
                  binding.objectImage.setImageBitmap(imageBitmap)
                    labelImage(imageBitmap)
                } else {
                   binding.labelText.text = "Unable to capture image"
                }
            }
        }

        binding.captureImageBtn.setOnClickListener {
            val clickPicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (clickPicture.resolveActivity(packageManager) != null){
                cameraLauncher.launch(clickPicture)
            } else {
                binding.labelText.text = "No camera application found"
            }
        }
    }

    private fun labelImage(bitmap: Bitmap){
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        imageLabeler.process(inputImage).addOnSuccessListener {label ->
            displayLabel(label)
        } .addOnFailureListener{ e->
            binding.labelText.text = "Error: ${e.message}"
        }
    }

    private fun displayLabel(labels: List<ImageLabel>){
        if (labels.isNotEmpty()){
            val mostConfidentLabel = labels[0]
            binding.labelText.text = "${mostConfidentLabel.text}"
        }else{
            binding.labelText.text = "no labels found"
        }
    }

    private fun checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 1)
        }

    }
}