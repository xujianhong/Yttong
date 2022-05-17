package com.ian.yttong

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.ian.yttong.databinding.MineFragmentBinding
import com.ian.yttong.viewmodel.MineViewModel
import java.util.regex.Pattern

class MineFragment : Fragment() {

    private lateinit var dataBinding:MineFragmentBinding

    companion object {
        const val TAG = "MineFragment"
        fun newInstance() = MineFragment()
    }

    private lateinit var viewModel: MineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        dataBinding = MineFragmentBinding.inflate(inflater,container,false)
        dataBinding.lifecycleOwner =this

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MineViewModel::class.java)

        dataBinding.viewModel = viewModel //关联dataBinding里的viewModel

        initViews()

        viewModel.getAbout()
        viewModel.about.observe(viewLifecycleOwner){

            val pattern = Pattern
                    .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$")
                if (pattern.matcher(it.remark).matches()) {
                   dataBinding.wvWeb.loadUrl(it.remark)
                } else {
                    dataBinding.wvWeb.loadDataWithBaseURL(null, it.remark, "text/html", "utf-8", null)

                }
        }




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
                    Log.d(WebActivity.TAG, request.toString())
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
                    Log.d(WebActivity.TAG, "Cookies==$s")
                    dataBinding.pbLoad.visibility = View.VISIBLE
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    dataBinding.pbLoad.progress = newProgress
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
//            intent.getStringExtra(WebActivity.URL_EXTRA)?.apply {
//                val pattern = Pattern
//                    .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$")
//                if (pattern.matcher(this).matches()) {
//                    loadUrl(this)
//                } else {
//                    loadDataWithBaseURL(null, this, "text/html", "utf-8", null)
//
//                }
//            }


            //加载js 的 window._VideoEnabledWebView.refreshStudentInfo(); 调用Android
            addJavascriptInterface(this, "_VideoEnabledWebView")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        dataBinding.wvWeb.clearHistory()
        dataBinding.constraintLayout.removeView(dataBinding.wvWeb)
        dataBinding.wvWeb.loadUrl("about:blank")
        dataBinding.wvWeb.stopLoading()
        dataBinding.wvWeb.destroy()
    }


}