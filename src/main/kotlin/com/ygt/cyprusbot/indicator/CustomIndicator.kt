package com.ygt.cyprusbot.indicator

import com.ygt.cyprusbot.model.PriceNumber
import org.ta4j.core.indicators.AbstractIndicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum
import java.math.BigDecimal

interface CustomIndicator{

     fun getValue(index: Int): Num

}

