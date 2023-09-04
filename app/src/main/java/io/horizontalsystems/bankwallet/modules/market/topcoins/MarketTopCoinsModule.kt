package io.horizontalsystems.bankwallet.modules.market.topcoins

import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.modules.market.MarketField
import io.horizontalsystems.bankwallet.modules.market.SortingField
import io.horizontalsystems.bankwallet.modules.market.TopMarket
import io.horizontalsystems.bankwallet.ui.compose.Select

object MarketTopCoinsModule {

    class Factory(
        private val topMarket: TopMarket? = null,
        private val sortingField: SortingField? = null,
        private val marketField: MarketField? = null
    ) : ViewModelProvider.Factory {
        /*@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val topMarketsRepository = MarketTopMoversRepository(App.marketKit)
            val service = MarketTopCoinsService(
                topMarketsRepository,
                App.currencyManager,
                App.marketFavoritesManager,
                topMarket ?: defaultTopMarket,
                sortingField ?: defaultSortingField
            )
            return MarketTopCoinsViewModel(
                service,
                marketField ?: defaultMarketField
            ) as T*/
    }

    val defaultSortingField = SortingField.HighestCap
    val defaultTopMarket = TopMarket.Top100
    val defaultMarketField = MarketField.PriceDiff
}

data class Menu(
    val sortingFieldSelect: Select<SortingField>,
    val topMarketSelect: Select<TopMarket>?,
    val marketFieldSelect: Select<MarketField>
)


sealed class SelectorDialogState() {
    data object Closed : SelectorDialogState()
    class Opened(val select: Select<SortingField>) : SelectorDialogState()
}
