package com.iamshekhargh.wikidhundoo.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Terms(
    val alias: List<String>,
    val description: List<String>,
    val label: List<String>
) : Parcelable