package com.example.lightningnews.presentation.fragments

import android.os.Bundle
import android.os.Handler
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
import com.example.lightningnews.presentation.CategoryNews
import com.example.lightningnews.presentation.adapter.NewsAdapter
import com.example.lightningnews.presentation.viewmodel.FRNewsVM
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FRNews : Fragment(R.layout.fr_news) {

    private lateinit var viewModel: FRNewsVM
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var fragmentNewsBinding: FrNewsBinding
    private var categoryNews = CategoryNews.general
    private var country = "tr"
    private var page = 1
    private var isScrolling = false
    private var isLoading = false
    private var isLastPage = false
    private var pages = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        fragmentNewsBinding = FrNewsBinding.inflate(inflater, container, false)
        return fragmentNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNewsBinding = FrNewsBinding.bind(view)
        viewModel = (activity as ACMain).viewModel
        newsAdapter = (activity as ACMain).newsAdapter

        newsAdapter.setOnItemClickListener {
            if (newsAdapter.clicked) {
                viewModel.saveArticle(it)
                Toast.makeText(context, "saved", Toast.LENGTH_LONG).show()
            } else {
                val bundle = Bundle().apply {
                    putSerializable("selected_article", it)
                }
                findNavController().navigate(R.id.action_FRNews_to_FRInfo, bundle)
            }
            newsAdapter.clicked = false
        }



        initRecyclerView()
        viewNewsList()
        setSearchView()

    }

    private fun viewNewsList() {

        viewModel.getNewsHeadLines(categoryNews.toString(), country, page)
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

        fragmentNewsBinding.btnSports.setOnClickListener {
            categoryNews = CategoryNews.sports

            viewNewsList()
        }

        fragmentNewsBinding.btnTech.setOnClickListener {
            categoryNews = CategoryNews.technology

            viewNewsList()
        }

        fragmentNewsBinding.btnHealth.setOnClickListener {
            categoryNews = CategoryNews.health

            viewNewsList()
        }

        fragmentNewsBinding.btnBusiness.setOnClickListener {

            categoryNews = CategoryNews.business

            viewNewsList()
        }

        fragmentNewsBinding.btnEntertainment.setOnClickListener {

            categoryNews = CategoryNews.entertainment

            viewNewsList()
        }

        fragmentNewsBinding.btnScience.setOnClickListener {
            categoryNews = CategoryNews.science

            viewNewsList()
        }

        fragmentNewsBinding.btnGeneral.setOnClickListener {
            categoryNews = CategoryNews.general

            viewNewsList()
        }

        fragmentNewsBinding.srlFRNews.setOnRefreshListener {

            viewModel.getNewsHeadLines(categoryNews.toString(), country, page)
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

            Handler().postDelayed({
                fragmentNewsBinding.srlFRNews.isRefreshing = false
            }, 2000)

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
                viewModel.getNewsHeadLines(categoryNews.toString(), country, page)
                isScrolling = false
            }
        }
    }

    private fun setSearchView() {

        fragmentNewsBinding.svFRNews.setOnQueryTextListener(

                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        viewModel.searchNews(country, p0.toString(), page)
                        viewSearchedNews()
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        MainScope().launch {
                            delay(1000)
                            viewModel.searchNews(country, p0.toString(), page)
                            viewSearchedNews()
                        }
                        return false
                    }

                })
        fragmentNewsBinding.svFRNews.setOnCloseListener {
            initRecyclerView()
            viewNewsList()
            false
        }
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