package ru.netology.faceyoga.ui.days

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.databinding.FragmentDaysBinding
import ru.netology.faceyoga.ui.common.StateKeys

@AndroidEntryPoint
class DaysFragment : Fragment(R.layout.fragment_days) {

    private var _binding: FragmentDaysBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DaysViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDaysBinding.bind(view)

        // Insets (чтобы статусбар/навигация не перекрывали)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        setupToolbarMenu()

        val adapter = DaysAdapter(
            onClick = { day ->
                val args = Bundle().apply {
                    putLong(StateKeys.PROGRAM_DAY_ID, day.programDayId)
                    putInt(StateKeys.DAY_NUMBER, day.dayNumber)
                }
                findNavController().navigate(
                    R.id.action_daysFragment_to_dayExercisesFragment,
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

        // ✅ NEW: Reset виден всегда, но выключен если прогресса нет
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hasProgress.collect { hasProgress ->
                    val resetItem = binding.toolbar.menu.findItem(R.id.action_reset_progress)
                    resetItem?.isEnabled = hasProgress
                    resetItem?.icon?.alpha = if (hasProgress) 255 else 120
                }
            }
        }
    }

    private fun setupToolbarMenu() {
        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(R.menu.menu_settings)

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.action_about -> {
                    findNavController().navigate(R.id.aboutFragment)
                    true
                }

                R.id.action_reset_progress -> {
                    if (!item.isEnabled) {
                        Snackbar.make(
                            binding.root,
                            "Нет прогресса для сброса",
                            Snackbar.LENGTH_SHORT
                        ).show()
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
            .setTitle(getString(R.string.reset_progress_title))
            .setMessage(getString(R.string.reset_progress_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.reset)) { _, _ ->
                viewModel.resetProgress()
                Snackbar.make(
                    binding.root,
                    getString(R.string.progress_reset_done),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            .show()
    }

    private fun showLockedDaySnack(day: DayUi) {
        val prevDay = (day.dayNumber - 1).coerceAtLeast(1)
        val text = getString(R.string.day_locked_toast, prevDay)
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
