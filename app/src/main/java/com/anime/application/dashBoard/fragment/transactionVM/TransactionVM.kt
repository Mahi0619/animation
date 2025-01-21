package com.anime.application.dashBoard.fragment.transactionVM

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.anime.application.data.repository.MainRepository
import com.anime.application.data.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject



/*@HiltViewModel
class TransactionVM @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel()
{

    private val _apiAnimeStateFlow: MutableStateFlow<ApiState<JsonObject>> =
        MutableStateFlow(ApiState.Empty)
    val apiAnimeStateFlow: StateFlow<ApiState<JsonObject>> = _apiAnimeStateFlow

    var currentPage = 1
    var hasNextPage = true

    // Fetches data with pagination
    fun getAnim(page: Int = currentPage) {
        viewModelScope.launch {
            // Emit loading state explicitly when the request starts
            _apiAnimeStateFlow.value = ApiState.Loading

            mainRepository.getAnime(page).collect { state ->
                when (state) {
                    is ApiState.Success -> {
                        // Check if pagination data exists and handle it
                        val pagination = state.data?.getAsJsonObject("pagination")
                        val hasMorePages = pagination?.get("has_next_page")?.asBoolean ?: false
                        val currentPage = pagination?.get("current_page")?.asInt ?: 1

                        // Update pagination state
                        this@TransactionVM.currentPage = currentPage
                        this@TransactionVM.hasNextPage = hasMorePages

                        // Emit the successful data response
                        _apiAnimeStateFlow.value = state
                        Log.d("AnimeDataVM", "Data received: ${state.data}")
                    }
                    is ApiState.Failure -> {
                        handleAnimeFailure(state.error)
                    }
                    else -> {
                        // Loading state already set above
                        Log.d("API_State", "Loading...")
                    }
                }
            }
        }
    }

    // Handle failure with more detailed error information
    private fun handleAnimeFailure(error: Throwable) {
        // Simplified error handling with more informative messages
        when (val httpException = error as? HttpException) {
            null -> {
                // Non-HTTP errors
                Log.e("API_State_Error", "Unknown Error: ${error.message}")
                _apiAnimeStateFlow.value = ApiState.Failure(Exception("Unknown error occurred: ${error.message}"))
            }
            else -> {
                // HTTP errors
                val errorMessage = when (httpException.code()) {
                    400 -> "Bad Request: Invalid parameters or request body"
                    401 -> "Unauthorized: Invalid username or password"
                    403 -> "Forbidden: You don't have permission to access this resource"
                    500 -> "Internal Server Error: Something went wrong on the server"
                    else -> "HTTP Error ${httpException.code()}: ${httpException.message()}"
                }

                Log.e("API_State_Error_${httpException.code()}", "Error: $errorMessage")
                _apiAnimeStateFlow.value = ApiState.Failure(Exception(errorMessage))
            }
        }
    }

    // Method to check if there is a next page
    fun hasNextPage(): Boolean {
        return hasNextPage
    }
}*/



//2nd page loaging and updating
/*@HiltViewModel
class TransactionVM @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _apiAnimeStateFlow: MutableStateFlow<ApiState<JsonObject>> =
        MutableStateFlow(ApiState.Empty)
    val apiAnimeStateFlow: StateFlow<ApiState<JsonObject>> = _apiAnimeStateFlow

    var currentPage = 1
    var hasNextPage = true

    fun getAnim(page: Int = currentPage) {
        viewModelScope.launch {
            _apiAnimeStateFlow.value = ApiState.Loading

            mainRepository.getAnime(page).collect { state ->
                when (state) {
                    is ApiState.Success -> {
                        val pagination = state.data?.getAsJsonObject("pagination")
                        val hasMorePages = pagination?.get("has_next_page")?.asBoolean ?: false
                        val currentPage = pagination?.get("current_page")?.asInt ?: 1

                        // Update pagination state
                        this@TransactionVM.currentPage = currentPage
                        this@TransactionVM.hasNextPage = hasMorePages

                        _apiAnimeStateFlow.value = state
                        Log.d("AnimeDataVM", "Data received: ${state.data}")
                    }
                    is ApiState.Failure -> {
                        handleAnimeFailure(state.error)
                    }
                    else -> {
                        Log.d("API_State", "Loading...")
                    }
                }
            }
        }
    }

    private fun handleAnimeFailure(error: Throwable) {
        when (val httpException = error as? HttpException) {
            null -> {
                Log.e("API_State_Error", "Unknown Error: ${error.message}")
                _apiAnimeStateFlow.value = ApiState.Failure(Exception("Unknown error occurred: ${error.message}"))
            }
            else -> {
                val errorMessage = when (httpException.code()) {
                    400 -> "Bad Request: Invalid parameters or request body"
                    401 -> "Unauthorized: Invalid username or password"
                    403 -> "Forbidden: You don't have permission to access this resource"
                    500 -> "Internal Server Error: Something went wrong on the server"
                    else -> "HTTP Error ${httpException.code()}: ${httpException.message()}"
                }
                Log.e("API_State_Error_${httpException.code()}", "Error: $errorMessage")
                _apiAnimeStateFlow.value = ApiState.Failure(Exception(errorMessage))
            }
        }
    }
}*/

