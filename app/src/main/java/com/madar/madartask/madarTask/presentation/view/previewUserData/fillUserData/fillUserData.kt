package com.madar.madartask.madarTask.presentation.view.previewUserData.fillUserData

import com.madar.madartask.R
import com.madar.madartask.core.presentation.utilities.Nav
import com.madar.madartask.madarTask.data.ModelUserData
import com.madar.madartask.madarTask.presentation.view.previewUserData.FragmentPreviewUserData

fun FragmentPreviewUserData.fillData(modelUserData: ModelUserData?){
    binding.edFullName.setText(modelUserData?.userName)
    binding.edAge.setText(modelUserData?.age)
    binding.edJobTitle.setText(modelUserData?.jobTitle)

    if(modelUserData?.gender == Nav.UserGender.male){
        userGender = Nav.UserGender.male
        binding.layoutMale.setBackgroundResource(R.drawable.drawable_border_edittext_main_color)
        binding.layoutFeMale.setBackgroundResource(R.drawable.drawable_corner_edittext_main_color)
    }else{
        userGender = Nav.UserGender.female
        binding.layoutMale.setBackgroundResource(R.drawable.drawable_corner_edittext_main_color)
        binding.layoutFeMale.setBackgroundResource(R.drawable.drawable_border_edittext_main_color)
    }
}