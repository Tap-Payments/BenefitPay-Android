package company.tap.tapbenefitpay.open.web_wrapper


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import company.tap.tapbenefitpay.open.web_wrapper.commondatamodels.Headers
import company.tap.tapbenefitpay.open.AppLifecycleObserver
import company.tap.tapbenefitpay.open.BenefitPayDataConfiguration
import company.tap.tapbenefitpay.open.BenefitPayDataConfiguration.configurationsAsHashMap
import company.tap.tapbenefitpay.open.TapBenefitPayStatusDelegate
import company.tap.tapnetworkkit.connection.NetworkApp
import company.tap.tapnetworkkit.utils.CryptoUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class BeneiftPayConfiguration {

    companion object {

        private val retrofit = ApiServiceBenefit.RetrofitClient.getClient()
        private val tapSDKConfigsUrl = retrofit.create(ApiServiceBenefit.TapSDKConfigUrls::class.java)
        private var testEncKey: String? = null
        private var prodEncKey: String? = null
        private var dynamicBaseUrlResponse: String? = null
        private var context: Context? = null
       var configApiUrl : String = "https://mw-sdk.tap.company/v2/button/config"
        fun configureWithTapBenfitPayDictionaryConfiguration(
            _context: Context,
            tapCardInputViewWeb: TapBenefitPay?,
            tapMapConfiguration: java.util.HashMap<String, Any>,
            tapBenefitPayStatusDelegate: TapBenefitPayStatusDelegate? = null
        ) {
     /*       with(tapMapConfiguration) {
                Log.e("map", tapMapConfiguration.toString())
                configurationsAsHashMap = tapMapConfiguration
                val operator = configurationsAsHashMap?.get(operatorKey) as HashMap<*, *>
                val publickKey = operator.get(publicKeyToGet)

                val appLifecycleObserver = AppLifecycleObserver()
                ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)

                addOperatorHeaderField(
                    tapCardInputViewWeb,
                    context,
                    CardConfiguraton.MapConfigruation,
                    publickKey.toString()
                )

                DataConfiguration.addTapBenefitPayStatusDelegate(tapBenefitPayStatusDelegate)
                tapCardInputViewWeb?.init(CardConfiguraton.MapConfigruation)

            }*/

            context = _context
            MainScope().launch {
                getTapSDKConfigUrls(
                    tapMapConfiguration,
                    tapCardInputViewWeb,
                    _context,
                    tapBenefitPayStatusDelegate
                )
            }

        }

        private suspend fun getTapSDKConfigUrls(
            tapMapConfiguration: HashMap<String, Any>,
            tapCardInputViewWeb: TapBenefitPay?,
            context: Context,
            tapBenefitPayStatusDelegate : TapBenefitPayStatusDelegate?
        ) {

            try {
                /**
                 * request to get Tap configs
                 */

                val tapSDKConfigUrlResponse = tapSDKConfigsUrl.getSDKConfigUrl()
               // BASE_URL = tapSDKConfigUrlResponse.baseURL
                configApiUrl = tapSDKConfigUrlResponse.baseURL
               // configApiUrl = "https://mw-sdk.beta.tap.company/v2/button/config"
                testEncKey = tapSDKConfigUrlResponse.testEncKey
                prodEncKey = tapSDKConfigUrlResponse.prodEncKey
                urlWebStarter = tapSDKConfigUrlResponse.baseURL

                startSDKWithConfigs(
                    tapMapConfiguration,
                    tapCardInputViewWeb,
                    context,
                    tapBenefitPayStatusDelegate
                )

            } catch (e: Exception) {
             //   BASE_URL = urlWebStarter
                val resId = context.resources.getIdentifier(
                    "enryptkeyTest", "string", context.packageName
                )
                val resIdprod = context.resources.getIdentifier(
                    "enryptkeyProduction", "string", context.packageName
                )
                 testEncKey = context.getString(resId)
                prodEncKey = context.getString(resIdprod)
              //  testEncKey =  tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyTest)
             //   prodEncKey = tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyProduction)

                startSDKWithConfigs(
                    tapMapConfiguration,
                    tapCardInputViewWeb,
                    context,
                    tapBenefitPayStatusDelegate,

                )
                Log.e("error Config", e.message.toString())
            }
        }

        @SuppressLint("SuspiciousIndentation")
        fun addOperatorHeaderField(
            tapCardInputViewWeb: TapBenefitPay?,
            context: Context,
            modelConfiguration: CardConfiguraton,
            publicKey: String?
        ) {
         val encodedeky = getPublicEncryptionKey(publicKey,tapCardInputViewWeb)

            Log.e("packagedname",context.packageName.toString())

            NetworkApp.initNetwork(
                tapCardInputViewWeb?.context ,
                publicKey ?: "",
               context.packageName,
                ApiServiceBenefit. BASE_URL.replace("benefitpay?configurations", ""),
                "android-benefitpay",
                true,
                encodedeky,
                null
            )
            val headers = Headers(
                application = NetworkApp.getApplicationInfo(),
                mdn = CryptoUtil.encryptJsonString(
                    context.packageName.toString(),
                    encodedeky,
                )
            )

            when (modelConfiguration) {
                CardConfiguraton.MapConfigruation -> {
                    val hashMapHeader = HashMap<String, Any>()
                    hashMapHeader[HeadersMdn] = headers.mdn.toString()
                    hashMapHeader[HeadersApplication] = headers.application.toString()
                    configurationsAsHashMap?.put(headersKey, hashMapHeader)

                }
                else -> {}
            }


        }
        private fun getPublicEncryptionKey(
            publicKey: String?,
            tapCardInputViewWeb: TapBenefitPay?
        ): String? {
            val ctx = tapCardInputViewWeb?.context ?: return null

            return if (!testEncKey.isNullOrBlank() && !prodEncKey.isNullOrBlank()) {
                if (publicKey?.contains("test", ignoreCase = true) == true) {
                    testEncKey
                } else {
                    prodEncKey
                }
            } else {
                if (publicKey?.contains("test", ignoreCase = true) == true) {
                    val resId = ctx.resources.getIdentifier("enryptkeyTest", "string", ctx.packageName)
                    testEncKey = if (resId != 0) ctx.getString(resId) else null
                    testEncKey
                } else {
                    val resIdProd = ctx.resources.getIdentifier("enryptkeyProduction", "string", ctx.packageName)
                    prodEncKey = if (resIdProd != 0) ctx.getString(resIdProd) else null
                    prodEncKey
                }
            }
        }


        private fun startSDKWithConfigs(
            tapMapConfiguration: java.util.HashMap<String, Any>,
            tapCardInputViewWeb: TapBenefitPay?,
            context: Context,
            tapBenefitPayStatusDelegate: TapBenefitPayStatusDelegate? = null,

        ) {
            with(tapMapConfiguration) {
                Log.e("map", tapMapConfiguration.toString())
                configurationsAsHashMap = tapMapConfiguration
                val operator = configurationsAsHashMap?.get(operatorKey) as HashMap<*, *>
                val publickKey = operator.get(publicKeyToGet)

                val appLifecycleObserver = AppLifecycleObserver()
                ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)

                addOperatorHeaderField(
                    tapCardInputViewWeb,
                    context,
                    CardConfiguraton.MapConfigruation,
                    publickKey.toString()
                )
                tapMapConfiguration.put("platform","mobile")
                BenefitPayDataConfiguration.addTapBenefitPayStatusDelegate(tapBenefitPayStatusDelegate)
                //tapCardInputViewWeb?.init(CardConfiguraton.MapConfigruation)
                tapCardInputViewWeb?.init(tapMapConfiguration)

            }
        }
    }

}


