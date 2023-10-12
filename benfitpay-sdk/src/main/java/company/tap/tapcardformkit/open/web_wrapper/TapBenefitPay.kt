package company.tap.tapcardformkit.open.web_wrapper
import TapLocal
import TapTheme
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.*
import cards.pay.paycardsrecognizer.sdk.Card
import com.google.gson.Gson
import company.tap.tapcardformkit.*
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.web_wrapper.enums.BenefitPayStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.model.ThreeDsResponse
import company.tap.tapuilibrary.themekit.ThemeManager
import company.tap.tapuilibrary.uikit.atoms.*
import java.net.URLEncoder
import java.util.*


@SuppressLint("ViewConstructor")
class TapBenefitPay : LinearLayout {
    lateinit var constraintLayout: ConstraintLayout
    lateinit var webViewFrame: FrameLayout
    private var isBenefitPayUrlIntercepted =false
    lateinit var dialog: Dialog
    lateinit var linearLayout: LinearLayout

    companion object{
         var alreadyEvaluated = false
        var NFCopened:Boolean = false
        lateinit var threeDsResponse: ThreeDsResponse
        lateinit var cardWebview: WebView
        lateinit var cardConfiguraton: CardConfiguraton

        lateinit var singleWebView:WebView


        var card:Card?=null
        fun fillCardNumber(cardNumber:String,expiryDate:String,cvv:String,cardHolderName:String){
          //  cardWebview.loadUrl("javascript:window.fillCardInputs({cardNumber:'$cardNumber',expiryDate:'$expiryDate',cvv:'$cvv',cardHolderName:'$cardHolderName'})")
        }

        fun generateTapAuthenticate(authIdPayer: String) {
          //  cardWebview.loadUrl("javascript:window.loadAuthentication('$authIdPayer')")
        }

        fun setWebView(webView: WebView){
            singleWebView = webView
        }
        fun getWebView():WebView = singleWebView



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
        LayoutInflater.from(context).inflate(R.layout.activity_card_web_wrapper, this)
        initWebView()


    }

     private fun initWebView() {
        cardWebview = findViewById(R.id.webview)
         setWebView(cardWebview)
        webViewFrame = findViewById(R.id.webViewFrame)
        constraintLayout = findViewById(R.id.constraint)
         cardWebview.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
         with(cardWebview.settings){
             javaScriptEnabled=true
             domStorageEnabled=true
         }
         cardWebview.setBackgroundColor(Color.TRANSPARENT)
        cardWebview.setLayerType(LAYER_TYPE_SOFTWARE, null)
         cardWebview.webViewClient = MyWebViewClient()


     }


     fun init(configuraton: CardConfiguraton) {
         cardConfiguraton = configuraton
        applyThemeForShimmer()
        when (configuraton) {
            CardConfiguraton.MapConfigruation -> {
                val url  = "${urlWebStarter}${encodeConfigurationMapToUrl(DataConfiguration.configurationsAsHashMap)}"
             Log.e("url",url.toString())
                cardWebview.loadUrl(url)

            }
            else -> {}
        }
    }

    private fun applyThemeForShimmer() {
        /**
         * need to be refactored : mulitple copies of same code
         */
        when(cardConfiguraton){
            CardConfiguraton.MapConfigruation ->{
                val tapInterface = DataConfiguration.configurationsAsHashMap?.get("interface") as? Map<*, *>
              setTapThemeAndLanguage(
                    this.context,
                    TapLocal.valueOf(tapInterface?.get("locale")?.toString() ?: TapLocal.en.name),
                  TapTheme.valueOf(tapInterface?.get("theme")?.toString() ?: TapTheme.light.name))
            }
            else -> {}
        }


    }

    private fun setTapThemeAndLanguage(context: Context, language: TapLocal?, themeMode: TapTheme?) {
        when (themeMode) {
            TapTheme.light -> {
                DataConfiguration.setTheme(
                    context, context.resources, null,
                    R.raw.defaultlighttheme, TapTheme.light.name
                )
                ThemeManager.currentThemeName = TapTheme.light.name
            }
            TapTheme.dark -> {
                DataConfiguration.setTheme(
                    context, context.resources, null,
                    R.raw.defaultdarktheme, TapTheme.dark.name
                )
                ThemeManager.currentThemeName = TapTheme.dark.name
            }
            else -> {}
        }
        DataConfiguration.setLocale(this.context, language?.name ?:"en", null, this@TapBenefitPay.context.resources, R.raw.lang)

    }


    private fun encodeConfigurationMapToUrl(configuraton: HashMap<String,Any>?): String? {
        val gson = Gson()
        val json: String = gson.toJson(configuraton)

        val encodedUrl = URLEncoder.encode(json, "UTF-8")
        return encodedUrl

    }


    inner class MyWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun shouldOverrideUrlLoading(
            webView: WebView?,
            request: WebResourceRequest?
        ): Boolean {

//            return false
            /**
             * main checker if url start with "tapCardWebSDK://"
             */

            if (request?.url.toString().startsWith(CardWebUrlPrefix, ignoreCase = true)) {
                Log.e("url",request?.url.toString())
                /**
                 * listen for states of cardWebStatus of onReady , onValidInput .. etc
                 */
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onReady.name)) {
                    DataConfiguration.getTapCardStatusListener()?.onReady()
                }
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onChargeCreated.name)) {
                    DataConfiguration.getTapCardStatusListener()?.onChargeCreated(request?.url?.getQueryParameterFromUri(keyValueName).toString())
                }

                if (request?.url.toString().contains(BenefitPayStatusDelegate.onOrderCreated.name)) {
                    DataConfiguration.getTapCardStatusListener()?.onOrderCreated(request?.url?.getQueryParameter(keyValueName).toString())
                }
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onClick.name)) {
                    isBenefitPayUrlIntercepted=false
                    DataConfiguration.getTapCardStatusListener()?.onClick()

                }
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onCancel.name)) {
                  //  Toast.makeText(context,"cancelled",Toast.LENGTH_SHORT).show()
                    linearLayout.removeView(getWebView())
                    dialog.dismiss()
                    (webViewFrame as ViewGroup).addView(getWebView())

                }
                if (request?.url.toString().contains(BenefitPayStatusDelegate.onError.name)) {
                    DataConfiguration.getTapCardStatusListener()?.onError(request?.url?.getQueryParameterFromUri(keyValueName).toString())
                }

                if (request?.url.toString().contains(BenefitPayStatusDelegate.onSuccess.name)) {
                    DataConfiguration.getTapCardStatusListener()?.onSuccess(request?.url?.getQueryParameterFromUri(keyValueName).toString())
                }

                return true

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
            when(request?.url?.toString()?.contains(beneiftPayCheckoutUrl)?.and((!isBenefitPayUrlIntercepted))) {
                true ->{

                    view?.post{
                        (webViewFrame as ViewGroup).removeView(getWebView())


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

                        if (getWebView().parent == null){
                            linearLayout.addView(getWebView())
                        }
                        Log.e("parent", getWebView().parent.toString())

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
            Log.e("error",error.toString())
            Log.e("error",request.toString())

            super.onReceivedError(view, request, error)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
        }
    }




}



enum class CardConfiguraton() {
    MapConfigruation, ModelConfiguration
}




