package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.general.FunctionIndicator
import org.ta4j.core.*
import org.ta4j.core.trading.rules.OverIndicatorRule
import org.ta4j.core.trading.rules.UnderIndicatorRule

class LargePinStrategy : AbstractCustomStrategy {

    var percentage: Double;

    constructor(series: BarSeries) : super(series) {
        percentage = 0.0299999;
    }

    constructor(series: BarSeries, double: Double) : this(series) {
        percentage = double;
    }

    override fun buildStrategy(series: BarSeries): Strategy {
        val difference = FunctionIndicator(series) { getDifference(it).dividedBy(it.openPrice) }


        val enter: Rule = OverIndicatorRule(difference, percentage)
        val exit: Rule = UnderIndicatorRule(difference, -percentage)
        return BaseStrategy(enter, exit)
    }

    private fun getDifference(bar: Bar) = bar.closePrice.minus(bar.openPrice)

}

