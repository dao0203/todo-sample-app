package com.example.home

import com.example.designsystem.theme.TodosampleappTheme
import com.example.testing.robot.ScreenRobot
import com.example.testing.robot.ScreenRobotImpl
import com.example.testing.rules.RobotTestRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.takahirom.robospec.DescribedBehavior
import io.github.takahirom.robospec.describeBehaviors
import io.github.takahirom.robospec.execute
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class HomeScreenTest(
    private val behavior: DescribedBehavior<HomeScreenRobot>
) {

    @get:Rule
    @BindValue
    val robotTestRule = RobotTestRule(testInstance = this)

    @Inject
    lateinit var homeScreenRobot: HomeScreenRobot


    @Test
    fun test() {
        runTest {
            behavior.execute(homeScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): List<DescribedBehavior<HomeScreenRobot>> {
            return describeBehaviors("HomeScreen") {
                describe("when setup screen content") {
                    doIt() { setupScreenContent() }
                    itShould("show home screen content") {
                        captureScreenWithCheck {
                            // Check the screen
                        }
                    }
                }
            }
        }
    }
}

class HomeScreenRobot @Inject constructor(
    private val screenRobotImpl: ScreenRobotImpl,
) : ScreenRobot by screenRobotImpl {
    fun setupScreenContent() {
        robotTestRule.setContent {
            TodosampleappTheme {
                HomeScreen(
                    onClickAddCategory = {},
                    onClickAddTodo = {},
                    onClickTodo = {},
                )
            }
        }
        waitUntilIdle()
    }


}
