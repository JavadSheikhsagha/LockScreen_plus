package com.a2mp.lockscreen.Emoji

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentLargeEmojiBinding
import com.a2mp.lockscreen.databinding.FragmentMediumEmojiBinding


class LargeEmojiFragment : Fragment() {

    private var _binding : FragmentLargeEmojiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLargeEmojiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        liveData.observe(viewLifecycleOwner) {
            binding.emojiLarge.reloadData()
        }

    }

    fun reinflateView() {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.fragment_large_emoji, null)
        // Perform any necessary view setup or data binding here
        // ...

        if (isAdded) {
            // Remove the old view from the parent
            (binding?.root?.parent as? ViewGroup)?.removeView(binding?.root)

            // Set the new view as the fragment's view
            _binding = FragmentLargeEmojiBinding.bind(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}