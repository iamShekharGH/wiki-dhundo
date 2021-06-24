package com.iamshekhargh.wikidhundoo.network.response.itemResponse

import com.iamshekhargh.wikidhundoo.network.response.Thumbnail

data class Page(
    val extract: String,
    val ns: Int,
    val pageid: Int,
    val pageimage: String,
    val thumbnail: Thumbnail,
    val title: String
)