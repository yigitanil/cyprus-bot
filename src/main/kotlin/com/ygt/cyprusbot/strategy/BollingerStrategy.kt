package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.strategy.rule.ConvergeRule
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.helpers.HighPriceIndicator
import org.ta4j.core.indicators.helpers.LowPriceIndicator
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule
import org.ta4j.core.trading.rules.OverIndicatorRule
import org.ta4j.core.trading.rules.UnderIndicatorRule

class BollingerStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val close = ClosePriceIndicator(series)
        val high = HighPriceIndicator(series)
        val low = LowPriceIndicator(series)
        val sma = SMAIndicator(close, 20)
        val middle = BollingerBandsMiddleIndicator(sma)
        val std = StandardDeviationIndicator(close, 20)
        val upper = BollingerBandsUpperIndicator(middle, std)
        val lower = BollingerBandsLowerIndicator(middle, std)

        val enter: Rule = UnderIndicatorRule(low, lower)
        val enter2: Rule = ConvergeRule(low, lower,middle)
        val exit: Rule = OverIndicatorRule(high, upper)
        val exit2: Rule = ConvergeRule(high, upper,middle)
        return BaseStrategy(enter.or(enter2), exit.or(exit2))
    }

}