package com.ygt.cyprusbot.strategy.rule

import org.ta4j.core.Indicator
import org.ta4j.core.TradingRecord
import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum
import org.ta4j.core.trading.rules.AbstractRule

class ConvergeRule(private val first: Indicator<Num>, private val second: Indicator<Num>, private val middle: Indicator<Num>) : AbstractRule() {

    override fun isSatisfied(index: Int, tradingRecord: TradingRecord?): Boolean {
        val betweenFirstAndSecond = first.getValue(index).minus(second.getValue(index)).abs()
        val betweenMiddleAndSecond = middle.getValue(index).minus(second.getValue(index)).abs()
        return betweenFirstAndSecond.dividedBy(betweenMiddleAndSecond).isLessThan(PrecisionNum.valueOf(0.1));
    }
}