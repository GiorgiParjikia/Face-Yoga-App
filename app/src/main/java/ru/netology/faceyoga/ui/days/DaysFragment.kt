package ru.netology.faceyoga.ui.lessons

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import ru.netology.faceyoga.R
import ru.netology.faceyoga.databinding.FragmentLessonsBinding

class DaysFragment : Fragment(R.layout.fragment_lessons) {

    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLessonsBinding.bind(view)

        // Insets (твоя “правильная” версия)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val adapter = LessonsAdapter { lesson ->
            // пока просто заглушка
            // позже: переход на экран деталей / плеера
        }

        binding.lessonsList.adapter = adapter

        adapter.submit(
            listOf(
                LessonUi(1, "Базовый уход", "10 мин • Лёгкий уровень"),
                LessonUi(2, "Глаза и отёки", "8 мин • Средний уровень"),
                LessonUi(3, "Овал лица", "12 мин • Средний уровень"),
                LessonUi(4, "Массаж гуаша", "15 мин • Продвинутый"),
            )
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
