package com.psi.shoppingadmin

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dmax.dialog.SpotsDialog

class MainActivity : AppCompatActivity() {

    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Init
        alertDialog = SpotsDialog.Builder().setContext(this@MainActivity).build()
        storageReference = FirebaseStorage.getInstance().getReference(("image_upload"))

        // event
        findViewById<Button>(R.id.buttonUpload).setOnClickListener {
            // Pick up an image
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == PICK_IMAGE_CODE ){
            alertDialog.show()
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask { task ->
                if( (!task.isSuccessful)){
                    Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                }

                storageReference.downloadUrl
            }.addOnCompleteListener { task ->
                val downloadUri = task.result
                var url = downloadUri!!.toString().substring(0, downloadUri.toString().indexOf("&token"))
                println("-- URI: ${url}")
                alertDialog.dismiss()
            }
        }
    }
    companion object {
        private val PICK_IMAGE_CODE = 1000
    }
}