package com.blueray.respect.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.blueray.respect_new.databinding.DialogSentBinding

class SuccessReplayAllDialog(
    private val message: String,
) : DialogFragment() {

    private var binding: DialogSentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSentBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.dialogTitle?.text = message

        binding?.okButton?.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("ok", "ok")
            setFragmentResult("ok", bundle)

            dismiss()
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}