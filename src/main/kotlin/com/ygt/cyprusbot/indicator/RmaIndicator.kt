package com.ygt.cyprusbot.indicator

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.AbstractEMAIndicator
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.num.Num

class RmaIndicator(src: Indicator<Num>, length:Int) : AbstractEMAIndicator(src,length,1.0/length) {

}