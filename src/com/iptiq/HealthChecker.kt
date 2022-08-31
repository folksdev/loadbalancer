package com.iptiq

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

class HealthChecker {

    fun simulateProvidersCheck(loadBalancer: LoadBalancer) {
        val executor: ExecutorService = Executors.newFixedThreadPool(2)
        executor.execute(providersCheck(loadBalancer))
        executor.execute(toggleIsAlive(loadBalancer))
    }

    private fun providersCheck(loadBalancer: LoadBalancer): Runnable {
        return Runnable {
            while (true) {
                loadBalancer.check()

                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    private fun toggleIsAlive(loadBalancer: LoadBalancer): Runnable {
        return Runnable {
            while (true) {
                toggleIsAlive(loadBalancer.getProviderList() + loadBalancer.getDeathProviderList())
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    private fun toggleIsAlive(providerList: List<Provider>) {
        val provider = providerList[Random.nextInt(providerList.size)]
        provider.isAlive = !provider.isAlive
        println(
            Thread.currentThread().name + ": " + this.javaClass.name +
                    ": " + provider.get() + " isAlive: " + provider.isAlive
        )
    }
}