package ru.netology.faceyoga.ui.day

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.media.VideoUrlResolver
import ru.netology.faceyoga.databinding.FragmentDayExercisesBinding
import ru.netology.faceyoga.ui.common.localizedItemName
import javax.inject.Inject

@AndroidEntryPoint
class DayExercisesFragment : Fragment(R.layout.fragment_day_exercises) {

    private var _binding: FragmentDayExercisesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DayExercisesViewModel by viewModels()

    // gs:// → https для превью
    @Inject
    lateinit var videoUrlResolver: VideoUrlResolver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDayExercisesBinding.bind(view)

        // Insets под статус-бар
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            binding.toolbar.setPadding(
                binding.toolbar.paddingLeft,
                topInset,
                binding.toolbar.paddingRight,
                binding.toolbar.paddingBottom
            )
            insets
        }

        // -------- Аргументы --------
        val args = requireArguments()
        val dayNumber = args.getInt("dayNumber", 1)
        val programDayId = args.getLong("programDayId")

        // ⚠️ Оставляем, но теперь по идее не понадобится (DaysFragment не пускает в locked)
        val isLocked = args.getBoolean("isLocked", false)

        // -------- Toolbar --------
        binding.toolbar.title = getString(R.string.day_title, dayNumber)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // -------- Список упражнений --------
        val adapter = DayExercisesAdapter(videoUrlResolver)
        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exercises.collect { list ->
                    adapter.submitList(list)
                }
            }
        }

        // -------- Инфо-блок про предметы --------
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.requiredItemKeys.collect { keys ->
                    if (keys.isEmpty()) {
                        binding.itemWarning.isGone = true
                    } else {
                        val itemsText = keys
                            .map { requireContext().localizedItemName(it) }
                            .filter { it.isNotBlank() }
                            .distinct()
                            .joinToString(", ")

                        binding.itemWarning.text =
                            getString(R.string.day_item_warning_format, itemsText)

                        binding.itemWarning.isGone = false
                    }
                }
            }
        }

        // -------- Start → Countdown (с защитой) --------
        if (isLocked) {
            binding.start.isEnabled = false
            binding.start.alpha = 0.45f

            binding.start.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.complete_previous_day),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            binding.start.setOnClickListener {
                val navArgs = Bundle().apply {
                    putLong("programDayId", programDayId)
                    putInt("dayNumber", dayNumber) // ✅ NEW: прокидываем dayNumber
                }
                findNavController().navigate(
                    R.id.action_dayExercisesFragment_to_countdownFragment,
                    navArgs
                )
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
