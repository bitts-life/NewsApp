package com.bittslife.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bittslife.newsapp.databinding.ActivityWebViewBinding

private const val TAG = "WebViewActivity"

class WebViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url =intent.getStringExtra("URL")
        if (url!=null){
            binding.webViewNews.settings.javaScriptEnabled = true
            binding.webViewNews.settings.userAgentString = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3"
            binding.webViewNews.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.e(TAG, "onPageFinished: ", )
                    binding.webViewNews.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
            binding.webViewNews.loadUrl(url)

        }


    }
}