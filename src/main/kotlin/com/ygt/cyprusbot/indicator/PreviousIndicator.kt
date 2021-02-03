package com.ygt.cyprusbot.indicator

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num

//Returns x[i-1]
class PreviousIndicator(private val src: Indicator<Num>, val count: Int) : CachedIndicator<Num>(src) {

    override fun calculate(index: Int): Num {
        if (index > 1) {
            return src.getValue(index - count)
        }
        return src.getValue(index)
    }
}