package com.ygt.cyprusbot.strategy

import org.ta4j.core.BarSeries
import org.ta4j.core.Indicator
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.AbstractIndicator
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.num.Num
import java.math.BigDecimal

interface CustomStrategy{
       fun buildStrategy(series: BarSeries): Strategy
}