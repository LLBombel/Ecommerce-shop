package com.rafalropel.ecommerceshop

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rafalropel.ecommerceshop.databinding.ActivityRegisterBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.User

private lateinit var binding: ActivityRegisterBinding

@Suppress("DEPRECATION")
class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN


            )
        }

        binding.tvHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btRegister.setOnClickListener {

            signUpUser()


        }
    }

    private fun validateRegister(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etRegisterNamesurname.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Proszę wprowadzić imię i nazwisko", true)
                false

            }

            TextUtils.isEmpty(binding.etRegisterEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Proszę wprowadzić adres e-mail", true)
                false
            }
            TextUtils.isEmpty(binding.etRegisterPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Proszę wprowadzić hasło", true)
                false
            }
            TextUtils.isEmpty(
                binding.etRegisterPasswordConfirm.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Proszę potwierdzić hasło", true)
                false
            }

            binding.etRegisterPassword.text.toString()
                .trim { it <= ' ' } != binding.etRegisterPasswordConfirm.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar("Hasła nie pasują do siebie", true)
                false
            }
            else -> {
                //showErrorSnackBar("Dziękujemy za rejestracje!", false)
                true
            }
        }
    }

    private fun signUpUser() {
        if (validateRegister()) {
            val email: String = binding.etRegisterEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etRegisterPassword.text.toString().trim { it <= ' ' }


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                            firebaseUser.uid,
                            binding.etRegisterNamesurname.text.toString().trim{it <=' '},
                            binding.etRegisterEmail.text.toString().trim{it <=' '}

                        )
                        FireStoreClass().registerUser(this@RegisterActivity, user)

                        FirebaseAuth.getInstance().signOut()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }

                }
        }
    }

    fun registrationSuccess(){
        Toast.makeText(this, "Zarejestrowano pomyślnie", Toast.LENGTH_LONG).show()
    }

}