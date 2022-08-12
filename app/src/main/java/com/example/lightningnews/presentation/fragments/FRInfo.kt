package com.example.lightningnews.presentation.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.lightningnews.R
import com.example.lightningnews.databinding.FrInfoBinding
import com.example.lightningnews.presentation.ACMain
import com.example.lightningnews.presentation.viewmodel.FRNewsVM
import com.google.android.material.snackbar.Snackbar

class FRInfo : Fragment() {

    private lateinit var viewModel: FRNewsVM
    private lateinit var fragmentInfoBinding: FrInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fr_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentInfoBinding = FrInfoBinding.bind(view)
        val args: FRInfoArgs by navArgs()
        val article = args.selectedArticle
        viewModel = (activity as ACMain).viewModel
        fragmentInfoBinding.wvInfo.apply {
            webViewClient = WebViewClient()
            if (article.url != null) {
                loadUrl(article.url)
            }
        }
        fragmentInfoBinding.fabFRInfo.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Saved Successfully!", Snackbar.LENGTH_LONG).show()
        }


    }
}