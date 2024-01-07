package com.a2mp.lockscreen.Home

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.databinding.ActivityPurchaseBinding
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.anjlab.android.iab.v3.SkuDetails


class PurchaseActivity : AppCompatActivity() , BillingProcessor.IBillingHandler{

    lateinit var binding : ActivityPurchaseBinding
    var bp: BillingProcessor? = null
    var tokenBilling = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmCIcdSZYdOnaOgQYcohpMlc1UZF9QF32qZbTg1OLHfSiXg9aNdrtm//IhieKH4KP7r3vgUmYcDlolHEqHDZSqBJz+/2trBBjg9GsxQYNyTi/yPAdrcMre8s2a6JTLD4f+3PIQvJI4brw6i80YEhObEb31Vth848lM3VO+Mjuplfra+xMW2aIAGy9p+KoO5S8cquBLbX1HA8KU5GN1pFaAPFuAh9Czr48VP8B/L4IgL66y2lWwaRdxohwFtQUssZuL9ZaDLUxUEpinm1Li8DpZ3pm+eG0Xf53q7wi65ekPWw/V3z8Q1E5d9qfMQcO24WH6QwpI0uQIWIWoZfMhAiqNQIDAQAB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bp = BillingProcessor(this, "$tokenBilling", this)
        bp?.let {
            it.initialize()
        }


        val content = SpannableString("Restore")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        binding.restoreTv.text = content


        binding.closePurchaseBtn.setOnClickListener {
            finish()
        }

        binding.continuePurchaseBtn.setOnClickListener {
            bp?.purchase(this, "lockscreen.lifetime.subscription")
        }

        binding.restoreTv.setOnClickListener {

            bp?.loadOwnedPurchasesFromGoogleAsync(object :
                BillingProcessor.IPurchasesResponseListener {
                override fun onPurchasesSuccess() {

                }

                override fun onPurchasesError() {

                }
            })


        }

        bp?.getPurchaseListingDetailsAsync("lockscreen.lifetime.subscription", object :
            BillingProcessor.ISkuDetailsResponseListener {
            override fun onSkuDetailsResponse(products: MutableList<SkuDetails>?) {
                products?.get(0)?.let {
                    binding.purchaseTv.text = "${it.priceText}/Lifetime"
                }
            }

            override fun onSkuDetailsError(error: String?) {

            }
        })



    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {

        MainLockScreenData.setPurchase(this , "Active")

        finish()

    }

    override fun onPurchaseHistoryRestored() {
        Log.i("LOG2", "onPurchaseHistoryRestored: ")

    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Toast.makeText(
            this,
            error?.localizedMessage ?: "There is a problem occurred during buy Premium.",
            Toast.LENGTH_SHORT
        ).show()
        Log.i("LOG2", "onBillingError: ${error?.message} // ${error?.localizedMessage} ")
    }

    override fun onBillingInitialized() {
        Log.i("LOG2", "onBillingInitialized: initialized")
    }
}