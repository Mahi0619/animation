package com.anime.application.dashBoard.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.anime.application.base.ErrorDialogHelper
import com.anime.application.dashBoard.DashboardActivity
import com.anime.application.dashBoard.fragment.adapter.AnimeAdapter
import com.anime.application.dashBoard.fragment.model.NewAnimeResponse
import com.anime.application.dashBoard.fragment.transactionVM.TransactionVM
import com.anime.application.data.util.ApiState
import com.anime.application.databinding.FragmentAnimeBinding
import com.anime.application.others.CallBack
import com.anime.application.others.Loader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AnimeFragment : Fragment() {
    private val animeVM: TransactionVM by viewModels()
    private var _binding: FragmentAnimeBinding? = null
    private val binding get() = _binding!!
    private lateinit var loader: Loader
    private lateinit var adapter: AnimeAdapter
    private val animeList = mutableListOf<NewAnimeResponse.Data>() // Original data
    private val filteredList = mutableListOf<NewAnimeResponse.Data>() // Filtered data
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeBinding.inflate(inflater, container, false)
        loader = Loader(requireContext())

        // Initialize RecyclerView
        binding.rcvAnime.layoutManager = LinearLayoutManager(requireContext())
        adapter = AnimeAdapter(filteredList, onItemClick)
        binding.rcvAnime.adapter = adapter

        // Observe data and fetch Anime list
        observeTransactionData()
        animeVM.getAnim() // Fetch first page

        // Set view click listeners (like swipe refresh and search)
        setViewClickListeners()

        // Set scroll listener for pagination
        setRecyclerViewScrollListener()

        return binding.root
    }

    private fun setRecyclerViewScrollListener() {
        binding.rcvAnime.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.rcvAnime.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                // Check if we should load the next page
                if (!isLoading && animeVM.hasNextPage &&
                    (firstVisibleItem + visibleItemCount) >= totalItemCount
                ) {
                    loadNextPage()
                }
            }
        })
    }

    private fun loadNextPage() {
        Log.d("Paginationmmmm", "Requesting page: ${animeVM.currentPage + 1}")
        isLoading = true
        animeVM.getAnim(animeVM.currentPage + 1)
    }


    private fun setViewClickListeners() {
        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            animeVM.getAnim() // Fetch fresh data
            binding.swipeRefresh.isRefreshing = false
        }

        // Search functionality
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterAnime(binding.searchText.text.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeTransactionData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                animeVM.apiAnimeStateFlow.collect { state ->
                    when (state) {
                        is ApiState.Success<*> -> {
                            isLoading = false
                            val rawJson = state.data as? JsonObject
                            if (rawJson != null) {
                                Log.d("RawJsonData", "Raw JSON Response: $rawJson")

                                // Parse the JSON response to a list of anime items
                                val animeItems = parseToAnimeItemList(rawJson)

                                if (animeItems.isNotEmpty()) {
                                    Log.d("ParsedAnimeItems", "Parsed Anime Items: $animeItems")

                                    loader.dismiss()

                                    // Append new data to the list (ensure data appends correctly after page 1)
                                    if (animeVM.currentPage == 1) {
                                        animeList.clear() // Clear list on refresh or first page load
                                    }
                                    animeList.addAll(animeItems) // Append new items

                                    // Filter and update the adapter
                                    filterAnime("") // Apply filters (if any)
                                } else {
                                    Log.e("ParsingError", "No anime items found after parsing.")
                                    showErrorState(Exception("No data available"))
                                }
                            }
                        }

                        is ApiState.Failure -> {
                            showErrorState(state.error)
                            isLoading = false
                        }

                        is ApiState.Loading -> {
                            isLoading = true
                            loader.show()
                            binding.rcvAnime.visibility = View.VISIBLE
                            binding.noDataFound.visibility = View.GONE
                        }

                        else -> Unit
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                animeVM.apiAnimeDetailStateFlow.collect { state ->
                    when (state) {
                        is ApiState.Success<*> -> {
                            val rawJson = state.data as? JsonObject
                            if (rawJson != null) {
                                Log.d("RawJsonDataDetails", "Raw JSON Response: $rawJson")
                                // Parse the JSON response to a list of anime items
                                val animeItems = parseToAnimeItemList(rawJson)

                                if (rawJson.toString().isNotEmpty()) {
                                    Log.d("ParsedAnimeItems", "Parsed Anime Items: $animeItems")
                                    loader.dismiss()

                                } else {
                                    Log.e("ParsingError", "No anime items found after parsing.")
                                    showErrorState(Exception("No data available"))
                                }
                            }
                        }

                        is ApiState.Failure -> {
                            showErrorState(state.error)

                        }

                        is ApiState.Loading -> {
                            loader.show()
                            binding.rcvAnime.visibility = View.VISIBLE
                            binding.noDataFound.visibility = View.GONE
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun parseToAnimeItemList(jsonObject: JsonObject): List<NewAnimeResponse.Data> {
        return try {
            val jsonArray = jsonObject.getAsJsonArray("data")
            Gson().fromJson(jsonArray, Array<NewAnimeResponse.Data>::class.java).toList()
        } catch (e: Exception) {
            Log.e("JsonParsingError", "Error parsing JSON: ${e.message}")
            emptyList()
        }
    }

    private fun showErrorState(error: Throwable) {
        ErrorDialogHelper().ErrorMessage(
            requireActivity(),
            error.message ?: "Unknown error occurred",
            "Transaction Error"
        )
        loader.dismiss()
    }

    private fun filterAnime(query: String?) {
        filteredList.clear()
        val filteredAnime = animeList.filter {
            query.isNullOrEmpty() || it.title.startsWith(query, ignoreCase = true)
        }

        if (filteredAnime.isEmpty()) {
            binding.rcvAnime.visibility = View.GONE
            binding.noDataFound.visibility = View.VISIBLE
        } else {
            binding.rcvAnime.visibility = View.VISIBLE
            binding.noDataFound.visibility = View.GONE
        }

        filteredList.addAll(filteredAnime)
        adapter.updateAnimeList(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onItemClick = object : CallBack<NewAnimeResponse.Data>() {
        override fun onError(error: String?) {
            Log.e("onError", "Error: $error")
        }

        override fun onSuccess(t: NewAnimeResponse.Data) {

            (activity as DashboardActivity).addToBackStackAndOpenFragment(
                AnimeDetailFragment.newInstance(
                    t.malId
                )
            )

        }
    }
}

/*
@AndroidEntryPoint
class AnimeFragment : Fragment() {
    private val animeVM: TransactionVM by viewModels()
    private var _binding: FragmentAnimeBinding? = null
    private val binding get() = _binding!!
    private lateinit var loader: Loader
    private lateinit var adapter: AnimeAdapter
    private val animeList = mutableListOf<NewAnimeResponse.Data>() // Original data
    private val filteredList = mutableListOf<NewAnimeResponse.Data>() // Filtered data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeBinding.inflate(inflater, container, false)
        loader = Loader(requireContext())

        // Initialize RecyclerView
        binding.rcvAnime.layoutManager = LinearLayoutManager(requireContext())
        adapter = AnimeAdapter(filteredList)  // Corrected: Use filteredList directly
        binding.rcvAnime.adapter = adapter

        // Observe data and fetch Anime list
        observeTransactionData()
        animeVM.getAnim()

        setViewClickListeners()

        return binding.root
    }

    private fun setViewClickListeners() {
        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            animeVM.getAnim() // Fetch fresh data
            binding.swipeRefresh.isRefreshing = false
        }

        // Search functionality
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterAnime(binding.searchText.text.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeTransactionData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                animeVM.apiAnimeStateFlow.collect { state ->
                    when (state) {
                        is ApiState.Success<*> -> {
                            val rawJson = state.data as? JsonObject
                            if (rawJson != null) {
                                Log.d("RawJsonData", "Raw JSON Response: $rawJson")
                                val animeItems = parseToAnimeItemList(rawJson)

                                if (animeItems.isNotEmpty()) {
                                    Log.d("ParsedAnimeItems", "Parsed Anime Items: $animeItems")

                                    loader.dismiss()
                                    animeList.clear()

                                    animeList.addAll(animeItems)
                                    filterAnime("") // Filter and update adapter
                                } else {
                                    Log.e("ParsingError", "No anime items found after parsing.")
                                    showErrorState(Exception("No data available"))
                                }
                            }
                        }
                        is ApiState.Failure -> showErrorState(state.error)
                        is ApiState.Loading -> {
                            loader.show()
                            binding.rcvAnime.visibility = View.VISIBLE
                            binding.noDataFound.visibility = View.GONE
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showErrorState(error: Throwable) {
        ErrorDialogHelper().ErrorMessage(
            requireActivity(),
            error.message ?: "Unknown error occurred",
            "Transaction Error"
        )
        loader.dismiss()
    }

    private fun filterAnime(query: String?) {
        filteredList.clear()
        val filteredAnime = animeList.filter {
            query.isNullOrEmpty() || it.title.startsWith(query, ignoreCase = true)
        }

        filteredList.addAll(filteredAnime)
        Log.e("fgfgf", filteredAnime.toString())

        // Handle UI visibility based on filter result
        if (filteredAnime.isEmpty()) {
            binding.rcvAnime.visibility = View.GONE
            binding.noDataFound.visibility = View.VISIBLE
        } else {
            binding.rcvAnime.visibility = View.VISIBLE
            binding.noDataFound.visibility = View.GONE
        }

        adapter.updateAnimeList(filteredList)  // Corrected: Update the adapter with filtered list
    }



    private fun parseToAnimeItemList(jsonObject: JsonObject): List<NewAnimeResponse.Data> {
        return try {
            val jsonArray = jsonObject.getAsJsonArray("data")
            Gson().fromJson(jsonArray, Array<NewAnimeResponse.Data>::class.java).toList()
        } catch (e: Exception) {
            Log.e("JsonParsingError", "Error parsing JSON: ${e.message}")
            emptyList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/
