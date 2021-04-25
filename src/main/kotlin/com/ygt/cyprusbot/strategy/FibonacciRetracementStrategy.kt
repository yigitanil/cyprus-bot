package com.ygt.cyprusbot.strategy

import com.ygt.cyprusbot.strategy.rule.NearPriceRule
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum
import org.ta4j.core.trading.rules.BooleanRule
import java.util.*

class FibonacciRetracementStrategy(series: BarSeries) : AbstractCustomStrategy(series) {

    override fun buildStrategy(series: BarSeries): Strategy {
        val closePrice = series.lastBar.closePrice
        val close = ClosePriceIndicator(series)
        val sma = SMAIndicator(close, 9)
        var maxClose = closePrice;
        var minClose = closePrice;
        val levels = TreeMap<Num, Num>()

        if (closePrice > sma.getValue(series.barCount - 1)) {
            for (i in series.barCount - 1 downTo 0) {
                if (sma.getValue(i) > close.getValue(i)) {
                    minClose = series.getBar(i).lowPrice;
                    for (j in i downTo i - 3) {
                        minClose =
                            PrecisionNum.valueOf(
                                Math.min(
                                    minClose.doubleValue(),
                                    series.getBar(j).lowPrice.doubleValue()
                                )
                            )
                    }
                    break
                }
            }
            for (i in series.barCount - 1 downTo series.barCount - 4) {
                maxClose =
                    PrecisionNum.valueOf(Math.max(maxClose.doubleValue(), series.getBar(i).highPrice.doubleValue()))
            }

            val difference = maxClose.minus(minClose)
            levels.put(PrecisionNum.valueOf(0), maxClose)
            levels.put(
                PrecisionNum.valueOf(0.236),
                maxClose.minus(difference.multipliedBy(PrecisionNum.valueOf(0.236)))
            )
            levels.put(
                PrecisionNum.valueOf(0.382),
                maxClose.minus(difference.multipliedBy(PrecisionNum.valueOf(0.382)))
            )
            levels.put(PrecisionNum.valueOf(0.5), maxClose.minus(difference.multipliedBy(PrecisionNum.valueOf(0.5))))
            levels.put(
                PrecisionNum.valueOf(0.618),
                maxClose.minus(difference.multipliedBy(PrecisionNum.valueOf(0.618)))
            )
            levels.put(
                PrecisionNum.valueOf(0.786),
                maxClose.minus(difference.multipliedBy(PrecisionNum.valueOf(0.786)))
            )


        }
        if (closePrice < sma.getValue(series.barCount - 1)) {
            for (i in series.barCount - 1 downTo 0) {
                if (sma.getValue(i) < close.getValue(i)) {
                    maxClose = series.getBar(i).highPrice;
                    for (j in i downTo i - 3) {
                        maxClose =
                            PrecisionNum.valueOf(
                                Math.max(
                                    maxClose.doubleValue(),
                                    series.getBar(j).highPrice.doubleValue()
                                )
                            )
                    }
                    break
                }
            }
            for (i in series.barCount - 1 downTo series.barCount - 4) {
                minClose =
                    PrecisionNum.valueOf(Math.min(minClose.doubleValue(), series.getBar(i).lowPrice.doubleValue()))
            }

            val difference = maxClose.minus(minClose)
            levels.put(PrecisionNum.valueOf(0), minClose)
            levels.put(PrecisionNum.valueOf(0.236), minClose.plus(difference.multipliedBy(PrecisionNum.valueOf(0.236))))
            levels.put(PrecisionNum.valueOf(0.382), minClose.plus(difference.multipliedBy(PrecisionNum.valueOf(0.382))))
            levels.put(PrecisionNum.valueOf(0.5), minClose.plus(difference.multipliedBy(PrecisionNum.valueOf(0.5))))
            levels.put(PrecisionNum.valueOf(0.618), minClose.plus(difference.multipliedBy(PrecisionNum.valueOf(0.618))))
            levels.put(PrecisionNum.valueOf(0.786), minClose.plus(difference.multipliedBy(PrecisionNum.valueOf(0.786))))
        }

        var baseRule: Rule = BooleanRule(true)
        for (level in levels) {
            baseRule = baseRule.or(NearPriceRule(closePrice, level.value))
        }

        return BaseStrategy(baseRule, baseRule)
    }

}