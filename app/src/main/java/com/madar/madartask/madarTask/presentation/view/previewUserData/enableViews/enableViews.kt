package com.madar.madartask.madarTask.presentation.view.previewUserData.enableViews

import com.madar.madartask.madarTask.presentation.view.previewUserData.FragmentPreviewUserData

fun FragmentPreviewUserData.enableViews(){
    binding.edFullName.isEnabled = true
    binding.edFullName.isFocusable = true
    binding.edFullName.isFocusableInTouchMode = true

    binding.edAge.isEnabled = true
    binding.edAge.isFocusable = true
    binding.edAge.isFocusableInTouchMode = true

    binding.edJobTitle.isEnabled = true
    binding.edJobTitle.isFocusable = true
    binding.edJobTitle.isFocusableInTouchMode = true

    canUserEditData = true
}

fun FragmentPreviewUserData.disableViews(){
    binding.edFullName.isEnabled = false
    binding.edFullName.isFocusable = false
    binding.edFullName.isFocusableInTouchMode = false

    binding.edAge.isEnabled = false
    binding.edAge.isFocusable = false
    binding.edAge.isFocusableInTouchMode = false

    binding.edJobTitle.isEnabled = false
    binding.edJobTitle.isFocusable = false
    binding.edJobTitle.isFocusableInTouchMode = false

    canUserEditData = false
}