package com.ygt.cyprusbot.model

enum class Strategies(val enterMessage:String,val exitMessage:String) {
    BOLLINGER("Last price is closing or went under of Bollinger Lower Band", "Last price is closing or went over of Bollinger High Band"),
    BOLLINGER_MIDDLE("Last price is went under of Bollinger Middle Band", "Last price is went over of Bollinger Middle Band"),
    LARGE_PIN("*Difference between HIGH and LOW has reached %3*", "*Difference between LOW and HIGH has reached %3*"),
    MACD("MACD Blue line is crossing up orange line", "MACD Blue line is crossing down orange line"),
    RSI("RSI is below Lower Band, over sold signal", "RSI is over Upper Band, over bought signal")


}