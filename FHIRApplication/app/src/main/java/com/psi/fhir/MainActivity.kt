package com.psi.fhir

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.fhir.FhirEngine
import com.psi.fhir.ui.theme.FHIRApplicationTheme
import com.psi.fhir.ui.viewmodels.PatientListViewModel
import android.Manifest
import android.os.Build
import java.io.File

/**
 * https://github.com/google/android-fhir/wiki/FEL%3A-Search-FHIR-resources
 */
class MainActivity : AppCompatActivity() {
    
    private val STORAGE_PERMISSION_CODE = 12345

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Check if permission is not granted
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // Request permission
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    STORAGE_PERMISSION_CODE
//                )
//            } else {
//                // Permission already granted, proceed with your action
//                // For example, you can call your method getFolderSize() here
//                // getFolderSize()
//
//                println("============= Permission already granted")
//            }
//        } else {
//            // Permission is automatically granted on devices lower than Marshmallow
//            // Proceed with your action
//            // getFolderSize()
//            println("============= Permission is automatically granted on devices")
//        }

//        var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)


        setContent {
            FHIRApplicationTheme {
               FhirApp(application)
            }
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//                // Proceed with your action
//                // getFolderSize()
//
//                println("=============  Permission granted")
//            } else {
//
//                println("=============  Permission denied")
//                // Permission denied
//                // You may want to handle this case, perhaps show a message to the user
//            }
//        }
//    }

//    // Your method to get folder size
//    fun getFolderSize(folder: File): Long {
//        var size: Long = 0
//
//        if (folder.isDirectory) {
//            val files = folder.listFiles()
//            if (files != null) {
//                for (file in files) {
//                    size += if (file.isDirectory) {
//                        getFolderSize(file)
//                    } else {
//                        file.length()
//                    }
//                }
//            }
//        } else {
//            size = folder.length()
//        }
//
//        return size
//    }
}
