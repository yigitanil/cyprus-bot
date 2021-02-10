package com.ygt.cyprusbot.config

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.BinanceApiRestClient
import com.binance.api.client.BinanceApiWebSocketClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableScheduling
class ClientConfig {
    @Bean
    @Qualifier("telegramWebClient")
    fun telegramWebClient(@Value("\${telegram.baseUrl}") baseUrl: String?): WebClient = WebClient
            .builder()
            .baseUrl(baseUrl!!)
            .build()

    @Bean
    @Qualifier("binanceWebClient")
    fun binanceWebClient(@Value("\${binance.url}") baseUrl: String?): WebClient = WebClient
            .builder()
            .baseUrl(baseUrl!!)
            .build()

    @Bean
    fun binanceApiClientFactory(): BinanceApiClientFactory = BinanceApiClientFactory.newInstance();

    @Bean
    fun binanceApiWebSocketClient(binanceApiClientFactory: BinanceApiClientFactory): BinanceApiWebSocketClient = binanceApiClientFactory.newWebSocketClient()

    @Bean
    fun binanceApiRestClient(binanceApiClientFactory: BinanceApiClientFactory): BinanceApiRestClient = binanceApiClientFactory.newRestClient()

}