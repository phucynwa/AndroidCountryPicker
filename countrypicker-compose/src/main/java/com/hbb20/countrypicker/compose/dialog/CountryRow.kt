package com.hbb20.countrypicker.compose.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.emoji.text.EmojiCompat
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.flagprovider.CPFlagImageProvider
import com.hbb20.countrypicker.flagprovider.CPFlagProvider
import com.hbb20.countrypicker.flagprovider.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.models.CPCountry

@Composable
fun CountryRow(
    country: CPCountry,
    cpRowConfig: CPRowConfig = CPRowConfig(),
    onClick: () -> Unit = {},
) {
    val countryName = remember(cpRowConfig, country) {
        cpRowConfig.primaryTextGenerator.invoke(country)
    }
    val secondaryText = remember(cpRowConfig, country) {
        cpRowConfig.secondaryTextGenerator?.invoke(country)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        CountryFlag(
            country = country,
            flagProvider = cpRowConfig.cpFlagProvider
        )
        Column(modifier = Modifier.weight(1f)) {
            BasicText(text = countryName)
            if (!secondaryText.isNullOrBlank()) {
                BasicText(text = secondaryText)
            }
        }
        HighlightedInfo(country, cpRowConfig.highlightedTextGenerator)
    }
}

@Composable
fun CountryFlag(
    country: CPCountry,
    flagProvider: CPFlagProvider? = CPRowConfig.defaultFlagProvider,
) {
    val flagEmoji = remember(country) { country.flagEmoji }
    val alpha2 = remember(country) { country.alpha2 }
    when (flagProvider) {
        is DefaultEmojiFlagProvider -> {
            val flagEmojiString = if (flagProvider.useEmojiCompat) {
                EmojiCompat.get().process(flagEmoji).toString()
            } else {
                flagEmoji
            }
            BasicText(text = flagEmojiString)
        }

        is CPFlagImageProvider -> {
            Image(
                painter = painterResource(id = flagProvider.getFlag(alpha2)),
                contentDescription = alpha2
            )
        }

        else -> Unit
    }
}

@Composable
fun HighlightedInfo(country: CPCountry, highlightedTextGenerator: ((CPCountry) -> String)?) {
    val highlightedText = remember(country) {
        highlightedTextGenerator?.invoke(country)
    }
    highlightedText?.let {
        BasicText(text = it)
    }
}

@Preview
@Composable
fun CountryRowPreview() {
    CountryRow(country = CPCountry(
        name = "Afghanistan",
        englishName = "Afghanistan",
        alpha2 = "AF",
        alpha3 = "AFG",
        phoneCode = 93,
        demonym = "Afghans",
        capitalEnglishName = "Kabul",
        areaKM2 = "652230",
        population = 36373176,
        currencyCode = "AFN",
        currencyName = "Afghan Afghani",
        currencySymbol = "؋",
        cctld = "af",
        flagEmoji = "\uD83C\uDDE6\uD83C\uDDEB"
    ))
}
