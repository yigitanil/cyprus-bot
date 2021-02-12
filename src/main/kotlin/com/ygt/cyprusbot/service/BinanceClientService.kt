package com.ygt.cyprusbot.service

import com.fasterxml.jackson.core.type.TypeReference
import com.ygt.cyprusbot.model.binance.future.FutureExchangeInfo
import com.ygt.cyprusbot.model.binance.spot.SpotExchangeInfo
import org.springframework.boot.json.JacksonJsonParser
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.ta4j.core.BaseBarSeries
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BinanceClientService(private val binanceFutureWebClient: WebClient, private val binanceSpotWebClient: WebClient) {

    fun getSpotExchangeInfo(): Mono<SpotExchangeInfo> {
        return getExchangeInfoResponse(binanceSpotWebClient)
                .bodyToMono(SpotExchangeInfo::class.java)
    }

    fun getFutureExchangeInfo(): Mono<FutureExchangeInfo> {
        return getExchangeInfoResponse(binanceFutureWebClient)
                .bodyToMono(FutureExchangeInfo::class.java)
    }

    private fun getExchangeInfoResponse(webClient: WebClient): WebClient.ResponseSpec {
        return webClient
                .get()
                .uri { it.path("/exchangeInfo").build() }
                .retrieve()
    }

    fun getSpotCandlesticks(symbol: String, interval: String): Mono<BaseBarSeries> {
        return getCandlesticks(symbol, interval, binanceSpotWebClient)
    }

    fun getFutureCandlesticks(symbol: String, interval: String): Mono<BaseBarSeries> {
        return getCandlesticks(symbol, interval, binanceFutureWebClient)
    }

    private fun getCandlesticks(symbol: String, interval: String, webClient: WebClient): Mono<BaseBarSeries> {
        return webClient
                .get()
                .uri {
                    it.path("/klines")
                            .queryParam("symbol", symbol)
                            .queryParam("interval", interval)
                            .build()
                }
                .retrieve()
                .bodyToMono(String::class.java)
                .map { JacksonJsonParser().parseList(it) }
                .flatMapMany { Flux.fromIterable(it as List<List<Any>>) }
                .map { CandleStickMapper.candleStickArrayToBar(it, interval) }
                .collectList()
                .map { BaseBarSeries(symbol, it) }

    }


    private class ListTypeReference private constructor() : TypeReference<List<List<String>>>()

}