package com.ygt.cyprusbot.indicator.inversefishertransform

import com.ygt.cyprusbot.indicator.function.FunctionIndicator
import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.StochasticOscillatorKIndicator
import org.ta4j.core.indicators.WMAIndicator
import org.ta4j.core.num.Num
import java.util.function.Function

//        stochlength=input(5, "STOCH Length")
//        v1=0.1*(stoch(close, high, low, stochlength)-50)
//        v2=wma(v1, wmalength)
//        INVLine=(exp(2*v2)-1)/(exp(2*v2)+1)
class InverseFisherTransformStoch(series: BarSeries,
                                  private val stochLength: Int,
                                  private val smoothLength: Int) : CachedIndicator<Num>(series) {


    override fun calculate(index: Int): Num {
        val expFunction: Function<Num, Num> = Function<Num, Num> { value: Num -> numOf(Math.pow(Math.E, value.multipliedBy(numOf(2)).doubleValue())) }
        val v1Function: Function<Num, Num> = Function<Num, Num> { value: Num -> value.minus(numOf(50)).multipliedBy(numOf(0.1)) }
        val invFunction: Function<Num, Num> = Function<Num, Num> { value: Num -> value.minus(numOf(1)).dividedBy(value.plus(numOf(1))) }
        val stoch = StochasticOscillatorKIndicator(barSeries, stochLength)
        val v1 = FunctionIndicator(stoch, v1Function)
        val v2 = WMAIndicator(v1, smoothLength)
        val exp = FunctionIndicator(v2, expFunction)
        val inv = FunctionIndicator(exp, invFunction)

        return inv.getValue(index)

    }
}