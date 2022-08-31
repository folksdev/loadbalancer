package com.iptiq

import java.util.concurrent.CopyOnWriteArrayList

abstract class LoadBalancer(providerList: List<Provider>) : ClientRequestHelper() {
    private val providerList: CopyOnWriteArrayList<Provider>
    private val deathProviderList: CopyOnWriteArrayList<Provider> = CopyOnWriteArrayList()

    init {
        if (providerList.size > MAX_NODE)
            this.providerList = CopyOnWriteArrayList(providerList.subList(0, MAX_NODE))
        else
            this.providerList = CopyOnWriteArrayList(providerList)
    }


    abstract fun get(): String?

    fun exclude(provider: Provider?) {
        provider?.let {
            this.providerList.remove(it)
            this.deathProviderList.add(it)
        }?: throw RuntimeException("The provider node can not be found")
    }

    fun include(provider: Provider) {
        if (providerList.size == MAX_NODE) {
            println("The maximum number of nodes is exceeded")
        }
        this.providerList.add(provider)
        this.deathProviderList.remove(provider)
    }

    @Synchronized
    fun check() {
        println(
            Thread.currentThread().name + ": " + this.javaClass.name +
                    ": Health check started"
        )
        this.providerList.forEach { provider: Provider ->
            if (!provider.isAlive) {
                println(
                    Thread.currentThread().name + ": " + this.javaClass.name +
                            ": Provider " + provider.get() + " is not alive. It is excluded"
                )
                exclude(provider)
            }
        }

        this.deathProviderList.forEach { provider: Provider ->
            if (provider.isAlive) {
                println(
                    Thread.currentThread().name + ": " + this.javaClass.name +
                            ": Provider " + provider.get() + " is alive. It is included"
                )
                include(provider)
            }
        }

        println(
            Thread.currentThread().name + ": " + this.javaClass.name +
                    ": Alive providers " + providerList.stream().map { obj: Provider -> obj.get() }.toList()
        )
    }

    fun getProviderList(): List<Provider> {
        return providerList
    }

    fun getDeathProviderList(): List<Provider> {
        return deathProviderList
    }

    companion object {
        private const val MAX_NODE = 10
    }
}