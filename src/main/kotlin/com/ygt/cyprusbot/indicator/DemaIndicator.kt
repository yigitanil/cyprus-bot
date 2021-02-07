package com.ygt.cyprusbot.indicator

import com.ygt.cyprusbot.indicator.function.BiFunctionIndicator
import org.ta4j.core.Indicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.num.Num

//study("MACD DEMA",shorttitle='MACD DEMA')
////by ToFFF
//sma = input(12,title='DEMA Courte')
//lma = input(26,title='DEMA Longue')
//tsp = input(9,title='Signal')
//dolignes = input(true,title="Lignes")
//
//MMEslowa = ema(close,lma)
//MMEslowb = ema(MMEslowa,lma)
//DEMAslow = ((2 * MMEslowa) - MMEslowb )
//
//MMEfasta = ema(close,sma)
//MMEfastb = ema(MMEfasta,sma)
//DEMAfast = ((2 * MMEfasta) - MMEfastb)
//
//LigneMACDZeroLag = (DEMAfast - DEMAslow)
//
//MMEsignala = ema(LigneMACDZeroLag, tsp)
//MMEsignalb = ema(MMEsignala, tsp)
//Lignesignal = ((2 * MMEsignala) - MMEsignalb )
//
//MACDZeroLag = (LigneMACDZeroLag - Lignesignal)
//
//swap1 = MACDZeroLag>0?green:red
//
//p1 = plot(dolignes?LigneMACDZeroLag:na,color=blue,title='LigneMACD')
//p2 = plot(dolignes?Lignesignal:na,color=red,title='Signal')

class DemaIndicator(private val indicator: Indicator<Num>, private val length: Int
) : CachedIndicator<Num>(indicator) {
    override fun calculate(index: Int): Num {
        val mmeSlowA = EMAIndicator(indicator, length)
        val mmeSlowB = EMAIndicator(mmeSlowA, length)
        val demaSlow = BiFunctionIndicator(mmeSlowA, mmeSlowB) { first, second -> first.multipliedBy(numOf(2)).minus(second) }
        return demaSlow.getValue(index)

    }
}