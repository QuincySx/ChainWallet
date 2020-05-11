package com.smallraw.chain.lib.util


class TimeDiff {
    private var name: String = "timeDiff"
    private var startTime: Long = 0
    private var stageTime: Long = 0
    private var endTime: Long = 0
    fun start(name: String = "timeDiff") {
        this.name = name
        startTime = System.currentTimeMillis()
        stageTime = startTime
    }

    fun pause(pauseName: String = "timeDiff") {
        System.err.println("${this.name} is run pause $pauseName ${(System.currentTimeMillis() - stageTime) / 1000.0} s")
        stageTime = System.currentTimeMillis()
    }

    fun end() {
        endTime = System.currentTimeMillis()
        System.err.println("$name is run complete ${(endTime - startTime) / 1000.0} s")
    }
}

fun timeDiff(diff: TimeDiff.() -> Unit) {
    TimeDiff().apply(diff)
}

fun test() {
    timeDiff {
        start()
        end()
    }
}
