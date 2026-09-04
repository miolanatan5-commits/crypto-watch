# CryptoWatch

App Android nativo em Kotlin + Jetpack Compose para acompanhar cotações de criptomoedas, favoritar moedas e manter os favoritos localmente.

## Stack
- Kotlin, Jetpack Compose e Material 3
- MVVM com StateFlow e coroutines
- Retrofit + Moshi consumindo a API pública CoinGecko
- Room Database para favoritos persistentes

## Executar
Abra a pasta no Android Studio Koala ou mais recente, sincronize o Gradle e execute em um dispositivo/emulador com Android 8 (API 26) ou superior. A aplicação precisa de internet.

## API
As cotações vêm de `https://api.coingecko.com/api/v3/simple/price`, sem chave no plano público. O botão de atualizar busca os dados novamente.
