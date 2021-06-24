package com.iamshekhargh.wikidhundoo.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thumbnail(
    val height: Int,
    val source: String = "",
    val width: Int
) : Parcelable