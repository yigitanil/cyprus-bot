package com.ygt.cyprusbot.service

import com.ygt.cyprusbot.model.RunningStrategy
import com.ygt.cyprusbot.model.RunningStrategyDto
import java.util.*

interface RunningStrategyRepository{
    fun list() : List<RunningStrategyDto>
    fun add(runningStrategy: RunningStrategy) : String
    fun delete(id: String) : Boolean
}