package com.ygt.cyprusbot.indicator.inversefishertransform

import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.statistics.MeanDeviationIndicator
import org.ta4j.core.num.Num

class CloseCCIIndicator(series: BarSeries, val timeFrame: Int) : CachedIndicator<Num>(series) {

    val FACTOR: Num = numOf(0.015)
    private val typicalPriceInd: ClosePriceIndicator
    private val smaInd: SMAIndicator
    private val meanDeviationInd: MeanDeviationIndicator

    init {
        typicalPriceInd = ClosePriceIndicator(series)
        smaInd = SMAIndicator(typicalPriceInd, timeFrame)
        meanDeviationInd = MeanDeviationIndicator(typicalPriceInd, timeFrame)
    }

    override fun calculate(index: Int): Num {
        val typicalPrice = typicalPriceInd.getValue(index)
        val typicalPriceAvg = smaInd.getValue(index)
        val meanDeviation = meanDeviationInd.getValue(index)
        return if (meanDeviation.isZero()) numOf(0)
        else typicalPrice.minus(typicalPriceAvg)
                .dividedBy(meanDeviation.multipliedBy(FACTOR))
    }
}