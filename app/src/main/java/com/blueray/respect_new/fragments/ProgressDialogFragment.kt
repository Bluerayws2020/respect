package com.blueray.respect_new.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.blueray.respect_new.databinding.ProgressDialogBinding

class ProgressDialogFragment : DialogFragment() {

    private var binding: ProgressDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProgressDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}