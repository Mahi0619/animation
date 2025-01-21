package com.anime.application.dashBoard.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.anime.application.R
import com.anime.application.base.ErrorDialogHelper
import com.anime.application.dashBoard.youtube.FullscreenExampleActivity
import com.anime.application.dashBoard.fragment.model.AnimeDetailBean
import com.anime.application.dashBoard.fragment.transactionVM.TransactionVM
import com.anime.application.data.util.ApiState
import com.anime.application.databinding.FragmentAnimeDetailBinding
import com.anime.application.others.Loader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class AnimeDetailFragment : Fragment() {
    private val animeVM: TransactionVM by viewModels()
    private var _binding: FragmentAnimeDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var loader: Loader
    private var animeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the anime ID from arguments
        arguments?.let {
            animeId = it.getInt(ARG_ANIME_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
        loader = Loader(requireContext())

        // Observe ViewModel data
        observeTransactionData()

        // Fetch anime details using the passed animeId
        animeId?.let { id ->
            animeVM.getAnimDetail(id)
        }

        return binding.root
    }

    private fun observeTransactionData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                animeVM.apiAnimeDetailStateFlow.collect { state ->
                    when (state) {
                        is ApiState.Success<*> -> {
                            loader.dismiss()
                            val rawJson = state.data as? JsonObject
                            rawJson?.let {
                                Log.d("RawJsonData", "Raw JSON Response: $rawJson")

                                // Parse the JSON response to AnimeDetailBean.Data
                                val animeDetails = parseToAnimeDetail(rawJson).firstOrNull()
                                if (animeDetails != null) {
                                    updateUI(animeDetails)
                                } else {
                                    Log.e("ParsingError", "Anime details not found after parsing.")
                                    showErrorState(Exception("No data available"))
                                }
                            }
                        }

                        is ApiState.Failure -> {
                            showErrorState(state.error)
                        }

                        is ApiState.Loading -> {
                            loader.show()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(animeDetails: AnimeDetailBean.Data) {
        // Update UI elements using the parsed AnimeDetailBean.Data
        binding.apply {
            title.text = animeDetails.title.takeIf { !it.isNullOrEmpty() } ?: "N/A"
            synopsis.text = animeDetails.synopsis.takeIf { !it.isNullOrEmpty() } ?: "N/A"
            totalEpisods.text = animeDetails.episodes.toString() ?: "N/A"
            rating.text = animeDetails.rating.takeIf { !it.isNullOrEmpty() } ?: "N/A"
        }


        val youtubeUrl = animeDetails.trailer.youtubeId
        if (youtubeUrl.isNullOrEmpty()) {
            binding.poster.visibility = View.VISIBLE
            binding.rlPlayer.visibility = View.GONE
            loadImageFromNetwork(animeDetails.images.jpg.imageUrl, binding.poster)
        } else {
            binding.poster.visibility = View.GONE
            binding.rlPlayer.visibility = View.VISIBLE
            loadImageFromNetwork(animeDetails.images.jpg.imageUrl, binding.thumb)
        }

       binding.ivPlayVideo.setOnClickListener {
           val intent: Intent = Intent(requireActivity(), FullscreenExampleActivity::class.java)
           startActivity(intent)
       }

        // loadImageFromNetwork(animeDetails.images.jpg.imageUrl,binding.poster)

    }

    private fun showErrorState(error: Throwable) {
        ErrorDialogHelper().ErrorMessage(
            requireActivity(),
            error.message ?: "Unknown error occurred",
            "Transaction Error"
        )
        loader.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }

    private fun parseToAnimeDetail(jsonObject: JsonObject): List<AnimeDetailBean.Data> {
        return try {
            if (jsonObject.get("data").isJsonArray) {
                val jsonArray = jsonObject.getAsJsonArray("data")
                Gson().fromJson(jsonArray, Array<AnimeDetailBean.Data>::class.java).toList()
            } else {
                val dataObject = jsonObject.getAsJsonObject("data")
                listOf(Gson().fromJson(dataObject, AnimeDetailBean.Data::class.java))
            }
        } catch (e: Exception) {
            Log.e("JsonParsingError", "Error parsing JSON: ${e.message}")
            emptyList()
        }
    }

    private fun loadImageFromNetwork(imgUrl: String, imageView: ImageView) {
        val imageUrl = imgUrl
        imageView.setImageResource(R.drawable.placeholder_loader)
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

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()

    }


    companion object {
        private const val ARG_ANIME_ID = "animeId"

        @JvmStatic
        fun newInstance(animeId: Int) = AnimeDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_ANIME_ID, animeId)
            }
        }
    }
}
