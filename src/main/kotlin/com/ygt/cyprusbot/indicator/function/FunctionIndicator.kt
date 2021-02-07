package com.ygt.cyprusbot.indicator.function

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num
import java.util.function.Function

class FunctionIndicator(private val indicator: Indicator<Num>, private val function: Function<Num, Num>) : CachedIndicator<Num>(indicator) {

    override fun calculate(index: Int): Num {
        return function.apply(indicator.getValue(index))
    }
}