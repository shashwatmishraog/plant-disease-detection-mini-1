package com.example.plantdis2


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.example.plantdis2.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.MediaType;
import android.view.WindowManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream





class MainActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200

    private lateinit var binding: ActivityMainBinding

    private val client = OkHttpClient() // Create OkHttpClient instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        binding.cameraButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }

        binding.galleryButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE || requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val bitmap: Bitmap

                if (requestCode == CAMERA_REQUEST_CODE) {
                    bitmap = data?.extras?.get("data") as Bitmap
                } else {
                    val selectedImageUri = data?.data
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                }

                // Convert bitmap to byte array (optional for some servers)
                val byteArray = ByteArrayOutputStream().apply {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, this)
                    close()
                }.toByteArray()

                // Create MultipartBody with image data
//                val requestBody = MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("image", "", RequestBody.create(MediaType.parse("application/octet-stream"), byteArray))
//                    .build()

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    //.addFormDataPart("image", "leaf1.jpg", RequestBody.create("application/octet-stream".toMediaTypeOrNull(), byteArray))
                    .addFormDataPart("image","leaf1",RequestBody.create("application/octet-stream".toMediaTypeOrNull(), byteArray))
                    .build()


                // Create the request
                val request = Request.Builder()
                    .url("https://plant-disease-detection-mini-1-3.onrender.com/predict") // Replace with your server URL
                    .post(requestBody)
                    .build()



                // Execute the request on a background thread
//                Thread {
//                    try {
//                        val response = client.newCall(request).execute()
//                        val responseBody = response.body?.string()
//
//                        // Handle the response from the server (e.g., display disease prediction)
//                        runOnUiThread {
//                                binding.diseaseTextView.text = responseBody // Assuming you have a TextView with id diseaseTextViw
//                            // Update UI with disease prediction
//                            // ...
//                            // You can use responseBody to display the predicted disease on the UI
//                        }
//                    } catch (e: Exception) {
//                        runOnUiThread {
//                            // Handle any errors that may occur during network request
//                            // ...
//                        }
//                    }
//                }.start()



                // ... (rest of your code)

                Thread {
                    try {
                        val response = client.newCall(request).execute()
                        val responseBody = response.body?.string()

                        runOnUiThread {
                            if (responseBody != null) {
                                try {
                                    // Parse JSON response
                                    val data = JsonParser.parseString(responseBody) as JsonObject

                                    // Extract disease name (assuming "disease" is the key)
                                    val diseaseName = data.get("disease")?.asString ?: "Unknown disease"

                                    // Update TextViews
                                    binding.diseaseTextView.text = diseaseName // Display raw response
                                   // binding.diseaseNameTextView.text = diseaseName // Display extracted disease name

                                } catch (e: JsonParseException) {
                                    // Handle parsing error
                                    binding.diseaseTextView.text = "Error: Invalid response format"
                                }
                            } else {
                                // Handle empty response
                                binding.diseaseTextView.text = "No response received"
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            // Handle network error
                            binding.diseaseTextView.text = "Error: Couldn't fetch prediction"
                        }
                    }
                }.start()



            }
        }
    }
}
