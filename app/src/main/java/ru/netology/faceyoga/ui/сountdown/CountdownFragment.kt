package ru.netology.faceyoga.ui.countdown

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.faceyoga.R
import ru.netology.faceyoga.databinding.FragmentCountdownBinding
import kotlin.math.ceil

class CountdownFragment : Fragment(R.layout.fragment_countdown) {

    private var _binding: FragmentCountdownBinding? = null
    private val binding get() = _binding!!

    private var timer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCountdownBinding.bind(view)

        val programDayId = requireArguments().getLong("programDayId")

        val totalSeconds = 10
        val skipAvailableAfter = 0 // можно поставить 3, если хочешь как YouTube (появится через 3 сек)

        binding.tvHint.text = getString(R.string.countdown_hint) // "Упражнения начнутся через"
        binding.btnSkip.isEnabled = skipAvailableAfter == 0

        binding.btnSkip.setOnClickListener {
            goToPlayer(programDayId)
        }

        startCountdown(
            totalSeconds = totalSeconds,
            skipAvailableAfter = skipAvailableAfter,
            twoDigits = true,          // true: 10, 09, 08...  false: 10, 9, 8...
            programDayId = programDayId
        )
    }

    private fun startCountdown(
        totalSeconds: Int,
        skipAvailableAfter: Int,
        twoDigits: Boolean,
        programDayId: Long
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
                goToPlayer(programDayId)
            }
        }.start()
    }

    private fun goToPlayer(programDayId: Long) {
        timer?.cancel()
        timer = null

        val args = Bundle().apply { putLong("programDayId", programDayId) }
        findNavController().navigate(R.id.videoPlayerFragment, args)
    }

    override fun onDestroyView() {
        timer?.cancel()
        timer = null
        _binding = null
        super.onDestroyView()
    }
}
