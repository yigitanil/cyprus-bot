package com.ygt.cyprusbot.service

import com.binance.api.client.domain.event.CandlestickEvent
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.strategy.CustomStrategy
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.ta4j.core.BaseBar
import org.ta4j.core.BaseBarSeries
import org.ta4j.core.num.PrecisionNum
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.ZonedDateTime

internal class StrategyRunnerTest {

    lateinit var telegramClientService: TelegramClientService
    lateinit var strategiesFactory: StrategiesFactory
    lateinit var strategyRunner: StrategyRunner

    @BeforeEach
    fun setUp() {
        telegramClientService = mock(TelegramClientService::class.java)
        strategiesFactory = mock(StrategiesFactory::class.java)
        strategyRunner = StrategyRunner(telegramClientService, strategiesFactory)
    }

    @Test
    fun runShouldSendTelegramMessageWhenBollingerStrategyWithNonFinalBarGivesEnterSignal() {
        //given
        val bars = listOf(BaseBar.builder()
                .trades(10)
                .closePrice(PrecisionNum.valueOf(100))
                .openPrice(PrecisionNum.valueOf(100))
                .highPrice(PrecisionNum.valueOf(100))
                .lowPrice(PrecisionNum.valueOf(100))
                .volume(PrecisionNum.valueOf(100))
                .timePeriod(Duration.ofMinutes(15))
                .endTime(ZonedDateTime.now())
                .build())
        val barSeries = BaseBarSeries(bars)
        val symbol = "btcusdt"
        val candlestickEvent = CandlestickEvent()
        candlestickEvent.barFinal = false
        candlestickEvent.intervalId = "15m"
        val strategyType = Strategies.BOLLINGER
        val strategies = listOf(strategyType)
        val notificationMap = HashMap<String, Boolean>()
        notificationMap.put(strategyType.name, false)
        val strategy = mock(CustomStrategy::class.java)
        val prefix = "${symbol.toUpperCase()}, ${barSeries.lastBar.timePeriod}"
        val message = "$prefix, ${strategyType.enterMessage}, Last price: ${barSeries.lastBar.closePrice}"

        //when
        `when`(strategiesFactory.get(strategyType, barSeries, candlestickEvent.intervalId)).thenReturn(strategy)
        `when`(strategy.evaluate(0)).thenReturn(1)
        Mockito.`when`(telegramClientService.sendMessage(message)).thenReturn(Mono.just(""))

        //then
        strategyRunner.run(notificationMap, barSeries, symbol, candlestickEvent, strategies)

        Mockito.verify(telegramClientService, times(1)).sendMessageAsync(message)
        notificationMap.get(strategyType.name)?.let { assertTrue(it) }

    }

    @Test
    fun runShouldSendTelegramMessageWhenRsiStrategyWithFinalBarGivesExitSignal() {
        //given
        val bars = listOf(BaseBar.builder()
                .trades(10)
                .closePrice(PrecisionNum.valueOf(100))
                .openPrice(PrecisionNum.valueOf(100))
                .highPrice(PrecisionNum.valueOf(100))
                .lowPrice(PrecisionNum.valueOf(100))
                .volume(PrecisionNum.valueOf(100))
                .timePeriod(Duration.ofMinutes(15))
                .endTime(ZonedDateTime.now())
                .build())
        val barSeries = BaseBarSeries(bars)
        val symbol = "btcusdt"
        val candlestickEvent = CandlestickEvent()
        candlestickEvent.barFinal = true
        candlestickEvent.intervalId = "15m"
        val strategyType = Strategies.STOCH
        val strategies = listOf(strategyType)
        val notificationMap = HashMap<String, Boolean>()
        notificationMap.put(strategyType.name, false)
        val strategy = mock(CustomStrategy::class.java)
        val prefix = "${symbol.toUpperCase()}, ${barSeries.lastBar.timePeriod}"
        val message = "$prefix, ${strategyType.exitMessage}, Last price: ${barSeries.lastBar.closePrice}"

        //when
        `when`(strategiesFactory.get(strategyType, barSeries, candlestickEvent.intervalId)).thenReturn(strategy)
        `when`(strategy.evaluate(0)).thenReturn(-1)
        Mockito.`when`(telegramClientService.sendMessage(message)).thenReturn(Mono.just(""))

        //then
        strategyRunner.run(notificationMap, barSeries, symbol, candlestickEvent, strategies)

        Mockito.verify(telegramClientService, times(1)).sendMessageAsync(message)
        notificationMap.get(strategyType.name)?.let { assertFalse(it) }

    }

    @Test
    fun runShouldNotSendTelegramMessageWhenRsiStrategyWithNonFinalBarGivesNoSignal() {
        //given
        val bars = listOf(BaseBar.builder()
                .trades(10)
                .closePrice(PrecisionNum.valueOf(100))
                .openPrice(PrecisionNum.valueOf(100))
                .highPrice(PrecisionNum.valueOf(100))
                .lowPrice(PrecisionNum.valueOf(100))
                .volume(PrecisionNum.valueOf(100))
                .timePeriod(Duration.ofMinutes(15))
                .endTime(ZonedDateTime.now())
                .build())
        val barSeries = BaseBarSeries(bars)
        val symbol = "btcusdt"
        val candlestickEvent = CandlestickEvent()
        candlestickEvent.barFinal = true
        candlestickEvent.intervalId = "15m"
        val strategyType = Strategies.STOCH
        val strategies = listOf(strategyType)
        val notificationMap = HashMap<String, Boolean>()
        notificationMap.put(strategyType.name, false)
        val strategy = mock(CustomStrategy::class.java)

        //when
        `when`(strategiesFactory.get(strategyType, barSeries, candlestickEvent.intervalId)).thenReturn(strategy)
        `when`(strategy.evaluate(0)).thenReturn(0)

        //then
        strategyRunner.run(notificationMap, barSeries, symbol, candlestickEvent, strategies)

        verify(telegramClientService, never()).sendMessage(any())
        notificationMap.get(strategyType.name)?.let { assertFalse(it) }

    }

}