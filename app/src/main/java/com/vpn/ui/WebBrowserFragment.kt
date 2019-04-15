package com.vpn.ui


import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.vpn.R
import com.vpn.db.model.AddressFilter
import com.vpn.db.viewmodel.IPViewModel
import kotlinx.android.synthetic.main.fragment_web_brower.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.Inet4Address
import java.net.URL


class WebBrowserFragment : Fragment() {

    private val list: ArrayList<AddressFilter> = ArrayList()
    private val model by lazy { ViewModelProviders.of(this).get(IPViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_web_brower, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // loading.hide()

        //--------------------------------------------->>
        initWebView()
        setUpSwipeLayout()

        go.setOnClickListener { openUrl() }

        fab.setOnClickListener { findNavController().navigate(R.id.IPFilterFragment) }

        searchBar.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d("suthar", "Enter button was pressed")
                openUrl()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        model.filters.observe(this, Observer {
            list.clear()
            list.addAll(it)
        })
    }

    private fun openUrl() {
        swipeLayout.isRefreshing = true
        hideSoftKeyboard()
        val url = searchBar.text?.toString()
        loadUrl(url)
    }

    private fun initWebView() {
        webview.settings.javaScriptEnabled = true
        webview.loadUrl("https://google.com/")
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                loadUrl(request?.url?.toString())
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                searchBar.setText(url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (this@WebBrowserFragment.isVisible) swipeLayout.isRefreshing = false
            }
        }
    }

    private fun loadUrl(url: String?) {

        url ?: return
        val url_ = if (url.startsWith("https://", true) || url.startsWith("http://", true)) url
        else "https://$url"

        doAsync {
            val y = try {
                Inet4Address.getByName(URL(url_).host).hostAddress
            } catch (e: Exception) {
                Log.d("suthar", e.toString())
                showErrorMessage()
                return@doAsync
            }

            Log.d("suthar", y)

            uiThread {
                if (shouldBlockUrl(url_) || shouldBlockUrl(y)) showErrorMessage()
                else webview.loadUrl(url_)
            }
        }
    }

    private fun showErrorMessage() {
        val unencodedHtml = "This website is blocked by Admin"
        val encodedHtml = Base64.encodeToString(unencodedHtml.toByteArray(), Base64.NO_PADDING)
        webview.loadData(encodedHtml, "text/html", "base64")
    }

    private fun shouldBlockUrl(url: String?): Boolean {
        for (filter in list)
            if (url?.contains(filter.address, true) == true) return true
        return false
    }

    private fun hideSoftKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun setUpSwipeLayout() {
        swipeLayout.setOnRefreshListener {
            webview.reload()
        }

        swipeLayout.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
    }
}
