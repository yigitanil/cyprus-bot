package com.ygt.cyprusbot.strategy

import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.helpers.HighPriceIndicator
import org.ta4j.core.indicators.helpers.LowPriceIndicator
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule

class BollingerMiddleStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val close = ClosePriceIndicator(series)
        val high = HighPriceIndicator(series)
        val low = LowPriceIndicator(series)
        val sma = SMAIndicator(close, 20)
        val middle = BollingerBandsMiddleIndicator(sma)
        val enter: Rule = CrossedDownIndicatorRule(low, middle)
        val exit: Rule = CrossedUpIndicatorRule(high, middle)
        return BaseStrategy(enter, exit)
    }

}