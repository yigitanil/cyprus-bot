package com.ygt.cyprusbot.indicator

import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num

class LowPriceIndicator(src: BarSeries) : CachedIndicator<Num>(src) {

    override fun calculate(index: Int): Num {
        // compute relative strength
        return barSeries.getBar(index).lowPrice
    }
}