package com.iamshekhargh.wikidhundoo.network.response

data class Page(
    val index: Int,
    val ns: Int,
    val pageid: Int,
    val pageimage: String,
    val terms: Terms,
    val thumbnail: Thumbnail,
    val title: String
)