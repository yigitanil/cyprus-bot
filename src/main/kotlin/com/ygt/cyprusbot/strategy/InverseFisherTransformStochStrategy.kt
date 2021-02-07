package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.inversefishertransform.InverseFisherTransformStoch
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule

class InverseFisherTransformStochStrategy(series: BarSeries, private val stochLength: Int,
                                          private val smoothLength: Int) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val stoch = InverseFisherTransformStoch(series, stochLength, smoothLength)
        val enter: Rule = CrossedUpIndicatorRule(stoch, -0.5)
        val exit: Rule = CrossedDownIndicatorRule(stoch, 0.5)
        return BaseStrategy(enter, exit)
    }

}