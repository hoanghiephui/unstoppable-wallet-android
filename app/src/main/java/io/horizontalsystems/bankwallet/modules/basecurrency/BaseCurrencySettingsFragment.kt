package io.horizontalsystems.bankwallet.modules.basecurrency

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wallet.blockchain.bitcoin.R
import io.horizontalsystems.bankwallet.core.BaseComposeFragment
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.AppBar
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryTransparent
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryYellow
import io.horizontalsystems.bankwallet.ui.compose.components.CellUniversalLawrenceSection
import io.horizontalsystems.bankwallet.ui.compose.components.HeaderText
import io.horizontalsystems.bankwallet.ui.compose.components.HsBackButton
import io.horizontalsystems.bankwallet.ui.compose.components.RowUniversal
import io.horizontalsystems.bankwallet.ui.compose.components.TextImportantWarning
import io.horizontalsystems.bankwallet.ui.compose.components.VSpacer
import io.horizontalsystems.bankwallet.ui.compose.components.body_leah
import io.horizontalsystems.bankwallet.ui.compose.components.subhead2_grey
import io.horizontalsystems.bankwallet.ui.extensions.BottomSheetHeader
import kotlinx.coroutines.launch

class BaseCurrencySettingsFragment : BaseComposeFragment() {

    @Composable
    override fun GetContent(navController: NavController) {
        BaseCurrencyScreen(navController)
    }

    override val logScreen: String
        get() = "BaseCurrencySettingsFragment"
}

@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun BaseCurrencyScreen(
    navController: NavController,
    viewModel: BaseCurrencySettingsViewModel = viewModel(
        factory = BaseCurrencySettingsModule.Factory()
    )
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        confirmValueChange = {
            if (it == ModalBottomSheetValue.Hidden) {
                viewModel.closeDisclaimer()
            }
            true
        }
    )

    if (viewModel.closeScreen) {
        navController.popBackStack()
    }

    if (viewModel.showDisclaimer) {
        LaunchedEffect(Unit) {
            sheetState.show()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = ComposeAppTheme.colors.transparent,
        sheetContent = {
            WarningBottomSheet(
                text = stringResource(
                    R.string.SettingsCurrency_DisclaimerText,
                    viewModel.disclaimerCurrencies
                ),
                onCloseClick = {
                    viewModel.closeDisclaimer()
                    scope.launch { sheetState.hide() }
                },
                onOkClick = {
                    viewModel.onAcceptDisclaimer()
                    scope.launch { sheetState.hide() }
                }
            )
        }
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.background,
            topBar = {
                AppBar(
                    title = stringResource(R.string.SettingsCurrency_Title),
                    navigationIcon = {
                        HsBackButton(onClick = { navController.popBackStack() })
                    }
                )
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {
                VSpacer(12.dp)
                CellUniversalLawrenceSection(viewModel.popularItems) { item ->
                    CurrencyCell(
                        item.currency.code,
                        item.currency.symbol,
                        item.currency.flag,
                        item.selected
                    ) { viewModel.onSelectBaseCurrency(item.currency) }
                }
                VSpacer(24.dp)
                HeaderText(
                    stringResource(R.string.SettingsCurrency_Other)
                )
                CellUniversalLawrenceSection(viewModel.otherItems) { item ->
                    CurrencyCell(
                        item.currency.code,
                        item.currency.symbol,
                        item.currency.flag,
                        item.selected
                    ) { viewModel.onSelectBaseCurrency(item.currency) }
                }
                VSpacer(24.dp)
            }
        }
    }
}

@Composable
private fun WarningBottomSheet(
    text: String,
    onCloseClick: () -> Unit,
    onOkClick: () -> Unit
) {
    BottomSheetHeader(
        iconPainter = painterResource(R.drawable.ic_attention_24),
        title = stringResource(R.string.SettingsCurrency_DisclaimerTitle),
        iconTint = ColorFilter.tint(ComposeAppTheme.colors.jacob),
        onCloseClick = onCloseClick
    ) {
        TextImportantWarning(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            text = text
        )

        ButtonPrimaryYellow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 20.dp),
            title = stringResource(id = R.string.Button_Change),
            onClick = onOkClick
        )

        ButtonPrimaryTransparent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            title = stringResource(id = R.string.Button_Cancel),
            onClick = onCloseClick
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun CurrencyCell(
    title: String,
    subtitle: String,
    icon: Int,
    checked: Boolean,
    onClick: () -> Unit
) {
    RowUniversal(
        onClick = onClick
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(32.dp),
            painter = painterResource(icon),
            contentDescription = null
        )
        Column(modifier = Modifier.weight(1f)) {
            body_leah(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(1.dp))
            subhead2_grey(
                text = subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box(
            modifier = Modifier
                .width(52.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    painter = painterResource(R.drawable.ic_checkmark_20),
                    tint = ComposeAppTheme.colors.jacob,
                    contentDescription = null,
                )
            }
        }
    }
}
