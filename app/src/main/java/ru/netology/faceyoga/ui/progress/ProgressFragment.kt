package ru.netology.faceyoga.ui.progress

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R

@AndroidEntryPoint
class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private val vm: ProgressViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Insets: опускаем контент ниже статус-бара + чуть воздуха
        val root = view.findViewById<View>(R.id.progressRoot)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val statusBarsTop = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val extraTop = (12 * v.resources.displayMetrics.density).toInt()
            v.updatePadding(top = statusBarsTop + extraTop)
            insets
        }

        val tvDone = view.findViewById<TextView>(R.id.tvDone)
        val tvPercent = view.findViewById<TextView>(R.id.tvPercent)
        val tvStreak = view.findViewById<TextView>(R.id.tvStreak)
        val progressBar = view.findViewById<LinearProgressIndicator>(R.id.progressBar)
        val grid = view.findViewById<RecyclerView>(R.id.daysGrid)

        val adapter = ProgressDaysAdapter { dayUi ->
            when (dayUi.state) {
                DayState.LOCKED -> {
                    Toast.makeText(
                        requireContext(),
                        "День ${dayUi.day} пока закрыт",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                DayState.AVAILABLE, DayState.DONE -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val programDayId = vm.resolveProgramDayId(dayUi.day)
                        if (programDayId == 0L) {
                            Toast.makeText(
                                requireContext(),
                                "Не удалось открыть день",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }

                        findNavController().navigate(
                            R.id.dayExercisesFragment,
                            bundleOf(
                                "programDayId" to programDayId,
                                "dayNumber" to dayUi.day
                            )
                        )
                    }
                }
            }
        }

        grid.layoutManager = GridLayoutManager(requireContext(), 5)
        grid.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { ui ->
                    tvDone.text = getString(R.string.progress_done_format, ui.doneDays, ui.totalDays)
                    tvPercent.text = getString(R.string.progress_percent_format, ui.percent)
                    tvStreak.text = getString(R.string.progress_streak_format, ui.streak)

                    progressBar.progress = ui.percent
                    adapter.submitList(ui.days)
                }
            }
        }

        // ✅ важно вызвать (внутри VM стоит защита от повторного запуска)
        vm.start()
    }
}
