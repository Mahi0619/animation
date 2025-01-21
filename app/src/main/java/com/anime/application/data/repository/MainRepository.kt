package com.anime.application.data.repository

import com.google.gson.JsonObject
import com.anime.application.data.network.ApiService
import com.anime.application.data.util.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAnimeDetail(animeId: Int): Flow<ApiState<JsonObject>> = flow {
        emit(ApiState.Loading)
        try {
            val response = apiService.getAnimeDetails(animeId) // Use animeId instead of page
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ApiState.Success(it))
                } ?: emit(ApiState.Failure(Exception("Empty response body")))
            } else {
                emit(ApiState.Failure(Exception("Failed to fetch data: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(ApiState.Failure(e)) // Handle any other exceptions
        }
    }

    suspend fun getAnime(page: Int): Flow<ApiState<JsonObject>> = flow {
        emit(ApiState.Loading)
        try {
            val response = apiService.getAnimeList(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ApiState.Success(it))
                } ?: emit(ApiState.Failure(Exception("Empty response body")))
            } else {
                emit(ApiState.Failure(Exception("Failed to fetch data: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(ApiState.Failure(e)) // Handle any other exceptions
        }
    }

}
