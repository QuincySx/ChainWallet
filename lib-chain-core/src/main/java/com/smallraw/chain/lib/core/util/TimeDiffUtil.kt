package com.smallraw.chain.lib.core.util

class TimeDiff {
    private var name: String = "timeDiff"
    private var startTime: Long = 0
    private var stageTime: Long = 0
    private var endTime: Long = 0
    fun start(name: String = "timeDiff") {
        this.name = name
        startTime = System.nanoTime()
        stageTime = startTime
    }

    fun pause(pauseName: String = "timeDiff") {
        System.err.println("${this.name} pause is run time $pauseName ${(System.nanoTime() - stageTime) / 1000000.0} ms")
        stageTime = System.nanoTime()
    }

    fun end() {
        endTime = System.nanoTime()
        System.err.println("$name complete is run time ${(endTime - startTime) / 1000000.0} ms")
    }
}

fun timeDiff(diff: TimeDiff.() -> Unit): TimeDiff {
    return TimeDiff().apply(diff)
}

private fun test() {
    timeDiff {
        // To do things
        start()
        // Code that needs to be timed
        end()
        // To do things
    }
}
