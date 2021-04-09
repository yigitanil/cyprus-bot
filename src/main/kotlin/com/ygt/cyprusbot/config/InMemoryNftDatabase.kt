package com.ygt.cyprusbot.config

import com.ygt.cyprusbot.model.NftMetaData
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class InMemoryNftDatabase {
    val map = HashMap<Int, NftMetaData>()


    @PostConstruct
    fun init() {
        map.put(48, NftMetaData(48, "HandStand",
                "ec2-52-56-90-225.eu-west-2.compute.amazonaws.com:8080/nft/48",
                "https://instagram.fsaw2-1.fna.fbcdn.net/v/t51.2885-15/e35/p1080x1080/122387561_293407004967732_8453874397758641229_n.jpg?tp=1&_nc_ht=instagram.fsaw2-1.fna.fbcdn.net&_nc_cat=100&_nc_ohc=wAjpOrDax1kAX_WOHkY&edm=AABBvjUAAAAA&ccb=7-4&oh=c4fd14540a0af8bc04a44b8ff76d37fa&oe=6096F207&_nc_sid=83d603",
                "HandStand"
        ))
        map.put(49, NftMetaData(48, "NftArticle",
                "ec2-52-56-90-225.eu-west-2.compute.amazonaws.com:8080/nft/49",
                "https://instagram.fsaw2-2.fna.fbcdn.net/v/t51.2885-15/e35/s1080x1080/167649313_195909358726059_963567306718545625_n.jpg?tp=1&_nc_ht=instagram.fsaw2-2.fna.fbcdn.net&_nc_cat=103&_nc_ohc=F0hKVenWRZEAX8gfE4H&edm=AABBvjUAAAAA&ccb=7-4&oh=e2116b3baf6ccd360a485724bae36a45&oe=60948F60&_nc_sid=83d603",
                "NftArticle"
        ))
    }


}