package com.ygt.cyprusbot.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.util.UriBuilder
import reactor.util.retry.Retry

@Service
class TelegramClientService( @Value("\${telegram.token}") val token: String,
                             @Value("\${telegram.chatId}") val chatId: String,
                             @Qualifier("telegramWebClient") val telegramWebClient: WebClient) {

    fun sendMessageAsync(message: String?) {
        sendMessage(message)
                .retryWhen(Retry.withThrowable { it.all { t -> t::class.java == WebClientRequestException::class.java } })
                .subscribe()
    }

    fun sendMessage(message: String?) = telegramWebClient
            .get()
            .uri { uriBuilder: UriBuilder ->
                uriBuilder.pathSegment(token, "sendMessage")
                        .queryParam("chat_id", chatId)
                        .queryParam("text", message)
                        .queryParam("parse_mode", "markdown")
                        .build()
            }.retrieve()
            .bodyToMono(String::class.java)
            .doOnNext { s: String? -> println("Message is sent to chat. Message: $message") }
}