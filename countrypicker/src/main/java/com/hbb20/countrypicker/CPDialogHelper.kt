package com.hbb20.countrypicker

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CPCountry
import com.hbb20.CPDataStore
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.hbb20.countrypicker.config.CPDialogConfig
import com.hbb20.countrypicker.config.CPRecyclerViewConfig

class CPDialogHelper(
    private val cpDataStore: CPDataStore,
    val onCountryClickListener: (CPCountry?) -> Unit,
    private val cpDialogConfig: CPDialogConfig,
    private val cpRecyclerViewConfig: CPRecyclerViewConfig,
    private val cpCountryRowConfig: CPCountryRowConfig
) {
    fun createDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView =
            LayoutInflater.from(context).inflate(cpDialogConfig.dialogViewIds.layoutId, null, false)
        dialog.window?.setContentView(dialogView)
        val recyclerView: RecyclerView =
            dialogView.findViewById(cpDialogConfig.dialogViewIds.recyclerViewId)
        val etQuery: EditText? =
            cpDialogConfig.dialogViewIds.queryEditTextId?.let { etQueryId ->
                dialogView.findViewById(
                    etQueryId
                )
            }
        val imgClearQuery: ImageView? =
            cpDialogConfig.dialogViewIds.clearQueryImageViewId?.let { imgClearQueryId ->
                dialogView.findViewById(imgClearQueryId)
            }
        val tvTitle: TextView? = cpDialogConfig.dialogViewIds.titleTextViewId?.let { tvTitleId ->
            dialogView.findViewById(tvTitleId)
        }
        val btnClearSelection: Button? =
            cpDialogConfig.dialogViewIds.clearSelectionButtonId?.let { btnClearSelectionId ->
                dialogView.findViewById(btnClearSelectionId)
            }

        // configuration
        fun refreshClearQueryButton() {
            imgClearQuery?.isVisible = etQuery?.text?.isNotEmpty() ?: false
        }

        tvTitle?.text = cpDataStore.messageGroup.dialogTitle
        tvTitle?.isVisible = cpDialogConfig.showTitle
        btnClearSelection?.text = cpDataStore.messageGroup.clearSelectionText
        btnClearSelection?.setOnClickListener {
            onCountryClickListener(null)
            dialog.dismiss()
        }
        btnClearSelection?.isVisible = cpDialogConfig.allowClearSelection
        btnClearSelection?.text = cpDataStore.messageGroup.clearSelectionText
        imgClearQuery?.setOnClickListener {
            etQuery?.setText("")
        }
        etQuery?.isVisible = cpDialogConfig.allowSearch
        etQuery?.hint = cpDataStore.messageGroup.searchHint
        etQuery?.doOnTextChanged { _, _, _, _ -> refreshClearQueryButton() }

        recyclerView.loadCountriesUsingDataStoreAndConfig(
            cpDataStore,
            cpCountryRowConfig,
            cpRecyclerViewConfig,
            etQuery
        ) { selectedCountry ->
            onCountryClickListener(selectedCountry)
            dialog.dismiss()
        }

        return dialog
    }
}