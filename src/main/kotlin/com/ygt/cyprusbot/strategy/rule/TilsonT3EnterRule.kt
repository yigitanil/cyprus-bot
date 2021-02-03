package com.ygt.cyprusbot.strategy.rule

import org.ta4j.core.Indicator
import org.ta4j.core.TradingRecord
import org.ta4j.core.num.Num
import org.ta4j.core.trading.rules.AbstractRule

class TilsonT3EnterRule(private val tilsonT3: Indicator<Num>, private val tilsonT3Minus1: Indicator<Num>, private val tilsonT3Minus2: Indicator<Num>) : AbstractRule() {

    override fun isSatisfied(index: Int, tradingRecord: TradingRecord?): Boolean {
        return tilsonT3.getValue(index).isGreaterThan(tilsonT3Minus1.getValue(index)) &&
                tilsonT3Minus2.getValue(index).isGreaterThan(tilsonT3Minus1.getValue(index))
    }
}