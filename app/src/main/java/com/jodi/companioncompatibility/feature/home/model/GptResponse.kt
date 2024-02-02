package com.jodi.companioncompatibility.feature.home.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GptResponse(
    @Json(name = "choices") val choices: List<Choice>,
    @Json(name = "created") val created: Int,
    @Json(name = "id") val id: String,
    @Json(name = "model") val model: String,
    @Json(name = "object") val `object`: String,
    @Json(name = "system_fingerprint") val system_fingerprint: Any,
    @Json(name = "usage") val usage: Usage
)

@JsonClass(generateAdapter = true)
data class Choice(
    @Json(name = "finish_reason") val finish_reason: String,
    @Json(name = "index") val index: Int,
    @Json(name = "logprobs") val logprobs: Any,
    @Json(name = "message") val message: Message
)

@JsonClass(generateAdapter = true)
data class Usage(
    @Json(name = "completion_tokens") val completion_tokens: Int,
    @Json(name = "prompt_tokens") val prompt_tokens: Int,
    @Json(name = "total_tokens") val total_tokens: Int
)