@HiltViewModel
class TransactionVM @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel()
{

    private val _apiAnimeStateFlow: MutableStateFlow<ApiState<JsonObject>> =
        MutableStateFlow(ApiState.Empty)
    val apiAnimeStateFlow: StateFlow<ApiState<JsonObject>> = _apiAnimeStateFlow

    private val _apiAnimeDetailStateFlow: MutableStateFlow<ApiState<JsonObject>> =
        MutableStateFlow(ApiState.Empty)
    val apiAnimeDetailStateFlow: StateFlow<ApiState<JsonObject>> = _apiAnimeDetailStateFlow

    var currentPage = 1
    var hasNextPage = true

    fun getAnim(page: Int = currentPage) {
        viewModelScope.launch {
            _apiAnimeStateFlow.value = ApiState.Loading

            mainRepository.getAnime(page).collect { state ->
                when (state) {
                    is ApiState.Success -> {
                        val pagination = state.data?.getAsJsonObject("pagination")
                        val hasMorePages = pagination?.get("has_next_page")?.asBoolean ?: false
                        val newCurrentPage = pagination?.get("current_page")?.asInt ?: currentPage

                        // Safely update the current page
                        currentPage = newCurrentPage
                        hasNextPage = hasMorePages

                        _apiAnimeStateFlow.value = state // Notify UI with the new state
                    }
                    is ApiState.Failure -> {
                        handleAnimeFailure(state.error)
                    }
                    else -> {
                        Log.d("API_State", "Loading...")
                    }
                }
            }
        }
    }


    fun getAnimDetail(id:Int) {
        viewModelScope.launch {
            _apiAnimeDetailStateFlow.value = ApiState.Loading
            mainRepository.getAnimeDetail(id).collect { state ->
                when (state) {
                    is ApiState.Success -> {

                        _apiAnimeDetailStateFlow.value = state // Notify UI with the new state

                    }
                    is ApiState.Failure -> {
                        handleAnimeFailure(state.error)
                    }
                    else -> {
                        Log.d("API_State", "Loading...")
                    }
                }
            }
        }
    }





    private fun handleAnimeFailure(error: Throwable) {
        when (val httpException = error as? HttpException) {
            null -> {
                Log.e("API_State_Error", "Unknown Error: ${error.message}")
                _apiAnimeStateFlow.value = ApiState.Failure(Exception("Unknown error occurred: ${error.message}"))
            }
            else -> {
                val errorMessage = when (httpException.code()) {
                    400 -> "Bad Request: Invalid parameters or request body"
                    401 -> "Unauthorized: Invalid username or password"
                    403 -> "Forbidden: You don't have permission to access this resource"
                    500 -> "Internal Server Error: Something went wrong on the server"
                    else -> "HTTP Error ${httpException.code()}: ${httpException.message()}"
                }
                Log.e("API_State_Error_${httpException.code()}", "Error: $errorMessage")
                _apiAnimeStateFlow.value = ApiState.Failure(Exception(errorMessage))
            }
        }
    }
}


















/*@HiltViewModel
class TransactionVM @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel()
{

    private val _apiAnimeStateFlow: MutableStateFlow<ApiState<JsonObject>> =
        MutableStateFlow(ApiState.Empty)
    val apiAnimeStateFlow: StateFlow<ApiState<JsonObject>> = _apiAnimeStateFlow

    fun getAnim() {
        viewModelScope.launch {
            // Emit loading state explicitly when the request starts
            _apiAnimeStateFlow.value = ApiState.Loading
            mainRepository.getAnime()
                .collect { state ->
                    when (state) {
                        is ApiState.Success<JsonObject> -> {
                            _apiAnimeStateFlow.value = state
                            Log.d("AnimeDataVM", "flatMapped data: ${state.data}")
                        }
                        is ApiState.Failure -> {
                            handleAnimeFailure(state.error)
                        }
                        else -> {
                            // You already have loading set above
                            Log.d("API_State", "Loading...")
                        }
                    }
                }
        }
    }

    private fun handleAnimeFailure(error: Throwable) {
        // Simplified error handling and provide more informative message
        when (val httpException = error as? HttpException) {
            null -> {
                // Non-HTTP errors
                Log.e("API_State_Error", "Unknown Error: ${error.message}")
                _apiAnimeStateFlow.value = ApiState.Failure(Exception("Unknown error occurred: ${error.message}"))
            }
            else -> {
                // HTTP errors
                val errorMessage = when (httpException.code()) {
                    400 -> "Bad Request: Invalid parameters or request body"
                    401 -> "Unauthorized: Invalid username or password"
                    403 -> "Forbidden: You don't have permission to access this resource"
                    500 -> "Internal Server Error: Something went wrong on the server"
                    else -> "HTTP Error ${httpException.code()}: ${httpException.message()}"
                }

                Log.e("API_State_Error_${httpException.code()}", "Error: $errorMessage")
                _apiAnimeStateFlow.value = ApiState.Failure(Exception(errorMessage))
            }

        }
    }
}*/

