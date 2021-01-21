package com.ygt.cyprusbot.indicator

import com.ygt.cyprusbot.model.PriceNumber
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.num.Num

fun CachedIndicator<Num>.numberOf(number: Number): PriceNumber {
    return PriceNumber.valueOf(number)
}