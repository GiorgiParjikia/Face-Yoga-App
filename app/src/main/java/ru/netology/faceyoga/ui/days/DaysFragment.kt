package ru.netology.faceyoga.ui.days

import android.os.Bundle
import android.view.View
import androidx.appcompat.R
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.analytics.AnalyticsEvents
import ru.netology.faceyoga.analytics.AnalyticsLogger
import ru.netology.faceyoga.databinding.FragmentDaysBinding
import ru.netology.faceyoga.ui.common.FySnack
import ru.netology.faceyoga.ui.common.StateKeys
import javax.inject.Inject

@AndroidEntryPoint
class DaysFragment : Fragment(ru.netology.faceyoga.R.layout.fragment_days) {

    private var _binding: FragmentDaysBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DaysViewModel by viewModels()

    @Inject lateinit var analytics: AnalyticsLogger

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDaysBinding.bind(view)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        setupToolbarMenu()

        val adapter = DaysAdapter(
            onClick = { day ->
                // ✅ Crashlytics: day_number + breadcrumb
                FirebaseCrashlytics.getInstance().setCustomKey("day_number", day.dayNumber)
                FirebaseCrashlytics.getInstance().log("open_day=${day.dayNumber}")

                // ✅ Analytics: открытие дня
                analytics.log(
                    AnalyticsEvents.DAY_OPEN,
                    Bundle().apply { putInt("day_number", day.dayNumber) }
                )

                val args = Bundle().apply {
                    putLong(StateKeys.PROGRAM_DAY_ID, day.programDayId)
                    putInt(StateKeys.DAY_NUMBER, day.dayNumber)
                }
                findNavController().navigate(
                    ru.netology.faceyoga.R.id.action_daysFragment_to_dayExercisesFragment,
                    args
                )
            },
            onLockedClick = { day ->
                showLockedDaySnack(day)
            }
        )

        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.days.collect { adapter.submitList(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hasProgress.collect { hasProgress ->
                    val resetItem = binding.toolbar.menu.findItem(ru.netology.faceyoga.R.id.action_reset_progress)
                    resetItem?.isEnabled = hasProgress
                    resetItem?.icon?.alpha = if (hasProgress) 255 else 120
                }
            }
        }
    }

    private fun setupToolbarMenu() {
        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(ru.netology.faceyoga.R.menu.menu_settings)

        // ✅ Убираем tooltip "Ещё" на overflow-кнопке (три точки)
        binding.toolbar.post {
            val overflowDesc = getString(R.string.abc_action_menu_overflow_description)

            for (i in 0 until binding.toolbar.childCount) {
                val child = binding.toolbar.getChildAt(i)
                if (child is ActionMenuView) {
                    for (j in 0 until child.childCount) {
                        val c = child.getChildAt(j)
                        if (c.contentDescription == overflowDesc) {
                            TooltipCompat.setTooltipText(c, null)
                            c.setOnLongClickListener { true }
                        }
                    }
                }
            }
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                ru.netology.faceyoga.R.id.action_about -> {
                    findNavController().navigate(ru.netology.faceyoga.R.id.aboutFragment)
                    true
                }

                ru.netology.faceyoga.R.id.action_reset_progress -> {
                    if (!item.isEnabled) {
                        FySnack.show(
                            rootView = binding.root,
                            message = "Нет прогресса для сброса",
                            anchor = requireActivity().findViewById(ru.netology.faceyoga.R.id.bottom_nav)
                        )
                        return@setOnMenuItemClickListener true
                    }
                    showResetDialog()
                    true
                }

                else -> false
            }
        }
    }

    private fun showResetDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(ru.netology.faceyoga.R.string.reset_progress_title))
            .setMessage(getString(ru.netology.faceyoga.R.string.reset_progress_message))
            .setNegativeButton(getString(ru.netology.faceyoga.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(ru.netology.faceyoga.R.string.reset)) { _, _ ->
                // ✅ Analytics: пользователь подтвердил сброс прогресса
                analytics.log(AnalyticsEvents.PROGRESS_RESET)

                // ✅ Crashlytics breadcrumb
                FirebaseCrashlytics.getInstance().log("progress_reset_confirmed")

                viewModel.resetProgress()
                FySnack.show(
                    rootView = binding.root,
                    message = getString(ru.netology.faceyoga.R.string.progress_reset_done),
                    anchor = requireActivity().findViewById(ru.netology.faceyoga.R.id.bottom_nav)
                )
            }
            .show()
    }

    private fun showLockedDaySnack(day: DayUi) {
        val prevDay = (day.dayNumber - 1).coerceAtLeast(1)
        val text = getString(ru.netology.faceyoga.R.string.day_locked_toast, prevDay)
        FySnack.show(
            rootView = binding.root,
            message = text,
            anchor = requireActivity().findViewById(ru.netology.faceyoga.R.id.bottom_nav)
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}