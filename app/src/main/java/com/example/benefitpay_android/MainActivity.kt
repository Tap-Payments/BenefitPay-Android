package com.example.benefitpay_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.BeneiftPayConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapBenefitPay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureSdk()
    }

    fun configureSdk(){
        /**
         * operator
         */
        val operator = HashMap<String,Any>()
        operator.put("publicKey","pk_test_HJN863LmO15EtDgo9cqK7sjS")
        operator.put("hashString","")

        /**
         * metadata
         */
        val metada = HashMap<String,Any>()
        metada.put("id","")

        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id","")
        order.put("amount",  "0.1" )
        order.put("currency","BHD")
        order.put("description","")
        order.put("reference","")
        order.put("metadata",metada)
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
        val interfacee = HashMap<String,Any>()
        interfacee.put("locale","en")
        interfacee.put("theme","light")
        interfacee.put("edges","curved")
        interfacee.put("colorStyle","colored")
        interfacee.put("loader",true)

        /**
         * post
         */
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


        BeneiftPayConfiguration.configureWithTapCardDictionaryConfiguration(
            this,
            findViewById<TapBenefitPay>(R.id.benfit_pay),
            configuration,
            object : TapCardStatusDelegate {
                override fun onSuccess(data: String) {
                    Toast.makeText(this@MainActivity, "onSuccess $data", Toast.LENGTH_SHORT).show()
                }
                override fun onReady() {
                      Toast.makeText(this@MainActivity, "onReady", Toast.LENGTH_SHORT).show()

                }
                override fun onValidInput(isValid: String) {
                    //        Toast.makeText(this@MainActivity, "onValidInput ${isValid}", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: String) {
                    Toast.makeText(this@MainActivity, "onError ${error}", Toast.LENGTH_SHORT).show()
                    Log.e("test",error.toString())

                }


            })


    }
}