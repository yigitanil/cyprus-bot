package com.ygt.cyprusbot.service

import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.model.mapByValue
import com.ygt.cyprusbot.strategy.*
import org.springframework.stereotype.Service
import org.ta4j.core.BarSeries

@Service
class StrategiesFactory {

    fun get(strategy: Strategies, barSeries: BarSeries, intervalId: String): CustomStrategy {
        val interval = CandlestickInterval.HOURLY.mapByValue(intervalId)
        if (strategy == Strategies.BOLLINGER) {
            return BollingerStrategy(barSeries)
        }
        if (strategy == Strategies.BOLLINGER_MIDDLE) {
            return BollingerMiddleStrategy(barSeries)
        }
        if (strategy == Strategies.STOCH) {
            if (interval == CandlestickInterval.FIFTEEN_MINUTES) {
                return InverseFisherTransformStochStrategy(barSeries, 34, 9)
            }
            if (interval == CandlestickInterval.HOURLY) {
                return InverseFisherTransformStochStrategy(barSeries, 21, 9)
            }
            return InverseFisherTransformStochStrategy(barSeries, 5, 9)
        }
        if (strategy == Strategies.MACD_DEMA) {
            return MacdStrategy(barSeries)
        }
        if (strategy == Strategies.LARGE_PIN) {
            return LargePinStrategy(barSeries)
        }
        if (strategy == Strategies.TILSON_T3) {
            if (interval == CandlestickInterval.FIFTEEN_MINUTES) {
                return TilsonT3Strategy(barSeries, 0.5, 3)

            }
            return TilsonT3Strategy(barSeries, 0.7, 8)
        }
        if (strategy == Strategies.COMBO_1H) {
            if (interval == CandlestickInterval.HOURLY) {
                return Combo1HStrategy(barSeries)
            }
        }
        if (strategy == Strategies.FIBO_BOLLINGER) {
            return FiboBollingerStrategy(barSeries)
        }

        throw IllegalArgumentException("Cannot get strategy: ${strategy.name}")
    }

}