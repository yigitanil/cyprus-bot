package com.ygt.cyprusbot.model

enum class Strategies(val enterMessage:String,val exitMessage:String) {
    BOLLINGER(
        "Last price is closing or went under of Bollinger Lower Band",
        "Last price is closing or went over of Bollinger High Band"
    ),
    BOLLINGER_MIDDLE(
        "Last price is went under of Bollinger Middle Band",
        "Last price is went over of Bollinger Middle Band"
    ),
    LARGE_PIN(
        "*Difference between OPEN and CLOSE has reached %3. Upwards*",
        "*Difference between OPEN and CLOSE has reached %3. Downwards *"
    ),
    MACD_DEMA("MACD_DEMA Blue line is crossing up orange line", "MACD_DEMA Blue line is crossing down orange line"),
    STOCH("*STOCH entry point*", "*STOCH exit point*"),
    TILSON_T3("Tilson T3 is turned GREEN", "Tilson T3 is turned RED"),
    TILSONT3_MAVILIM("TilsonT3-MAVILIMW entry point", "TILSONT3_MAVILIM exit point"),
    COMBO_1H("*COMBO_1H entry point*", "*COMBO_1H exit point*"),
    FIBO_BOLLINGER("*FIBO_BOLLINGER*", "*FIBO_BOLLINGER*")


}