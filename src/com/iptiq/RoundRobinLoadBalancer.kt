package com.iptiq

import java.util.concurrent.locks.ReentrantLock

open class RoundRobinLoadBalancer(providerList: List<Provider>) : LoadBalancer(providerList) {
    private var counter = 0
    private val lock: ReentrantLock = ReentrantLock()

    override fun get(): String {
        lock.lock()
        return try {
            val provider = getProviderList()[counter].get()
            counter++
            if (counter == getProviderList().size) {
                counter = 0
            }
            provider
        } finally {
            lock.unlock()
        }
    }
}