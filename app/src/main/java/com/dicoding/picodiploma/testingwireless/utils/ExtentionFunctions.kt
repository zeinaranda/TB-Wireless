package com.dicoding.picodiploma.testingwireless.data

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.dicoding.picodiploma.testingwireless.dialog.PopupDialog

fun Fragment.setDialogSuccess(msg: String) =
    PopupDialog(DialogType.SUCCESS, msg, object : PopupDialog.DialogCallback{
        override fun dismissDialog(dialog: DialogFragment) {
            dialog.dismiss()
        }
    }).show(parentFragmentManager, null)

/**
this function call dialog with error animation
 */
fun Fragment.setDialogError(msg: String) =
    PopupDialog(DialogType.ERROR, msg, object : PopupDialog.DialogCallback{
        override fun dismissDialog(dialog: DialogFragment) {
            dialog.dismiss()
        }
    }).show(parentFragmentManager, null)

