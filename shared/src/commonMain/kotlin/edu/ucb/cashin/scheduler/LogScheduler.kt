package edu.ucb.cashin.scheduler

expect class LogScheduler(context: Any?) {
    fun schedulePeriodicalUpload()
}
