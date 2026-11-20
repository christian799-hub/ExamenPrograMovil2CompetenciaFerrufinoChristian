package edu.ucb.cashin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import edu.ucb.cashin.Crypto.presentation.screen.CryptoScreen
import edu.ucb.cashin.exchange.presentation.screen.ExchangeScreen
import edu.ucb.cashin.movie.presentation.screen.CatalogScreen

@Composable
@Preview
fun App() {
    MaterialTheme {

        var screen by remember { mutableStateOf("exchange") }

        Column(Modifier.fillMaxSize().semantics { testTagsAsResourceId = true }) {
            Row(Modifier.fillMaxWidth()) {
                Button(onClick = { screen = "exchange" }, modifier = Modifier.weight(1f).testTag("tab-exchange")) {
                    Text("Cambio")
                }
                Button(onClick = { screen = "movies" }, modifier = Modifier.weight(1f).testTag("tab-movies")) {
                    Text("Cartelera")
                }
                Button(onClick = { screen = "crypto" }, modifier = Modifier.weight(1f).testTag("tab-crypto")) {
                    Text("Crypto")
                }
            }

            when (screen) {
                "movies" -> CatalogScreen()
                "crypto" -> CryptoScreen()
                else -> ExchangeScreen()
            }
        }
    }
}
