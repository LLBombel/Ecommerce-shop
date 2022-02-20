package com.rafalropel.ecommerceshop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rafalropel.ecommerceshop.LoginActivity
import com.rafalropel.ecommerceshop.RegisterActivity
import com.rafalropel.ecommerceshop.SettingsActivity
import com.rafalropel.ecommerceshop.UserProfileActivity
import com.rafalropel.ecommerceshop.model.User
import com.rafalropel.ecommerceshop.utils.Constants

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.registrationSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""

        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.SHOPPOLANDIA_PREFERENES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME, "${user.nameSurname}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.logInSuccess(user)
                    }

                    is SettingsActivity ->{
                        activity.userDetailsSuccess(user)

                    }
                }
            }
    }

    fun updateUser(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        Log.e(activity.javaClass.simpleName, "Błąd", e)
                    }
                }
            }

    }

    fun uploadImage(activity: Activity, imageFileURI: Uri?) {


        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.PROFILE_IMAGE + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )


        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->

                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()

                )


                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        when(activity){
                            is UserProfileActivity ->{
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }

                    }
            }
            .addOnFailureListener { exception ->


                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

}