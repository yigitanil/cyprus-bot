package com.ygt.cyprusbot.indicator

import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.WMAIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.Num

//    mavilimold = input(false, title="Show Previous Version of MavilimW?")
//    fmal=input(3,"First Moving Average length")
//    smal=input(5,"Second Moving Average length")
//    tmal=fmal+smal
//    Fmal=smal+tmal
//    Ftmal=tmal+Fmal
//    Smal=Fmal+Ftmal
//
//    M1= wma(close, fmal)
//    M2= wma(M1, smal)
//    M3= wma(M2, tmal)
//    M4= wma(M3, Fmal)
//    M5= wma(M4, Ftmal)
//    MAVW= wma(M5, Smal)
//    col1= MAVW>MAVW[1]
//    col3= MAVW<MAVW[1]
//    colorM = col1 ? color.blue : col3 ? color.red : color.yellow
class MavilimWIndicator(src: BarSeries, private val firstMal: Int, private val secondMal: Int) : CachedIndicator<Num>(src) {

    override fun calculate(index: Int): Num {
        val tMal = firstMal + secondMal
        val fMal = tMal + secondMal
        val ftMal = tMal + fMal
        val sMal = ftMal + fMal

        val closePriceIndicator = ClosePriceIndicator(barSeries)
        val m1 = WMAIndicator(closePriceIndicator, firstMal)
        val m2 = WMAIndicator(m1, secondMal)
        val m3 = WMAIndicator(m2, tMal)
        val m4 = WMAIndicator(m3, fMal)
        val m5 = WMAIndicator(m4, ftMal)
        val mavilimW = WMAIndicator(m5, sMal)

        return mavilimW.getValue(index)
    }
}