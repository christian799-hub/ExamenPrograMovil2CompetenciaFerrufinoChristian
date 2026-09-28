package edu.ucb.cashin.Crypto.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucb.cashin.Crypto.presentation.viewmodel.CryptoViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CryptoScreen(
    viewModel: CryptoViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Cryptos Precio A Tiempo Real")

        state.error?.let { message -> Text(message) }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.cryptos, key = { crypto -> crypto.id }) { crypto ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = crypto.name,
                            modifier = Modifier.padding(16.dp),
                        )
                        Text(
                            text = "${crypto.currentPrice}",
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}
