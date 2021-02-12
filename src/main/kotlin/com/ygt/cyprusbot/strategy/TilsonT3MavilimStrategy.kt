package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.MavilimWIndicator
import com.ygt.cyprusbot.indicator.general.PreviousIndicator
import com.ygt.cyprusbot.indicator.tilsont3.TilsonT3
import com.ygt.cyprusbot.strategy.rule.TilsonT3EnterRule
import org.ta4j.core.*
import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum
import org.ta4j.core.trading.rules.BooleanRule
import org.ta4j.core.trading.rules.OverIndicatorRule

class TilsonT3MavilimStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val tilsonT3: Indicator<Num> = TilsonT3(series, PrecisionNum.valueOf(0.5), 3)
        val tilsonT3Minus1 = PreviousIndicator(tilsonT3, 1)
        val tilsonT3Minus2 = PreviousIndicator(tilsonT3, 2)

        val mavilimW = MavilimWIndicator(series, 3, 5)

        val tilsonEntry = TilsonT3EnterRule(tilsonT3, tilsonT3Minus1, tilsonT3Minus2)
        val mavilimWEntry = OverIndicatorRule(mavilimW, series.lastBar.closePrice)

        val enterRule: Rule = tilsonEntry.and(mavilimWEntry)
        val exitRule: Rule = BooleanRule(false)

        return BaseStrategy(enterRule, exitRule)
    }
}