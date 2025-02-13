package com.madar.madartask.core.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.madar.madartask.R
import com.madar.madartask.databinding.LayoutDeleteAccountBinding

class DeleteAccountDialog(private val itemSelectedForAction: () -> Unit) :
    DialogFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        LayoutDeleteAccountBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
    }

    private fun initClickListeners() {

        binding.btnOk.setOnClickListener {
            itemSelectedForAction.invoke()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}