package com.iamshekhargh.wikidhundoo.network.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "pages_list")
@Parcelize
data class Page(
    val index: Int,
    val ns: Int,
    val pageimage: String,
    val terms: Terms,
    @PrimaryKey() val pageid: Int,
    val thumbnail: Thumbnail?,
    val title: String
) : Parcelable