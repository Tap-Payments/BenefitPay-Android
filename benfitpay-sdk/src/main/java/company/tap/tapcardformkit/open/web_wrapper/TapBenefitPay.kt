package company.tap.tapbenefitpay.open.web_wrapper

import TapLocal
import TapTheme
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.os.postDelayed
import androidx.core.view.*
import company.tap.tapbenefitpay.*
import company.tap.tapbenefitpay.open.ApplicationLifecycle
import company.tap.tapbenefitpay.open.BenefitPayDataConfiguration
import company.tap.tapbenefitpay.open.web_wrapper.BeneiftPayConfiguration.Companion.configApiUrl
import company.tap.tapbenefitpay.open.web_wrapper.enums.BenefitPayStatusDelegate
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
//import company.tap.tapuilibrary.themekit.ThemeManager
//import company.tap.tapuilibrary.uikit.atoms.*
import java.net.URISyntaxException
import java.util.*


@SuppressLint("ViewConstructor")
class TapBenefitPay : LinearLayout,ApplicationLifecycle {
    lateinit var webViewFrame: LinearLayout
    lateinit var progressBar: ProgressBar
    private var isBenefitPayUrlIntercepted =false
    lateinit var dialog: Dialog
     var pair =  Pair("",false)
    lateinit var linearLayout: LinearLayout
     var iSAppInForeground = true
     var onSuccessCalled = false
    lateinit var urlToBeloaded: String
    lateinit var cardConfiguraton: java.util.HashMap<String, Any>
    companion object{
        lateinit var cardWebview: WebView
       // lateinit var cardConfiguraton: CardConfiguraton
    }

    /**
     * Simple constructor to use when creating a TapPayCardSwitch from code.
     *  @param context The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     **/
    constructor(context: Context) : super(context)

