package com.ygt.cyprusbot.indicator.tilsont3

import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num

//e1=ema((high + low + 2*close)/4, length1)
class TilsonEmaValueIndiocator(val series: BarSeries) : CachedIndicator<Num>(series) {

    override fun calculate(index: Int): Num {
        val bar = series.getBar(index)
        return bar.closePrice.multipliedBy(numOf(2))
                .plus(bar.highPrice).plus(bar.lowPrice).dividedBy(numOf(4))
    }
}