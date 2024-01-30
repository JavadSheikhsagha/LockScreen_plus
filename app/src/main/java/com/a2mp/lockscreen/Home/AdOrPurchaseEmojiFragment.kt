package com.a2mp.lockscreen.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentAdOrPurchaseBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

val REWARDED_AD_1 = "ca-app-pub-5541510796756413/7122868976"
//val REWARDED_AD_1 = "ca-app-pub-3940256099942544/5224354917"
val REWARDED_AD_2 = "ca-app-pub-5541510796756413/8683308758"
//val REWARDED_AD_2 = "ca-app-pub-3940256099942544/5224354917"
val REWARDED_AD_3 = "ca-app-pub-5541510796756413/9557460625"
//val REWARDED_AD_3 = "ca-app-pub-3940256099942544/5224354917"


val TEST_REWARDED_AD = "ca-app-pub-3940256099942544/5224354917"

class AdOrPurchaseEmojiFragment (var title : String , var count: Int) : DialogFragment() {

    lateinit var binding: FragmentAdOrPurchaseBinding
    private var mRewardedEmoji1: RewardedAd? = null
    private var mRewardedEmoji2: RewardedAd? = null
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

        var adRequestEmoji1 = AdRequest.Builder().build()
        var adRequestEmoji2 = AdRequest.Builder().build()

        binding.adOrPurTitle.text = title

        binding.dimBackAdPurDia.setOnClickListener{

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
                    dismiss()
                }
                1 -> {
                    RewardedAd.load(
                        requireActivity(),
                        REWARDED_AD_1,
                        adRequestEmoji1,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.i("LOG23", "onAdFailedToLoad: ${adError.message}")

                                mRewardedEmoji1 = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {

                                mRewardedEmoji1 = rewardedAd

                                mRewardedEmoji1?.show(requireActivity()) {

                                    fun onUserEarnedReward(rewardItem: RewardItem) {
                                        binding.gifImageViewAdPurDia.visibility = View.GONE
                                        binding.dimBackAdPurDia.visibility = View.GONE
                                        count -= 1
                                        dismiss()
                                    }
                                    onUserEarnedReward(it)

                                }

                            }
                        })
                }
                2 -> {
                    RewardedAd.load(
                        requireActivity(),
                        REWARDED_AD_1,
                        adRequestEmoji2,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.i("LOG23", "onAdFailedToLoad: ${adError.message}")

                                mRewardedEmoji2 = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {

                                mRewardedEmoji2 = rewardedAd

                                mRewardedEmoji2?.show(requireActivity()) {

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
                else -> {

                }
            }


        }


    }

    fun getAdCount() : Int{
        return count
    }

}