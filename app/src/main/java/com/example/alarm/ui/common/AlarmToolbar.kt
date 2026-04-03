package com.example.alarm.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alarm.R
import com.example.alarm.ui.theme.AlarmTheme

@Composable
fun BackToolbarButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onBack, modifier = modifier) {
        Text(stringResource(R.string.common_back))
    }
}

@Composable
fun ScreenToolbar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (onBack != null) {
            BackToolbarButton(
                onBack = onBack,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BackToolbarButtonPreview() {
    AlarmTheme {
        BackToolbarButton(onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenToolbarWithBackPreview() {
    AlarmTheme {
        ScreenToolbar(
            title = "Экран фотографий",
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenToolbarWithoutBackPreview() {
    AlarmTheme {
        ScreenToolbar(
            title = "Главный экран"
        )
    }
}
