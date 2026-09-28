package edu.ucb.cashin

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.ucb.cashin.Crypto.presentation.screen.CryptoScreen
import edu.ucb.cashin.movie.presentation.screen.CatalogScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        CryptoScreen()
    }
}
