package com.natan.cryptowatch.domain

data class Coin(val id: String, val name: String, val symbol: String, val priceUsd: Double, val change24h: Double?, val isFavorite: Boolean)
