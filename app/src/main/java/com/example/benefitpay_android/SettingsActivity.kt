package com.example.benefitpay_android

import TapLocal
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import com.chillibits.simplesettings.core.SimpleSettings
import com.chillibits.simplesettings.core.SimpleSettingsConfig
import com.chillibits.simplesettings.tool.getPrefBooleanValue
import com.chillibits.simplesettings.tool.getPrefStringValue
import com.chillibits.simplesettings.tool.getPrefs

class SettingsActivity : AppCompatActivity(),SimpleSettingsConfig.PreferenceCallback  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val configuration = SimpleSettingsConfig.Builder()
            .setActivityTitle("Configuration")
            .setPreferenceCallback(this)
            .displayHomeAsUpEnabled(false)
            .build()




        SimpleSettings(this, configuration).show(R.xml.preferences)

    }


    override fun onPreferenceClick(context: Context, key: String): Preference.OnPreferenceClickListener? {
        return when(key) {
            "dialog_preference" -> Preference.OnPreferenceClickListener {
                navigateToMainActivity()
                true
            }
            else -> super.onPreferenceClick(context, key)
        }
    }
    fun navigateToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        /**
         * operator
         */
        intent.putExtra("publicKey", getPrefStringValue("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"))
        intent.putExtra("hashStringKey", getPrefStringValue("hashStringKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"))
        intent.putExtra("scopeKey", getPrefStringValue("scopeKey","charge"))

        /**
         * order
         */
        intent.putExtra("orderIdKey", getPrefStringValue("orderIdKey",""))
        intent.putExtra("orderDescKey", getPrefStringValue("orderDescKey","test"))
        intent.putExtra("amountKey", getPrefStringValue("amountKey","0.100"))
        intent.putExtra("paymentMethodKey", getPrefStringValue("paymentMethodKey","benefitpay"))
        intent.putExtra("orderTransactionRefrence", getPrefStringValue("orderTransactionRefrence","test"))
        intent.putExtra("selectedCurrencyKey", getPrefStringValue("selectedCurrencyKey","test"))


        /**
         * interface
         */

        intent.putExtra("selectedcardedgeKey",if (getPrefStringValue("selectedcardedgeKey","") == "1")  "flat" else  getPrefStringValue("selectedcardedgeKey","flat"))
        intent.putExtra("selectedCardDirection", if (getPrefStringValue("selectedcardirectKey","") == "0") "ltr" else getPrefStringValue("selectedcardirectKey","dynamic"))
        intent.putExtra("selectedcolorstyleKey", getPrefStringValue("selectedcolorstyleKey","colored"))
        intent.putExtra("selectedthemeKey", if (getPrefStringValue("selectedthemeKey","") == "1") TapTheme.light.name else  getPrefStringValue("selectedthemeKey","light"))
        intent.putExtra("selectedlangKey", if (getPrefStringValue("selectedlangKey","") == "1") "en" else getPrefStringValue("selectedlangKey", default = "en"))
        intent.putExtra("loaderKey", getPrefBooleanValue("loaderKey",true))


        /**
         * posturl
         */


        intent.putExtra("posturlKey", getPrefStringValue("posturlKey",""))

        /**
         * Customer details
         */


        intent.putExtra("editFirstNameKey", getPrefStringValue("editFirstNameKey","TAP"))
        intent.putExtra("editMiddleNameKey", getPrefStringValue("editMiddleNameKey","Middle"))
        intent.putExtra("editLastNameKey", getPrefStringValue("editLastNameKey","Payments"))
        intent.putExtra("editPhoneCodeKey", getPrefStringValue("editPhoneCodeKey","965"))
        intent.putExtra("editPhoneNoKey", getPrefStringValue("editPhoneNoKey","66278989"))
        intent.putExtra("editEmailKey", getPrefStringValue("editEmailKey","emial@email.com"))

        finish()
        startActivity(intent)

    }
}