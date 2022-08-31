package com.iptiq

import java.util.*

class RandomLoadBalancer(providerList: List<Provider>) : LoadBalancer(providerList) {
    override fun get(): String {
        val random = Random()
        return getProviderList()[random.nextInt(getProviderList().size)].get()
    }
}