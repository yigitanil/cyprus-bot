package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.DemaIndicator
import com.ygt.cyprusbot.indicator.function.BiFunctionIndicator
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule

class MacdStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val close = ClosePriceIndicator(series)
        val demaSlow = DemaIndicator(close, 26)
        val demaFast = DemaIndicator(close, 12)
        val ligneMACDZeroLag = BiFunctionIndicator(demaFast, demaSlow) { first, second -> first.minus(second) }
        val signal = DemaIndicator(ligneMACDZeroLag, 9)

        val enter: Rule = CrossedUpIndicatorRule(ligneMACDZeroLag, signal)
        val exit: Rule = CrossedDownIndicatorRule(ligneMACDZeroLag, signal)
        return BaseStrategy(enter, exit)
    }

}