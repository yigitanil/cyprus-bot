package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.DemaIndicator
import com.ygt.cyprusbot.indicator.MavilimWIndicator
import com.ygt.cyprusbot.indicator.function.BiFunctionIndicator
import com.ygt.cyprusbot.indicator.general.PreviousIndicator
import com.ygt.cyprusbot.indicator.inversefishertransform.InverseFisherTransformStoch
import com.ygt.cyprusbot.indicator.tilsont3.TilsonT3
import org.ta4j.core.*
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum
import org.ta4j.core.trading.rules.BooleanRule
import org.ta4j.core.trading.rules.OverIndicatorRule
import org.ta4j.core.trading.rules.UnderIndicatorRule

class Combo1HStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val tilsonT3: Indicator<Num> = TilsonT3(series, PrecisionNum.valueOf(0.5), 3)
        val tilsonT3Minus1 = PreviousIndicator(tilsonT3, 1)
        val tilsonT3Minus2 = PreviousIndicator(tilsonT3, 2)
        val stoch = InverseFisherTransformStoch(series, 21, 9)
        val stochMinus1 = PreviousIndicator(tilsonT3, 1)
        val stochMinus2 = PreviousIndicator(tilsonT3, 2)
        val stochMinus3 = PreviousIndicator(tilsonT3, 3)
        val stochMinus4 = PreviousIndicator(tilsonT3, 4)
        val stochMinus5 = PreviousIndicator(tilsonT3, 5)
        val mavilimW = MavilimWIndicator(series, 3, 5)

        val close = ClosePriceIndicator(series)
        val demaSlow = DemaIndicator(close, 26)
        val demaFast = DemaIndicator(demaSlow, 12)
        val ligneMACDZeroLag = BiFunctionIndicator(demaFast, demaSlow) { first, second -> first.minus(second) }
        val signal = DemaIndicator(ligneMACDZeroLag, 9)

        val macdDemaEntry: Rule = OverIndicatorRule(ligneMACDZeroLag, signal)

        val tilsonEntry = OverIndicatorRule(tilsonT3, tilsonT3Minus1).and(OverIndicatorRule(tilsonT3, tilsonT3Minus2))
        val stochEntry = OverIndicatorRule(stoch, -0.5).and(UnderIndicatorRule(stoch, 0.5)).and(
                UnderIndicatorRule(stochMinus1, -0.5)
                        .or(UnderIndicatorRule(stochMinus2, -0.5))
                        .or(UnderIndicatorRule(stochMinus3, -0.5))
                        .or(UnderIndicatorRule(stochMinus4, -0.5))
                        .or(UnderIndicatorRule(stochMinus5, -0.5))
        )
        val mavilimWEntry = OverIndicatorRule(mavilimW, series.lastBar.closePrice)

        val enterRule: Rule = tilsonEntry.and(stochEntry).and(mavilimWEntry).and(macdDemaEntry)
        val exitRule: Rule = BooleanRule(false)

        return BaseStrategy(enterRule, exitRule)
    }
}