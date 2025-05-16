package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.search.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchEdit = view.findViewById<EditText>(R.id.searchEditText)
        val resultRecycler = view.findViewById<RecyclerView>(R.id.searchResultsRecycler)
        val adapter = SearchAdapter { track ->
            findNavController().navigate(
                R.id.playerFragment,
                bundleOf("track" to track)
            )
        }
        resultRecycler.adapter = adapter

        searchEdit.addTextChangedListener { text ->
            viewModel.search(text.toString())
        }
        viewModel.searchResults.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}