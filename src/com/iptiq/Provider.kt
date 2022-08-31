package com.iptiq

class Provider private constructor(private val name: String, var isAlive: Boolean) {
    companion object Factory {
        private var providerSet: MutableSet<Provider> = mutableSetOf()

        fun create(name: String): Provider {
            return providerSet.stream()
                .filter { i -> i.name == name }
                .findFirst()
                .orElseGet {
                    val p = Provider(name, true)
                    providerSet.add(p)
                    p
                }
        }

        fun get(identifier: String): Provider? {
            return providerSet.find { i -> i.name == identifier }
        }
    }

    fun get(): String {
        return name
    }

}
