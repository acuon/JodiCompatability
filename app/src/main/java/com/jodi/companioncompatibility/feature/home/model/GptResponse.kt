package com.jodi.companioncompatibility.feature.home.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class GptResponse(
    @Json(name = "choices") val choices: List<Choice?>? = null,
    @Json(name = "created") val created: Int? = null,
    @Json(name = "id") val id: String? = null,
    @Json(name = "model") val model: String? = null,
    @Json(name = "object") val `object`: String? = null,
    @Json(name = "system_fingerprint") val system_fingerprint: String? = null,
    @Json(name = "usage") val usage: Usage? = null
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Choice(
    @Json(name = "finish_reason") val finish_reason: String? = null,
    @Json(name = "index") val index: Int? = null,
    @Json(name = "logprobs") val logprobs: String? = null,
    @Json(name = "message") val message: Message? = null
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Usage(
    @Json(name = "completion_tokens") val completion_tokens: Int? = null,
    @Json(name = "prompt_tokens") val prompt_tokens: Int? = null,
    @Json(name = "total_tokens") val total_tokens: Int? = null
) : Parcelable