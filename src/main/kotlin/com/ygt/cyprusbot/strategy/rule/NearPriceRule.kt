package com.ygt.cyprusbot.strategy.rule

import org.ta4j.core.TradingRecord
import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum
import org.ta4j.core.trading.rules.AbstractRule

class NearPriceRule(private val first: Num, private val second: Num) : AbstractRule() {

    override fun isSatisfied(index: Int, tradingRecord: TradingRecord?): Boolean {
        val pointNine = second.multipliedBy(PrecisionNum.valueOf(0.9))
        val onePointOne = second.multipliedBy(PrecisionNum.valueOf(1.1))
        return first.isLessThanOrEqual(onePointOne) && first.isGreaterThanOrEqual(pointNine)
    }
}