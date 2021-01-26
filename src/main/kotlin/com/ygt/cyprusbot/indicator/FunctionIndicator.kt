package com.ygt.cyprusbot.indicator

import org.ta4j.core.Bar
import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num

class FunctionIndicator(private val src: BarSeries, private val function: java.util.function.Function<Bar, Num>) : CachedIndicator<Num>(src) {

    override fun calculate(index: Int): Num {
        return function.apply(src.getBar(index))
    }
}