package com.natan.cryptowatch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.natan.cryptowatch.data.repository.CoinRepository
import com.natan.cryptowatch.domain.Coin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class CoinUiState(val coins: List<Coin> = emptyList(), val loading: Boolean = false, val error: String? = null)
class CoinViewModel(private val repository: CoinRepository) : ViewModel() {
    private val quotes = MutableStateFlow<List<Coin>>(emptyList()); private val loading = MutableStateFlow(false); private val error = MutableStateFlow<String?>(null)
    val state: StateFlow<CoinUiState> = combine(quotes, loading, error, repository.observeFavorites()) { coins, isLoading, message, favs -> CoinUiState(coins.map { it.copy(isFavorite = it.id in favs) }, isLoading, message) }.let { flow -> kotlinx.coroutines.flow.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000), CoinUiState()) }
    init {
        refresh()
        viewModelScope.launch { while (true) { delay(30_000); refresh() } }
    }
    fun refresh() = viewModelScope.launch { loading.value = true; error.value = null; runCatching { repository.quotes() }.onSuccess { quotes.value = it }.onFailure { error.value = "Não foi possível atualizar as cotações." }; loading.value = false }
    fun toggle(coin: Coin) = viewModelScope.launch { repository.toggleFavorite(coin, !coin.isFavorite) }
    companion object { fun factory(repo: CoinRepository) = object : ViewModelProvider.Factory { @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = CoinViewModel(repo) as T } }
}
