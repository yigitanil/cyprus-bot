package com.ygt.cyprusbot.indicator.function

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num
import java.util.function.BiFunction

class BiFunctionIndicator(private val indicator1: Indicator<Num>,
                          private val indicator2: Indicator<Num>,
                          private val function: BiFunction<Num, Num, Num>) : CachedIndicator<Num>(indicator1.barSeries) {

    override fun calculate(index: Int): Num {
        return function.apply(indicator1.getValue(index), indicator2.getValue(index))
    }
}