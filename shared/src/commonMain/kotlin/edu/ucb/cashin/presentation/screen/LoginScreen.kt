package edu.ucb.cashin.presentation.screen

import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(){

}

fun SigninPage() {

    var userSignIn by remember { mutableStateOf("") }
    var passwordSignIn by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = "Sign In")
        TextField(value = userSignIn, onValueChange = { userSignIn = it} )
        TextField(value = passwordSignIn, onValueChange = { passwordSignIn = it})
        Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
            Text(text = "Sign In" )
        }
    }
}

