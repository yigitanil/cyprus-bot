package com.ygt.cyprusbot.strategy

import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.helpers.HighPriceIndicator
import org.ta4j.core.indicators.helpers.LowPriceIndicator
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule

abstract class AbstractCustomStrategy(series: BarSeries) {

    protected val strategy: Strategy = buildStrategy(series)

    protected abstract fun buildStrategy(series: BarSeries): Strategy

    fun evaluate(ndx: Int): Int {
        if (strategy.shouldEnter(ndx)) {
            return 1;
        }
        if (strategy.shouldExit(ndx)) {
            return -1;
        }
        return 0;
    }
}