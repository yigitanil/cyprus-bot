package com.ygt.cyprusbot.model

enum class Strategies(val enterMessage:String,val exitMessage:String) {
    BOLLINGER("Last price is closing or went under of Bollinger Lower Band","Last price is closing or went over of Bollinger High Band"),
    MACD("MACD Blue line is crossing up orange line","MACD Blue line is crossing down orange line"),
    RSI("RSI is below Lower Band, over sold signal","RSI is over Upper Band, over bought signal")
}