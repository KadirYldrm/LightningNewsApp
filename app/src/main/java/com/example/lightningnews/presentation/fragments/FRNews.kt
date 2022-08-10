package com.example.lightningnews.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightningnews.R
import com.example.lightningnews.data.util.Resource
import com.example.lightningnews.databinding.FrNewsBinding
import com.example.lightningnews.presentation.ACMain
import com.example.lightningnews.presentation.adapter.NewsAdapter
import com.example.lightningnews.presentation.viewmodel.NewsVM
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FRNews : Fragment(R.layout.fr_news) {

    private lateinit var viewModel: NewsVM
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var fragmentNewsBinding: FrNewsBinding
    private var country = "us"
    private var page = 1
    private var isScrolling = false
    private var isLoading = false
    private var isLastPage = false
    private var pages = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        fragmentNewsBinding = FrNewsBinding.inflate(inflater, container, false)
        val view = fragmentNewsBinding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNewsBinding = FrNewsBinding.bind(view)
        viewModel = (activity as ACMain).viewModel
        newsAdapter = (activity as ACMain).newsAdapter
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("selected_article", it)
            }
            findNavController().navigate(R.id.action_FRNews_to_FRInfo, bundle)
        }
        initRecyclerView()
        viewNewsList()
        setSearchView()

    }

    private fun viewNewsList() {
        viewModel.getNewsHeadLines(country, page)
        viewModel.newsHeadLines.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    isLoading = false
                    fragmentNewsBinding.pbFRNews.visibility = View.GONE
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        if (it.totalResults % 20 == 0) {
                            pages = it.totalResults / 20
                        } else {
                            pages = it.totalResults / 20 + 1
                        }
                        isLastPage = page == pages
                    }
                }
                is Resource.Error -> {
                    isLoading = false
                    fragmentNewsBinding.pbFRNews.visibility = View.GONE
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    isLoading = true
                    fragmentNewsBinding.pbFRNews.visibility = View.VISIBLE

                }
            }
        }
    }

    private fun initRecyclerView() {

        with(fragmentNewsBinding) {
            rvFRNews.layoutManager = LinearLayoutManager(context)
            rvFRNews.adapter = newsAdapter
            rvFRNews.addOnScrollListener(this@FRNews.onScrollListener)
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = fragmentNewsBinding.rvFRNews.layoutManager as LinearLayoutManager
            val sizeOfTheCurrentList = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val topPosition = layoutManager.findFirstVisibleItemPosition()
            val hasReachedToEnd = topPosition + visibleItems >= sizeOfTheCurrentList
            val shouldPaginate = !isLoading && !isLastPage && hasReachedToEnd && isScrolling

            if (shouldPaginate) {
                page++
                viewModel.getNewsHeadLines(country, page)
                isScrolling = false
            }
        }
    }

    private fun setSearchView() {

        fragmentNewsBinding.svFRNews.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        viewModel.searchNews("us", p0.toString(), page)
                        viewSearchedNews()
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        MainScope().launch {
                            delay(1000)
                            viewModel.searchNews("us", p0.toString(), page)
                            viewSearchedNews()
                        }
                        return false
                    }

                })
                fragmentNewsBinding.svFRNews.setOnCloseListener(
                        object :SearchView.OnCloseListener{
                            override fun onClose(): Boolean {
                                initRecyclerView()
                                viewNewsList()
                                return false
                            }

                        })
    }

    fun viewSearchedNews() {

        viewModel.searchedNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    isLoading = false
                    fragmentNewsBinding.pbFRNews.visibility = View.GONE
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        if (it.totalResults % 20 == 0) {
                            pages = it.totalResults / 20
                        } else {
                            pages = it.totalResults / 20 + 1
                        }
                        isLastPage = page == pages
                    }
                }
                is Resource.Error -> {
                    isLoading = false
                    fragmentNewsBinding.pbFRNews.visibility = View.GONE
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    isLoading = true
                    fragmentNewsBinding.pbFRNews.visibility = View.VISIBLE

                }
            }
        }

    }
}