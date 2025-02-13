package com.madar.madartask.madarTask.presentation.view.previewUserData.validateInputs

import com.madar.madartask.R
import com.madar.madartask.core.presentation.extensions.TxT
import com.madar.madartask.core.presentation.extensions.onTextChange
import com.madar.madartask.core.presentation.extensions.resetError
import com.madar.madartask.core.presentation.extensions.setError
import com.madar.madartask.madarTask.presentation.view.previewUserData.FragmentPreviewUserData

fun FragmentPreviewUserData.initEditTextCollectData() = with(binding) {
    edFullName.onTextChange {
        edFullName.resetError(tvErrorFullName)
    }
    edAge.onTextChange {
        edAge.resetError(tvErrorAge)
    }
    edJobTitle.onTextChange {
        edJobTitle.resetError(tvErrorJobTitle)
    }
}

fun FragmentPreviewUserData.validateCollectData(): Boolean =
    with(binding) {
        if (edFullName.TxT().isEmpty()) {
            edFullName.setError(
                tvErrorFullName,
                requireContext().getString(R.string.please_enter_full_name)
            )
            false
        } else if (edAge.TxT().isEmpty()) {
            edAge.setError(
                tvErrorAge,
                requireContext().getString(R.string.please_enter_your_age)
            )
            false
        } else if (edJobTitle.TxT().isEmpty()) {
            edJobTitle.setError(
                tvErrorJobTitle,
                requireContext().getString(R.string.please_enter_your_job_title)
            )
            false
        } else {
            true
        }
}
