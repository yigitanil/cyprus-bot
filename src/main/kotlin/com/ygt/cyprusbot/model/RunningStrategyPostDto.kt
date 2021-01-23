package com.ygt.cyprusbot.model

data class RunningStrategyPostDto(
        val symbol: String,
        val interval: String,
        val strategies: List<Strategies>
)
