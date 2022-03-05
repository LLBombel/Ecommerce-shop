package com.rafalropel.ecommerceshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val user_id: String = "",
    val product_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    var cart_amount: String = "",
    var amount: String = "",
    var id: String = ""
): Parcelable
