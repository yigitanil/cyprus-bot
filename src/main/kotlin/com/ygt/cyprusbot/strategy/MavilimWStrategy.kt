package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.MavilimWIndicator
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule

class MavilimWStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {

        val mavilimW = MavilimWIndicator(series, 3, 5)

        val close = ClosePriceIndicator(series)

        val enterRule: Rule = CrossedUpIndicatorRule(close, mavilimW)
        val exitRule: Rule = CrossedDownIndicatorRule(close, mavilimW)

        return BaseStrategy(enterRule, exitRule)
    }
}