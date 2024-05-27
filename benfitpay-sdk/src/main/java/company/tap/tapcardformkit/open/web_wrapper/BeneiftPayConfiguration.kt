package company.tap.tapbenefitpay.open.web_wrapper

import Headers
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import company.tap.tapbenefitpay.R
import company.tap.tapbenefitpay.open.AppLifecycleObserver
import company.tap.tapbenefitpay.open.DataConfiguration
import company.tap.tapbenefitpay.open.DataConfiguration.configurationsAsHashMap
import company.tap.tapbenefitpay.open.TapBenefitPayStatusDelegate
import company.tap.tapbenefitpay.open.web_wrapper.ApiService.BASE_URL
import company.tap.tapnetworkkit.connection.NetworkApp
import company.tap.tapnetworkkit.utils.CryptoUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class BeneiftPayConfiguration {

    companion object {

        private val retrofit = ApiService.RetrofitClient.getClient()
        private val tapSDKConfigsUrl = retrofit.create(ApiService.TapSDKConfigUrls::class.java)
        private var testEncKey: String? = null
       // private var prodEncKey: String? = null
        private var dynamicBaseUrlResponse: String? = null
        fun configureWithTapBenfitPayDictionaryConfiguration(
            context: Context,
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

            MainScope().launch {
                getTapSDKConfigUrls(
                    tapMapConfiguration,
                    tapCardInputViewWeb,
                    context,
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
                testEncKey = tapSDKConfigUrlResponse.testEncKey
                urlWebStarter = tapSDKConfigUrlResponse.baseURL

                startSDKWithConfigs(
                    tapMapConfiguration,
                    tapCardInputViewWeb,
                    context,
                    tapBenefitPayStatusDelegate
                )

            } catch (e: Exception) {
             //   BASE_URL = urlWebStarter
                testEncKey =  tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyTest)
               // prodEncKey = tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyProduction)
               // prodEncKey = tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyTest)

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
                ApiService. BASE_URL.replace("benefitpay?configurations", ""),
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
            //if (!testEncKey.isNullOrBlank() && !prodEncKey.isNullOrBlank()) {
            if (!testEncKey.isNullOrBlank()) {

                    // println("EncKey>>>>>" + testEncKey)
               return  testEncKey

            } else {

                return tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyTest)



            }

        }

        private fun startSDKWithConfigs(
            tapMapConfiguration: HashMap<String, Any>,
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

                DataConfiguration.addTapBenefitPayStatusDelegate(tapBenefitPayStatusDelegate)
                tapCardInputViewWeb?.init(CardConfiguraton.MapConfigruation)

            }
        }
    }

}


