package company.tap.tapbenefitpay.open


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.tap.commondatamodels.Customer
import com.tap.commondatamodels.TapAuthentication
import com.tap.commondatamodels.TapCardConfigurations
import company.tap.tapbenefitpay.R
import company.tap.tapbenefitpay.open.web_wrapper.BeneiftPayConfiguration
import company.tap.tapbenefitpay.open.web_wrapper.TapBenefitPay
//import company.tap.taplocalizationkit.LocalizationManager
//import company.tap.tapuilibrary.themekit.ThemeManager
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
@SuppressLint("StaticFieldLeak")
object BenefitPayDataConfiguration {

    private var tapBenefitPayStatusDelegate: TapBenefitPayStatusDelegate? = null
    private var applicationLifecycle: ApplicationLifecycle? = null

    var configurations: TapCardConfigurations? = null
        get() = field
        set(value) {
            field = value
        }

    var customerExample: Customer? = null
        get() = field
        set(value) {
            field = value
        }

    var authenticationExample: TapAuthentication? = null
        get() = field
        set(value) {
            field = value
        }
    var configurationsAsJson: String? = null
        get() = field
        set(value) {
            field = value
        }

    var configurationsAsHashMap: HashMap<String,Any>? = null
        get() = field
        set(value) {
            field = value
        }

    var lanuage: String? = null
        get() = field
        set(value) {
            field = value
        }









    fun setTheme(
        context: Context?,
        resources: Resources?,
        urlString: String?,
        urlPathLocal: Int?,
        fileName: String?
    ) {
        if (resources != null && urlPathLocal != null) {
            if (fileName != null && fileName.contains("dark")) {
                if (urlPathLocal != null) {
                   // ThemeManager.loadTapTheme(resources, urlPathLocal, "darktheme")
                }
            } else {
                if (urlPathLocal != null) {
                   // ThemeManager.loadTapTheme(resources, urlPathLocal, "lighttheme")
                }
            }
        } else if (urlString != null) {
            if (context != null) {
                println("urlString>>>" + urlString)
             //   ThemeManager.loadTapTheme(context, urlString, "lighttheme")
            }
        }

    }

    fun setLocale(
        context: Context,
        languageString: String,
        urlString: String?,
        resources: Resources?,
        urlPathLocal: Int?
    ) {
       // LocalizationManager.setLocale(context, Locale(languageString))
        lanuage = languageString
        if (resources != null && urlPathLocal != null) {
          //  LocalizationManager.loadTapLocale(resources, R.raw.lang)
        } else if (urlString != null) {
            if (context != null) {
             //   LocalizationManager.loadTapLocale(context, urlString)
                Log.e("local", urlString.toString())

            }
        }

    }

    fun setCustomer(customer: Customer) {
        customerExample = customer
    }


    fun setTapAuthentication(tapAuthentication: TapAuthentication) {
        authenticationExample = tapAuthentication
    }

    fun addTapBenefitPayStatusDelegate(_tapCardStatuDelegate: TapBenefitPayStatusDelegate?) {
        this.tapBenefitPayStatusDelegate = _tapCardStatuDelegate

    }
    fun addAppLifeCycle(appLifeCycle: ApplicationLifecycle?) {
        this.applicationLifecycle = appLifeCycle
    }

    fun getAppLifeCycle(): ApplicationLifecycle? {
        return this.applicationLifecycle
    }
    fun getTapCardStatusListener(): TapBenefitPayStatusDelegate? {
        return tapBenefitPayStatusDelegate
    }

    fun initializeSDK(activity: Activity, configurations: HashMap<String,Any>, tapBenefitPay: TapBenefitPay , tapBenefitPayStatusDelegate: TapBenefitPayStatusDelegate){
        BeneiftPayConfiguration.configureWithTapBenfitPayDictionaryConfiguration(activity,tapBenefitPay,configurations , tapBenefitPayStatusDelegate)
    }






}

interface TapBenefitPayStatusDelegate {
    fun onBenefitPaySuccess(data: String)
    fun onBenefitPayReady(){}
    fun onBenefitPayClick(){}
    fun onBenefitPayOrderCreated(data: String){}
    fun onBenefitPayChargeCreated(data:String){}
    fun onBenefitPayError(error: String)
    fun onBenefitPayCancel(){}
}

interface ApplicationLifecycle {

     fun onEnterForeground()
     fun onEnterBackground()


}
