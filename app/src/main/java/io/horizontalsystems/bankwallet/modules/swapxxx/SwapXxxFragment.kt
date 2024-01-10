package io.horizontalsystems.bankwallet.modules.swapxxx

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.BaseComposeFragment
import io.horizontalsystems.bankwallet.core.iconPlaceholder
import io.horizontalsystems.bankwallet.core.imageUrl
import io.horizontalsystems.bankwallet.core.slideFromBottom
import io.horizontalsystems.bankwallet.core.slideFromBottomForResult
import io.horizontalsystems.bankwallet.modules.swap.SwapMainModule
import io.horizontalsystems.bankwallet.ui.compose.ColoredTextStyle
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.TranslatableString
import io.horizontalsystems.bankwallet.ui.compose.components.AppBar
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryYellow
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryYellowWithSpinner
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonSecondaryCircle
import io.horizontalsystems.bankwallet.ui.compose.components.CoinImage
import io.horizontalsystems.bankwallet.ui.compose.components.HFillSpacer
import io.horizontalsystems.bankwallet.ui.compose.components.HSpacer
import io.horizontalsystems.bankwallet.ui.compose.components.MenuItem
import io.horizontalsystems.bankwallet.ui.compose.components.VSpacer
import io.horizontalsystems.bankwallet.ui.compose.components.body_grey
import io.horizontalsystems.bankwallet.ui.compose.components.body_grey50
import io.horizontalsystems.bankwallet.ui.compose.components.headline1_grey
import io.horizontalsystems.bankwallet.ui.compose.components.subhead1_jacob
import io.horizontalsystems.bankwallet.ui.compose.components.subhead1_leah
import io.horizontalsystems.bankwallet.ui.compose.components.subhead2_grey
import io.horizontalsystems.marketkit.models.Blockchain
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.Token
import java.math.BigDecimal
import java.util.UUID

class SwapXxxFragment : BaseComposeFragment() {
    @Composable
    override fun GetContent(navController: NavController) {
        SwapXxxScreen(navController)
    }
}

@Composable
fun SwapXxxScreen(navController: NavController) {
    val viewModel = viewModel<SwapXxxViewModel>(
        viewModelStoreOwner = navController.currentBackStackEntry!!,
        factory = SwapXxxViewModel.Factory()
    )
    val uiState = viewModel.uiState

    Yyy(
        uiState = uiState,
        onClickClose = navController::popBackStack,
        onClickCoinFrom = {
            val dex = SwapMainModule.Dex(
                blockchain = Blockchain(BlockchainType.Ethereum, "Ethereum", null),
                provider = SwapMainModule.OneInchProvider
            )
            navController.slideFromBottomForResult<SwapMainModule.CoinBalanceItem>(
                R.id.selectSwapCoinDialog,
                dex
            ) {
                viewModel.onSelectTokenFrom(it.token)
            }
        },
        onClickCoinTo = {
            val dex = SwapMainModule.Dex(
                blockchain = Blockchain(BlockchainType.Ethereum, "Ethereum", null),
                provider = SwapMainModule.OneInchProvider
            )
            navController.slideFromBottomForResult<SwapMainModule.CoinBalanceItem>(
                R.id.selectSwapCoinDialog,
                dex
            ) {
                viewModel.onSelectTokenTo(it.token)
            }
        },
        onSwitchPairs = viewModel::onSwitchPairs,
        onEnterAmount = viewModel::onEnterAmount,
        onClickProvider = {
            navController.slideFromBottom(R.id.swapXxxSelectProvider)
        }
    )
}

@Composable
private fun Yyy(
    uiState: SwapXxxUiState,
    onClickClose: () -> Unit,
    onClickCoinFrom: () -> Unit,
    onClickCoinTo: () -> Unit,
    onSwitchPairs: () -> Unit,
    onEnterAmount: (BigDecimal?) -> Unit,
    onClickProvider: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.Swap),
                menuItems = listOf(
                    MenuItem(
                        title = TranslatableString.ResString(R.string.Button_Close),
                        icon = R.drawable.ic_close,
                        onClick = onClickClose
                    )
                ),
            )
        },
        backgroundColor = ComposeAppTheme.colors.tyler,
    ) {
        Column(modifier = Modifier.padding(it)) {
            VSpacer(height = 12.dp)
            SwapInput(
                spendingCoinAmount = uiState.spendingCoinAmount,
                onSwitchPairs = onSwitchPairs,
                receivingCoinAmount = uiState.selectedQuote?.quote?.amountOut,
                onValueChange = onEnterAmount,
                onClickCoinFrom = onClickCoinFrom,
                onClickCoinTo = onClickCoinTo,
                tokenFrom = uiState.tokenFrom,
                tokenTo = uiState.tokenTo
            )

            uiState.selectedQuote?.let { selectedQuote ->
                val swapProvider = selectedQuote.provider
                VSpacer(height = 12.dp)
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, ComposeAppTheme.colors.steel20, RoundedCornerShape(12.dp)),
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        subhead2_grey(text = stringResource(R.string.Swap_Provider))
                        HFillSpacer(minWidth = 8.dp)
                        Selector(
                            icon = {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(swapProvider.icon),
                                    contentDescription = null
                                )
                            },
                            text = {
                                subhead1_leah(text = swapProvider.title)
                            },
                            onClickSelect = onClickProvider
                        )
                    }
                }
            }

            VSpacer(height = 24.dp)
            if (uiState.calculating) {
                ButtonPrimaryYellowWithSpinner(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    title = stringResource(R.string.Alert_Loading),
                    enabled = false,
                    onClick = { /*TODO*/ }
                )
            } else {
                ButtonPrimaryYellow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    title = stringResource(R.string.Swap_Proceed),
                    enabled = uiState.swapEnabled,
                    onClick = { /*TODO*/ }
                )
            }
        }
    }
}

