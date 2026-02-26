package ru.netology.faceyoga.ui.player.countdown

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.faceyoga.R
import ru.netology.faceyoga.analytics.AnalyticsEvents
import ru.netology.faceyoga.analytics.AnalyticsLogger
import ru.netology.faceyoga.databinding.FragmentCountdownBinding
import javax.inject.Inject
import kotlin.math.ceil

@AndroidEntryPoint
class CountdownFragment : Fragment(R.layout.fragment_countdown) {

    private var _binding: FragmentCountdownBinding? = null
    private val binding get() = _binding!!

    private var timer: CountDownTimer? = null

    @Inject lateinit var analytics: AnalyticsLogger

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCountdownBinding.bind(view)

        val programDayId = requireArguments().getLong("programDayId")
        val dayNumber = requireArguments().getInt("dayNumber", 1)

        // ✅ Crashlytics: day_number + breadcrumb
        FirebaseCrashlytics.getInstance().setCustomKey("day_number", dayNumber)
        FirebaseCrashlytics.getInstance().log("workout_start day=$dayNumber (countdown_enter)")

        // ✅ Analytics: старт тренировки (вход в countdown)
        analytics.log(
            AnalyticsEvents.WORKOUT_START,
            Bundle().apply { putInt("day_number", dayNumber) }
        )

        val totalSeconds = 10
        val skipAvailableAfter = 0 // можно поставить 3, если хочешь как YouTube (появится через 3 сек)

        binding.tvHint.text = getString(R.string.countdown_hint) // "Упражнения начнутся через"
        binding.btnSkip.isEnabled = skipAvailableAfter == 0

        binding.btnSkip.setOnClickListener {
            FirebaseCrashlytics.getInstance().log("countdown_skip day=$dayNumber")
            goToPlayer(programDayId, dayNumber)
        }

        startCountdown(
            totalSeconds = totalSeconds,
            skipAvailableAfter = skipAvailableAfter,
            twoDigits = true,          // true: 10, 09, 08...  false: 10, 9, 8...
            programDayId = programDayId,
            dayNumber = dayNumber
        )
    }

    private fun startCountdown(
        totalSeconds: Int,
        skipAvailableAfter: Int,
        twoDigits: Boolean,
        programDayId: Long,
        dayNumber: Int
    ) {
        timer?.cancel()

        fun formatSec(sec: Int): String {
            return if (twoDigits) "%02d".format(sec) else sec.toString()
        }

        // сразу показываем 10 (а не 09)
        binding.tvCounter.text = formatSec(totalSeconds)

        timer = object : CountDownTimer((totalSeconds * 1000L) + 50L, 100L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = ceil(millisUntilFinished / 1000.0).toInt().coerceAtLeast(0)

                // обновляем цифры
                binding.tvCounter.text = formatSec(secondsLeft)

                // логика "пропустить через N секунд" (если надо)
                if (skipAvailableAfter > 0) {
                    val passed = totalSeconds - secondsLeft
                    if (passed >= skipAvailableAfter) {
                        binding.btnSkip.isEnabled = true
                    }
                }
            }

            override fun onFinish() {
                binding.tvCounter.text = if (twoDigits) "00" else "0"
                FirebaseCrashlytics.getInstance().log("countdown_finish_to_player day=$dayNumber")
                goToPlayer(programDayId, dayNumber)
            }
        }.start()
    }

    private fun goToPlayer(programDayId: Long, dayNumber: Int) {
        timer?.cancel()
        timer = null

        val args = Bundle().apply {
            putLong("programDayId", programDayId)
            putInt("dayNumber", dayNumber)
        }

        // лучше навигировать по action, чтобы не было сюрпризов со стеком
        findNavController().navigate(
            R.id.action_countdownFragment_to_videoPlayerFragment,
            args
        )
    }

    override fun onDestroyView() {
        timer?.cancel()
        timer = null
        _binding = null
        super.onDestroyView()
    }
}