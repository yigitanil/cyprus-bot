package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.general.PreviousIndicator
import com.ygt.cyprusbot.indicator.inversefishertransform.InverseFisherTransformStoch
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule
import org.ta4j.core.trading.rules.OverIndicatorRule

class InverseFisherTransformStochStrategy(series: BarSeries, private val stochLength: Int,
                                          private val smoothLength: Int) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val stoch = InverseFisherTransformStoch(series, stochLength, smoothLength)
        val stochMinus1 = PreviousIndicator(stoch, 1)
        val stochMinus2 = PreviousIndicator(stoch, 2)
        val stochMinus3 = PreviousIndicator(stoch, 3)
        val enter: Rule = CrossedUpIndicatorRule(stoch, -0.5)
                .or(CrossedUpIndicatorRule(stoch, 0.5)
                        .and(OverIndicatorRule(stochMinus1, 0.5)
                                .or(OverIndicatorRule(stochMinus2, 0.5))
                                .or(OverIndicatorRule(stochMinus3, 0.5))
                        ))
        val exit: Rule = CrossedDownIndicatorRule(stoch, 0.5)
                .or(CrossedDownIndicatorRule(stoch, -0.5)
                        .and(OverIndicatorRule(stochMinus1, -0.5)
                                .or(OverIndicatorRule(stochMinus2, -0.5))
                                .or(OverIndicatorRule(stochMinus3, -0.5))
                        ))
        return BaseStrategy(enter, exit)
    }

}