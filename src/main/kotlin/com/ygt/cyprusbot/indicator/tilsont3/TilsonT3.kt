package com.ygt.cyprusbot.indicator.tilsont3

import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.num.Num
import java.util.*
import java.util.function.Function

//        length1 = input(21, "T3 Length")
//        a1 = input(0.9, "Volume Factor")
//
//        e1=ema((high + low + 2*close)/4, length1)
//        e2=ema(e1,length1)
//        e3=ema(e2,length1)
//        e4=ema(e3,length1)
//        e5=ema(e4,length1)
//        e6=ema(e5,length1)
//        c1=-a1*a1*a1
//        c2=3*a1*a1 + 3*a1*a1*a1
//        c3= -6*a1*a1 - 3*a1 - 3*a1*a1*a1
//        c4= 1+ 3*a1 + a1*a1*a1 + 3*a1*a1
//        T3=c1*e6 + c2*e5 + c3*e4 + c4*e3
//
//        col1= T3>T3[1]
//        col3= T3<T3[1]
//        color = col1 ? green : col3 ? red : yellow
//        plot(T3, color=color, linewidth=3, title="T3")
class TilsonT3(val series: BarSeries, val volumeFactor: Num, val t3Length: Int) : CachedIndicator<Num>(series) {
    override fun calculate(index: Int): Num {

        val c1Function = Function { value: Num -> value.pow(3).multipliedBy(numOf(-1)) }
        val c2Function = Function { value: Num -> value.pow(2).multipliedBy(numOf(3)).plus(value.pow(3).multipliedBy(numOf(3))) }
        val c3Function = Function { value: Num -> value.pow(2).multipliedBy(numOf(-6)).minus(value.multipliedBy(numOf(3))).minus(value.pow(3).multipliedBy(numOf(3))) }
        val c4Function = Function { value: Num -> numOf(1).plus(value.multipliedBy(numOf(3))).plus(value.pow(3)).plus(value.pow(2).multipliedBy(numOf(3))) }


        val tilsonEmaValueIndiocator = TilsonEmaValueIndiocator(series)

        val e1 = EMAIndicator(tilsonEmaValueIndiocator, t3Length)
        val e2 = EMAIndicator(e1, t3Length)
        val e3 = EMAIndicator(e2, t3Length)
        val e4 = EMAIndicator(e3, t3Length)
        val e5 = EMAIndicator(e4, t3Length)
        val e6 = EMAIndicator(e5, t3Length)

        val c1 = c1Function.apply(volumeFactor)
        val c2 = c2Function.apply(volumeFactor)
        val c3 = c3Function.apply(volumeFactor)
        val c4 = c4Function.apply(volumeFactor)


        val t3Indicator = T3Indicator(series, Arrays.asList(e6, e5, e4, e3), Arrays.asList(c1, c2, c3, c4))
        return t3Indicator.calculate(index)
    }
}