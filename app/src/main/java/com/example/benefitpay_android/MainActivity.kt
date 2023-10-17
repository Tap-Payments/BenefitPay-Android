package com.example.benefitpay_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import company.tap.tapcardformkit.open.TapBenefitPayStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.BeneiftPayConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapBenefitPay

class MainActivity : AppCompatActivity() {
    lateinit var tapBenefitPay: TapBenefitPay
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureSdk()
    }

    fun configureSdk(){


        /**
         * operator
         */
        val publicKey = intent.getStringExtra("publicKey")
        val hashStringKey = intent.getStringExtra("hashStringKey")
        val operator = HashMap<String,Any>()
        operator.put("publicKey","pk_test_HJN863LmO15EtDgo9cqK7sjS")
        operator.put("hashString","")
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
        phone.put("countryCode","+20")
        phone.put("number","011")


        /**
         * contact
         */
        val contact = java.util.HashMap<String,Any>()
        contact.put("email","test@gmail.com")
        contact.put("phone",phone)
        /**
         * name
         */
        val name = java.util.HashMap<String,Any>()
        name.put("lang","en")
        name.put("first", "first")
        name.put("middle", "middle")
        name.put("last","last")

        /**
         * customer
         */
        val customer = java.util.HashMap<String,Any>()
        customer.put("id", "")
        customer.put("contact",contact)
        customer.put("name", listOf(name))

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
        interfacee.put("theme",selectedTheme ?: "light")
        interfacee.put("edges",selectedCardEdge ?: "curved")
        interfacee.put("colorStyle",selectedColorStylee ?:"colored")
        interfacee.put("loader",loader)

        /**
         * post
         */
        val postUrl =  intent.getStringExtra("posturlKey")

        val post = HashMap<String,Any>()
        post.put("url","")
        val configuration = LinkedHashMap<String,Any>()

        configuration.put("operator",operator)
        configuration.put("order",order)
        configuration.put("merchant",merchant)
        configuration.put("invoice",invoice)
        configuration.put("customer",customer)
        configuration.put("interface",interfacee)
        configuration.put("post",post)


        BeneiftPayConfiguration.configureWithTapBenfitPayDictionaryConfiguration(
            this,
            findViewById(R.id.benfit_pay),
            configuration,
            object : TapBenefitPayStatusDelegate {
                override fun onSuccess(data: String) {
                    findViewById<TextView>(R.id.data).text = data + "\n " + findViewById<TextView>(R.id.data).text
                    Toast.makeText(this@MainActivity, "onSuccess $data", Toast.LENGTH_SHORT).show()
                }
                override fun onReady() {
                      Toast.makeText(this@MainActivity, "onReady", Toast.LENGTH_SHORT).show()

                }


                override fun onError(error: String) {
                    findViewById<TextView>(R.id.data).text = error + "\n " + findViewById<TextView>(R.id.data).text

                    Toast.makeText(this@MainActivity, "onError ${error}", Toast.LENGTH_SHORT).show()
                    Log.e("test",error.toString())

                }

                override fun onChargeCreated(data: String) {
                    findViewById<TextView>(R.id.data).text = data + "\n " + findViewById<TextView>(R.id.data).text

                    Toast.makeText(this@MainActivity, "chargeCreated ${data}", Toast.LENGTH_SHORT).show()

                }

                override fun onOrderCreated(data: String) {
                    findViewById<TextView>(R.id.data).text = data + "\n " + findViewById<TextView>(R.id.data).text

                    Toast.makeText(this@MainActivity, "orderCreated ${data}", Toast.LENGTH_SHORT).show()
                }

                override fun onClick() {
                    Toast.makeText(this@MainActivity, "onClick ", Toast.LENGTH_SHORT).show()

                }


            })


    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
        startActivity(intent)

    }

}