package com.madar.madartask.core.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.madar.madartask.core.presentation.extensions.gone
import com.madar.madartask.core.presentation.extensions.show
import com.madar.madartask.R
import com.madar.madartask.databinding.DialogLayoutBinding

class Dialog : DialogFragment() {

    private var errorTitle: String? = null
    private var errorMessage: String? = null
    private var positiveAction: String? = null
    private var negativeAction: String? = null
    private var actionListener: ActionListener? = null

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DialogLayoutBinding.inflate(
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

        initializeView()
        initClickListeners()
    }

    private fun initializeView() {
        if (errorTitle.isNullOrEmpty()) {
           binding.dialogTitleTextview.gone()
        } else {
            binding.dialogTitleTextview.text = errorTitle
            binding.dialogTitleTextview.show()
        }
        if (errorMessage.isNullOrEmpty()) {
            binding.dialogMessageTextview.gone()
        } else {
            binding.dialogMessageTextview.text = errorMessage
            binding.dialogMessageTextview.show()
        }
        if (negativeAction.isNullOrEmpty()) {
            binding.dialogNegativeButton.gone()
        } else {
            binding.dialogNegativeButton.text = negativeAction.toString()
            binding.dialogNegativeButton.show()
        }
        if (positiveAction.isNullOrEmpty()) {
            binding.dialogPositiveButton.gone()
        } else {
            binding.dialogPositiveButton.text = positiveAction.toString()
            binding.dialogPositiveButton.show()
        }
    }

    private fun initClickListeners() {
        binding.dialogNegativeButton.setOnClickListener {
            isDialogShown = false
            dialog?.dismiss()
            actionListener?.onNegativeActionClicked()
        }
        binding.dialogPositiveButton.setOnClickListener {
            isDialogShown = false
            dialog?.dismiss()
            actionListener?.onPositiveActionClicked()
        }
    }

    interface ActionListener{
        fun onPositiveActionClicked()
        fun onNegativeActionClicked()
    }

    companion object {
        private var isDialogShown = false

        fun newInstance(
            errorTitle: String? = "",
            errorMessage: String? = "",
            positiveAction: String? = "",
            negativeAction: String? = "",
            actionListener: ActionListener? = null
        ): Dialog? {
            return if (!isDialogShown) {
                val objDialog = Dialog()
                objDialog.errorTitle = errorTitle
                objDialog.errorMessage = errorMessage
                objDialog.positiveAction = positiveAction
                objDialog.negativeAction = negativeAction
                isDialogShown = true
                objDialog.actionListener = actionListener
                objDialog
            } else {
                null
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}