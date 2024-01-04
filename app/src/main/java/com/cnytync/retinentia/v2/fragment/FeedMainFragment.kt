package com.cnytync.retinentia.v2.fragment

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.cnytync.retinentia.v2.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TOKEN = "token"
private const val ARG_USER_ID = "userId"

/**
 * A simple [Fragment] subclass.
 * Use the [BooksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedMainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var token: String? = null
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_TOKEN)
            userId = it.getInt(ARG_USER_ID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_feed_main, container, false)

        WebView.setWebContentsDebuggingEnabled(true);

        val webView: WebView = view!!.findViewById<WebView>(R.id.feed_web_view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        val cookie = "apitoken=" + token!!

        CookieManager.getInstance().setCookie("http://192.168.1.34:8081/feed", cookie)
        CookieManager.getInstance().setCookie("http://192.168.1.34:8081/feed", "mobileapp=true")

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //return true load with system-default-browser or other browsers, false with your webView
                //put all headers in this header map

                webView.loadUrl(url)

                return false
            }

            override fun onLoadResource(view: WebView, url: String) {
                super.onLoadResource(view, url)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                println("**********************************************")
                super.onPageStarted(view, url, favicon)
            }
        }
        webView.loadUrl("http://192.168.1.34:8081/feed")

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BooksFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(token: String, userId: String) =
            FeedMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TOKEN, token)
                    putString(ARG_USER_ID, userId)
                }
            }
    }
}