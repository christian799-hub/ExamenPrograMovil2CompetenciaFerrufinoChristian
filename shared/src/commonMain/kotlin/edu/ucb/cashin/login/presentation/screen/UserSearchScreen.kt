package edu.ucb.cashin.login.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import edu.ucb.cashin.login.presentation.viewmodel.UserSearchEffect
import edu.ucb.cashin.login.presentation.viewmodel.UserSearchEvent
import edu.ucb.cashin.login.presentation.viewmodel.UserSearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserSearchScreen(viewModel: UserSearchViewModel = koinViewModel()){
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit){
        viewModel.effect.collect{ effect ->
            when(effect){
                is UserSearchEffect.ShowToast -> {

                }
            }
        }
    }

    Column {
        TextField(
            value = state.alias,
            onValueChange = { value ->
                viewModel.emitEvent(UserSearchEvent.onAliasChange(value))
            },
        )
        Button(onClick = { viewModel.emitEvent(UserSearchEvent.onSubmit) }) {
            Text("Buscar")
        }
    }
}
