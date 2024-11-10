package com.example.testing.rules

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.TimeZone

class TimeZoneTestRule : TestWatcher() {
    private lateinit var defaultTimeZone: TimeZone
    override fun starting(description: Description?) {
        super.starting(description)
        defaultTimeZone = TimeZone.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"))
    }

    override fun finished(description: Description?) {
        super.finished(description)
        TimeZone.setDefault(defaultTimeZone)
    }
}
