package com.hbb20.countrypicker.compose.dialog

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.emoji.text.EmojiCompat
import com.hbb20.countrypicker.config.CPDialogConfig
import com.hbb20.countrypicker.config.CPDialogViewIds
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.config.SizeMode
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.flagprovider.CPFlagImageProvider
import com.hbb20.countrypicker.flagprovider.CPFlagProvider
import com.hbb20.countrypicker.flagprovider.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import com.hbb20.countrypicker.recyclerview.defaultDataStoreModifier

@Composable
fun CountryPickerDialog(
    onDismissRequest: () -> Unit = {},
    customMasterCountries: String = CPDataStoreGenerator.defaultMasterCountries,
    customExcludedCountries: String = CPDataStoreGenerator.defaultExcludedCountries,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.defaultCountryFileReader,
    useCache: Boolean = CPDataStoreGenerator.defaultUseCache,
    customDataStoreModifier: ((CPDataStore) -> Unit)? = defaultDataStoreModifier,
    cpFlagProvider: CPFlagProvider? = CPRowConfig.defaultFlagProvider,
    primaryTextGenerator: (CPCountry) -> String = CPRowConfig.defaultPrimaryTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultHighlightedTextGenerator,
    dialogViewIds: CPDialogViewIds = CPDialogConfig.defaultCPDialogViewIds,
    allowSearch: Boolean = CPDialogConfig.defaultCPDialogAllowSearch,
    allowClearSelection: Boolean = CPDialogConfig.defaultCPDialogAllowClearSelection,
    showTitle: Boolean = CPDialogConfig.defaultCPDialogDefaultShowTitle,
    showFullScreen: Boolean = CPDialogConfig.defaultCPDialogShowFullScreen,
    sizeMode: SizeMode = CPDialogConfig.defaultCPDialogDefaultSizeMode,
    onCountryClickListener: (CPCountry?) -> Unit = {},
) {
    val context = LocalContext.current
    val cpDataStore = CPDataStoreGenerator.generate(
        context = context,
        customMasterCountries = customMasterCountries,
        customExcludedCountries = customExcludedCountries,
        countryFileReader = countryFileReader,
        useCache = useCache
    )

    CountryPickerDialogInternal(
        cpDataStore = cpDataStore,
        cpDialogConfig = CPDialogConfig(
            dialogViewIds = dialogViewIds,
            allowSearch = allowSearch,
            allowClearSelection = allowClearSelection,
            showTitle = showTitle,
            showFullScreen = showFullScreen,
            sizeMode = sizeMode,
        ),
        cpRowConfig = CPRowConfig(
            cpFlagProvider = cpFlagProvider,
            primaryTextGenerator = primaryTextGenerator,
            secondaryTextGenerator = secondaryTextGenerator,
            highlightedTextGenerator = highlightedTextGenerator
        ),
        onCountryClickListener = onCountryClickListener,
        onDismissRequest = onDismissRequest
    )

    SideEffect {
        customDataStoreModifier?.invoke(cpDataStore)
    }
}

@Composable
internal fun CountryPickerDialogInternal(
    cpDataStore: CPDataStore,
    cpDialogConfig: CPDialogConfig,
    cpRowConfig: CPRowConfig,
    onCountryClickListener: (CPCountry?) -> Unit,
    onDismissRequest: () -> Unit,
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties()
    ) {
        CountryPickerDialogLayout(
            cpDataStore = cpDataStore,
            cpDialogConfig = cpDialogConfig,
            cpRowConfig = cpRowConfig,
            onCountryClick = {
                onCountryClickListener(it)
                onDismissRequest()
            },
            onDismissRequest = onDismissRequest
        )
    }
}

@Preview
@Composable
fun CountryPickerDialogPreview() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(true) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ClickableText(
            text = AnnotatedString("Click"),
            onClick = {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                showDialog = true
            }
        )
        if (showDialog) {
            CountryPickerDialog(
                onDismissRequest = { showDialog = false },
            ) {
                Toast.makeText(context, "${it?.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
