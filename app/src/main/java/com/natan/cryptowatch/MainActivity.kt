package com.natan.cryptowatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.natan.cryptowatch.di.AppContainer
import com.natan.cryptowatch.domain.Coin
import com.natan.cryptowatch.ui.CoinViewModel
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); val repo = AppContainer.repository(applicationContext); setContent { val vm: CoinViewModel = viewModel(factory = CoinViewModel.factory(repo)); CryptoWatchScreen(vm) } }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun CryptoWatchScreen(vm: CoinViewModel) { val state by vm.state.collectAsStateWithLifecycle(); Scaffold(topBar = { TopAppBar(title = { Text("CryptoWatch", fontWeight = FontWeight.Bold) }, actions = { IconButton(onClick = vm::refresh) { Icon(Icons.Default.Refresh, "Atualizar") } }) }) { padding -> Column(Modifier.padding(padding).padding(horizontal = 16.dp)) { Text("Cotações em tempo real", style = MaterialTheme.typography.titleMedium); Text("Atualizado pela CoinGecko", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(12.dp)); if (state.loading && state.coins.isEmpty()) LinearProgressIndicator(Modifier.fillMaxWidth()); state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 12.dp)) }; LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(state.coins, key = { it.id }) { CoinRow(it) { vm.toggle(it) } } } } } }
@Composable private fun CoinRow(coin: Coin, onFavorite: () -> Unit) { Card(Modifier.fillMaxWidth()) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(coin.name, fontWeight = FontWeight.SemiBold); Text(coin.symbol, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }; Column(horizontalAlignment = Alignment.End) { Text(NumberFormat.getCurrencyInstance(Locale.US).format(coin.priceUsd), fontWeight = FontWeight.Bold); coin.change24h?.let { Text(String.format(Locale.US, "%+.2f%%", it), color = if (it >= 0) Color(0xFF16803C) else MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) } }; IconButton(onClick = onFavorite) { Icon(if (coin.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder, "Favoritar", tint = if (coin.isFavorite) Color(0xFFFFB300) else LocalContentColor.current) } } } }
