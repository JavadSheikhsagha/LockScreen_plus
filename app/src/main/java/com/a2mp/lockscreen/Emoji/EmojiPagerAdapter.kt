package com.a2mp.lockscreen.Emoji

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class EmojiPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val emojiFragments = mutableListOf<Fragment>(
        SmallEmojiFragment(),
        MediumEmojiFragment(),
        LargeEmojiFragment(),
        RingEmojiFragment(),
        SpiralEmojiFragment()
    )



    fun hasChange(){

        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return emojiFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return emojiFragments[position]
    }
}