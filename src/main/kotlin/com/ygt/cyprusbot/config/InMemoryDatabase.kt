package com.ygt.cyprusbot.config

import com.ygt.cyprusbot.model.RunningStrategy
import com.ygt.cyprusbot.model.RunningStrategyDto
import com.ygt.cyprusbot.service.RunningStrategyRepository
import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.collections.HashMap

@Configuration
class InMemoryDatabase: RunningStrategyRepository {
    private val log = KotlinLogging.logger {}
    private val runningStrategyMap=HashMap<String, RunningStrategy>()


    override fun list() : List<RunningStrategyDto> {
       return runningStrategyMap.entries
                .map { RunningStrategyDto(it.key,it.value.symbol,it.value.interval,it.value.createdDate) }
                .sortedBy { it.createdDate }
    }


    override fun add(runningStrategy: RunningStrategy) : String{
        val id=UUID.randomUUID().toString()
        runningStrategyMap.put(id,runningStrategy);
        return id;
    }


    override fun delete(id: String) : Boolean{
        if (runningStrategyMap.containsKey(id)) {
            runningStrategyMap.get(id)?.disposable?.dispose()
            val runningStrategy = runningStrategyMap.remove(id)
            log.info { "${runningStrategy?.symbol?.toUpperCase()} with ${runningStrategy?.interval} is stopped" }
            return true;
        }
        return false;
    }


}