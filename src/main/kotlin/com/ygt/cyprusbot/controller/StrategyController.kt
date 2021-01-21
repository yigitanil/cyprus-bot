package com.ygt.cyprusbot.controller

import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.model.CandleStickExtension
import com.ygt.cyprusbot.model.RunningStrategy
import com.ygt.cyprusbot.model.RunningStrategyDto
import com.ygt.cyprusbot.model.RunningStrategyPostDto
import com.ygt.cyprusbot.service.RunningStrategyRepository
import com.ygt.cyprusbot.service.SignalService
import com.ygt.cyprusbot.service.StrategyRunner
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.ZoneId
import java.time.ZonedDateTime

@RestController
class StrategyController(private val repository: RunningStrategyRepository,
                      private val signalService: SignalService) {

    @GetMapping("/strategies")
    fun list(): Flux<RunningStrategyDto> {
        return Flux.fromIterable(repository.list())
    }


    @DeleteMapping("/strategies/{id}")
    fun delete(@PathVariable("id") id:String): Mono<Boolean> {
        return Mono.just(repository.delete(id));
    }

    @PostMapping("/strategies")
    fun add(@RequestBody runningStrategyPostDtos:List<RunningStrategyPostDto>): Flux<RunningStrategyDto> {
        val runningStrategyDtos = runningStrategyPostDtos.map {
            val candlestickInterval = CandleStickExtension.mapByValue(it.interval)
            val disposable = Mono.fromCallable { signalService.run(it.symbol, candlestickInterval) }
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe()
            val runningStrategy = RunningStrategy(it.symbol, candlestickInterval, disposable, ZonedDateTime.now(ZoneId.of("UTC+3")))
            val id = repository.add(runningStrategy)
            RunningStrategyDto(id, it.symbol, candlestickInterval, runningStrategy.createdDate)
        }

        return Flux.fromIterable(runningStrategyDtos)

    }
}