    /**
     *  @param context The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     *  @param attrs The attributes of the XML Button tag being used to inflate the view.
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    init {
        LayoutInflater.from(context).inflate(R.layout.activity_benefit_pay_layout_wrapper, this)
        initWebView()

    }


     private fun initWebView() {
        cardWebview = findViewById(R.id.webview)
        webViewFrame = findViewById(R.id.webViewFrame)
         progressBar = findViewById(R.id.progress_circular)
         with(cardWebview.settings){
             javaScriptEnabled=true
             domStorageEnabled=true
             cacheMode = WebSettings.LOAD_NO_CACHE


         }
         cardWebview.setBackgroundColor(Color.TRANSPARENT)
         cardWebview.setLayerType(LAYER_TYPE_SOFTWARE, null)
         cardWebview.webViewClient = MyWebViewClient()


     }


    // fun init(configuraton: CardConfiguraton) {
     fun init( configuraton: java.util.HashMap<String, Any>) {
         cardConfiguraton = configuraton
        // progressBar.visibility = VISIBLE
         BenefitPayDataConfiguration.addAppLifeCycle(this)

         callConfigAPI(configuraton)
     //    applyTheme()
       /* when (configuraton) {
            CardConfiguraton.MapConfigruation -> {
                val url  = "${urlWebStarter}${encodeConfigurationMapToUrl(BenefitPayDataConfiguration.configurationsAsHashMap)}"
             Log.e("url",url.toString())
                cardWebview.loadUrl(url)

            }
            else -> {}
        }*/
    }


  /*  private fun applyTheme() {
        *//**
         * need to be refactored : mulitple copies of same code
         *//*
        when(cardConfiguraton){
            CardConfiguraton.MapConfigruation ->{
                val tapInterface = BenefitPayDataConfiguration.configurationsAsHashMap?.get("interface") as? Map<*, *>
              setTapThemeAndLanguage(
                    this.context,
                    TapLocal.valueOf(tapInterface?.get("locale")?.toString() ?: TapLocal.en.name),
                  TapTheme.valueOf(tapInterface?.get("theme")?.toString() ?: TapTheme.light.name))
            }
            else -> {}
        }


    }*/

    private fun setTapThemeAndLanguage(context: Context, language: TapLocal?, themeMode: TapTheme?) {
        when (themeMode) {
            TapTheme.light -> {
                BenefitPayDataConfiguration.setTheme(
                    context, context.resources, null,
                    R.raw.defaultlighttheme, TapTheme.light.name
                )
               // ThemeManager.currentThemeName = TapTheme.light.name
            }
            TapTheme.dark -> {
                BenefitPayDataConfiguration.setTheme(
                    context, context.resources, null,
                    R.raw.defaultdarktheme, TapTheme.dark.name
                )
               // ThemeManager.currentThemeName = TapTheme.dark.name
            }
            else -> {}
        }
        BenefitPayDataConfiguration.setLocale(this.context, language?.name ?:"en", null, this@TapBenefitPay.context.resources, R.raw.lang)

    }




    inner class MyWebViewClient : WebViewClient() {
        @SuppressLint("SuspiciousIndentation")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun shouldOverrideUrlLoading(
            webView: WebView?,
            request: WebResourceRequest?
        ): Boolean {


            /**
             * main checker if url start with "tapCardWebSDK://"
             */

            if (request?.url.toString().startsWith(CardWebUrlPrefix, ignoreCase = true)) {
                Log.e("url",request?.url.toString())
                /**
                 * listen for states of cardWebStatus of onReady , onValidInput .. etc
                 */
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onReady.name)) {
                    BenefitPayDataConfiguration.getTapCardStatusListener()?.onBenefitPayReady()
                  //  progressBar.visibility = GONE
                }
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onChargeCreated.name)) {
                    BenefitPayDataConfiguration.getTapCardStatusListener()?.onBenefitPayChargeCreated(request?.url?.getQueryParameterFromUri(keyValueName).toString())
                  //  progressBar.visibility = GONE
                }

                if (request?.url.toString().contains(BenefitPayStatusDelegate.onOrderCreated.name)) {
                    BenefitPayDataConfiguration.getTapCardStatusListener()?.onBenefitPayOrderCreated(request?.url?.getQueryParameter(keyValueName).toString())
                  //  progressBar.visibility = GONE
                }
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onClick.name)) {
                   // progressBar.visibility = VISIBLE
                    isBenefitPayUrlIntercepted=false
                    pair = Pair("",false)
                    BenefitPayDataConfiguration.getTapCardStatusListener()?.onBenefitPayClick()
                    onSuccessCalled = false


                }
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onCancel.name)) {
                    android.os.Handler(Looper.getMainLooper()).postDelayed(3000) {
                        if(!onSuccessCalled){
                            BenefitPayDataConfiguration.getTapCardStatusListener()?.onBenefitPayCancel()
                        }


                    }

                    if (!(pair.first.isNotEmpty() and pair.second)) {
                            dismissDialog()
                        }
                   // progressBar.visibility = GONE

                }

                if (request?.url.toString().contains(BenefitPayStatusDelegate.onError.name)) {
                    android.os.Handler(Looper.getMainLooper()).postDelayed(3000) {
                        if(!onSuccessCalled){
                            BenefitPayDataConfiguration.getTapCardStatusListener()?.onBenefitPayError(request?.url?.getQueryParameterFromUri(keyValueName).toString())

                        }

                    }
                    pair = Pair(request?.url?.getQueryParameterFromUri(keyValueName).toString(),true)
                        closePayment()
                  //  progressBar.visibility = GONE

                }

                if (request?.url.toString().contains(BenefitPayStatusDelegate.onSuccess.name)) {
                    onSuccessCalled = true
                    pair = Pair(request?.url?.getQueryParameterFromUri(keyValueName).toString(),true)
                    when(iSAppInForeground) {

                        true ->{closePayment()
                            Log.e("success","one")
                        }
                        false ->{}
                    }
                  //  progressBar.visibility = GONE

                }

                return true

            }
            if (request?.url.toString().startsWith("intent://")) {
                try {
                    val context: Context = context
                    val intent: Intent = Intent.parseUri(request?.url.toString(), Intent.URI_INTENT_SCHEME)
                    if (intent != null) {
//                            view.stopLoading()
                        val packageManager: PackageManager = context.packageManager
                        val info: ResolveInfo? = packageManager.resolveActivity(
                            intent,
                            PackageManager.MATCH_DEFAULT_ONLY
                        )
                        if (info != null) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent)
                        } else {
                            return false
                        }
                        return true
                    }
                } catch (e: URISyntaxException) {
                    Log.e("error", "Can't resolve intent://", e)

                }
             //   progressBar.visibility = GONE
            }


            return false

        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

        }



        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            Log.e("intercepted",request?.url.toString())


            when(request?.url?.toString()?.contains(beneiftPayCheckoutUrl)?.and((!isBenefitPayUrlIntercepted))) {

                true ->{
                    view?.post{
                        (webViewFrame as ViewGroup).removeView(cardWebview)


                        dialog= Dialog(context,android.R.style.Theme_Translucent_NoTitleBar)
                        //Create LinearLayout Dynamically
                        linearLayout = LinearLayout(context)
                        //Setup Layout Attributes
                        val params = LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        linearLayout.layoutParams = params
                        linearLayout.orientation = VERTICAL

                        /**
                         * onBackPressed in Dialog
                         */
                        dialog.setOnKeyListener { view, keyCode, keyEvent ->
                            if (keyEvent.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                                dismissDialog()
                                init(cardConfiguraton)
                                return@setOnKeyListener  true
                            }
                            return@setOnKeyListener false
                        }


                        if (cardWebview.parent == null){
                            linearLayout.addView(cardWebview)
                        }

                        dialog.setContentView(linearLayout)
                        dialog.show()
                    }

                    isBenefitPayUrlIntercepted = true
                }
                else -> {}
            }

            return super.shouldInterceptRequest(view, request)
        }





        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            Log.e("error code",error.errorCode.toString())
            Log.e("error description ",error.description.toString())

            Log.e("request header ",request.requestHeaders.toString())

            super.onReceivedError(view, request, error)
        }

    }

    private fun dismissDialog() {
        if (::dialog.isInitialized) {
            linearLayout.removeView(cardWebview)
            dialog.dismiss()
            if (cardWebview.parent == null){
                (webViewFrame as ViewGroup).addView(cardWebview)
            }
        }
    }

    override fun onEnterForeground() {
        iSAppInForeground = true
        Log.e("applifeCycle","onEnterForeground")
      //  closePayment()





    }

    private fun closePayment() {

        if (pair.second) {
            Log.e("app","one")
            dismissDialog()
          //  init(cardConfiguraton) // was reloading url cz problem stopped
            BenefitPayDataConfiguration.getTapCardStatusListener()?.onBenefitPaySuccess(pair.first)

        }
    }

    override fun onEnterBackground() {
        iSAppInForeground = false
        Log.e("applifeCycle","onEnterBackground")

    }
    private fun callConfigAPI(configuraton: java.util.HashMap<String, Any>) {
        try {
            val baseURL = configApiUrl
           // val baseURL = "https://mw-sdk.beta.tap.company/v2/button/config"
            val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)

            val body = (configuraton as Map<*, *>?)?.let { JSONObject(it).toString().toRequestBody("application/json".toMediaTypeOrNull()) }
            val okHttpClient: OkHttpClient = builder.build()
            val request: Request = Request.Builder()
                .url(baseURL)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        var responseBody: JSONObject? =
                            response.body?.string()?.let { JSONObject(it) } // toString() is not the response body, it is a debug representation of the response body

                        if(!responseBody.toString().contains("errors")){
                            var redirectURL = responseBody?.getString("redirect_url")
                            if (redirectURL != null) {
                                // knetWebView.loadUrl(redirectURL)
                                urlToBeloaded = redirectURL
                                Handler(Looper.getMainLooper()).post {
                                    cardWebview.loadUrl(redirectURL)

                                }
                            }
                        }else{


                        }

                    } catch (ex: JSONException) {
                        throw RuntimeException(ex)
                    } catch (ex: IOException) {
                        throw RuntimeException(ex)
                    }

                }

                override fun onFailure(call: Call, e: IOException) {}
            })
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

}



enum class CardConfiguraton() {
    MapConfigruation
}




