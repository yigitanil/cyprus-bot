package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.HistogramIndicator
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.MACDIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.helpers.HighPriceIndicator
import org.ta4j.core.indicators.helpers.LowPriceIndicator
import org.ta4j.core.indicators.helpers.PriceIndicator
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule
import org.ta4j.core.trading.rules.OverIndicatorRule
import org.ta4j.core.trading.rules.UnderIndicatorRule

class MacdStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val close = ClosePriceIndicator(series)
        val macd = MACDIndicator(close)
        val signal = EMAIndicator(macd, 9)
        val histogram = HistogramIndicator(macd,signal);

        val rule: Rule = CrossedUpIndicatorRule(macd, signal)
        return BaseStrategy(rule, rule)
    }

}