@Composable
private fun SwapInput(
    spendingCoinAmount: BigDecimal?,
    onSwitchPairs: () -> Unit,
    receivingCoinAmount: BigDecimal?,
    onValueChange: (BigDecimal?) -> Unit,
    onClickCoinFrom: () -> Unit,
    onClickCoinTo: () -> Unit,
    tokenFrom: Token?,
    tokenTo: Token?,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(ComposeAppTheme.colors.lawrence)
                .padding()
        ) {
            Xxx(
                coinAmount = spendingCoinAmount,
                onValueChange = onValueChange,
                token = tokenFrom,
                onClickCoin = onClickCoinFrom
            )
            Xxx(
                coinAmount = receivingCoinAmount,
                onValueChange = { },
                enabled = false,
                token = tokenTo,
                onClickCoin = onClickCoinTo
            )
        }
        Divider(
            modifier = Modifier.align(Alignment.Center),
            thickness = 1.dp,
            color = ComposeAppTheme.colors.steel10
        )
        ButtonSecondaryCircle(
            modifier = Modifier.align(Alignment.Center),
            icon = R.drawable.ic_arrow_down_20,
            onClick = onSwitchPairs
        )
    }
}

@Composable
private fun Xxx(
    coinAmount: BigDecimal?,
    onValueChange: (BigDecimal?) -> Unit,
    enabled: Boolean = true,
    token: Token?,
    onClickCoin: () -> Unit,
) {
    val uuid = remember { UUID.randomUUID().toString() }
    val fiatViewModel = viewModel<FiatViewModel>(key = uuid, factory = FiatViewModel.Factory())
    val currencyAmount = fiatViewModel.fiatAmountString

    LaunchedEffect(token) {
        fiatViewModel.setCoin(token?.coin)
    }
    LaunchedEffect(coinAmount) {
        fiatViewModel.setAmount(coinAmount)
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            AmountInput(coinAmount, onValueChange, enabled)
            VSpacer(height = 3.dp)
            if (currencyAmount != null) {
                body_grey(text = currencyAmount)
            } else {
                body_grey50(text = fiatViewModel.currencyAmountHint)
            }
        }
        HSpacer(width = 8.dp)
        Selector(
            icon = {
                CoinImage(
                    iconUrl = token?.coin?.imageUrl,
                    placeholder = token?.iconPlaceholder,
                    modifier = Modifier.size(32.dp)
                )
            },
            text = {
                if (token != null) {
                    subhead1_leah(text = token.coin.code)
                } else {
                    subhead1_jacob(text = stringResource(R.string.Swap_TokenSelectorTitle))
                }
            },
            onClickSelect = onClickCoin
        )
    }
}

@Composable
private fun Selector(
    icon: @Composable() (RowScope.() -> Unit),
    text: @Composable() (RowScope.() -> Unit),
    onClickSelect: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClickSelect,
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon.invoke(this)
        HSpacer(width = 8.dp)
        text.invoke(this)
        HSpacer(width = 8.dp)
        Icon(
            painter = painterResource(R.drawable.ic_arrow_big_down_20),
            contentDescription = "",
            tint = ComposeAppTheme.colors.grey
        )
    }
}

@Composable
private fun AmountInput(
    value: BigDecimal?,
    onValueChange: (BigDecimal?) -> Unit,
    enabled: Boolean,
) {
    var text by remember(value) {
        mutableStateOf(value?.toPlainString() ?: "")
    }
    BasicTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = {
            try {
                val amount = if (it.isBlank()) {
                    null
                } else {
                    it.toBigDecimal()
                }
                text = it
                onValueChange.invoke(amount)
            } catch (e: Exception) {

            }
        },
        enabled = enabled,
        textStyle = ColoredTextStyle(
            color = ComposeAppTheme.colors.leah, textStyle = ComposeAppTheme.typography.headline1
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        cursorBrush = SolidColor(ComposeAppTheme.colors.jacob),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                headline1_grey(text = "0")
            }
            innerTextField()
        },
    )
}

//@Preview
//@Composable
//fun SwapInputPreview() {
//    ComposeAppTheme(darkTheme = true) {
//        SwapInput(
//            coinAmountHint = "0",
//            currencyAmountHint = "$0",
//            spendingCoinAmount = BigDecimal.ZERO,
//            spendingCurrencyAmount = "$123.30",
//            onSwitchPairs = {},
//            receivingCoinAmount = BigDecimal(12),
//            receivingCurrencyAmount = "$123",
//            onValueChange = {},
//            onClickCoinFrom = {},
//            onClickCoinTo = {},
//            tokenFrom = null,
//            tokenTo = null
//        )
//    }
//}

@Preview
@Composable
fun ScreenPreview() {
    ComposeAppTheme(darkTheme = true) {
        val uiState = SwapXxxUiState(
            spendingCoinAmount = BigDecimal.ZERO,
            tokenFrom = null,
            tokenTo = null,
            calculating = false,
            swapEnabled = false,
            quotes = listOf(),
            bestQuote = null,
            selectedQuote = null
        )
        Yyy(
            uiState = uiState,
            onClickClose = { /*TODO*/ },
            onClickCoinFrom = { /*TODO*/ },
            onClickCoinTo = { /*TODO*/ },
            onSwitchPairs = { /*TODO*/ },
            onEnterAmount = {}
        ) {

        }
    }
}