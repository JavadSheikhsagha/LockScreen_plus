package com.a2mp.lockscreen.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentAdOrPurchaseBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdOrPurchaseAddLockScreenFragment(
    var title: String,
    var count: Int,
    var onAdsWatched: () -> Unit
) : DialogFragment() {

    lateinit var binding: FragmentAdOrPurchaseBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdOrPurchaseBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.adOrPurTitle.text = title

        binding.dimBackAdPurDia.setOnClickListener {

        }

        if (count == 4) {
            binding.dialogAdOrPurchaseTvAd.text = "Watch 1 ads"
        } else {
            binding.dialogAdOrPurchaseTvAd.text = "Watch ad"
        }



        binding.aOPPurchaseLl.setOnClickListener {
            var intent = Intent(requireActivity(), PurchaseActivity::class.java)
            startActivity(intent)
        }

        binding.aOPAdLl.setOnClickListener {

            binding.gifImageViewAdPurDia.visibility = View.VISIBLE
            binding.gifImageViewAdPurDia.repeatCount = 280
            binding.gifImageViewAdPurDia.playAnimation()

            binding.dimBackAdPurDia.visibility = View.VISIBLE

            when (count) {
                0 -> {
                    onAdsWatched.invoke()
                    dismiss()
                }
                1 -> {
                    onAdsWatched.invoke()
                    dismiss()
                }
                else -> {

                }
            }


        }


    }


}