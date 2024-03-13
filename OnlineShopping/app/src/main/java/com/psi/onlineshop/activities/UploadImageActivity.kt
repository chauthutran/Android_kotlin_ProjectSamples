package com.psi.onlineshop.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.psi.onlineshop.R
import com.psi.onlineshop.httpRequest.FileDataPart
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.VolleyFileUploadRequest
//import com.psi.onlineshop.httpRequest.urlEncoded
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


class UploadImageActivity : AppCompatActivity() {

    var bitmap: Bitmap? = null
    var uri: Uri? = null
    var imageData: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        val imageView: ImageView = findViewById(R.id.clickToUploadImg)
        val button: Button = findViewById(R.id.btnUpload)


        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult())  { result ->
            if (result.resultCode == Activity.RESULT_OK) {
               val intent = result.data
                if (intent?.data != null) {
                    uri = intent.data
//                    bitmap = MediaStore.Images.Media.getBitmap( contentResolver, uri)
//                    imageView.setImageBitmap(bitmap)
//                    println("========================================= uri")
//                    println(uri)

                    val inputStream = contentResolver.openInputStream(uri!!)
                    inputStream?.buffered()?.use {
                        imageData = it.readBytes()
                    }

                }




            }
        }



        imageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(  Intent.ACTION_PICK )
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)

//                startActivity(intent)
            }
        })

        button.setOnClickListener {
            val stream = ByteArrayOutputStream()
            println("----------------- button.setOnClickListener")
            if( bitmap != null )  {
//                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 10, stream)
//                val imageAsByteArray= stream.toByteArray()
//                val base64Image: String = Base64.encodeToString(imageAsByteArray, Base64.DEFAULT)



println("============================== uri : ${uri}")
                val queue = Volley.newRequestQueue(this)
                val url = "${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}/upload"

                val stringRequest = object : VolleyFileUploadRequest(
                    Method.POST,
                    url,
                    Response.Listener { response ->
                        println("-------------------- response : ${response.data}")
//                        if( response == "success") {
//                            Toast.makeText(applicationContext, "Image uploaded", Toast.LENGTH_SHORT).show()
//                        }
//                        else {
//                            Toast.makeText(applicationContext, "Fail to upload image", Toast.LENGTH_SHORT).show()
//                        }

                    },
                    Response.ErrorListener {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                )
                {

                    override  fun getByteData(): MutableMap<String, FileDataPart> {
                        var params = HashMap<String, FileDataPart>()

                        params.put("file" , FileDataPart("image", imageData!!, "jpg"))

                        return params
                    }
                }
                queue.add(stringRequest)


            }
            else {
                Toast.makeText(applicationContext, "select an image first", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

