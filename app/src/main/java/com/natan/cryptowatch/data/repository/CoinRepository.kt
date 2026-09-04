package com.natan.cryptowatch.data.repository

import com.natan.cryptowatch.data.local.FavoriteCoin
import com.natan.cryptowatch.data.remote.CoinGeckoApi
import com.natan.cryptowatch.domain.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinRepository(private val api: CoinGeckoApi, private val favorites: com.natan.cryptowatch.data.local.FavoriteDao) {
    private val catalog = mapOf("bitcoin" to ("Bitcoin" to "BTC"), "ethereum" to ("Ethereum" to "ETH"), "solana" to ("Solana" to "SOL"), "binancecoin" to ("BNB" to "BNB"), "cardano" to ("Cardano" to "ADA"), "ripple" to ("XRP" to "XRP"), "polkadot" to ("Polkadot" to "DOT"), "dogecoin" to ("Dogecoin" to "DOGE"))
    fun observeFavorites(): Flow<Set<String>> = favorites.observeAll().map { it.map(FavoriteCoin::id).toSet() }
    suspend fun quotes(): List<Coin> { val prices = api.getPrices(); return prices.mapNotNull { (id, quote) -> catalog[id]?.let { (name, symbol) -> Coin(id, name, symbol, quote.usd, quote.change24h, false) } }.sortedByDescending { it.priceUsd } }
    suspend fun toggleFavorite(coin: Coin, selected: Boolean) { if (selected) favorites.upsert(FavoriteCoin(coin.id, coin.name, coin.symbol)) else favorites.delete(coin.id) }
}
