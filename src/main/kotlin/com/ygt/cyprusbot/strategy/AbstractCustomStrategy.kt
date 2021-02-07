package com.ygt.cyprusbot.strategy

import org.ta4j.core.BarSeries
import org.ta4j.core.Strategy

abstract class AbstractCustomStrategy(val series: BarSeries) : CustomStrategy {

    protected abstract fun buildStrategy(series: BarSeries): Strategy

    override fun evaluate(ndx: Int): Int {
        val strategy = buildStrategy(series)
        if (strategy.shouldEnter(ndx)) {
            return 1;
        }
        if (strategy.shouldExit(ndx)) {
            return -1;
        }
        return 0;
    }
}