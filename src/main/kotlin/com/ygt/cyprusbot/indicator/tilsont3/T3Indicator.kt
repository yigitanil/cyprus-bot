package com.ygt.cyprusbot.indicator.tilsont3

import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.num.Num

class T3Indicator(val series: BarSeries, val indicators: List<EMAIndicator>, val cValues: List<Num>) : CachedIndicator<Num>(series) {


    public override fun calculate(index: Int): Num {
        val e6 = indicators[0].getValue(index)
        val c1 = cValues[0]

        val e5 = indicators[1].getValue(index)
        val c2 = cValues[1]

        val e4 = indicators[2].getValue(index)
        val c3 = cValues[2]

        val e3 = indicators[3].getValue(index)
        val c4 = cValues[3]


        return e6.multipliedBy(c1).plus(e5.multipliedBy(c2)).plus(e4.multipliedBy(c3)).plus(e3.multipliedBy(c4))

    }


}