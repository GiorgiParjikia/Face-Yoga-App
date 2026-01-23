package ru.netology.faceyoga.ui.day

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.media.VideoUrlResolver
import ru.netology.faceyoga.databinding.FragmentDayExercisesBinding
import javax.inject.Inject

@AndroidEntryPoint
class DayExercisesFragment : Fragment(R.layout.fragment_day_exercises) {

    private var _binding: FragmentDayExercisesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DayExercisesViewModel by viewModels()

    // ✅ Нужен для резолвинга gs:// -> https для превью
    @Inject lateinit var videoUrlResolver: VideoUrlResolver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDayExercisesBinding.bind(view)

        // ✅ Корректный отступ под статус-бар (стрелка и заголовок не налезают на время)
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

        // Заголовок и back
        val dayNumber = requireArguments().getInt("dayNumber")
        binding.toolbar.title = "Day $dayNumber"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // ✅ Список упражнений (адаптер теперь принимает resolver)
        val adapter = DayExercisesAdapter(videoUrlResolver)
        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exercises.collect { list ->
                    adapter.submitList(list)
                }
            }
        }

        // Start → Countdown
        binding.start.setOnClickListener {
            val programDayId = requireArguments().getLong("programDayId")
            val args = Bundle().apply {
                putLong("programDayId", programDayId)
            }
            findNavController().navigate(
                R.id.action_dayExercisesFragment_to_countdownFragment,
                args
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
