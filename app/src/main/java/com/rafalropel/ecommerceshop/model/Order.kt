package com.rafalropel.ecommerceshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<Cart> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val subtotal_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val order_date: Long = 0,
    var id: String = ""
    ): Parcelable
