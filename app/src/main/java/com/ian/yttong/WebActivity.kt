package com.ian.yttong

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ian.yttong.databinding.ActivityWebBinding
import java.util.regex.Pattern

class WebActivity : AppCompatActivity() {
    companion object {
        const val TAG = "WebActivity"
        const val TITLE_EXTRA = "TITLE_EXTRA"
        const val URL_EXTRA = "URL_EXTRA"
    }

    private lateinit var dataBinding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_web)
        dataBinding.lifecycleOwner = this

        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.let {
            Log.d(TAG, intent.getStringExtra(TITLE_EXTRA).toString())
            it.title = intent.getStringExtra(TITLE_EXTRA)
        }

        initViews()

    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun initViews() {
        dataBinding.wvWeb.apply {
            //支持获取手势焦点，输入用户名、密码或其他
            requestFocusFromTouch()


            settings.apply {

                useWideViewPort = true //将图片调整至适合WebView大小
                setSupportZoom(true) //支持缩放
                loadWithOverviewMode = true //缩放至屏幕大小
                loadsImagesAutomatically = true //支持自动加载图片
                mediaPlaybackRequiresUserGesture = false
                // 加载JS
                javaScriptEnabled = true //支持js


                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webView中缓存
                layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING //支持内容重新布局

                allowFileAccess = true //设置可以访问文件
                javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
                defaultTextEncodingName = "utf-8" //设置编码格式
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                domStorageEnabled = true // 开启 DOM storage API 功能
                databaseEnabled = true   //开启 database storage API 功能

            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d(TAG, request.toString())
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    dataBinding.pbLoad.visibility = View.GONE
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    val cookieManager = CookieManager.getInstance()
                    val s = cookieManager.getCookie(url)
                    Log.d(TAG, "Cookies==$s")
                    url?.let {   Log.d(TAG,it) }

                    dataBinding.pbLoad.visibility = View.VISIBLE
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    dataBinding.pbLoad.progress = newProgress
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    if(!title.isNullOrEmpty()){
                        supportActionBar?.let {
                            it.title = title
                        }
                    }
                }

                //处理alert弹出框，html 弹框的一种方式
                override fun onJsAlert(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    return true
                }

                //处理confirm弹出框
                override fun onJsPrompt(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    defaultValue: String?,
                    result: JsPromptResult?
                ): Boolean {
                    return true
                }

                //处理prompt弹出框
                override fun onJsConfirm(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    return true
                }
            }
            intent.getStringExtra(URL_EXTRA)?.apply {
                val pattern = Pattern
                    .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$")
                if (pattern.matcher(this).matches()) {
                    loadUrl(this)
                } else {
                    loadDataWithBaseURL(null, this, "text/html", "utf-8", null)

                }
            }


            //加载js 的 window._VideoEnabledWebView.refreshStudentInfo(); 调用Android
            addJavascriptInterface(this, "_VideoEnabledWebView")
        }

    }

    //设置回退
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (dataBinding.wvWeb.canGoBack()) {
//                dataBinding.wvWeb.goBack()
//                return true
//            }
        }
        return super.onKeyDown(keyCode, event)
    }


//    //增加h5调用原生方法“h5QuitOut”。用于h5跳转到原生首页。
//    @JavascriptInterface
//    fun h5QuitOut() {
//        (activity!! as MainActivity).onTabSelected(HomeFragment.TAG)
//    }

    override fun onDestroy() {
        super.onDestroy()
        dataBinding.wvWeb.clearHistory()
        dataBinding.rlWeb.removeView(dataBinding.wvWeb)
        dataBinding.wvWeb.loadUrl("about:blank")
        dataBinding.wvWeb.stopLoading()
        dataBinding.wvWeb.destroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {//toolbar 返回键点击事件
//                if (dataBinding.wvWeb.canGoBack()) {
//                    dataBinding.wvWeb.goBack()
//                } else {
                    finish()
//                }
            }
        }
        return true
    }
}