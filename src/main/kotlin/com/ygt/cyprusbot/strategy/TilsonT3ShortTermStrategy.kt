package com.ygt.cyprusbot.strategy

import org.ta4j.core.BarSeries
import org.ta4j.core.Strategy

class TilsonT3ShortTermStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        return TilsonT3StrategyBuilder.build(series, 0.5, 3)
    }
}