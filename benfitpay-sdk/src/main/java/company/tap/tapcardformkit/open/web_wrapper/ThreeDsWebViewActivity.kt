package company.tap.tapcardformkit.open.web_wrapper

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.getDimensionsInDp
import java.util.*


class ThreeDsWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_ds_web_view)
        val cardWebFrame = findViewById<FrameLayout>(R.id.webViewFrame)
        cardWebFrame.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)


    }


    }
