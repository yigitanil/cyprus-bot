package com.ygt.cyprusbot.config

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.BinanceApiWebSocketClient

class BinanceWebSocketClient {
    companion object {
        private const val THRESHOLD = 5
        private var count = 0
        private var webSocketClients = ArrayDeque<BinanceApiWebSocketClient>()

        init {
            webSocketClients.add(BinanceApiClientFactory.newInstance().newWebSocketClient())
        }

        fun get(): BinanceApiWebSocketClient {
            if (count == THRESHOLD) {
                webSocketClients.add(BinanceApiClientFactory.newInstance().newWebSocketClient())
                count = 0
            }
            count++
            return webSocketClients.last();
        }

    }

}