package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.general.FunctionIndicator
import org.ta4j.core.*
import org.ta4j.core.trading.rules.OverIndicatorRule

class LargePinStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val enterIndicator = FunctionIndicator(series) { getDifference(it).abs().dividedBy(it.highPrice) }
        val exitIndicator = FunctionIndicator(series) { getDifference(it).dividedBy(it.lowPrice) }


        val enter: Rule = OverIndicatorRule(enterIndicator, 0.0299999)
        val exit: Rule = OverIndicatorRule(exitIndicator, 0.0299999)
        return BaseStrategy(enter, exit)
    }

    private fun getDifference(bar: Bar) = bar.highPrice.minus(bar.lowPrice)

}