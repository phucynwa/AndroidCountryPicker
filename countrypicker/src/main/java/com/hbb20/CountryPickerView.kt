package com.hbb20

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.config.CPListConfig
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.dialog.CPDialogConfig
import com.hbb20.countrypicker.dialog.CPDialogHelper
import com.hbb20.countrypicker.helper.readDialogConfigFromAttrs
import com.hbb20.countrypicker.helper.readListConfigFromAttrs
import com.hbb20.countrypicker.models.CPCountry

class CountryPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val dataStore = CPDataStoreGenerator.generate(context)
    val tvCountryInfo: TextView by lazy { findViewById<TextView>(R.id.tvCountryInfo) }
    val tvEmojiFlag: TextView by lazy { findViewById<TextView>(R.id.tvEmojiFlag) }
    var rowConfig: CPRowConfig = CPRowConfig()
    var listConfig: CPListConfig = CPListConfig()
    var dialogConfig: CPDialogConfig = CPDialogConfig()
    var selectedCountry: CPCountry? = null

    init {
        attrs?.let { readAttrs(it) }
        selectedCountry = dataStore.countryList.first { it.alpha2 == "IN" }
        setOnClickListener {
            launchDialog()
        }
        refresh()
    }

    private fun readAttrs(attrs: AttributeSet) {
        applyLayout(attrs)
        val styledAttrs =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CountryPickerView, 0, 0)
        styledAttrs?.let {
            dialogConfig = readDialogConfigFromAttrs(styledAttrs)
            listConfig = readListConfigFromAttrs(styledAttrs)
            styledAttrs.recycle()
        }
    }

    /**
     * If width is match_parent, 0
     */
    private fun applyLayout(attrs: AttributeSet) {
        val xmlWidth =
            attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width")
        //todo: eventually use single layout and just change constraints
        if (xmlWidth != null && (xmlWidth == LayoutParams.MATCH_PARENT.toString() || xmlWidth == "fill_parent" || xmlWidth == "match_parent")) {
            LayoutInflater.from(context)
                .inflate(R.layout.cp_country_picker_view_constrained, this, true)
        } else {
            LayoutInflater.from(context).inflate(R.layout.cp_country_picker_view, this, true)
        }
    }

    private fun launchDialog() {
        val dialogHelper = CPDialogHelper(dataStore, dialogConfig, listConfig, rowConfig) {
            selectedCountry = it
            refresh()
        }
        dialogHelper.createDialog(context).show()
    }

    fun refresh() {
        tvCountryInfo.text =
            selectedCountry?.name ?: dataStore.messageGroup.selectionPlaceholderText
        tvEmojiFlag.text = if (isInEditMode) "\uD83C\uDFC1" else selectedCountry?.flagEmoji
    }

    data class State(val country: CPCountry?)
}