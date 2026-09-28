package edu.ucb.cashin.movie.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import edu.ucb.cashin.movie.presentation.screen.viewmodel.CatalogViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CatalogScreen(
    // Koin crea el ViewModel y entrega automáticamente el caso de uso requerido.
    viewModel: CatalogViewModel = koinViewModel(),
) {
    // Compose redibuja la pantalla cada vez que cambia el StateFlow.
    val state by viewModel.state.collectAsState()

    // Los effects se escuchan sin guardarlos permanentemente en el estado.
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CatalogEffect.NavigateToDetail -> {
                    // Aquí podrás navegar cuando agregues una pantalla de detalle.
                }
                is CatalogEffect.ShowMessage -> {
                    // El mismo mensaje ya se muestra mediante state.error.
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Cartelera")

        // El indicador aparece mientras llega la respuesta del API.

        if (state.isLoading) {
            CircularProgressIndicator()
        }
        // let ejecuta el bloque solamente cuando error no es null.
        state.error?.let { message -> Text(message) }

        // LazyColumn crea únicamente los elementos visibles de la lista.
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.movies, key = { movie -> movie.id }) { movie ->
                // Cada película se presenta como una unidad visual independiente.
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        // TMDB entrega una ruta parcial; aquí se forma la URL completa del póster.
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                            contentDescription = "Póster de ${movie.title}",
                            contentScale = ContentScale.Crop,
                        )

                        Text(
                            text = movie.title,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}
