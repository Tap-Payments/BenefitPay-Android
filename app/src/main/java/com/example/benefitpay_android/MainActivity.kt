package com.example.benefitpay_android

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.http.SslError
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import company.tap.tapbenefitpay.getQueryParameterFromUri
import company.tap.tapbenefitpay.open.DataConfiguration
import company.tap.tapbenefitpay.open.TapBenefitPayStatusDelegate
import company.tap.tapbenefitpay.open.web_wrapper.*
import company.tap.tapbenefitpay.open.web_wrapper.enums.BenefitPayStatusDelegate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URISyntaxException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class MainActivity : AppCompatActivity() ,TapBenefitPayStatusDelegate{
    var publicKeyLive:String = "pk_live_0zHLeUTOXBNEyJ8p6csbK52m"
    var amount:Double = 1.0
    var currency:String = "BHD"
    var transactionReference:String = ""
    var postUrl:String = ""
    var secretString = "sk_live_x28QGHEwiVet6yKq07zMOrjU"
   val number3digits:String = String.format("%.3f", amount)
   lateinit var dataTextView:TextView

    object Hmac {
        fun digest(
            msg: String,
            key: String,
            alg: String = "HmacSHA256"
        ): String {
            val signingKey = SecretKeySpec(key.toByteArray(), alg)
            val mac = Mac.getInstance(alg)
            mac.init(signingKey)

            val bytes = mac.doFinal(msg.toByteArray())
            return format(bytes)
        }

        private fun format(bytes: ByteArray): String {
            val formatter = Formatter()
            bytes.forEach { formatter.format("%02x", it) }
            return formatter.toString()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureSdk()
        val stringmsg = "x_publickey${publicKeyLive}x_amount${number3digits}x_currency${currency}x_transaction${transactionReference}x_post$postUrl"
        Log.e("stringMessage",stringmsg.toString())
        val string = Hmac.digest(msg = stringmsg, key =secretString )
        Log.e("encrypted hashString",string.toString())

        dataTextView = findViewById(R.id.data)
    }





    fun configureSdk(){


        /**
         * operator
         */
        val publicKey = intent.getStringExtra("publicKey")
        val hashStringKey = intent.getStringExtra("hashStringKey")
        val operator = HashMap<String,Any>()
        operator.put("publicKey",publicKey.toString())
        operator.put("hashString",hashStringKey.toString())
        Log.e("orderData","pbulc" + publicKey.toString() + " \nhash" + hashStringKey.toString())

        /**
         * metadata
         */
        val metada = HashMap<String,Any>()
        metada.put("id","")

        /**
         * order
         */
        val ordrId =  intent.getStringExtra("orderIdKey")
        val orderDescription =  intent.getStringExtra("orderDescKey")
        val orderAmount =  intent.getStringExtra("amountKey")
        val orderRefrence =  intent.getStringExtra("orderTransactionRefrence")
        val selectedCurrency: String = intent.getStringExtra("selectedCurrencyKey").toString()

        val order = HashMap<String,Any>()
        order.put("id",ordrId ?: "")
        order.put("amount",  if (orderAmount?.isEmpty() == true)"1" else orderAmount.toString() )
        order.put("currency",selectedCurrency)
        order.put("description",orderDescription ?: "")
        order.put("reference",orderRefrence ?: "")
        order.put("metadata",metada)
        Log.e("orderData","id" + ordrId.toString() + "  \n dest" + orderDescription.toString() +" \n orderamount " + orderAmount.toString() +"  \n orderRef" + orderRefrence.toString() + "  \n currency " + selectedCurrency.toString())

        /**
         * merchant
         */
        val merchant = HashMap<String,Any>()
        merchant.put("id", "")

        /**
         * invoice
         */
        val invoice = HashMap<String,Any>()
        invoice.put("id","")


        /**
         * phone
         */
        val phone = java.util.HashMap<String,Any>()
        phone.put("countryCode","+965")
        phone.put("number","88888888")


        /**
         * contact
         */
        val contact = java.util.HashMap<String,Any>()
        contact.put("email","tap@tap.company")
        contact.put("phone",phone)


        /**
         * interface
         */

        val selectedLanguage: String? =  intent.getStringExtra("selectedlangKey")
        val selectedTheme: String? = intent.getStringExtra("selectedthemeKey")
        val selectedCardEdge = intent.getStringExtra("selectedcardedgeKey")
        val selectedColorStylee = intent.getStringExtra("selectedcolorstyleKey")
        val loader = intent.getBooleanExtra("loaderKey",false)

        Log.e("interfaceData",selectedTheme.toString() + "language" + selectedLanguage.toString() + "cardedge " + selectedCardEdge.toString() +" loader" + loader.toString() + "selectedColorStylee " + selectedColorStylee.toString())
        val interfacee = HashMap<String,Any>()
        interfacee.put("locale",selectedLanguage ?: "en")
      //  interfacee.put("theme",selectedTheme ?: "light")
        interfacee.put("edges",selectedCardEdge ?: "circular")
     //   interfacee.put("colorStyle",selectedColorStylee ?:"colored")
     //   interfacee.put("loader",loader)


        val post = HashMap<String,Any>()
        post.put("url","")
        val configuration = LinkedHashMap<String,Any>()

        /**
       * Transaction
        * ***/
        val transaction = HashMap<String,Any>()

        transaction.put("amount",  if (orderAmount?.isEmpty() == true)"1" else orderAmount.toString() )
        transaction.put("currency",selectedCurrency)
      /*  transaction.put("amount",  "12" )
        transaction.put("currency","BHD")*/

        Log.e("transaction", " \n orderamount " + orderAmount.toString() + "  \n currency " + selectedCurrency.toString())

        /**
       * Reference */
        val reference = HashMap<String,Any>()

        reference.put("transaction",  "transaction" )
        reference.put("order","order")
        /**
         * name
         */
        val name = java.util.HashMap<String,Any>()
        name.put("lang","en")
        name.put("first", "TAP")
        name.put("middle", "middle")
        name.put("last","PAYMENTS")


        /**
         * customer
         */
        val customer = java.util.HashMap<String,Any>()
        customer.put("id", "")
        customer.put("names", listOf(name))
        customer.put("contact",contact)

        configuration.put("operator",operator)
       // configuration.put("order",order)
        configuration.put("transaction",transaction)
        configuration.put("reference",reference)

        configuration.put("customer",customer)

        configuration.put("merchant",merchant)
       // configuration.put("invoice",invoice)
        configuration.put("interface",interfacee)
        configuration.put("post",post)



        BeneiftPayConfiguration.configureWithTapBenfitPayDictionaryConfiguration(
            this,
            findViewById(R.id.benfit_pay),
            configuration,
           this)


    }



    override fun onSuccess(data: String) {
        dataTextView.text = data
    }

    override fun onReady() {
        super.onReady()
    }

    override fun onClick() {
        super.onClick()
    }

    override fun onChargeCreated(data: String) {
        super.onChargeCreated(data)
    }

    override fun onOrderCreated(data: String) {
        super.onOrderCreated(data)
    }

    override fun onCancel() {
        super.onCancel()

            dataTextView.text = "Result is :: Cancelled!!!"



    }

    override fun onError(error: String) {

            dataTextView.text = "Result is :: "+error
            Log.e("onError RECIEVED ::",error.toString())
        }



}