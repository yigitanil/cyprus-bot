package com.ygt.cyprusbot.indicator

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num

class MinIndicator(private val src: Indicator<Num>, private val length : Int) : CachedIndicator<Num>(src){

    override fun calculate(index: Int): Num {
        val number = numOf(length)
        return src.getValue(index).min(number)
    }

}