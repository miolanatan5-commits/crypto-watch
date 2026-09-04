package com.natan.cryptowatch.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class CoinQuote(val usd: Double, @com.squareup.moshi.Json(name = "usd_24h_change") val change24h: Double? = null)

typealias CoinQuotes = Map<String, CoinQuote>

interface CoinGeckoApi {
    @GET("api/v3/simple/price") suspend fun getPrices(
        @Query("ids") ids: String = "bitcoin,ethereum,solana,binancecoin,cardano,ripple,polkadot,dogecoin",
        @Query("vs_currencies") currency: String = "usd",
        @Query("include_24hr_change") includeChange: Boolean = true
    ): CoinQuotes
}
