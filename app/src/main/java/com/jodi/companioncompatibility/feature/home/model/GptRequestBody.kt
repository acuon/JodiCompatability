package com.jodi.companioncompatibility.feature.home.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GptRequestBody(
    @Json(name = "messages") val messages: List<Message?>? = null,
    @Json(name = "model") val model: String? = null
)

@JsonClass(generateAdapter = true)
data class Message(
    @Json(name = "content") val content: String? = null,
    @Json(name = "role") val role: String? = null
)
