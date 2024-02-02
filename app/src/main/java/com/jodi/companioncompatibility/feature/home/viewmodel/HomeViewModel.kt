package com.jodi.companioncompatibility.feature.home.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.liveData
import com.jodi.companioncompatibility.base.BaseViewModel
import com.jodi.companioncompatibility.feature.home.NavigationHome
import com.jodi.companioncompatibility.feature.home.model.GptRequestBody
import com.jodi.companioncompatibility.feature.home.model.Message
import com.jodi.companioncompatibility.feature.home.repository.HomeRepository
import com.jodi.companioncompatibility.utils.network.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) :
    BaseViewModel<NavigationHome>() {

    val content = ObservableField<String?>()

    fun callGpt() {
        getNavigator()?.callGpt()
    }

    fun _chatCompletion(
        requestBody: GptRequestBody? = GptRequestBody(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message(
                    role = "user",
                    content = "Please consider yourself as an astrologer who can provide insights into the future based on Vedic Numerology. You are fair and provide both optimistic and pessimistic views. You also provide both positive and negative insights. I am interested in a person who could be my spouse. I was born on 1st of May 1997 in the city of Bangalore in Karnataka. The person whom I wish to be my companion was born on 2nd of May 1997 in the city of Mysore in Karnataka. Based on Vedic numerology could you please suggest what would be the compatibility?"
                )
            )
        )
    ) {
        execute {
            setIsLoading(true)
            kotlin.runCatching {
                repository._chatCompletion(requestBody)
            }.onSuccess {
                it?.choices?.get(0).let { choice ->
                    content.set(choice?.message?.content)
                }
            }.onFailure {

            }
            setIsLoading(false)
        }
    }

    fun chatCompletion(
        requestBody: GptRequestBody? = GptRequestBody(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message(
                    role = "user",
                    content = "Please consider yourself as an astrologer who can provide insights into the future based on Vedic Numerology. You are fair and provide both optimistic and pessimistic views. You also provide both positive and negative insights. I am interested in a person who could be my spouse. I was born on 1st of May 1997 in the city of Bangalore in Karnataka. The person whom I wish to be my companion was born on 2nd of May 1997 in the city of Mysore in Karnataka. Based on Vedic numerology could you please suggest what would be the compatibility?"
                )
            )
        )
    ) = liveData {
        repository.chatCompletion(requestBody).onStart {
            setIsLoading(true)
            emit(ResultOf.Progress(true))
        }.onCompletion {
            setIsLoading(false)
            emit(ResultOf.Progress(false))
        }.catch {
            setIsLoading(false)
            emit(ResultOf.Failure(it.message, it))
        }.collect {
            setIsLoading(false)
            it?.choices?.get(0).let { choice ->
                content.set(choice?.message?.content)
            }
            emit(ResultOf.Success(it))
        }
    }
}