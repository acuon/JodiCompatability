package com.jodi.companioncompatibility.data.remote

import com.jodi.companioncompatibility.feature.home.model.GptRequestBody
import com.jodi.companioncompatibility.feature.home.model.GptResponse
import com.jodi.companioncompatibility.utils.AppInfoManager
import retrofit2.http.Body
import retrofit2.http.POST

interface GptService {

    @POST(AppInfoManager.GPT.CHAT_COMPLETIONS)
    suspend fun gptChatCompletions(@Body requestBody: GptRequestBody?): GptResponse?

}