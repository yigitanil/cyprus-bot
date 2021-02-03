package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.indicator.PreviousIndicator
import com.ygt.cyprusbot.indicator.tilsont3.TilsonT3
import com.ygt.cyprusbot.strategy.rule.TilsonT3EnterRule
import com.ygt.cyprusbot.strategy.rule.TilsonT3ExitRule
import org.ta4j.core.*
import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum

class TilsonT3StrategyBuilder {
    companion object {
        fun build(series: BarSeries, volumeFactor: Double, t3Length: Int): Strategy {
            val tilsonT3: Indicator<Num> = TilsonT3(series, PrecisionNum.valueOf(volumeFactor), t3Length)
            val tilsonT3Minus1 = PreviousIndicator(tilsonT3, 1)
            val tilsonT3Minus2 = PreviousIndicator(tilsonT3, 2)

            val exitRule: Rule = TilsonT3ExitRule(tilsonT3, tilsonT3Minus1, tilsonT3Minus2)
            val enterRule: Rule = TilsonT3EnterRule(tilsonT3, tilsonT3Minus1, tilsonT3Minus2)

            return BaseStrategy(enterRule, exitRule)
        }
    }

}