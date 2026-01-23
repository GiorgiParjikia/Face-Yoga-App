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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.databinding.FragmentDaysBinding

@AndroidEntryPoint
class DaysFragment : Fragment(R.layout.fragment_days) {

    private var _binding: FragmentDaysBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DaysViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentDaysBinding.bind(view)

        // Insets (чтобы статусбар/навигация не перекрывали)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val adapter = DaysAdapter { day ->
            val args = Bundle().apply {
                putLong("programDayId", day.programDayId)
                putInt("dayNumber", day.dayNumber)
            }
            findNavController().navigate(R.id.action_daysFragment_to_dayExercisesFragment, args)
        }


        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.days.collect { adapter.submitList(it) }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
