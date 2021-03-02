package com.weedscomm.smartdelivery.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment


/**
 * Full Screen Progress DialogFragment
 * @param layoutResId Layout Resource
 */
class ProgressDialog(private val layoutResId: Int) : DialogFragment() {

    lateinit var dialogView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(layoutResId, container, false)
        return dialogView
    }

    fun getInstance(): ProgressDialog {
        return ProgressDialog(layoutResId)
    }

    fun dialogDismiss() {
        dialog?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        // full Screen
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
}