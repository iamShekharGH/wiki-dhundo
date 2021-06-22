package com.iamshekhargh.wikidhundoo.network.response

data class Thumbnail(
    val height: Int,
    val source: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/480px-No_image_available.svg.png",
    val width: Int
)