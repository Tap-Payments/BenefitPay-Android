package company.tap.tapbenefitpay.open.web_wrapper

import Headers
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import company.tap.tapbenefitpay.R
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
                testEncKey =  tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyTest)
                prodEncKey = tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyProduction)

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
       //  val encodedeky = getPublicEncryptionKey(publicKey,tapCardInputViewWeb)
         val encodedeky = tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyTest) //TODO replace to above one commented get dynamic from cdn
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
            if (!testEncKey.isNullOrBlank() && !prodEncKey.isNullOrBlank()) {
                return if (publicKey?.contains("test") == true) {
                    // println("EncKey>>>>>" + testEncKey)
                    testEncKey
                } else {
                    //  println("EncKey<<<<<<" + prodEncKey)
                    prodEncKey
                }
            } else {
                //  println("EncKey<<<<<<>>>>>>>>>" + testEncKey)
                return if (publicKey?.contains("test") == true) {
                    tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyTest)
                }else{
                    tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyProduction)
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

                BenefitPayDataConfiguration.addTapBenefitPayStatusDelegate(tapBenefitPayStatusDelegate)
                //tapCardInputViewWeb?.init(CardConfiguraton.MapConfigruation)
                tapCardInputViewWeb?.init(tapMapConfiguration)

            }
        }
    }

}


