package com.rafalropel.ecommerceshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val nameSurname: String = "",
    val email: String = "",
    val image: String = "",
    val gender: String = "",
    val phoneNumber: Long = 0,
    val profileCompleted: Int = 0
):Parcelable
