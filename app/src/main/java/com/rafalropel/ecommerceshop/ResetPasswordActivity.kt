package com.rafalropel.ecommerceshop

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rafalropel.ecommerceshop.databinding.ActivityResetPasswordBinding

private lateinit var binding: ActivityResetPasswordBinding

@Suppress("DEPRECATION")
class ResetPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN


            )
        }

        binding.btResetPasswordSubmit.setOnClickListener {
            if (binding.etResetPasswordEmail.text.toString().isEmpty()) {
                showErrorSnackBar("Proszę wprowadzić adres e-mail", true)
            } else {
                val email: String = binding.etResetPasswordEmail.text.toString().trim { it <= ' ' }

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Na podany adres została wysłana wiadomość",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }
    }
}