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

class AdOrPurchaseFragment(var title : String,var count: Int, var onAdsWatched: () -> Unit) : DialogFragment() {

    lateinit var binding: FragmentAdOrPurchaseBinding
    private var mRewardedAdNotif: RewardedAd? = null
    private var mRewardedAdWidget1: RewardedAd? = null
    private var mRewardedAdWidget2: RewardedAd? = null
    private var mRewardedAdWidget3: RewardedAd? = null
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

        var adRequestNotif = AdRequest.Builder().build()
        var adRequestWidget1 = AdRequest.Builder().build()
        var adRequestWidget2 = AdRequest.Builder().build()
        var adRequestWidget3 = AdRequest.Builder().build()

        binding.adOrPurTitle.text = title

        binding.dimBackAdPurDia.setOnClickListener{

        }

        if (count == 4) {
            binding.dialogAdOrPurchaseTvAd.text = "Watch 1 ads"
        } else {
            binding.dialogAdOrPurchaseTvAd.text = "Watch ads"
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
                    RewardedAd.load(
                        requireActivity(),
                        "ca-app-pub-6545436330357450/6718265989",
                        adRequestWidget1,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {

                                mRewardedAdWidget1 = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {

                                mRewardedAdWidget1 = rewardedAd

                                mRewardedAdWidget1?.show(requireActivity()) {

                                    fun onUserEarnedReward(rewardItem: RewardItem) {
                                        binding.gifImageViewAdPurDia.visibility = View.GONE
                                        binding.dimBackAdPurDia.visibility = View.GONE
                                        onAdsWatched.invoke()
                                        dismiss()

                                    }

                                    Log.i(
                                        "oaksdoaksd21312312321414qwsfacsqwwqeqwqwert",
                                        "onViewCreated: ad notif watched"
                                    )
                                    onUserEarnedReward(it)

                                }

                            }
                        })
                }
                2 -> {
                    RewardedAd.load(
                        requireActivity(),
                        "ca-app-pub-6545436330357450/6718265989",
                        adRequestWidget2,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {

                                mRewardedAdWidget2 = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {

                                mRewardedAdWidget2 = rewardedAd

                                mRewardedAdWidget2?.show(requireActivity()) {

                                    fun onUserEarnedReward(rewardItem: RewardItem) {
                                        binding.gifImageViewAdPurDia.visibility = View.GONE
                                        binding.dimBackAdPurDia.visibility = View.GONE
                                        count -= 1
                                        binding.dialogAdOrPurchaseTvAd.text = "Watch $count ads"

                                    }

                                    Log.i(
                                        "oaksdoaksd21312312321414qwsfacsqwwqeqwqwert",
                                        "onViewCreated: ad notif watched"
                                    )
                                    onUserEarnedReward(it)

                                }

                            }
                        })
                }
                3 -> {
                    RewardedAd.load(
                        requireActivity(),
                        "ca-app-pub-6545436330357450/6718265989",
                        adRequestWidget3,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {

                                mRewardedAdWidget3 = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {

                                mRewardedAdWidget3 = rewardedAd

                                mRewardedAdWidget3?.show(requireActivity()) {

                                    fun onUserEarnedReward(rewardItem: RewardItem) {

                                        binding.gifImageViewAdPurDia.visibility = View.GONE
                                        binding.dimBackAdPurDia.visibility = View.GONE
                                        count -= 1
                                        binding.dialogAdOrPurchaseTvAd.text = "Watch $count ads"


                                    }
                                    onUserEarnedReward(it)

                                }

                            }
                        })
                }
                4 -> {
                    RewardedAd.load(
                        requireActivity(),
                        "ca-app-pub-6545436330357450/3091680468",
                        adRequestNotif,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {

                                mRewardedAdNotif = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {

                                mRewardedAdNotif = rewardedAd

                                mRewardedAdNotif?.show(requireActivity()) {

                                    fun onUserEarnedReward(rewardItem: RewardItem) {

                                        onAdsWatched.invoke()
                                        dismiss()

                                    }
                                    onUserEarnedReward(it)

                                }

                            }
                        })
                }
                else -> {

                }
            }


        }


    }


}