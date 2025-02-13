package com.madar.madartask.core.presentation.extensions

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.madar.madartask.core.presentation.utilities.ViewSidesEnum
import com.madar.madartask.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(rootView?.windowToken, 0)
}

fun Context.hideKeyboard(view: View?) = view?.let {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive) {
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

val ViewGroup.layoutInflater: LayoutInflater get() = LayoutInflater.from(this.context)

@Throws(Throwable::class)
fun getImageFromVideo(videoPath: String): Bitmap {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(videoPath)
    return retriever.getFrameAtTime(1000000)!!
}

fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(
            inContext.contentResolver, inImage,
            "Title", null
        )
    return Uri.parse(path)
}

fun getFileSize(filePath: String?): Double {
    val file = File(filePath)
    val fileSize = (file.length() / 1024).toString().toInt().toDouble()
    return fileSize / 1024
}

fun Context.shareToSocialMedia(shareText: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, shareText)
    startActivity(Intent.createChooser(intent, "Share"))
}


fun TextView.setDrawable(side: ViewSidesEnum, drawable: Drawable) {
    when (side) {
        ViewSidesEnum.LEFT -> setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        ViewSidesEnum.TOP -> setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
        ViewSidesEnum.RIGHT -> setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        ViewSidesEnum.BOTTOM -> setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
    }
}

fun TextView.transformIntoDatePicker(context: Context, format: String, minDate: Date? = null) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false
    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(format, Locale("ar"))
            setText(sdf.format(myCalendar.time))
        }

    DatePickerDialog(
        context, datePickerOnDataSetListener, myCalendar
            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)
    ).run {
        minDate?.time?.also { datePicker.minDate = it }
        show()
    }
}

fun dateToMillis(dateString: String, dateFormat: String = "yyyy-MM-dd"): Long {
    val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    val date = sdf.parse(dateString)
    return date?.time ?: 0L
}

fun EditText.selectCurrentDayOrFutureDays(
    context: Context,
    dateFormat: String,
    minDate: Long?= null,
    itemApplyAction: (value: String) -> Unit
) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false
    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val formattedDate = sdf.format(myCalendar.time)
            setText(formattedDate)
            itemApplyAction.invoke(formattedDate)
        }

    val datePickerDialog = DatePickerDialog(
        context,
        datePickerOnDataSetListener,
        myCalendar.get(Calendar.YEAR),
        myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)
    )

    // Set minimum date to the current date to prevent selecting past dates
    if(minDate==null) {
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
    }else{
        datePickerDialog.datePicker.minDate = minDate
    }

    datePickerDialog.show()
}

fun EditText.selectCurrentDayOrPastDays(
    context: Context,
    dateFormat: String,
    minDate: Date? = null,
    itemApplyAction: (value: String) -> Unit
) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false
    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val formattedDate = sdf.format(myCalendar.time)
            setText(formattedDate)
            itemApplyAction.invoke(formattedDate)
        }

    val datePickerDialog = DatePickerDialog(
        context,
        datePickerOnDataSetListener,
        myCalendar.get(Calendar.YEAR),
        myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)
    )

    // Set minimum date if provided
    minDate?.time?.also { datePickerDialog.datePicker.minDate = it }

    // Set maximum date to the current date to prevent selecting future dates
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    datePickerDialog.show()
}

fun TextView.transformFromDatePicker(context: Context, format: String, maxDate: Date? = null) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false
    var date: Date
    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(format, Locale.UK)
            setText(sdf.format(myCalendar.time))
        }

    DatePickerDialog(
        context, datePickerOnDataSetListener, myCalendar
            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)
    ).run {
        date = myCalendar.time
        maxDate?.time?.also { datePicker.maxDate = it }
        show()
    }
}

@SuppressLint("NewApi", "SimpleDateFormat")
fun String.reFormateDate(pattern: String? = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"): String {
    val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
    var Date1: Date? = null
    var Date2: Date? = null
    var count = 0
    try {
        var currentDate = Date()
        val formattedCurrentDate: String = sdf.format(currentDate)
        Date1 = sdf.parse(this)
        Date2 = sdf.parse(formattedCurrentDate)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val diff = Date2!!.time - Date1!!.time
    var dateFormat = ""
//    val secondsAgo = diff / 1000
//    val minutes = 60
//    val hour = 60 * minutes
//    val day = 24 * hour
//    val week = 7 * day
//    val month = 4 + week

    val suffix = "Ago"

    val second: Long = TimeUnit.MILLISECONDS.toSeconds(diff)
    val minute: Long = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hour: Long = TimeUnit.MILLISECONDS.toHours(diff)
    val day: Long = TimeUnit.MILLISECONDS.toDays(diff)

    if (second < 60) {
//        dateFormat = "$second Seconds $suffix"
        dateFormat = "Just Now"
    } else if (minute < 60) {
        if (minute > 1) {
            dateFormat = "$minute Minutes $suffix"

        } else {
            dateFormat = "$minute Minute $suffix"

        }
    } else if (hour < 24) {
        if (hour > 1) {
            dateFormat = "$hour Hours $suffix"

        } else {
            dateFormat = "$hour Hour $suffix"

        }
    } else if (day >= 7) {
        if (day > 360) {
            if (day / 360 > 1) {
                dateFormat = (day / 360).toString() + " Years " + suffix

            } else {
                dateFormat = (day / 360).toString() + " Year " + suffix

            }
        } else if (day > 30) {
            if (day / 30 > 1) {
                dateFormat = (day / 30).toString() + " Months " + suffix
            } else {
                dateFormat = (day / 30).toString() + " Month " + suffix
            }
        } else {
            if (day / 7 > 1) {
                dateFormat = (day / 7).toString() + " Weeks " + suffix

            } else {
                dateFormat = (day / 7).toString() + " Week " + suffix

            }
        }
    } else if (day < 7) {
        if (day > 1) {
            dateFormat = "$day Days $suffix"

        } else {
            dateFormat = "$day Day $suffix"
        }
    }



    return dateFormat
}

fun EditText.onTextChange(text: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            text(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun View.setError(textView: TextView, msg: String) {
    this.setBackgroundResource(R.drawable.bg_error)
    textView.text = msg
    textView.isVisible = true
}

fun View.resetError(textView: TextView) {
    this.setBackgroundResource(R.drawable.drawable_corner_edittext_main_color)
    textView.isVisible = false
}

fun EditText.TxT(): String {
    return this.text.toString()
}

fun TextView.TxT(): String {
    return this.text.toString()
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateTime(): String {
    val tz = TimeZone.getTimeZone("UTC")
    val df: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    df.timeZone = tz
    return df.format(Date())
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateTimeForReview(): String {
    val tz = TimeZone.getTimeZone("UTC")
    val df: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    df.timeZone = tz
    return df.format(Date())
}

fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

