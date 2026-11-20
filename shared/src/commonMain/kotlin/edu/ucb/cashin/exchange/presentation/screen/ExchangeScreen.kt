package edu.ucb.cashin.exchange.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.koin.compose.viewmodel.koinViewModel
import edu.ucb.cashin.exchange.presentation.viewmodel.ExchangeViewModel

@Composable
fun ExchangeScreen(viewModel: ExchangeViewModel = koinViewModel()) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val message = viewModel.message.collectAsState()
        Text("Tipo de cambio", modifier = Modifier.testTag("exchange-title"))
        Text(message.value.ifEmpty { "Cargando cotización..." }, modifier = Modifier.testTag("exchange-value"))
    }

}
