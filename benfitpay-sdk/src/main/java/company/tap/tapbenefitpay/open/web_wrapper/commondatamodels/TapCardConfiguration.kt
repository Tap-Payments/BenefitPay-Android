package company.tap.tapbenefitpay.open.web_wrapper.commondatamodels
import com.google.gson.annotations.SerializedName
import company.tap.tapbenefitpay.open.web_wrapper.commondatamodels.cardBrands.CardBrand
import company.tap.tapbenefitpay.open.web_wrapper.commondatamodels.currencies.GlobalCurrency
import kotlinx.serialization.Serializable


/**
 *  - header , operator not seen to user
 *   - scanner , nfc
 *   - tapMetaData = "hashmap"
 */

@Serializable
data class TapCardConfigurations(
    var scope: Scope = Scope.Token,
    var publicKey: String?,
    var merchant: Merchant?,
    var transaction: Transaction?,
    var customer: Customer?,
    var fields: Fields?,
    var acceptance: Acceptance?,
    var addons: Addons?,
    @SerializedName("interface")
    var tapCardConfigurationInterface: `interface`?,
    @SerializedName("authentication")
    var authentication: TapAuthentication
){
    private var operator: Operator? = null
    var operatorData: Operator
        get() = this.operator ?: Operator(publicKey) // <-- default value
        set(value) {
            this.operator = value
        }

    private var headers: Headers? = null
    var headersData: Headers
        get() = this.headers ?: Headers() // <-- default value
        set(value) {
            this.headers = value
        }
}


@Serializable
data class Operator(var publicKey: String?)

@Serializable
data class Headers(var mdn: String?=null, var application: String?=null)

@Serializable
data class Merchant(var id: String)

@Serializable
data class Transaction(var amount: String, var currency: GlobalCurrency = GlobalCurrency.SAR)

@Serializable
data class Customer(
//    var id: String,
    var nameOnCard: String = "",
    var editable: Boolean = false,
    var contact: Contact,
    var name: List<Name>
)

@Serializable
data class Acceptance(
    var supportedBrands: MutableList<CardBrand>? = mutableListOf(),
    var supportedCards: MutableList<String>? = mutableListOf(
        CardType.CREDIT.name,
        CardType.DEBIT.name
    )
)

@Serializable
data class Fields(var cardHolder: Boolean = true)

@Serializable
data class Addons(
    var loader: Boolean = true,
    var saveCard: Boolean = false,
    var displayPaymentBrands: Boolean = true,
    var scanner: Boolean = false,
    var nfc: Boolean = false
)

@Serializable
data class `interface`(
    var locale: TapLocal = TapLocal.en,
    var theme: TapTheme = TapTheme.dynamic,
    var edges: TapCardEdges = TapCardEdges.curved,
    var direction: TapCardDirections = TapCardDirections.dynamic,

    )

@Serializable
data class Contact(var email: String, var phone: Phone)

@Serializable
data class Phone(var countryCode: String, var number: String)

@Serializable
data class Name(var lang: TapLocal, var first: String, var last: String, var middle: String)

@Serializable
data class TapAuthentication(
    var description: String,
    @SerializedName("metadata")
    var tapMetadata: HashMap<String, String> = HashMap(),
    var reference: Refrence,
    var invoice: Invoice? = null,
    var authentication: Authentication,
    var post: Post? = null
)

@Serializable
data class TapMetadata(var udf1: String = "test 1", var udf2: String = "test 1")

@Serializable
data class HeaderMetaData(var application: String = "test 1", var mdn: String = "")

@Serializable
data class Refrence(var transaction: String, var order: String)

@Serializable
data class Invoice(var id: String? = null)

@Serializable
data class Authentication(var channel: String, var purpose: String)

@Serializable
data class Post(var url: String)


enum class Scope {
    AuthenticatedToken, Token
}

enum class PaymentChannels{
    PAYMENT_TRANSACTION,RECURRING_TRANSACTION,INSTALLMENT_TRANSACTION,ADD_CARD,CARDHOLDER_VERIFICATION,SAVED_CARD
}

enum class TapCardColorStyle {
    colored, monochrome
}

enum class TapLocal {
    en, ar ,dynamic
}

enum class TapTheme {
    light, dark, dynamic
}

enum class TapCardEdges {
    curved, flat
}

enum class TapCardDirections {
    dynamic, ltr
}

enum class CardType {
    CREDIT, DEBIT, ALL
}



