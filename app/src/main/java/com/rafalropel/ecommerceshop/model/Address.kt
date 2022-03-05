package com.rafalropel.ecommerceshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val user_id: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var address: String = "",
    var zipCode: String = "",
    var additionalNotes: String = "",
    val type: String = "",
    val otherDetails: String = "",
    var id: String = ""
): Parcelable
