package com.ygt.cyprusbot.indicator.general

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num

//Difference between current value and previous, x - x[y].
class ChangeIndicator(private val src: Indicator<Num>) : CachedIndicator<Num>(src){

    override fun calculate(index: Int): Num {
        if (index>1){
           return src.getValue(index).minus(src.getValue(index-1))
        }
        return src.getValue(index)
    }
}