package com.jodi.companioncompatibility.feature.home.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.liveData
import com.jodi.companioncompatibility.base.BaseViewModel
import com.jodi.companioncompatibility.feature.home.NavigationHome
import com.jodi.companioncompatibility.feature.home.model.GptRequestBody
import com.jodi.companioncompatibility.feature.home.model.Message
import com.jodi.companioncompatibility.feature.home.repository.HomeRepository
import com.jodi.companioncompatibility.utils.extensions.isNotNullOrEmpty
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

    val userName = ObservableField<String?>()
    val partnerName = ObservableField<String?>()
    val userDob = ObservableField<String?>()
    val partnerDob = ObservableField<String?>()
    val userTob = ObservableField<String?>()
    val partnerTob = ObservableField<String?>()
    val userPob = ObservableField<String?>()
    val partnerPob = ObservableField<String?>()


    val isButtonEnabled = ObservableField<Boolean>()

    private fun makeRequestContent(): String {
        return "Please consider yourself as an astrologer who can provide insights into the future based on Vedic Numerology. You are fair and provide both optimistic and pessimistic views. You also provide both positive and negative insights. I(${userName.get()}) am interested in a person(${partnerName.get()}) who could be my spouse. I was born on ${userDob.get()} at ${userTob.get()} in the city of ${userPob.get()} The person(${partnerName.get()}) whom I(${userName.get()}) wish to be my companion was born on ${partnerDob.get()} at ${partnerTob.get()} in the city of ${partnerPob.get()} Based on Vedic numerology could you please suggest what would be the compatibility?"
    }

    private fun makeRequestBody(): GptRequestBody? {
        return GptRequestBody(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message(
                    role = "user",
                    content = makeRequestContent()
                )
            )
        )
    }

    fun getButtonVisibility(): Boolean {
        isButtonEnabled.set(
            userName.get().isNotNullOrEmpty() &&
                    userDob.get().isNotNullOrEmpty() &&
                    userTob.get().isNotNullOrEmpty() &&
                    userPob.get().isNotNullOrEmpty() &&
                    partnerName.get().isNotNullOrEmpty() &&
                    partnerDob.get().isNotNullOrEmpty() &&
                    partnerTob.get().isNotNullOrEmpty() &&
                    partnerPob.get().isNotNullOrEmpty()
        )
        return isButtonEnabled.get() ?: false
    }

    fun callGpt() {
        getNavigator()?.callGpt()
    }

    fun _chatCompletion(
        requestBody: GptRequestBody? = makeRequestBody()
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
        requestBody: GptRequestBody? = makeRequestBody()
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