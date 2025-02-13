package com.madar.madartask.madarTask.presentation.view.collectUserData

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.madar.madartask.R
import com.madar.madartask.core.presentation.base.BaseFragmentBinding
import com.madar.madartask.core.presentation.extensions.TxT
import com.madar.madartask.core.presentation.utilities.Nav
import com.madar.madartask.databinding.FragmentCollectUserDataBinding
import com.madar.madartask.madarTask.data.ModelUserData
import com.madar.madartask.madarTask.presentation.view.collectUserData.validateInputs.initEditTextCollectData
import com.madar.madartask.madarTask.presentation.view.collectUserData.validateInputs.validateCollectData
import com.madar.madartask.madarTask.presentation.view.previewUserData.validateInputs.initEditTextCollectData
import com.madar.madartask.madarTask.presentation.view.previewUserData.validateInputs.validateCollectData
import com.madar.madartask.madarTask.presentation.view.collectUserData.viewmodel.CollectDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentCollectUserData : BaseFragmentBinding<FragmentCollectUserDataBinding>() {

    private val viewModel: CollectDataViewModel by viewModels()

    private var userGender = Nav.UserGender.male

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCollectedData()
        initEditTextCollectData()
        addListenerOnView()

    }

    private fun observeCollectedData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectUserDataState.collect { userData ->
                    if (userData!=null) {
                        intentToPreviewData()
                    }
                }
            }
        }
    }

    private fun addListenerOnView() {

        binding.btnCollectData.setOnClickListener {
            if (validateCollectData()) {
                saveUserData()
            }
        }

        binding.layoutMale.setOnClickListener {
            userGender = Nav.UserGender.male
            binding.layoutMale.setBackgroundResource(R.drawable.drawable_border_edittext_main_color)
            binding.layoutFeMale.setBackgroundResource(R.drawable.drawable_corner_edittext_main_color)
        }

        binding.layoutFeMale.setOnClickListener {
            userGender = Nav.UserGender.female
            binding.layoutMale.setBackgroundResource(R.drawable.drawable_corner_edittext_main_color)
            binding.layoutFeMale.setBackgroundResource(R.drawable.drawable_border_edittext_main_color)
        }
    }

    private fun saveUserData() {
        val modelUserData = ModelUserData(
            userName = binding.edFullName.TxT(),
            age = binding.edAge.TxT(),
            jobTitle = binding.edJobTitle.TxT(),
            gender = userGender,
        )
        viewModel.collectUserData(modelUserData)
    }

    private fun intentToPreviewData() {
        findNavController().navigate(R.id.fragmentPreviewUserData)
    }

}