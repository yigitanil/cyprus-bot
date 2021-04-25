package com.ygt.cyprusbot.strategy

import org.ta4j.core.BarSeries
import org.ta4j.core.Strategy

class FiboBollingerStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val fibonacciRetracementStrategy = FibonacciRetracementStrategy(series).buildStrategy(series)
        val bollingerMiddleStrategy = BollingerMiddleStrategy(series).buildStrategy(series)
        val bollingerStrategy = BollingerStrategy(series).buildStrategy(series)
        return fibonacciRetracementStrategy.and(bollingerMiddleStrategy.or(bollingerStrategy))
    }

}