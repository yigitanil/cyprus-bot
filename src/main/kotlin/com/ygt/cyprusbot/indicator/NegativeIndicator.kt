package com.ygt.cyprusbot.indicator

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum

class NegativeIndicator(private val src: Indicator<Num>) : CachedIndicator<Num>(src){

    override fun calculate(index: Int): Num {
        return src.getValue(index).multipliedBy(numberOf(-1))
    }
}