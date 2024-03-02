package com.psi.shoppingapp.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.psi.shoppingapp.ShoppingApplication
import com.psi.shoppingapp.data.User
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.RegisterValidation
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.utils.validateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference,
    app: Application
): AndroidViewModel(app)  {

    private val _user = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    val updateInfo = _updateInfo.asStateFlow()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch { _user.emit(Resource.Loading()) }

        firestore.collection(Constants.USER_COLLECTION).document(auth.uid!!).get()
            .addOnSuccessListener {
                var userData = it.toObject(User::class.java);
                userData?.let {
                    viewModelScope.launch { _user.emit(Resource.Success(it)) }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch { _user.emit(Resource.Error(it.message.toString())) }
            }
    }

    fun updateUser(user: User, imageUri: Uri?) {

        if( validateInputs(user)) {
            viewModelScope.launch { _updateInfo.emit(Resource.Loading()) }

            if( imageUri == null )
            {
                saveUserInformation( user, true )
            }
            else
            {
                saveUserInformationWithImage( user, imageUri )
            }

            firestore.collection(Constants.USER_COLLECTION).document(auth.uid!!)
                .set(user)
                .addOnSuccessListener {
                    viewModelScope.launch { _updateInfo.emit(Resource.Success(user)) }
                }
                .addOnFailureListener {
                    viewModelScope.launch { _updateInfo.emit(Resource.Error(it.message.toString())) }
                }
        }
        else
        {
            viewModelScope.launch { _user.emit(Resource.Error("All fields are required")) }
        }

    }

    private fun saveUserInformation(user: User, shouldRetrieveOldImage: Boolean) {
        firestore.runTransaction { transaction ->
            val documentRef = firestore.collection( Constants.USER_COLLECTION ).document(auth.uid!!)

            if( shouldRetrieveOldImage ) {
                var currrentUser = transaction.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currrentUser?.imagePath ?: "")
                transaction.set(documentRef, newUser )
            }
            else
            {
                transaction.set(documentRef, user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch { _updateInfo.emit(Resource.Loading()) }
        }.addOnFailureListener {
            viewModelScope.launch { _updateInfo.emit(Resource.Error(it.message.toString())) }
        }
    }

    private fun saveUserInformationWithImage( user: User, imageUri: Uri )
    {
        viewModelScope.launch {
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(getApplication<ShoppingApplication>().contentResolver, imageUri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory = storage.child("{Constants.PROFILE_USER_COLLECTION}/${auth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(imagePath = imageUrl), false)
            }
            catch (e: Exception) {
                viewModelScope.launch { _updateInfo.emit(Resource.Error(e.message.toString())) }
            }
        }
    }

    private fun validateInputs ( user: User ): Boolean {
        return validateEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty()
                && user.lastName.trim().isNotEmpty()
    }
}