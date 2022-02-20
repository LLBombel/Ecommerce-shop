

package com.rafalropel.ecommerceshop

import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
open class BaseActivity : AppCompatActivity() {

    private var doubleExit = false

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.ThemePink
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.green
                )
            )
        }
        snackBar.show()
    }

    fun doubleBackExit(){
        if(doubleExit){
            super.onBackPressed()
            return
        }

        this.doubleExit = true

        Toast.makeText(this, resources.getString(R.string.double_click), Toast.LENGTH_SHORT).show()

        Handler().postDelayed({doubleExit = false}, 2000)
    }

}