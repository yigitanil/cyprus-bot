package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.RsiIndicator
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.trading.rules.OverIndicatorRule
import org.ta4j.core.trading.rules.UnderIndicatorRule

class RsiStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val rsi=RsiIndicator(series,14)
        val enter: Rule = UnderIndicatorRule(rsi, 30)
        val exit: Rule = OverIndicatorRule(rsi, 70)
        return BaseStrategy(enter, exit)
    }

}