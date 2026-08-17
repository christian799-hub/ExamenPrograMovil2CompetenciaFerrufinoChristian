package edu.ucb.cashin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform