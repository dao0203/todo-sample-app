package com.example.testing.rules

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import dagger.hilt.android.AndroidEntryPoint
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@AndroidEntryPoint
class HiltTestActivity : ComponentActivity()

fun RobotTestRule(
    testInstance: Any,
    bundle: Bundle? = null,
): RobotTestRule {
    // ActivityScenarioRule: Activityをテスト中に起動・終了するためのルール
    // AndroidComposeTestRule: Android固有のComposeContentTestRuleの実装で、
    // ComposeコンテンツはActivityによってホストされます。
    // Activityは通常、テスト開始前に指定されたactivityRuleによって起動されますが、
    // 後でActivityを起動するようなテストルールを渡すこともできます。
    // アクティビティはactivityRuleからactivityProviderによって取得されます。
    // activityProviderはactivityRule上のアクティビティに対するゲッターと考えることができます。
    // 後でActivityを起動するactivityRuleを使用する場合は、
    // activityProviderが呼び出されるまでにActivityが起動されるようにする必要があります。
    val composeTestRule =
        AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>(
            activityRule = ActivityScenarioRule(
                Intent(
                    ApplicationProvider.getApplicationContext(),
                    HiltTestActivity::class.java
                ),
                bundle,
            ),
            activityProvider = { rule ->
                var activity: HiltTestActivity? = null
                rule.scenario.onActivity {
                    activity = it
                }
                if (activity == null) {
                    throw IllegalStateException("Activity is null")
                }
                return@AndroidComposeTestRule activity!!
            }
        )
    return RobotTestRule(
        testInstance,
        composeTestRule as AndroidComposeTestRule<ActivityScenarioRule<*>, *>,
    )
}

class RobotTestRule(
    private val testInstance: Any,
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<*>, *>,
) : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain
            .outerRule(HiltAndroidAutoInjectRule(testInstance))
            .around(CoroutinesTestRule())
            .around(TimeZoneTestRule())
            .around(composeTestRule)
            .apply(base, description)
    }

    fun setContent(content: @Composable () -> Unit) {
        composeTestRule.setContent(content)
    }

    @OptIn(ExperimentalRoborazziApi::class)
    fun captureScreen(name: String? = null) {
        if (name != null) {
            captureScreenRoboImage("$name.png")
        } else {
            captureScreenRoboImage()
        }
    }
}
