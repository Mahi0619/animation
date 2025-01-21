package com.anime.application.dashBoard.fragment.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class AnimeDetailBean(
    @SerializedName("data")
    @Expose
    val `data`: Data
) {
    data class Data(
        @SerializedName("aired")
        @Expose
        val aired: Aired,
        @SerializedName("airing")
        @Expose
        val airing: Boolean,
        @SerializedName("approved")
        @Expose
        val approved: Boolean,
        @SerializedName("background")
        @Expose
        val background: String,
        @SerializedName("broadcast")
        @Expose
        val broadcast: Broadcast,
        @SerializedName("demographics")
        @Expose
        val demographics: List<Demographic>,
        @SerializedName("duration")
        @Expose
        val duration: String,
        @SerializedName("episodes")
        @Expose
        val episodes: Int,
        @SerializedName("explicit_genres")
        @Expose
        val explicitGenres: List<Any>,
        @SerializedName("favorites")
        @Expose
        val favorites: Int,
        @SerializedName("genres")
        @Expose
        val genres: List<Genre>,
        @SerializedName("images")
        @Expose
        val images: Images,
        @SerializedName("licensors")
        @Expose
        val licensors: List<Licensor>,
        @SerializedName("mal_id")
        @Expose
        val malId: Int,
        @SerializedName("members")
        @Expose
        val members: Int,
        @SerializedName("popularity")
        @Expose
        val popularity: Int,
        @SerializedName("producers")
        @Expose
        val producers: List<Producer>,
        @SerializedName("rank")
        @Expose
        val rank: Int,
        @SerializedName("rating")
        @Expose
        val rating: String,
        @SerializedName("score")
        @Expose
        val score: Double,
        @SerializedName("scored_by")
        @Expose
        val scoredBy: Int,
        @SerializedName("season")
        @Expose
        val season: String,
        @SerializedName("source")
        @Expose
        val source: String,
        @SerializedName("status")
        @Expose
        val status: String,
        @SerializedName("studios")
        @Expose
        val studios: List<Studio>,
        @SerializedName("synopsis")
        @Expose
        val synopsis: String,
        @SerializedName("themes")
        @Expose
        val themes: List<Theme>,
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("title_english")
        @Expose
        val titleEnglish: String,
        @SerializedName("title_japanese")
        @Expose
        val titleJapanese: String,
        @SerializedName("title_synonyms")
        @Expose
        val titleSynonyms: List<Any>,
        @SerializedName("titles")
        @Expose
        val titles: List<Title>,
        @SerializedName("trailer")
        @Expose
        val trailer: Trailer,
        @SerializedName("type")
        @Expose
        val type: String,
        @SerializedName("url")
        @Expose
        val url: String,
        @SerializedName("year")
        @Expose
        val year: Int
    ) {
        data class Aired(
            @SerializedName("from")
            @Expose
            val from: String,
            @SerializedName("prop")
            @Expose
            val prop: Prop,
            @SerializedName("string")
            @Expose
            val string: String,
            @SerializedName("to")
            @Expose
            val to: String
        ) {
            data class Prop(
                @SerializedName("from")
                @Expose
                val from: From,
                @SerializedName("to")
                @Expose
                val to: To
            ) {
                data class From(
                    @SerializedName("day")
                    @Expose
                    val day: Int,
                    @SerializedName("month")
                    @Expose
                    val month: Int,
                    @SerializedName("year")
                    @Expose
                    val year: Int
                )

                data class To(
                    @SerializedName("day")
                    @Expose
                    val day: Int,
                    @SerializedName("month")
                    @Expose
                    val month: Int,
                    @SerializedName("year")
                    @Expose
                    val year: Int
                )
            }
        }

        data class Broadcast(
            @SerializedName("day")
            @Expose
            val day: String,
            @SerializedName("string")
            @Expose
            val string: String,
            @SerializedName("time")
            @Expose
            val time: String,
            @SerializedName("timezone")
            @Expose
            val timezone: String
        )

        data class Demographic(
            @SerializedName("mal_id")
            @Expose
            val malId: Int,
            @SerializedName("name")
            @Expose
            val name: String,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("url")
            @Expose
            val url: String
        )

        data class Genre(
            @SerializedName("mal_id")
            @Expose
            val malId: Int,
            @SerializedName("name")
            @Expose
            val name: String,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("url")
            @Expose
            val url: String
        )

        data class Images(
            @SerializedName("jpg")
            @Expose
            val jpg: Jpg,
            @SerializedName("webp")
            @Expose
            val webp: Webp
        ) {
            data class Jpg(
                @SerializedName("image_url")
                @Expose
                val imageUrl: String,
                @SerializedName("large_image_url")
                @Expose
                val largeImageUrl: String,
                @SerializedName("small_image_url")
                @Expose
                val smallImageUrl: String
            )

            data class Webp(
                @SerializedName("image_url")
                @Expose
                val imageUrl: String,
                @SerializedName("large_image_url")
                @Expose
                val largeImageUrl: String,
                @SerializedName("small_image_url")
                @Expose
                val smallImageUrl: String
            )
        }

        data class Licensor(
            @SerializedName("mal_id")
            @Expose
            val malId: Int,
            @SerializedName("name")
            @Expose
            val name: String,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("url")
            @Expose
            val url: String
        )

        data class Producer(
            @SerializedName("mal_id")
            @Expose
            val malId: Int,
            @SerializedName("name")
            @Expose
            val name: String,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("url")
            @Expose
            val url: String
        )

        data class Studio(
            @SerializedName("mal_id")
            @Expose
            val malId: Int,
            @SerializedName("name")
            @Expose
            val name: String,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("url")
            @Expose
            val url: String
        )

        data class Theme(
            @SerializedName("mal_id")
            @Expose
            val malId: Int,
            @SerializedName("name")
            @Expose
            val name: String,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("url")
            @Expose
            val url: String
        )

        data class Title(
            @SerializedName("title")
            @Expose
            val title: String,
            @SerializedName("type")
            @Expose
            val type: String
        )

        data class Trailer(
            @SerializedName("embed_url")
            @Expose
            val embedUrl: String,
            @SerializedName("images")
            @Expose
            val images: Images,
            @SerializedName("url")
            @Expose
            val url: String,
            @SerializedName("youtube_id")
            @Expose
            val youtubeId: String
        ) {
            data class Images(
                @SerializedName("image_url")
                @Expose
                val imageUrl: String,
                @SerializedName("large_image_url")
                @Expose
                val largeImageUrl: String,
                @SerializedName("maximum_image_url")
                @Expose
                val maximumImageUrl: String,
                @SerializedName("medium_image_url")
                @Expose
                val mediumImageUrl: String,
                @SerializedName("small_image_url")
                @Expose
                val smallImageUrl: String
            )
        }
    }
}