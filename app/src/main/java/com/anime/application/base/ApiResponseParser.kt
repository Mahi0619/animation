package com.anime.application.base

import org.json.JSONObject
import retrofit2.HttpException


object ApiResponseParser {
    fun parseApiError(e: Throwable): String {
        return if (e is HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            try {
                // Parse the error response
                val errorJson = JSONObject(errorBody ?: "")
                errorJson.optString("message", "Unknown error")
            } catch (ex: Exception) {
                "Failed to parse error response"
            }
        } else {
            e.message ?: "Unknown error"
        }
    }
}