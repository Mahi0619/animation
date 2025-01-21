package com.anime.application.dashBoard.fragment.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anime.application.R
import com.anime.application.dashBoard.fragment.model.NewAnimeResponse
import com.anime.application.others.CallBack
import java.net.HttpURLConnection
import java.net.URL


class AnimeAdapter(private var animeList: List<NewAnimeResponse.Data>,val onItemClick: CallBack<NewAnimeResponse.Data>) :
    RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {
    // Local cache to store bitmaps
    private val localCache: HashMap<String, Bitmap> = HashMap()

    inner class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val episodes: TextView = itemView.findViewById(R.id.tvEpisodes)
        val score: TextView = itemView.findViewById(R.id.tvScore)
        val image: ImageView = itemView.findViewById(R.id.ivAnimeImage)
        val aDetail: RelativeLayout = itemView.findViewById(R.id.rlMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anime, parent, false)
        return AnimeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]

        holder.title.text = anime.title
        holder.episodes.text = "Episodes: ${anime.episodes}"
        holder.score.text = "Score: ${anime.score}"
        holder.aDetail.setOnClickListener {
            onItemClick.onSuccess(anime)

        }

        if (anime.images.jpg.smallImageUrl.isNullOrEmpty()) {
            Log.e("AnimeAdapter", "Image URL is empty or null: ${anime.images.jpg.smallImageUrl}")
            holder.image.setImageResource(R.drawable.placeholder_loader) // Show placeholder
            return
        } else {
            loadImageFromNetwork(anime.images.jpg.smallImageUrl, holder.image)
        }
    }

    override fun getItemCount(): Int = animeList.size

    private fun loadImageFromNetwork(imgUrl: String, imageView: ImageView) {
        val imageUrl = imgUrl
        imageView.setImageResource(R.drawable.placeholder_loader)

        // Check if the bitmap exists in the local cache
        val cachedBitmap = localCache[imageUrl]
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap) // Use cached image
        } else {
            val thread = Thread {
                try {
                    val url = URL(imageUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val inputStream = connection.inputStream
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    // Set the bitmap to the ImageView on the main UI thread
                    imageView.post {
                        imageView.setImageBitmap(bitmap)
                    }

                    // Cache the bitmap locally
                    localCache[imageUrl] = bitmap

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
    }

    // Function to update the anime list with filtered data
    fun updateAnimeList(newList: List<NewAnimeResponse.Data>) {
        this.animeList = newList
        notifyDataSetChanged() // Notify adapter to refresh the views
    }
}
