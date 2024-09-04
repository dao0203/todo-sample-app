package com.example.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.turbineScope
import com.example.home.todo.AddTodoViewModel
import com.example.repository.FakeCategoryRepository
import com.example.repository.FakeTodoRepository
import com.example.testing.turbine.assertContainsExactly
import com.example.testing.turbine.gatherAsList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddTodoViewModelTest {
    private val fakeTodoRepository = FakeTodoRepository()
    private val fakeCategoryRepository = FakeCategoryRepository()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test changeTitle success`() {
        runTest {
            val viewModel = createViewModel()

            turbineScope {
                val uiState = viewModel.uiState.testIn(this)
                viewModel.changeTitle("title")
                viewModel.changeTitle("title2")
                uiState.gatherAsList().map { it.title }.assertContainsExactly(
                    "",
                    "title",
                    "title2",
                )
            }


//            viewModel.uiState.test {
//                viewModel.changeTitle("title")
//                advanceUntilIdle()
//                viewModel.changeTitle("title2")
//                advanceUntilIdle()
//                Assert.assertEquals("", awaitItem().title)
//                Assert.assertEquals("title", awaitItem().title)
//                Assert.assertEquals("title2", awaitItem().title)
//            }
        }
    }


    private fun createViewModel(): AddTodoViewModel =
        AddTodoViewModel(fakeTodoRepository, fakeCategoryRepository)

}