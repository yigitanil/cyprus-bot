package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.general.FunctionIndicator
import org.ta4j.core.*
import org.ta4j.core.trading.rules.OverIndicatorRule
import org.ta4j.core.trading.rules.UnderIndicatorRule

class LargePinStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val difference = FunctionIndicator(series) { getDifference(it).dividedBy(it.openPrice) }


        val enter: Rule = OverIndicatorRule(difference, 0.0299999)
        val exit: Rule = UnderIndicatorRule(difference, -0.0299999)
        return BaseStrategy(enter, exit)
    }

    private fun getDifference(bar: Bar) = bar.closePrice.minus(bar.openPrice)

}

