package com.a2mp.lockscreen.Emoji

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentMediumEmojiBinding
import com.a2mp.lockscreen.databinding.FragmentSmallEmojiBinding


class MediumEmojiFragment : Fragment() {

    private var _binding : FragmentMediumEmojiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMediumEmojiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        liveData.observe(viewLifecycleOwner) {
            binding.mediumEmoji.reloadData()
        }
    }


}