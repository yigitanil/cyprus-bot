package com.ygt.cyprusbot.indicator

import org.ta4j.core.num.Num

interface CustomIndicator{

     fun getValue(index: Int): Num

}

