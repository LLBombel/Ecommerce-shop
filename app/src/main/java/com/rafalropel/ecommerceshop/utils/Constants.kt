package com.rafalropel.ecommerceshop.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "users"
    const val SHOPPOLANDIA_PREFERENES: String = "ShoppolandiaPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val IMAGE_REQUEST_CODE = 2
    const val MALE: String = "Mężczyzna"
    const val FEMALE: String = "Kobieta"
    const val PHONE_NUMBER: String = "phoneNumber"
    const val GENDER: String = "gender"
    const val PROFILE_IMAGE: String = "profile_image"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"
    const val PRODUCT_IMAGE: String = "Product_Image"
    const val PRODUCTS: String = "products"
    const val USER_ID: String = "user_id"
    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID: String = "extra_product_owner_id"
    const val DEFAULT_CART_QUANTITY: String = "1"
    const val CART_ITEMS: String = "cart_items"
    const val PRODUCT_ID: String = "product_id"

    fun imageChooser(activity: Activity) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(gallery, IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }

}