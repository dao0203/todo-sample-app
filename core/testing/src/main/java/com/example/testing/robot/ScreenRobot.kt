package com.example.testing.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.example.testing.rules.RobotTestRule
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.provideRoborazziContext
import com.github.takahirom.roborazzi.roboOutputName
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.robolectric.shadows.ShadowLooper
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

interface ScreenRobot : ComposeScreenRobot, CaptureScreenRobot, WaitRobot {
    val robotTestRule: RobotTestRule
    val testDispatcher: TestDispatcher

    fun <T : ScreenRobot> run(thiz: T, block: T.() -> Unit) {
        runTest(testDispatcher) {
            thiz.block()
        }
    }
}

class ScreenRobotImpl @Inject constructor(
    override val robotTestRule: RobotTestRule,
    override val testDispatcher: TestDispatcher,
    val composeScreenRobot: ComposeScreenRobotImpl,
    val captureScreenRobot: CaptureScreenRobotImpl,
    val waitRobot: WaitRobotImpl,
) : ScreenRobot,
    ComposeScreenRobot by composeScreenRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot

interface ComposeScreenRobot {
    val composeTestRule: ComposeTestRule
}

class ComposeScreenRobotImpl @Inject constructor(
    private val robotTestRule: RobotTestRule
) : ComposeScreenRobot {
    override val composeTestRule: ComposeTestRule
        get() = robotTestRule.composeTestRule
}

interface CaptureScreenRobot {
    fun captureScreenWithCheck(check: () -> Unit)
}

class CaptureScreenRobotImpl @Inject constructor(
    private val robotTestRule: RobotTestRule,
) : CaptureScreenRobot {
    @OptIn(ExperimentalRoborazziApi::class, InternalRoborazziApi::class)
    override fun captureScreenWithCheck(check: () -> Unit) {
        val roboOutputName = roboOutputName()
        if (roboOutputName.contains("[") && roboOutputName.contains("]")) {
            val name = roboOutputName.substringAfter("[").substringBefore("]")
            val className =
                provideRoborazziContext().description?.className?.substringAfterLast(".")
            if (className == null) {
                robotTestRule.captureScreen(name)
                check()
                return
            }
            robotTestRule.captureScreen("$className[$name]")
            check()
            return
        }
        robotTestRule.captureScreen()
        check()
    }
}

interface WaitRobot {
    fun waitUntilIdle()
    fun wait5Seconds()
}

class WaitRobotImpl @Inject constructor(
    private val robotTestRule: RobotTestRule,
    private val testDispatcher: TestDispatcher,
) : WaitRobot {
    override fun waitUntilIdle() {
        robotTestRule.composeTestRule.waitForIdle()
        testDispatcher.scheduler.advanceUntilIdle()
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
    }

    override fun wait5Seconds() {
        repeat(5) {
            testDispatcher.scheduler.advanceTimeBy(1.seconds)
            robotTestRule.composeTestRule.mainClock.advanceTimeBy(1_000)
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        }
    }
}