package com.anime.application.data.network



import com.google.gson.JsonObject
import com.anime.application.others.Cons
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET(Cons.ANIME)
    suspend fun getAnimeList(
        @Query("page") page: Int, // Query parameter
    ): Response<JsonObject> // Use Response wrapper for Retrofit


    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") animeId: Int // Path parameter for anime ID
    ): Response<JsonObject> // Use Response wrapper for Retrofit

}









