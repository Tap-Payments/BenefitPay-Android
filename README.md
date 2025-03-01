# BenefitPay-Android
Integrating Android BenefitPay SDK in your application

# Introduction
Before diving into the development process, it's essential to establish the prerequisites and criteria necessary for a successful build. In this step, we'll outline the specific Android requirements, including the minimum SDK version and other important details you need to consider. Let's ensure your project is set up for success from the very beginning.

[![Platform](https://img.shields.io/badge/platform-Android-inactive.svg?style=flat)](https://tap-payments.github.io/goSellSDK-Android/)
[![Documentation](https://img.shields.io/badge/documentation-100%25-bright%20green.svg)](https://tap-payments.github.io/goSellSDK-Android/)
[![SDK Version](https://img.shields.io/badge/minSdkVersion-24-blue.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/targetSdkVersion-33-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/latestVersion-0.0.9-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)


# Sample Demo
![Imgur](https://imgur.com/Rw2vb6J.gif)

# Step 1 :Requirements

 1. We support from Android minSdk 24
 2. Kotlin support version 1.8.0+

# Step 2 :Get Your Public Keys

 While you can certainly use the sandbox keys available within our sample app which you can get by following
 [installation page](https://developers.tap.company/docs/](https://developers.tap.company/docs/benefit-pay-ios#step-3-installation-using-gradle)),
 however, we highly recommend visiting our [onboarding page](https://register.tap.company/sell](https://register.tap.company/ae/en/sell)), there you'll have the opportunity to register your package name and acquire your essential Tap Key for activating BenefitPay-android integration.

# Step 3 :Installation

## Gradle

in project module gradle 

```kotlin
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

Then get latest dependency  in your app module gradle
```kotlin
dependencies {
  implementation : 'com.github.Tap-Payments:BenefitPay-Android:0.0.20'
}
```

# Step 3 :Integrating BenefitPay-Android
This integration offers two distinct options: a [simple integration](https://register.tap.company/sell) designed for rapid development and streamlined merchant requirements, and an [advanced integration](https://register.tap.company/sell) that adds extra features for a more dynamic payment integration experience.

# Integration Flow
Noting that in Android, you have the ability to create the UI part of the BenefitPay-Android by creating it as normal view in your XML then implement the functionality through code or fully create it by code. Below we will describe both flows:

You will have to create a variable of type BenefitPayButton, which can be done in one of two ways:
 - Created in the XML and then linked to a variable in code.
 - Created totally within the code.
Once you create the variable in any way, you will have to follow these steps:
 - Create the parameters.
 - Pass the parameters to the variable.
 - Implement TapBenefitPayStatusDelegate interface, which allows you to get notified by different events fired from within the BenefitPay-Android SDK, also called callback functions.

# Initialising the UI
## Using xml
  ### 1- create view in xml

```kotlin
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:orientation="vertical"
    android:layout_height="match_parent"
    tools:context=".main_activity.MainActivity">

 <company.tap.tapbenefitpay.open.web_wrapper.TapBenefitPay
        android:id="@+id/tapBenefitPay"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        />

</LinearLayout>
 
```
### 2- Accessing the BenefitPayButton created in XML in your code 
### 3. Create an TapBenefitPay instance from the created view above to your Activity :
```kotlin
    lateinit var tapBenefitPay: TapBenefitPay
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
taBenefitPay = findViewById<TapBenefitPay>(R.id.tapBenefitPay)
    }

```

## Using Code to create the BenefitPayButton

```kotlin
     lateinit var tapBenefitPay: TapBenefitPay
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        /** create dynamic view of TapBenefitPay view **/ 
        tapBenefitPay  = TapBenefitPay(this)
        tapBenefitPay.layoutParams = linearLayoutParams
        /** refrence to parent layout view **/  
        this.findViewById<LinearLayout>(R.id.linear_layout).addView(tapBenefitPay)
}
```
# Simple Integration
Here, you'll discover a comprehensive table featuring the parameters applicable to the simple integration. Additionally, you'll explore the various methods for integrating the SDK, either using xml to create the layout and then implementing the interface  functionalities by code, or directly using code. Furthermore, you'll gain insights into how to receive the callback notifications.
# Parameters
Each parameter is linked to the reference section, which provides a more in depth explanation of it.

|Parameters|Description | Required | Type| Sample
|--|--|--| --|--|
| operator| It has the key obtained after registering your package name, also known as Public key. Also, the [hashString](https://developers.tap.company/docs/webhook#validate-the-webhook-hashstring) value which is used to validate live charges | True  | String| `var operator=HashMap<String,Any>(),operator.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"),operator.put("hashString","")` |
| order| Order details linked to the charge.| True  | `Dictionary`| ` var order = HashMap<String, Any>(), order.put("id","") order.put("amount",1),order.put("currency","BHD"),order.put("description",""), order.put("reference":"A reference to this order in your system"))` |
| customer| Customer details for charge process.| True  | `Dictionary`| ` var customer =  HashMap<String,Any> ,customer.put("id,""), customer.put("nameOnCard","Tap Payments"),customer.put("editable",true),) var name :HashMap<String,Any> = [["lang":"en","first":"TAP","middle":"","last":"PAYMENTS"]] "contact":["email":"tap@tap.company", "phone":["countryCode":"+965","number":"88888888"]]] customer.put("name",name) , customer.put("contact",contact)` |

# Configuring the BenefitPay-Android SDK
After creating the UI using any of the previously mentioned ways, it is time to pass the parameters needed for the SDK to work as expected and serve your need correctly.
### 1- Creating the parameters
To allow flexibility and to ease the integration, your application will only has to pass the parameters as a HashMap<String,Any> .
First, let us create the required parameters:

```kotlin
     /**
       * operator
       */
      val operator = HashMap<String,Any>()
        operator.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7")
        operator.put("hashString","")

        /**
         * phone
         */
        val phone = HashMap<String,Any>()
        phone.put("countryCode","+20")
        phone.put("number","011")

        /**
         * contact
         */
        val contact = HashMap<String,Any>()
        contact.put("email","test@gmail.com")
        contact.put("phone",phone)
        /**
         * name
         */
        val name = HashMap<String,Any>()
        name.put("lang","en")
        name.put("first","Tap")
        name.put("middle","")
        name.put("last","Payment")

        /**
         * customer
         */
        val customer = HashMap<String,Any>()
        customer.put("id", "")
customer.put("contact", contact)
customer.put("names", listOf(name)) 

        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id","order_id")
        order.put("amount","1")
        order.put("currency","BHD")
        order.put("description","description")
        order.put("reference","refrence_id")

        /**
         * configuration request
         */

        val configuration = LinkedHashMap<String,Any>()


        configuration.put("paymentMethod",paymentMethod ?: "benefitpay")
        configuration.put("merchant",merchant)
        configuration.put("scope",scopeKey.toString())
        configuration.put("redirect","tapredirectionwebsdk://") // TODO what will be in this
        configuration.put("customer",customer)
        configuration.put("interface",interfacee)
        configuration.put("reference",reference)
         configuration.put("metadata","")
         configuration.put("post",post)
        configuration.put("transaction",transaction)
        configuration.put("operator",operator)


```
### 2 - Pass these parameters to the created Button variable before as follows

```kotlin
     BeneiftPayConfiguration.configureWithTapBenfitPayDictionaryConfiguration(
            this, 
            findViewById(R.id.benfit_pay),
            configuration,
            TapCardStatusDelegate)
```

### Full code snippet for creating the parameters + passing it BenefitPayButton variable
```kotlin

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      /**
       * operator
       */
      val operator = HashMap<String,Any>()
        operator.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7")
        operator.put("hashString","")

        /**
         * phone
         */
        val phone = HashMap<String,Any>()
        phone.put("countryCode","+20")
        phone.put("number","011")

        /**
         * contact
         */
        val contact = HashMap<String,Any>()
        contact.put("email","test@gmail.com")
        contact.put("phone",phone)
        /**
         * name
         */
        val name = HashMap<String,Any>()
        name.put("lang","en")
        name.put("first","Tap")
        name.put("middle","")
        name.put("last","Payment")

        /**
         * customer
         */
        val customer = HashMap<String,Any>()
        customer.put("id", "")
customer.put("contact", contact)
customer.put("names", listOf(name)) 

        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id","order_id")
        order.put("amount","1")
        order.put("currency","BHD")
        order.put("description","description")
        order.put("reference","refrence_id")

        /**
         * configuration request
         */

        val configuration = LinkedHashMap<String,Any>()
        configuration.put("operator", operator)
        configuration.put("order",order)
        configuration.put("customer",customer)

     BeneiftPayConfiguration.configureWithTapBenfitPayDictionaryConfiguration(
            this, 
            findViewById(R.id.benfit_pay),
            configuration,
            object : TapBenefitPayStatusDelegate {
                override fun onSuccess(data: String) {
                    Log.i("data",data.toString())
                }
                override fun onReady() {
                    Log.i("data","onReady")

                }


                override fun onError(error: String) {
                    Log.i("data","onError")


                }
                
            })
}
```
# Receiving Callback Notifications
Now we have created the UI and the parameters required to to correctly display BenefitPayButton form. For the best experience, your class will have to implement TapBenefitPayStatusDelegate interface, which is a set of optional callbacks, that will be fired based on different events from within the benefit button. This will help you in deciding the logic you need to do upon receiving each event. Kindly follow the below steps in order to complete the mentioned flow:
- Go back to Activity file you want to get the callbacks into.
- Head to the class declaration line
- Add TapBenefitPayStatusDelegate
- Override the required callbacks as follows:
```kotlin
 object : TapBenefitPayStatusDelegate {
                override fun onBenefitPaySuccess(data: String) {
                    Log.i("data",data.toString())
                }
                override fun onBenefitPayReady() {
                    Log.i("data","onReady")

                }


                override fun onBenefitPayError(error: String) {
                    Log.i("data","onError")


                }
                
            }
```

# Advanced Integration
The advanced configuration for the BenefitPay-Android integration not only has all the features available in the simple integration but also introduces new capabilities, providing merchants with maximum flexibility. You can find a code below, where you'll discover comprehensive guidance on implementing the advanced flow as well as a complete description of each parameter.

# Parameters
Each parameter is linked to the reference section, which provides a more in depth explanation of it.

|Parameters |Description | Required | Type| Sample
|--|--|--| --|--|
| operator| It has the key obtained after registering your package name, also known as Public key. Also, the [hashString](https://developers.tap.company/docs/webhook#validate-the-webhook-hashstring) value which is used to validate live charges | True  | String| `var operator=HashMap<String,Any>(),operator.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"),operator.put("hashString","")` |
| order| Order details linked to the charge. | True  | `Dictionary`| ` var order = HashMap<String, Any>(), order.put("id","") order.put("amount",1),order.put("currency","BHD"),order.put("description",""), order.put("reference":"A reference to this order in your system"))` |
| invoice|Invoice id to link to the order (optional). | False  | `Dictionary`| ` var invoice = HashMap<String,Any>.put("id","")` |
| merchant| Merchant id obtained after registering your package name . | True  | `Dictionary`| ` var merchant = HashMap<String,Any>.put("id","")` |
| customer| Customer details for charge process. | True  | `Dictionary`| ` var customer =  HashMap<String,Any> ,customer.put("id,""), customer.put("nameOnCard","Tap Payments"),customer.put("editable",true),) var name :HashMap<String,Any> = [["lang":"en","first":"TAP","middle":"","last":"PAYMENTS"]] "contact":["email":"tap@tap.company", "phone":["countryCode":"+965","number":"88888888"]]] customer.put("name",name) , customer.put("contact",contact)` |
| interface| Look and feel related configurations (optional). | False  | `Dictionary`| ` var interface = HashMap<String,Any> ,interface.put("locale","en"), interface.put("theme","light"), interface.put("edges","curved"),interface.put("colorStyle","colored"),interface.put("loader",true) // Allowed values for theme : light/dark. locale: en/ar, edges: curved/flat, direction:ltr/dynaimc,colorStyle:colored/monochrome` |
| post| Webhook for server-to-server updates (optional). | False  | `Dictionary`| ` var post = HashMap<String,Any>.put("url","")` |

# Initialisation of the input
You can use a Hashmap to send data to our SDK. The benefit is that you can generate this data from one of your APIs. If we make updates to the configurations, you can update your API, avoiding the need to update your app on the Google play  Store.


```kotlin
     /**
       * operator
       */
      val operator = HashMap<String,Any>()
        operator.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7")
        operator.put("hashString","")

        /**
         * phone
         */
        val phone = HashMap<String,Any>()
        phone.put("countryCode","+20")
        phone.put("number","011")

        /**
         * contact
         */
        val contact = HashMap<String,Any>()
        contact.put("email","test@gmail.com")
        contact.put("phone",phone)
        /**
         * name
         */
        val name = HashMap<String,Any>()
        name.put("lang","en")
        name.put("first","Tap")
        name.put("middle","")
        name.put("last","Payment")

        /**
         * customer
         */
        val customer = HashMap<String,Any>()
        customer.put("id", "")
customer.put("contact", contact)
customer.put("names", listOf(name)) 

       
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

     /** interface **/

      val interfacee = HashMap<String,Any>()
        interfacee.put("locale",selectedLanguage ?: "en")
        interfacee.put("theme",selectedTheme ?: "light")
        interfacee.put("edges",selectedCardEdge ?: "curved")
        interfacee.put("colorStyle",selectedColorStylee ?:"colored")
        interfacee.put("loader",loader)

     /** post  **/

        val post = HashMap<String,Any>()
        post.put("url","")
        /**
         * configuration request
         */
/**
 * Transaction
 * ***/
val transaction = HashMap<String,Any>()
transaction.put("amount",  if (orderAmount?.isEmpty() == true)"1" else orderAmount.toString() )
transaction.put("currency",selectedCurrency)

/**
 * invoice
 */
val invoice = HashMap<String,Any>()
invoice.put("id","")

val post = HashMap<String,Any>()
post.put("url","")

val configuration = LinkedHashMap<String,Any>()
        configuration.put("operator", operator)
        configuration.put("order",order)
        configuration.put("customer",customer)
        configuration.put("merchant",merchant)
        configuration.put("invoice",invoice)
        configuration.put("interface",interfacee)
        configuration.put("post",post)

```
# Receiving Callback Notifications (Advanced Version)
The below will allow the integrators to get notified from events fired from the BenefitPayButton.

```kotlin
    override fun onBenefitPayReady() {
           print("\n\n========\n\nonReady")
    }

    override fun onBenefitPayClick() {
         print("\n\n========\n\noClick")
    }

    override fun onBenefitPayChargeCreated(data: String) {
           print("\n\n========\n\nonChargeCreated")
    }

    override fun onBenefitPayOrderCreated(data: String) {
           print("\n\n========\n\nonOrderCreated")

    }

    override fun onBenefitPayCancel() {
           print("\n\n========\n\nonCancel")
    }

    override fun onBenefitPayError(error: String) {
           print("\n\n========\n\nonError")
    }
    override fun onBenefitPaySuccess(data: String) {
    print("onBenefitPaySuccess >>")
    }

```
# Full Code Sample
Once all of the above steps are successfully completed, your Activity file should look like this:
```kotlin

class MainActivity : AppCompatActivity() ,TapBenefitPayStatusDelegate{
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
        val operator = HashMap<String,Any>()
        operator.put("publicKey",publicKey.toString())
        operator.put("hashString",hashStringKey.toString())

       

        /**
         * merchant
         */
        val merchant = HashMap<String,Any>()
        merchant.put("id", "")

       


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
        interfacee.put("locale",selectedLanguage ?: "en")
        interfacee.put("edges",selectedCardEdge ?: "curved")
        


        val post = HashMap<String,Any>()
        post.put("url","")
        val configuration = LinkedHashMap<String,Any>()

        configuration.put("paymentMethod",paymentMethod ?: "benefitpay")
        configuration.put("merchant",merchant)
        configuration.put("scope","charge")
        configuration.put("redirect","tapredirectionwebsdk://") 
        configuration.put("customer",customer)
        configuration.put("interface",interfacee)
        configuration.put("reference",reference)
        configuration.put("metadata","")
        configuration.put("post",post)
        configuration.put("transaction",transaction)
        configuration.put("operator",operator)



        BeneiftPayConfiguration.configureWithTapBenfitPayDictionaryConfiguration(
            this,
            findViewById(R.id.benfit_pay),
            configuration,
           this)


    }


    override fun onSuccess(data: String) {
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
    }

    override fun onError(error: String) {
    }

}
```


## Parameters Reference
Below you will find more details about each parameter shared in the above tables that will help you easily integrate BenefitPay-Android SDK.

### Documentation per variable

 # operator:
  - Definition: It links the payment gateway to your merchant account with Tap, in order to know your business name, logo, etc...
  - Type: string (required)
  - Fields:
     - publicKey :
        - Definition: This is a unique public key that you will receive after creating an account with Tap which is considered a reference to identify you as a merchant. You will receive 2 public keys, one for 
            sandbox/testing and another one for production.
  - Example: 
      ```kotlin
       val operator = HashMap<String,Any>()
        operator.put("publicKey","publickKeyValue")
        operator.put("hashString","hashstringValue")
      ```

## order:
  - Definition:This defined the details of the order that you are trying to purchase, in which you need to specify some details like the id, amount, currency ...
  - Type: Dictionary, (required)
  - Fields:
     - id :
        - Definition: Pass the order ID created for the order you are trying to purchase, which will be available in your database.
         Note: This field can be empty
     - currency :
        - Definition: The currency which is linked to the order being paid.

     - amount :
       -  Definition: The order amount to be paid by the customer.
         Note: Minimum amount to be added is 0.1.   
     - description :
        - Definition: Order details, which defines what the customer is paying for or the description of the service you are providing.
          
     - reference :
        - Definition: This will be the order reference present in your database in which the paying is being done for.
     
     - Example: 
      ```kotlin
      val order = HashMap<String,Any>()
      order.put("id", "")
      order.put("amount",  "1" )
      order.put("currency","BHD")
      order.put("description", "")
      order.put("reference", "")
      ```


## merchant:
  - Definition:It is the Merchant id that you get from our onboarding team. This will be used as reference for your account in Tap.
  - Type: Dictionary, (required)
  - Fields:
     - id :
        - Definition: Generated once your account with Tap is created, which is unique for every merchant. 
     - Example: 
      ```kotlin
        val merchant = HashMap<String,Any>()
        merchant.put("id", "")
      ```


## invoice:
  - Definition:After the token is generated, you can use it to pay for any invoice. Each invoice will have an invoice ID which you can add here using the SDK.
Note: An invoice will first show you a receipt/summary of the order you are going to pay for as well as the amount, currency, and any related field before actually opening the payment form and completing the payment.
  - Type: Dictionary, (optional)
  - Fields:
     - id :
        - Definition: Unique Invoice ID which we are trying to pay.
     
     - Example: 
      ```kotlin
        val invoice = HashMap<String,Any>()
        invoice.put("id","")

      ```

## post:
  - Definition:Here you can pass the webhook URL you have, in order to receive notifications of the results of each Transaction happening on your application.
  - Type: Dictionary, (optional)
  - Fields:
     - url :
        - Definition:The webhook server's URL that you want to receive notifications on.
     
     - Example: 
      ```kotlin
         val post = HashMap<String,Any>()
        post.put("url","")

      ```

## customer:
  - Definition: Here, you will collect the information of the customer that is paying..
  - Type: Dictionary, (required)
  - Fields:
     - id :
        - Definition: This is an optional field that you do not have before the charge is processed. But, after the charge, then you will receive the customer ID in the response which can be handled in the onSuccess callback function.
      
    - name :
        - Definition: Full Name of the customer paying.
            - Fields:
               - lang : Language chosen to write the customer name.
               - first : Customer's first name.
               - middle :Customer's middle name.
               - last : Customer's last name.
  
    - contact :
        - Definition: The customer's contact information like email address and phone number.
Note: The contact information has to either have the email address or the phone details of the customers or both but it should not be empty.

      - Fields:
         - email :
            - Customer's email address (string)
         - phone :
            -  Customer's Phone number details with country code and number 
           

     - Example: 
      ```kotlin
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

      ```


## interface:
  - Definition:This will help you control the layout (UI) of the payment form, like changing the theme light to dark, the language used (en or ar), ...
  - Type: Dictionary, (required)
  - Fields:
     - loader :
        - Definition: A boolean to indicate wether or not you want to show a loading view on top of the benefit button while it is performing api requests.
     - locale :
        - Definition: The language of the benefit button. Accepted values as of now are: (en -> english /ar -> arabic )
Possible Values:

     - theme :
       -  Definition: The display styling of the benefit button. Accepted values as of now are: light /dark /dynamic
     - edges :
        - Definition: Control the edges of the payment form.  (flat/curved)  
     - colorStyle :
        - Definition: How do you want the icons rendered inside the benefit button Possible Values: colored / monochrome
     
     - Example: 
      ```kotlin
       val interfacee = HashMap<String,Any>()
        interfacee.put("locale","en")
        interfacee.put("theme", "light")
        interfacee.put("edges", "curved")
        interfacee.put("colorStyle","colored")
        interfacee.put("loader",loader)
      ```      
# Expected Callbacks Responses

## onError
``` kotlin 
{
    "error":""
}
``` 
## onOrderCreated
``` kotlin 
{
    "ord_uAx145231127yHYS19Ou9B126"

}
``` 
## onChargeCreated
``` kotlin 
{
    {
    "id": "chg_TS07A5220231433Ql241910314",
    "object": "charge",
    "live_mode": false,
    "customer_initiated": true,
    "api_version": "V2",
    "method": "CREATE",
    "status": "INITIATED",
    "amount": 0.1,
    "currency": "BHD",
    "threeDSecure": true,
    "card_threeDSecure": false,
    "save_card": false,
    "order_id": "ord_uAx145231127yHYS19Ou9B126",
    "product": "GOSELL",
    "order": {
        "id": "ord_uAx145231127yHYS19Ou9B126"
    },
    "transaction": {
        "timezone": "UTC+03:00",
        "created": "1697726033236",
        "url": "",
        "expiry": {
            "period": 30,
            "type": "MINUTE"
        },
        "asynchronous": false,
        "amount": 0.1,
        "currency": "BHD"
    },
    "response": {
        "code": "100",
        "message": "Initiated"
    },
    "receipt": {
        "email": true,
        "sms": true
    },
    "customer": {
        "first_name": "TAP",
        "last_name": "PAYMENTS",
        "email": "tap@tap.company",
        "phone": {
            "country_code": " 965",
            "number": "88888888"
        }
    },
    "merchant": {
        "country": "KW",
        "currency": "KWD",
        "id": "599424"
    },
    "source": {
        "object": "source",
        "id": "src_benefit_pay"
    },
    "redirect": {
        "status": "PENDING",
        "url": ""
    },
    "post": {
        "status": "PENDING",
        "url": ""
    },
    "activities": [
        {
            "id": "activity_TS02A5420231433Mx4g1910470",
            "object": "activity",
            "created": 1697726033236,
            "status": "INITIATED",
            "currency": "BHD",
            "amount": 0.1,
            "remarks": "charge - created"
        }
    ],
    "auto_reversed": false,
    "gateway_response": {
        "name": "BENEFITPAY",
        "request": {
            "amount": "0.100",
            "currency": "BHD",
            "hash": "gMjpC12Ffz+CMhyvm+/jdYJmqbPdgAhHJvvGBABYhCI=",
            "reference": {
                "transaction": "chg_TS07A5220231433Ql241910314"
            },
            "merchant": {
                "id": "00000101"
            },
            "application": {
                "id": "4530082749"
            },
            "configuration": {
                "show_result": "0",
                "hide_mobile_qr": "0",
                "frequency": {
                    "start": 120,
                    "interval": 60,
                    "count": 10,
                    "type": "SECOND"
                }
            }
        }
    }
}

}
``` 
## onSuccess
``` kotlin 
{
   {
    "id": "chg_TS07A5220231433Ql241910314",
    "object": "charge",
    "live_mode": false,
    "customer_initiated": true,
    "api_version": "V2",
    "method": "UPDATE",
    "status": "INITIATED",
    "amount": 0.1,
    "currency": "BHD",
    "threeDSecure": true,
    "card_threeDSecure": false,
    "save_card": false,
    "order_id": "ord_uAx145231127yHYS19Ou9B126",
    "product": "GOSELL",
    "description": "",
    "order": {
        "id": "ord_uAx145231127yHYS19Ou9B126"
    },
    "transaction": {
        "timezone": "UTC+03:00",
        "created": "1697726033236",
        "url": "https://sandbox.payments.tap.company/test_gosell/v2/payment/tap_process.aspx?chg=8D9e9fdEo5N03hWrGnROvEEFw4DfqYVFv8R7R34GITc%3d",
        "expiry": {
            "period": 30,
            "type": "MINUTE"
        },
        "asynchronous": false,
        "amount": 0.1,
        "currency": "BHD"
    },
    "response": {
        "code": "100",
        "message": "Initiated"
    },
    "receipt": {
        "email": true,
        "sms": true
    },
    "customer": {
        "first_name": "TAP",
        "last_name": "PAYMENTS",
        "email": "tap@tap.company",
        "phone": {
            "country_code": " 965",
            "number": "88888888"
        }
    },
    "merchant": {
        "country": "KW",
        "currency": "KWD",
        "id": "599424"
    },
    "source": {
        "object": "source",
        "id": "src_benefit_pay"
    },
    "redirect": {
        "status": "PENDING",
        "url": ""
    },
    "post": {
        "status": "PENDING",
        "url": ""
    },
    "activities": [
        {
            "id": "activity_TS02A5420231433Mx4g1910470",
            "object": "activity",
            "created": 1697726033236,
            "status": "INITIATED",
            "currency": "BHD",
            "amount": 0.1,
            "remarks": "charge - created"
        }
    ],
    "auto_reversed": false,
    "gateway_response": {
        "name": "BENEFITPAY",
        "request": {
            "amount": "0.100",
            "currency": "BHD",
            "hash": "gMjpC12Ffz+CMhyvm+/jdYJmqbPdgAhHJvvGBABYhCI=",
            "reference": {
                "transaction": "chg_TS07A5220231433Ql241910314"
            },
            "merchant": {
                "id": "00000101"
            },
            "application": {
                "id": "4530082749"
            },
            "configuration": {
                "show_result": "0",
                "hide_mobile_qr": "0",
                "frequency": {
                    "start": 120,
                    "interval": 60,
                    "count": 10,
                    "type": "SECOND"
                }
            }
        }
    }
}

}
``` 



# Generate the hash string[](https://developers.tap.company/docs/benefit-pay-android#generate-the-hash-string)

1. Import the Crypto
```kotlin
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.util.Formatter
``` 
2. Copy this helper singleton class
```kotlin

/// Create a singleton class where you can use as a helper to generate hash strings
object TapHmac {
	/**
     This is a helper method showing how can you generate a hash string when performing live charges
     - Parameter publicKey:             The Tap public key for you as a merchant pk_.....
     - Parameter secretKey:             The Tap secret key for you as a merchant sk_.....
     - Parameter amount:                The amount you are passing to the SDK, ot the amount you used in the order if you created the order before.
     - Parameter currency:              The currency code you are passing to the SDK, ot the currency code you used in the order if you created the order before. PS: It is the capital case of the 3 iso country code ex: SAR, KWD.
     - Parameter post:                  The post url you are passing to the SDK, ot the post url you pass within the Charge API. If you are not using postUrl please pass it as empty string
     - Parameter transactionReference:  The reference.trasnsaction you are passing to the SDK(not all SDKs supports this,) or the reference.trasnsaction  you pass within the Charge API. If you are not using reference.trasnsaction please pass it as empty string
     */
    fun generateTapHashString(publicKey:String, secretKey:String, amount:Double, currency:String, postUrl:String = "", transactionReference:String = ""): String {
        // Let us generate our encryption key
        val signingKey = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(signingKey)
        // For amounts, you will need to make sure they are formatted in a way to have the correct number of decimal points. For BHD we need them to have 3 decimal points
		val formattedAmount:String = String.format("%.3f", amount)
        // Let us format the string that we will hash
        val toBeHashed = "x_publickey${publicKey}x_amount${formattedAmount}x_currency${currency}x_transaction${transactionReference}x_post$postUrl"
        // let us generate the hash string now using the HMAC SHA256 algorithm
        val bytes = mac.doFinal(toBeHashed.toByteArray())
        return format(bytes)
    }

    private fun format(bytes: ByteArray): String {
        val formatter = Formatter()
        bytes.forEach { formatter.format("%02x", it) }
        return formatter.toString()
    }
}
```
3. Call it as follows:
```kotlin
val hashString:String = TapHmac.generateTapHashString(publicKey, secretKey, amount, currency, postUrl)
```
4. Pass it within the operator model
```kotlin
val operator = HashMap<String,Any>()
operator.put("publicKey","publickKeyValue")
operator.put("hashString",hashString)
```
