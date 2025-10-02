package com.example.hakey.ui.login.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isEmailError = MutableLiveData<Boolean>()
    val isEmailError: LiveData<Boolean> = _isEmailError

    private val _isPasswordError = MutableLiveData<Boolean>()
    val isPasswordError: LiveData<Boolean> = _isPasswordError

    fun onLoginDataChanged(email: String, pass: String) {
        _email.value = email
        _password.value = pass
        _isEmailError.value = !isValidEmail(email)
        _isPasswordError.value = !isValidPassword(pass)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 8
    }

    fun onLoginClicked() {
        // Here you would normally trigger the login process
    }
}
