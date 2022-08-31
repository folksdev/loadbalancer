package com.iptiq

import com.iptiq.Provider.Factory.create
import com.iptiq.Provider.Factory.get
import java.util.*

object Main {
    private const val NUM_OF_REQUESTS = 15
    private const val NUM_OF_PROVIDER = 5

    private val providerList: List<Provider> = (1..NUM_OF_PROVIDER).map { i -> create("provider-$i") }

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {

        //Random Execution
        val random: LoadBalancer = RandomLoadBalancer(providerList)
        random.printNextTurn("Random")
        random.simulateConcurrentClientRequest(random, NUM_OF_REQUESTS)

        //Round Robin Execution
        val roundRobbin: LoadBalancer = RoundRobinLoadBalancer(providerList)
        roundRobbin.printNextTurn("Round-Robin")
        roundRobbin.simulateConcurrentClientRequest(roundRobbin, NUM_OF_REQUESTS)
        val exclude = Optional.ofNullable(get("provider-1"))
        exclude.ifPresent { provider: Provider? -> roundRobbin.exclude(provider) }
        roundRobbin.include(create("provider-5"))
        println("provider-5 excluded")
        roundRobbin.simulateConcurrentClientRequest(roundRobbin, NUM_OF_REQUESTS)
        println()

        //Health Checker
        val healthChecker = HealthChecker()
        healthChecker.simulateProvidersCheck(roundRobbin)
    }
}