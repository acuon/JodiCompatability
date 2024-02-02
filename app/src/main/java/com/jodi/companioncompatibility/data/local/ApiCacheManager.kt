package com.jodi.companioncompatibility.data.local

import android.content.Context
import com.jodi.companioncompatibility.utils.AppInfoManager
import com.google.gson.Gson
import java.lang.reflect.Type
import kotlin.reflect.full.declaredMemberProperties

class ApiCacheManager(context: Context) {

    private val gson = Gson()

    private val objectCacheMap = HashMap<String, String>()
    private val timeCacheMap = HashMap<String, Long>()

    private val EXPIRATION_TIME_KEY_SUFFIX = "_expiration_time"
    private val API_RESPONSE_KEY_SUFFIX = "_api_response"

    fun saveApiResponse(key: String, data: Any) {
        val currentTimeMillis = System.currentTimeMillis()
        val expirationTimeMillis = currentTimeMillis + 10 * 60 * 1000

        val expirationTimeKey = key + EXPIRATION_TIME_KEY_SUFFIX
        val apiResponseKey = key + API_RESPONSE_KEY_SUFFIX

        objectCacheMap[apiResponseKey] = gson.toJson(data)
        timeCacheMap[expirationTimeKey] = expirationTimeMillis
    }

    fun <T> getApiResponse(key: String, type: Type): T? {
        val currentTimeMillis = System.currentTimeMillis()
        val expirationTimeMillis: Long = timeCacheMap[key + EXPIRATION_TIME_KEY_SUFFIX] ?: 0

        return if (currentTimeMillis < expirationTimeMillis) {
            val apiResponseKey = key + API_RESPONSE_KEY_SUFFIX
            val json = objectCacheMap[apiResponseKey]
            gson.fromJson(json, type)
        } else {
            clearCache(key)
            null
        }
    }

    private fun getAllCacheKeyValues(): Map<String, String> {
        AppInfoManager.CACHE.let { obj ->
            return obj::class.declaredMemberProperties
                .filter { it.isConst }
                .associate { it.name to it.getter.call(obj).toString() }
        }
    }

    fun clearAllCache(key: String? = null) {
        getAllCacheKeyValues().forEach { (_, value) ->
            objectCacheMap.clear()
            timeCacheMap.clear()
        }
    }

    private fun clearCache(key: String) {
        objectCacheMap.remove(key + API_RESPONSE_KEY_SUFFIX)
        timeCacheMap.remove(key + EXPIRATION_TIME_KEY_SUFFIX)
    }
}
