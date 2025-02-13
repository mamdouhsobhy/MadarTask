package com.madar.madartask.core.presentation.utilities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.DatePicker
import java.util.Calendar

class DatePickerDialogFragment : DialogFragment() {
    private var listener: OnDateSetListener? = null
    var cal = Calendar.getInstance()
    fun setListener(listener: OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val args = arguments
        var dpd: DatePickerDialog? = null
        if (listener != null && args != null) {
            val startInYears = args.getBoolean(START_IN_YEARS)
            var context: Context? = activity
            val requireSpinnerMode = isBrokenSamsungDevice
            if (requireSpinnerMode) {
                context = ContextThemeWrapper(context, AlertDialog.THEME_HOLO_LIGHT)
            }
            val year = args.getInt(YEAR)
            val month = args.getInt(MONTH)
            val day = args.getInt(DAY_OF_MONTH)
            dpd = DatePickerDialog(context!!, listener, year, month, day)
            //            dpd.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
//            dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
            if (startInYears && !requireSpinnerMode) {
                val canOpenYearView = openYearView(dpd.datePicker)
                if (!canOpenYearView) {
                    context = ContextThemeWrapper(activity, AlertDialog.THEME_HOLO_LIGHT)
                    dpd = DatePickerDialog(context, listener, year, month, day)
                    //                    dpd.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
//                    dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
                }
            }
        } else {
            showsDialog = false
            dismissAllowingStateLoss()
        }
        return dpd!!
    }

    companion object {
        private const val START_IN_YEARS = "com.myapp.picker.START_IN_YEARS"
        private const val YEAR = "com.myapp.picker.YEAR"
        private const val MONTH = "com.myapp.picker.MONTH"
        private const val DAY_OF_MONTH = "com.myapp.picker.DAY_OF_MONTH"
        fun newInstance(startInYears: Boolean, c: Calendar): DatePickerDialogFragment {
            val f = DatePickerDialogFragment()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val args = Bundle()
            args.putBoolean(START_IN_YEARS, startInYears)
            args.putInt(YEAR, year)
            args.putInt(MONTH, month)
            args.putInt(DAY_OF_MONTH, day)
            f.arguments = args
            return f
        }

        private val isBrokenSamsungDevice: Boolean
            private get() = Build.MANUFACTURER.equals("samsung", ignoreCase = true) &&
                    isBetweenAndroidVersions(
                        Build.VERSION_CODES.LOLLIPOP,
                        Build.VERSION_CODES.LOLLIPOP_MR1
                    )

        private fun isBetweenAndroidVersions(min: Int, max: Int): Boolean {
            return Build.VERSION.SDK_INT >= min && Build.VERSION.SDK_INT <= max
        }

        private fun openYearView(datePicker: DatePicker): Boolean {
            if (isBrokenSamsungDevice) {
                return false
            }
            try {
                val mDelegateField = datePicker.javaClass.getDeclaredField("mDelegate")
                mDelegateField.isAccessible = true
                val delegate = mDelegateField[datePicker]
                val setCurrentViewMethod = delegate.javaClass.getDeclaredMethod(
                    "setCurrentView",
                    Int::class.javaPrimitiveType
                )
                setCurrentViewMethod.isAccessible = true
                setCurrentViewMethod.invoke(delegate, 1)
            } catch (e: Exception) {
                return false
            }
            return true
        }
    }
}