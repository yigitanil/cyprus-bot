package com.ygt.cyprusbot.indicator

import org.ta4j.core.Indicator
import org.ta4j.core.num.Num

class HistogramIndicator(private val macd: Indicator<Num>,private val signal: Indicator<Num>) : CustomIndicator{

    override fun getValue(index: Int): Num {
        return macd.getValue(index).minus(signal.getValue(index))
    }
}