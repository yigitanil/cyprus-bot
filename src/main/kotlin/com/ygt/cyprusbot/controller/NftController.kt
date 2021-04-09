package com.ygt.cyprusbot.controller

import com.ygt.cyprusbot.config.InMemoryNftDatabase
import com.ygt.cyprusbot.model.NftMetaData
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class NftController(private val repository: InMemoryNftDatabase) {

    private val log = KotlinLogging.logger {}

    @GetMapping("/nft/{id}")
    fun delete(@PathVariable("id") id: Int): Mono<NftMetaData> {
        val nft = repository.map.get(id)
        return Mono.just(nft!!)
    }

}