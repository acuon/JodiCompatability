package com.jodi.companioncompatibility.feature.home.repository

import com.jodi.companioncompatibility.data.remote.GptService
import com.jodi.companioncompatibility.feature.home.model.GptRequestBody
import com.jodi.companioncompatibility.feature.home.model.GptResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepository @Inject constructor(private val service: GptService) {

    suspend fun chatCompletion(requestBody: GptRequestBody?): Flow<GptResponse?> = flow {
        emit(service.gptChatCompletions(requestBody))
    }.flowOn(Dispatchers.Main)

    suspend fun _chatCompletion(requestBody: GptRequestBody?): GptResponse? {
        return service.gptChatCompletions(requestBody)
    }

}