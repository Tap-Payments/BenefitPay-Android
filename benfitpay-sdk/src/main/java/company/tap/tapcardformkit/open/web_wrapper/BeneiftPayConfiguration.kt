package company.tap.tapcardformkit.open.web_wrapper

import Headers
import android.content.Context
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.open.AppLifecycleObserver
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.DataConfiguration.configurationsAsHashMap
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapnetworkkit.connection.NetworkApp
import company.tap.tapnetworkkit.utils.CryptoUtil


class BeneiftPayConfiguration {

    companion object {


        fun configureWithTapCardDictionaryConfiguration(
            context: Context,
            tapCardInputViewWeb: TapBenefitPay?,
            tapMapConfiguration: java.util.HashMap<String, Any>,
            tapCardStatusDelegate: TapCardStatusDelegate? = null
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

                DataConfiguration.addTapCardStatusDelegate(tapCardStatusDelegate)
                tapCardInputViewWeb?.init(CardConfiguraton.MapConfigruation)

            }
        }

        fun addOperatorHeaderField(
            tapCardInputViewWeb: TapBenefitPay?,
            context: Context,
            modelConfiguration: CardConfiguraton,
            publicKey: String?
        ) {
         val encodedeky = when(publicKey.toString().contains("test")){
                true->{
                    tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyTest)
                }
                false->{
                    tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkeyProduction)

                }
            }
            Log.e("publick",publicKey.toString())

            Log.e("encodedkey",encodedeky.toString())
            NetworkApp.initNetwork(
                tapCardInputViewWeb?.context ,
                publicKey ?: "",
                context.packageName,
                ApiService.BASE_URL,
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
    }
}


