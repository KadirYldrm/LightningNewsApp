package com.example.lightningnews.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.lightningnews.R
import com.example.lightningnews.databinding.AcMainBinding
import com.example.lightningnews.presentation.adapter.NewsAdapter
import com.example.lightningnews.presentation.viewmodel.NewsVM
import com.example.lightningnews.presentation.viewmodel.NewsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ACMain : AppCompatActivity() {

    @Inject
    lateinit var factory: NewsViewModelFactory
    @Inject
    lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsVM
    private lateinit var binding: AcMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcvMain) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnNewsMain.setupWithNavController(
                navController
        )
        viewModel = ViewModelProvider(this,factory)[NewsVM::class.java]

    }
}