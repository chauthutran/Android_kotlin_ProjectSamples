package com.psi.onlineshop.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.R
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequest
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.httpRequest.Method
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class UploadImageActivity : AppCompatActivity() {

    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        val imageView: ImageView = findViewById(R.id.clickToUploadImg)
        val button: Button = findViewById(R.id.btnUpload)


        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult())  { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent?.data != null) {
                    val uri = intent.data
                    bitmap = MediaStore.Images.Media.getBitmap( contentResolver, uri)
                    imageView.setImageBitmap(bitmap)
                    println("================ uri")
                    println(uri)

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
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 10, stream)
                val imageAsByteArray= stream.toByteArray()
                val base64Image: String = Base64.encodeToString(imageAsByteArray, Base64.DEFAULT)

                println("----------------- ${base64Image}")
                var searchConditions = JSONObject()
                searchConditions.put("file", base64Image)

                var payload = JSONObject()
                payload.put("payload", searchConditions)
                payload.put("collectionName", HttpRequestConfig.COLLECTION_IMAGES)

                println("----------------- request")
                val request = HttpRequest(
                    path = "/upload",
                    method = Method.POST,
                    parameters = mapOf("action" to HttpRequestConfig.REQUEST_ACTION_ADD_ONE),
//                    postedData = payload.toString()
                    postedData = searchConditions.toString()
                )
                request.json<User>{ result, response ->

                    println("----------------- request.json")
                    if( response.error != null )
                    {
                        val message = response.error?.getString("message") ?: ""
//                        viewModelScope.launch { _register.emit(Resource.Error(message)) }
                    }
                    else if(result != null)
                    {
                        println("================ success")
//                        viewModelScope.launch { _register.emit(Resource.Success(result.get(0) as User)) }
                    }
                }


            }
            else {
                Toast.makeText(applicationContext, "select an image first", Toast.LENGTH_SHORT).show()
            }
        }

    }
}