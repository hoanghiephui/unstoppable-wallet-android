package io.horizontalsystems.bankwallet.ui.compose.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.Select
import io.horizontalsystems.bankwallet.ui.compose.WithTranslatableTitle

@Composable
fun <T : WithTranslatableTitle> AlertGroup(
    @StringRes title: Int,
    select: Select<T>,
    onSelect: (T) -> Unit,
    onDismiss: (() -> Unit)
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            AlertHeader(title)
            select.options.forEach { option ->
                AlertItem(
                    onClick = { onSelect.invoke(option) }
                ) {
                    Text(
                        option.title.getString(),
                        color = if (option == select.selected) ComposeAppTheme.colors.jacob else ComposeAppTheme.colors.leah,
                        style = ComposeAppTheme.typography.body,
                    )
                }
            }
        }
    }
}

@Composable
fun AlertHeader(@StringRes title: Int) {
    Box(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        subhead1_grey(stringResource(title))
    }
}

@Composable
fun AlertItem(
    onClick: (() -> Unit),
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center
    ) {
        HorizontalDivider(
            modifier = Modifier.align(Alignment.TopCenter)
        )

        content.invoke()
    }
}
