package com.ygt.cyprusbot.strategy

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.ta4j.core.BaseBar
import org.ta4j.core.BaseBarSeries
import org.ta4j.core.num.PrecisionNum
import java.time.Duration
import java.time.ZonedDateTime

internal class LargePinStrategyTest {

    @Test
    fun buildStrategy_shouldReturnEnter() {
        //given
        val bar = BaseBar.builder()
                .lowPrice(PrecisionNum.valueOf(97))
                .openPrice(PrecisionNum.valueOf(97))
                .closePrice(PrecisionNum.valueOf(100))
                .highPrice(PrecisionNum.valueOf(100))
                .amount(PrecisionNum.valueOf(100))
                .volume(PrecisionNum.valueOf(100))
                .trades(100)
                .timePeriod(Duration.ofHours(1))
                .endTime(ZonedDateTime.now())
                .build()
        val barSeries = BaseBarSeries(listOf(bar))

        //then
        val strategy = LargePinStrategy(barSeries);

        val evaluate = strategy.evaluate(0)

        assertEquals(1, evaluate)
    }

    @Test
    fun buildStrategy_shouldReturnExit() {
        //given
        val bar = BaseBar.builder()
                .lowPrice(PrecisionNum.valueOf(100))
                .openPrice(PrecisionNum.valueOf(103))
                .closePrice(PrecisionNum.valueOf(100))
                .highPrice(PrecisionNum.valueOf(103))
                .amount(PrecisionNum.valueOf(100))
                .volume(PrecisionNum.valueOf(100))
                .trades(100)
                .timePeriod(Duration.ofHours(1))
                .endTime(ZonedDateTime.now())
                .build()
        val bar2 = BaseBar.builder()
                .lowPrice(PrecisionNum.valueOf(100))
                .openPrice(PrecisionNum.valueOf(100))
                .closePrice(PrecisionNum.valueOf(100))
                .highPrice(PrecisionNum.valueOf(101))
                .amount(PrecisionNum.valueOf(100))
                .volume(PrecisionNum.valueOf(100))
                .trades(100)
                .timePeriod(Duration.ofHours(1))
                .endTime(ZonedDateTime.now())
                .build()
        val barSeries = BaseBarSeries(listOf(bar, bar2))

        //then
        val strategy = LargePinStrategy(barSeries);

        val evaluate = strategy.evaluate(0)

        assertEquals(-1, evaluate)
    }

    @Test
    fun buildStrategy_shouldReturnNeutral() {
        //given
        val bar = BaseBar.builder()
                .lowPrice(PrecisionNum.valueOf(100))
                .openPrice(PrecisionNum.valueOf(100))
                .closePrice(PrecisionNum.valueOf(100))
                .highPrice(PrecisionNum.valueOf(102))
                .amount(PrecisionNum.valueOf(100))
                .volume(PrecisionNum.valueOf(100))
                .trades(100)
                .timePeriod(Duration.ofHours(1))
                .endTime(ZonedDateTime.now())
                .build()
        val barSeries = BaseBarSeries(listOf(bar))

        //then
        val strategy = LargePinStrategy(barSeries);

        val evaluate = strategy.evaluate(0)

        assertEquals(0, evaluate)
    }
}