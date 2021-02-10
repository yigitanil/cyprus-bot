package com.ygt.cyprusbot.service

import com.fasterxml.jackson.core.type.TypeReference
import com.ygt.cyprusbot.model.binance.ExchangeInfo
import org.springframework.boot.json.JacksonJsonParser
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.ta4j.core.BaseBarSeries
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BinanceClientService(private val binanceWebClient: WebClient) {

    fun getExchangeInfo(): Mono<ExchangeInfo> {
        return binanceWebClient
                .get()
                .uri { it.path("/exchangeInfo").build() }
                .retrieve()
                .bodyToMono(ExchangeInfo::class.java)
    }

    fun getCandlesticks(symbol: String, interval: String): Mono<BaseBarSeries> {
        return binanceWebClient
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