package com.example.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.home.todo.AddTodoViewModel
import com.example.model.Category
import com.example.repository.CategoryRepository
import com.example.repository.TodoRepository
import com.example.testing.turbine.assertContainsExactly
import com.example.testing.turbine.gatherAsList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddTodoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val todoRepository: TodoRepository = mockk()
    private val categoryRepository: CategoryRepository = mockk()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test changeTitle success`() {
        runTest(UnconfinedTestDispatcher()) {
            val viewModel = createViewModel()

            turbineScope {
                val uiState = viewModel.uiState.testIn(backgroundScope)
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

    @Test
    fun `test addTodo success`() = runTest {
        val dummyCategory = Category(1, "Category 1")
        val dummyCategories = listOf(dummyCategory)
        coEvery { categoryRepository.getAll() } returns flowOf(dummyCategories)
        coEvery { todoRepository.create(any()) } returns Unit
        val viewModel = createViewModel()

        viewModel.uiState.test {
            viewModel.changeCategory(dummyCategory)
            viewModel.addTodo()
            testScheduler.advanceUntilIdle()
            val expectedIsLoadings = this.gatherAsList().map { it.isLoading }
            expectedIsLoadings.assertContainsExactly(
                false,
                false,
                true,
                false
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sample test`() = runTest(UnconfinedTestDispatcher()) {
        flowOf("", "title", "title2").test {
            val flows = this.gatherAsList()


            flows.assertContainsExactly(
                "",
                "title",
                "title2"
            )
        }
    }

    private fun createViewModel(): AddTodoViewModel =
        AddTodoViewModel(
            todoRepository = this.todoRepository,
            categoryRepository = this.categoryRepository
        )
}
