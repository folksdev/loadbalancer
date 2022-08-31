package com.iptiq

open class ClientRequestHelper {
    fun simulateConcurrentClientRequest(loadBalancer: LoadBalancer, numOfCalls: Int) {
        (0..numOfCalls)
            .toList()
            .parallelStream()
            .forEach { i: Int ->
                print(
                    "Provider: " + loadBalancer.get() +
                            " --- Request from Client: " + i +
                            " --- [Thread: " + Thread.currentThread().name + "]\n"
                )
            }
    }

    fun printNextTurn(name: String?) {
        print("--- Clients starts to send requests to " + name + "Load Balancer ---\n")
    }
}