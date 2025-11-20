package com.example.gogym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // Kiểm tra xem đã đăng nhập chưa
    fun checkAuthState() {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    // Đăng nhập
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    // Đăng ký
    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    // Đăng xuất
    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

// Trạng thái đăng nhập
sealed class AuthState {
    object Unauthenticated : AuthState()
    object Authenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
