package com.weedscomm.smartdelivery.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog


/**
 * title, message, ok, cancel 버튼이 있는 MaterialDialog
 * MaterialCallback : OK, CANCEL 버튼 클릭 시 실행할 내용 설정
 */
object MaterialDialog {

    interface MaterialCallback {
        fun onClickOk()
        fun onClickCancel()
    }

    fun createDialog(context: Context?, title: String, message: String, callback: MaterialCallback): MaterialDialog? {
        val dialog = context?.let { MaterialDialog(it) }
        dialog?.title(text = title)
        dialog?.message(text = message)
        dialog?.cancelOnTouchOutside(false)
        dialog?.positiveButton(text = "OK") {
            callback.onClickOk()
        }
        dialog?.negativeButton(text = "CANCEL") {
            it.dismiss()
            callback.onClickCancel()
        }
        return dialog
    }

    fun createConfirmDialog(context: Context?, title: String, message: String, callback: MaterialCallback): MaterialDialog? {
        val dialog = context?.let { MaterialDialog(it) }
        dialog?.title(text = title)
        dialog?.message(text = message)
        dialog?.cancelOnTouchOutside(false)
        dialog?.positiveButton(text = "OK") {
            callback.onClickOk()
        }
        return dialog
    }
}
