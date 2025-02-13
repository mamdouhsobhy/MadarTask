package com.madar.madartask.core.presentation.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Patterns
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordMatch(password: String): Boolean {
    return this == password
}

fun String.formatHighNumber(): String {
    val amount = this.toDouble()
    val symbols = DecimalFormatSymbols(Locale.ENGLISH)
    val formatter = DecimalFormat("#,###.00", symbols)
    return formatter.format(amount)
}

fun Float.formatHighNumber(): Float {
    val symbols = DecimalFormatSymbols(Locale.ENGLISH)
    val decimalFormat = DecimalFormat("#.#", symbols)
    decimalFormat.roundingMode = RoundingMode.HALF_UP
    return decimalFormat.format(this).toFloat()
}

fun String.formatHighNumberWithoutDot(): String {
    val amount = this.toDouble()
    val symbols = DecimalFormatSymbols(Locale.ENGLISH)
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(amount)
}

fun String.formatNumberWith2Digits(): String {
    val amount = this.toDouble()
    return String.format(Locale.ENGLISH, "%.2f", amount)
}

@SuppressLint("SimpleDateFormat")
fun String.getFormatDate(): String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return dfFormat.format(date)

}

@SuppressLint("SimpleDateFormat")
fun String.getFormatDateTimeInMyOrder(): String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val date: Date = dateFormat.parse(this)!!

    val dfFormat: DateFormat =
        SimpleDateFormat("MMMM d, yyyy - hh:mm:ss a")
    return dfFormat.format(date)

}

@SuppressLint("SimpleDateFormat")
fun String.getFormatDateTimeInReviews(): String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")

    val date: Date = dateFormat.parse(this)!!

    val dfFormat: DateFormat =
        SimpleDateFormat("MMMM d, yyyy - hh:mm:ss a")
    return dfFormat.format(date)

}

fun String.getHumanReadableTimeDifference(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
    val targetDate = sdf.parse(this)
    val currentTime = Date()

    val diffInMillis = currentTime.time - targetDate.time
    val seconds = diffInMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "منذ $days يوم"
        hours > 0 -> "منذ $hours ساعة"
        minutes > 0 -> "منذ $minutes دقيقة"
        else -> "منذ أقل من دقيقة"
    }
}

@SuppressLint("SimpleDateFormat")
fun String.getFormatDateTime(): String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("MMMM d, yyyy - HH:mm:ss a")
    return dfFormat.format(date)

}

@SuppressLint("SimpleDateFormat")
fun String.getUpcomingEventDateFormat(): String {

    val dateFormat = SimpleDateFormat("MM/dd/yyyy - HH:mm")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("MMMM/yyyy")
    return dfFormat.format(date)

}

@SuppressLint("SimpleDateFormat")
fun String.getPastEventDateFormat(): String {

    val dateFormat = SimpleDateFormat("MM/dd/yyyy - HH:mm")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("yyyy")
    return dfFormat.format(date)

}

fun String.prepareFilePart(partName: String): MultipartBody.Part {
    val file = File(this)
    // create RequestBody instance from file
    val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

    // MultipartBody.Part is used to send also the actual file name
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)

}

fun Uri.requestBody(context: Context, partName: String): MultipartBody.Part {
    val mediaType = extension(context)?.toMediaTypeOrNull()
    val bytes = context.contentResolver.openInputStream(this)?.buffered()?.use { it.readBytes() }

    return bytes?.let {
        MultipartBody.Part.createFormData(
            partName, fileNme(context),
            bytes.toRequestBody(mediaType, 0, bytes.size)
        )
    } ?: MultipartBody.Part.createFormData(partName, "", "".requestBodyFromString())
}

fun Uri.extension(context: Context) =
    MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType(context))

fun Uri.mimeType(context: Context) = context.contentResolver.getType(this)

fun Uri.fileNme(context: Context) =
    context.contentResolver.query(this, null, null, null, null)?.let {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        return it.getString(nameIndex)
    } ?: ""


fun String.createPartFromString(): RequestBody {
    return RequestBody.create(MultipartBody.FORM, this)
}

fun String.requestBodyFromString(): RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}


