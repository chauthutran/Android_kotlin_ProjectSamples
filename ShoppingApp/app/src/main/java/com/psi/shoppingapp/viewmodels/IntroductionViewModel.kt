package com.psi.shoppingapp.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.psi.shoppingapp.R
import com.psi.shoppingapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate = _navigate

    companion object {
        const val SHOPPING_ACTIVITY = -1
        val ACCOUNT_OPTIONS_FRAGMENT = R.id.action_introductionFragment_to_accountOptionsFragment
    }

    init {
        println("===== INIT IntroductionViewModel")

        val isButtonClicked = sharedPreferences.getBoolean(Constants.INTRODUCTION_KEY,false)
        val user = firebaseAuth.currentUser

        if (user != null){
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        }else if (isButtonClicked){
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
            }
        }else{
            Unit
        }
    }

    fun startButtonClick(){
        sharedPreferences.edit().putBoolean(Constants.INTRODUCTION_KEY,true).apply()
    }
}