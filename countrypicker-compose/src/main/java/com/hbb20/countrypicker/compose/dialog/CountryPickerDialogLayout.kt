package com.hbb20.countrypicker.compose.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.hbb20.countrypicker.config.CPDialogConfig
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.config.SizeMode
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore

@Composable
fun CountryPickerDialogLayout(
    cpDataStore: CPDataStore,
    cpDialogConfig: CPDialogConfig,
    cpRowConfig: CPRowConfig,
    onCountryClick: (CPCountry) -> Unit,
    onDismissRequest: () -> Unit,
) {

    val (countryList, messageGroup) = cpDataStore
    val (_, allowSearch, allowClearSelection, showTitle, showFullScreen, _) = cpDialogConfig
    val resizeMode = remember(cpDialogConfig) {
        cpDialogConfig.getApplicableResizeMode()
    }

    Column(
        modifier = Modifier.apply {
            if (resizeMode == SizeMode.Wrap) {
                wrapContentHeight()
            } else if (showFullScreen) {
                fillMaxSize()
            }
        }
    ) {
        if (showTitle) {
            BasicText(text = messageGroup.dialogTitle)
        }

        if (allowSearch) {
            // Edit this later
            BasicTextField(value = "", onValueChange = {})
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(
                items = countryList,
                key = CPCountry::alpha2
            ) { country ->
                CountryRow(
                    country = country,
                    cpRowConfig = cpRowConfig,
                    onClick = { onCountryClick(country) }
                )
            }
        }

        if (allowClearSelection) {
            ClickableText(
                text = AnnotatedString(messageGroup.clearSelectionText),
                onClick = {
                    onDismissRequest()
                }
            )
        }
    }
}
