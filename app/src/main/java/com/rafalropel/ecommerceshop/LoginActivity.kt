package com.rafalropel.ecommerceshop

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.rafalropel.ecommerceshop.databinding.ActivityLoginBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.User
import com.rafalropel.ecommerceshop.utils.Constants

private lateinit var binding: ActivityLoginBinding

@Suppress("DEPRECATION")
class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN


            )
        }

        binding.tvNoAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvPasswordForgot.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btLogin.setOnClickListener {
            logInUser()
        }
    }


    private fun validateLogin(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etLoginEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Proszę wprowadzić e-mail", true)
                false
            }
            TextUtils.isEmpty(binding.etLoginPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Proszę wprowadzić hasło", true)
                false
            }

            else -> {
                //showErrorSnackBar("Zalogowano pomyślnie", false)
                true
            }


        }
    }

    private fun logInUser() {
        if (validateLogin()) {

            val email = binding.etLoginEmail.text.toString().trim { it <= ' ' }
            val password = binding.etLoginPassword.text.toString().trim { it <= ' ' }


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FireStoreClass().getUserDetails(this@LoginActivity)

                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }

    }

    fun logInSuccess(user: User) {



        if(user.profileCompleted==0){
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }else{
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }

        finish()

    }
}


