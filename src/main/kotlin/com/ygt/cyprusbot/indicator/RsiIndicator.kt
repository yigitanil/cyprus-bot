package com.ygt.cyprusbot.indicator

import org.ta4j.core.BarSeries
import org.ta4j.core.Indicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.Num

class RsiIndicator(src: BarSeries,private val length:Int) : CachedIndicator<Num>(src) {
    private val up:Indicator<Num>
    private val down:Indicator<Num>

    init {
        val close = ClosePriceIndicator(barSeries)
        up=RmaIndicator(MaxIndicator(ChangeIndicator(close), 0), length)
        down=RmaIndicator(NegativeIndicator(MinIndicator(ChangeIndicator(close), 0)), length)
    }

    override fun calculate(index: Int): Num {
        // compute relative strength
        val averageGain: Num = up.getValue(index)
        val averageLoss: Num = down.getValue(index)
        val hundred = numOf(100)
        if (averageLoss.isZero) {
            return if (averageGain.isZero) {
                numOf(0)
            } else {
                hundred
            }
        }
        val relativeStrength = averageGain.dividedBy(averageLoss)
        // compute relative strength index
        return hundred.minus(hundred.dividedBy(numOf(1).plus(relativeStrength)))
    }
}