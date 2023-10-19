# BenefitPay-Android
Integrating Android BenefitPay SDK in your application

# Introduction
Before diving into the development process, it's essential to establish the prerequisites and criteria necessary for a successful build. In this step, we'll outline the specific Android requirements, including the minimum SDK version and other important details you need to consider. Let's ensure your project is set up for success from the very beginning.

[![Platform](https://img.shields.io/badge/platform-Android-inactive.svg?style=flat)](https://tap-payments.github.io/goSellSDK-Android/)
[![Documentation](https://img.shields.io/badge/documentation-100%25-bright%20green.svg)](https://tap-payments.github.io/goSellSDK-Android/)
[![SDK Version](https://img.shields.io/badge/minSdkVersion-24-blue.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/targetSdkVersion-33-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/latestVersion-0.0.3-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)


# Sample Demo
![Imgur](https://imgur.com/Rw2vb6J.gif)

# Step 1 :Requirements

 1. We support from Android minSdk 24
 2. Kotlin support version 1.8.0+

# Step 2 :Get Your Public Keys

 While you can certainly use the sandbox keys available within our sample app which you can get by following
 [installation page](https://developers.tap.company/docs/),
 however, we highly recommend visiting our [onboarding page](https://register.tap.company/sell), there you'll have the opportunity to register your package name and acquire your essential Tap Key for activating BenefitPay-android integration.

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
  implementation : 'com.github.Tap-Payments:BenefitPay-Android:{latest-tag-release}'
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

 <company.tap.tapcardformkit.open.web_wrapper.TapBenefitPay
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
| operator| This is the `Key` that you will get after registering you package name. | True  | String| `var operator=HashMap<String,Any>(),operator.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"),operator.put("hashString","")` |
| order| This is the `order id` that you created before or `amount` , `currency` , `transaction` to generate a new order .   It will be linked this token. | True  | `Dictionary`| ` var order = HashMap<String, Any>(), order.put("id","") order.put("amount",1),order.put("currency","BHD"),order.put("description",""), order.put("reference":"A reference to this order in your system"))` |
| customer| The customer details you want to attach to this tokenization process. | True  | `Dictionary`| ` var customer =  HashMap<String,Any> ,customer.put("id,""), customer.put("nameOnCard","Tap Payments"),customer.put("editable",true),) var name :HashMap<String,Any> = [["lang":"en","first":"TAP","middle":"","last":"PAYMENTS"]] "contact":["email":"tap@tap.company", "phone":["countryCode":"+965","number":"88888888"]]] customer.put("name",name) , customer.put("contact",contact)` |

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
        customer.put("nameOnCard","")
        customer.put("editable",true)
        customer.put("contact",contact)
        customer.put("name", listOf(name))

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
        customer.put("nameOnCard","")
        customer.put("editable",true)
        customer.put("contact",contact)
        customer.put("name", listOf(name))

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
                override fun onSuccess(data: String) {
                    Log.i("data",data.toString())
                }
                override fun onReady() {
                    Log.i("data","onReady")

                }


                override fun onError(error: String) {
                    Log.i("data","onError")


                }
                
            }
```
## Simple TapCardStatusDelegate
A protocol that allows integrators to get notified from events fired from the `BenefitPay-Android`. 

```kotlin
    interface TapBenefitPayStatusDelegate {
    
     ///   Will be fired whenever the card sdk finishes successfully the task assigned to it. Whether `TapToken` or `AuthenticatedToken`
    override fun  onSuccess(data: String) {
     }
    /// Will be fired whenever there is an error related to the card connectivity or apis
    /// - Parameter data: includes a JSON format for the error description and error
    override fun onError(data: String){
    }
}
```

# Advanced Integration

## Advanced Documentation

### Main input documentation
To make our sdk as dynamic as possible, we accept the input in a form of a `HashMap dictionary` . We will provide you with a sample full one for reference.
It is always recommended, that you generate this `HashMap dictionary` from your server side, so in future if needed you may be able to change the format if we did an update.



### Documentation per variable

 - operator:
	 - Responsible for passing the data that defines you as a merchant within Tap system.
 - operator.publicKey:
	 - A string, which you get after registering the app bundle id within the Tap system. It is required to correctly identify you as a merchant.
	 - You will receive a sandbox and a production key. Use, the one that matches your environment at the moment of usage.
 - order:
	 - The details about the order that you will be using the token you are generating within.
 - order.id:
	 - The id of the `order` if you already created it using our apis.
 - order.currency:
	 - The intended currency you will perform the order linked to this token afterwards.
 -  order.amount:
	 - The intended amount you will perform the order linked to this token afterwards.
 - order.description:
	 - Optional string to put some clarifications about the order if needed.
 - order.reference:
	 - Pass this value if you want to link this order to the a one you have within your system.

 - invoice.id:
	 - Optional string to pass an invoice id, that you want to link to this token afterwards.
 - merchant.id:
	 - Optional string to pass to define a sub entity registered under your key in Tap. It is the `Merchant id` that you get from our onboarding team.
 - customer.id:
	 - If you have previously have created a customer using our apis and you want to link this token to him. please pass his id.
 - customer.name:
	 - It is a list of localized names. each name will have:
		 - lang : the 2 iso code for the locale of this name for example `en`
		 - first : The first name.
		 - middle: The middle name.
		 - last : The last name.
 - customer.nameOnCard:
	 - If you want to prefill the card holder's name field.
 - customer.editable:
	 - A boolean that controls whether the customer can edit the card holder's name field or not.
 - customer.contact.email:
	 - An email string for  the customer we are creating. At least the email or the phone is required.
 - customer.contact.phone:
	 - The customer's phone:
		 - countryCode
		 - number
- interface.loader:
	- A boolean to indicate wether or not you want to show a loading view on top of the card form while it is performing api requests.
- interface.locale:
	- The language of the card form. Accepted values as of now are:
		- en
		- ar
- interface.theme:
	- The display style of the card form. Accepted values as of now are:
		- light
		- dark
		- dynamic // follow the device's display style
- interface.edges:
	- How do you want the edges of the card form to. Accepted values as of now are:
		- curved
		- flat
- interface.colorStyle:
	- How do you want the icons rendered inside the card form to. Accepted values as of now are:
		- colored
		- monochrome

## Initialization of the input

You can create a Dictionary HashMap to pass the data to our sdk. The good part about this, is that you can generate the data from one of your apis. Whenever we have an update to the configurations, you can update your api. This will make sure, that you will not have to update your app on the Google Play Store.

```kotlin
     /**
       * operator
       */
      val operator = HashMap<String,Any>()
        operator.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7")
        operator.put("hashString","")

        /**
         * merchant
         */
        val merchant = HashMap<String,Any>()
        merchant.put("id","")

        /**
         * invoice
         */
        val invoice = HashMap<String,Any>()
        invoice.put("id","")
        /**
         * post
         */
        val post = HashMap<String,Any>()
        post.put("url","")


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
        customer.put("nameOnCard","")
        customer.put("editable",true)
        customer.put("contact",contact)
        customer.put("name", listOf(name))

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
         * interface
         */
        val tapInterface = HashMap<String,Any>()
        tapInterface.put("locale","en")
        tapInterface.put("theme","light")
        tapInterface.put("edges","curved")
        tapInterface.put("colorStyle","colored")
        tapInterface.put("loader",true)

        /**
         * configuration request
         */

        val configuration = LinkedHashMap<String,Any>()
        configuration.put("operator", operator)
        configuration.put("merchant",merchant)
        configuration.put("order",order)
        configuration.put("customer",customer)
        configuration.put("invoice",invoice)
        configuration.put("post",post)
        configuration.put("interface",tapInterface) 

```



## Advanced TapCardStatusDelegate
An interface that allows integrators to get notified from events fired from the `TapBenefitPay`. 
```kotlin

interface TapBenefitPayStatusDelegate {


    override fun onSuccess(data: String)
/**
        Will be fired whenever the TapBenefitPay sdk finishes successfully the task assigned to it.
     - Parameter data: will include the data in JSON format. For `TapToken`:
     {
         "id": "tok_MrL97231045SOom8cF8G939",
         "created": 1694169907939,
         "object": "token",
         "live_mode": false,
         "type": "CARD",
         "source": "CARD-ENCRYPTED",
         "used": false,
         "card": {
             "id": "card_d9Vj7231045akVT80B8n944",
             "object": "card",
             "address": {},
             "funding": "CREDIT",
             "fingerprint": "gRkNTnMrJPtVYkFDVU485Gc%2FQtEo%2BsV44sfBLiSPM1w%3D",
             "brand": "VISA",
             "scheme": "VISA",
             "category": "",
             "exp_month": 4,
             "exp_year": 24,
             "last_four": "4242",
             "first_six": "424242",
             "name": "AHMED",
             "issuer": {
                "bank": "",
                "country": "GB",
                "id": "bnk_TS07A0720231345Qx1e0809820"
            }
         },
         "url": ""
     }
     */

    override fun onReady()
    /**
     *  Will be fired whenever the TapBenefitPay is rendered and loaded
     */

  override fun onClick()
    /**
     *  Will be fired whenever the TapBenefitPay button is clicked
     */


  override fun onOrderCreated()
    /**
     *  Will be fired whenever order is successfully created
     */


  override fun onChargeCreated()
    /**
     *  Will be fired whenever charging occurs
     */

 override fun onCancel()
    /**
     *  Will be fired whenever cancelling charging 
     */


    override fun onError(error: String)=
   /**
     *  Will be fired whenever there is an error related to the card connectivity or apis
     *  Parameter data: includes a JSON format for the error description and error
     */



}


```
