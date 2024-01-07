package com.a2mp.lockscreen.Emoji

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentSmallEmojiBinding
import com.a2mp.lockscreen.databinding.FragmentSpiralEmojiBinding


class SmallEmojiFragment : Fragment() {

    private var _binding : FragmentSmallEmojiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSmallEmojiBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        liveData.observe(viewLifecycleOwner) {
            binding.emojiViewSmall.reloadData()
            Log.i("LOG44444", "onViewCreated: did change")
        }
    }




}