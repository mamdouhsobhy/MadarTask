package com.madar.madartask.madarTask.presentation.view.previewUserData

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.madar.madartask.R
import com.madar.madartask.core.presentation.base.BaseFragmentBinding
import com.madar.madartask.core.presentation.dialog.DeleteAccountDialog
import com.madar.madartask.core.presentation.extensions.TxT
import com.madar.madartask.core.presentation.extensions.showGenericAlertDialog
import com.madar.madartask.core.presentation.extensions.showInfoMessage
import com.madar.madartask.core.presentation.extensions.showSuccessMessage
import com.madar.madartask.core.presentation.utilities.Nav
import com.madar.madartask.databinding.FragmentPreviewUserDataBinding
import com.madar.madartask.madarTask.data.ModelUserData
import com.madar.madartask.madarTask.presentation.view.previewUserData.enableViews.disableViews
import com.madar.madartask.madarTask.presentation.view.previewUserData.enableViews.enableViews
import com.madar.madartask.madarTask.presentation.view.previewUserData.fillUserData.fillData
import com.madar.madartask.madarTask.presentation.view.previewUserData.validateInputs.initEditTextCollectData
import com.madar.madartask.madarTask.presentation.view.previewUserData.validateInputs.validateCollectData
import com.madar.madartask.madarTask.presentation.view.previewUserData.viewmodel.PreviewUserDataViewModel
import com.madar.madartask.splash.ActivitySplash
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentPreviewUserData : BaseFragmentBinding<FragmentPreviewUserDataBinding>() {

    private val viewModel: PreviewUserDataViewModel by viewModels()

    var canUserEditData = false

    var userGender = Nav.UserGender.male

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        initEditTextCollectData()
        addListenerOnView()

    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserDataState.collect { userData ->
                    if (userData!=null) {
                        disableViews()
                        fillData(userData)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deleteUserDataState.collect { userData ->
                    if (userData==null) {
                        intentToSplash()
                    }
                }
            }
        }
    }

    private fun addListenerOnView() {
        binding.btnDeleteAccount.setOnClickListener {
            DeleteAccountDialog {
                showLoading()
                Handler(Looper.getMainLooper()).postDelayed({
                    dismissLoading()
                    successToast(getString(R.string.your_account_deletion_request_has_been_submitted_successfully))
                    viewModel.deleteData()
                }, 3000)
            }.show(parentFragmentManager, "delete account")
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setMessage(getString(R.string.do_you_need_to_logout))
                setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                    viewModel.deleteData()
                }
                setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
            }.show()
        }

        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }

        binding.btnEditData.setOnClickListener {
            if(!canUserEditData) {
                requireContext().showGenericAlertDialog(message = getString(R.string.now_you_enabled_views_and_can_edit_your_data))
                enableViews()
            }else{
                if(validateCollectData()) {
                    changeUserData()
                }
            }
        }

        binding.layoutMale.setOnClickListener {
            if(canUserEditData) {
                userGender = Nav.UserGender.male
                binding.layoutMale.setBackgroundResource(R.drawable.drawable_border_edittext_main_color)
                binding.layoutFeMale.setBackgroundResource(R.drawable.drawable_corner_edittext_main_color)
            }
        }

        binding.layoutFeMale.setOnClickListener {
            if(canUserEditData) {
                userGender = Nav.UserGender.female
                binding.layoutMale.setBackgroundResource(R.drawable.drawable_corner_edittext_main_color)
                binding.layoutFeMale.setBackgroundResource(R.drawable.drawable_border_edittext_main_color)
            }
        }

    }

    private fun changeUserData() {
        val modelUserData = ModelUserData(
            userName = binding.edFullName.TxT(),
            age = binding.edAge.TxT(),
            jobTitle = binding.edJobTitle.TxT(),
            gender = userGender,
        )
        viewModel.updateUserData(modelUserData)
        requireContext().showSuccessMessage(getString(R.string.edited_success))
    }

    private fun showLanguageDialog() {
        val languages = arrayOf(
            getString(R.string.english),
            getString(R.string.arabic),
            getString(R.string.cancel)
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, languages)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.change_language))
            .setAdapter(adapter) { dialog, which ->
                val selectedLanguage = languages[which]
                if (selectedLanguage == getString(R.string.cancel)) {
                    dialog.dismiss()
                } else {
                    setLocale(selectedLanguage)
                    dialog.dismiss()
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun setLocale(language: String) {
        when (language) {
            getString(R.string.english) -> {
               viewModel.changeLanguage(Nav.Language.english)
            }

            getString(R.string.arabic) -> {
                viewModel.changeLanguage(Nav.Language.arabic)
            }
            else -> {}
        }

        intentToSplash()
    }

    private fun intentToSplash(){
        requireActivity().startActivity(Intent(requireActivity(), ActivitySplash::class.java))
        requireActivity().finish()
    }
}