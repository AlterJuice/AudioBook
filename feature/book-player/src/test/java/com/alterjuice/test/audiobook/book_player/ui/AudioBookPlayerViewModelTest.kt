package com.alterjuice.test.audiobook.book_player.ui

import app.cash.turbine.test
import com.alterjuice.test.audiobook.book_player.FakeAudioBookPlayerController
import com.alterjuice.test.audiobook.book_player.ui.model.PlayerEvent
import com.alterjuice.test.audiobook.book_player.ui.model.PlayerSideEffects
import com.alterjuice.test.audiobook.domain.usecase.GetBookUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AudioBookPlayerViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getBookUseCase: GetBookUseCase = mockk()

    private lateinit var fakeAudioPlayer: FakeAudioBookPlayerController


    // Point of tests
    private lateinit var viewModel: AudioBookPlayerViewModel



    @Before
    fun setUp() {
        // Set testing dispatcher before each test
        Dispatchers.setMain(testDispatcher)

        // return success on each getBookUseCase invoke
        coEvery { getBookUseCase.invoke(any()) } returns Result.success(mockk(relaxed = true))

        fakeAudioPlayer = FakeAudioBookPlayerController()
        viewModel = AudioBookPlayerViewModel(
            getBookUseCase = getBookUseCase,
            audioPlayer = fakeAudioPlayer
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `onEvent Play SHOULD call play on audioPlayer`() = runTest {
        // GIVEN
        val event = PlayerEvent.Play

        // WHEN
        viewModel.onEvent(event)

        // THEN
        assertEquals(1, fakeAudioPlayer.playCallCount)
    }

    @Test
    fun `onEvent Pause SHOULD call pause on audioPlayer`() = runTest {
        // GIVEN
        val event = PlayerEvent.Pause

        // WHEN
        viewModel.onEvent(event)

        // THEN
        assertEquals(1, fakeAudioPlayer.pauseCallCount)
    }

    @Test
    fun `onEvent ShowSpeedPicker SHOULD emit UpdateSpeedPicker effect`() = runTest {
        // GIVEN
        val event = PlayerEvent.ShowSpeedPicker

        viewModel.effect.test {
            // WHEN
            viewModel.onEvent(event)

            // THEN
            val emittedEffect = awaitItem()
            assert(emittedEffect is PlayerSideEffects.UpdateSpeedPicker)
            assertEquals(true, (emittedEffect as PlayerSideEffects.UpdateSpeedPicker).isVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent HideSpeedPicker SHOULD emit UpdateSpeedPicker effect`() = runTest {
        // GIVEN
        val event = PlayerEvent.HideSpeedPicker

        viewModel.effect.test {
            // WHEN
            viewModel.onEvent(event)

            // THEN
            val emittedEffect = awaitItem()
            assert(emittedEffect is PlayerSideEffects.UpdateSpeedPicker)
            assertEquals(false, (emittedEffect as PlayerSideEffects.UpdateSpeedPicker).isVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent ShowChapterPicker SHOULD emit UpdateChapterPicker effect`() = runTest {
        // GIVEN
        val event = PlayerEvent.ShowChapterPicker

        viewModel.effect.test {
            // WHEN
            viewModel.onEvent(event)

            // THEN
            val emittedEffect = awaitItem()
            assert(emittedEffect is PlayerSideEffects.UpdateChapterPicker)
            assertEquals(true, (emittedEffect as PlayerSideEffects.UpdateChapterPicker).isVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent HideChapterPicker SHOULD emit UpdateChapterPicker effect`() = runTest {
        // GIVEN
        val event = PlayerEvent.HideChapterPicker

        viewModel.effect.test {
            // WHEN
            viewModel.onEvent(event)

            // THEN
            val emittedEffect = awaitItem()
            assert(emittedEffect is PlayerSideEffects.UpdateChapterPicker)
            assertEquals(false, (emittedEffect as PlayerSideEffects.UpdateChapterPicker).isVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent SetSpeed SHOULD call setSpeed on audioPlayer with correct value`() = runTest {
        // GIVEN
        val speed = 1.5f
        val event = PlayerEvent.SetSpeed(speed)

        // WHEN
        viewModel.onEvent(event)

        // THEN
        assertEquals(speed, fakeAudioPlayer.lastSetSpeedValue)
    }

    @Test
    fun `player state change SHOULD be reflected in ViewModel state`() = runTest {
        viewModel.state.test {
            awaitItem() // Skip initial value of viewmodel.state

            // WHEN
            // Simulate is playing
            fakeAudioPlayer.simulateIsPlaying(true)

            // THEN
            val playingState = awaitItem()
            assertEquals(true, playingState.isPlaying)

            // WHEN
            fakeAudioPlayer.simulateIsPlaying(false)

            // THEN
            val pausedState = awaitItem()
            assertEquals(false, pausedState.isPlaying)
        }
    }
}