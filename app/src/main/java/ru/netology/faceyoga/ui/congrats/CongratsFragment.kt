package ru.netology.faceyoga.ui.congrats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.repository.ProgressRepository
import javax.inject.Inject

@AndroidEntryPoint
class CongratsFragment : Fragment(R.layout.fragment_congrats) {

    @Inject
    lateinit var progressRepo: ProgressRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnOpenArticle = view.findViewById<MaterialButton>(R.id.btnOpenArticle)
        val btnBackToDays = view.findViewById<MaterialButton>(R.id.btnBackToDays)

        // 👉 номер дня, который только что был завершён
        val dayNumber = arguments?.getInt("dayNumber", -1) ?: -1

        // ✅ сохраняем прогресс сразу при показе экрана Congrats
        if (dayNumber > 0) {
            viewLifecycleOwner.lifecycleScope.launch {
                progressRepo.setLastCompletedDay(dayNumber)
            }
        }

        btnOpenArticle.setOnClickListener {
            // защита от кривых аргументов (на всякий случай)
            if (dayNumber <= 0) return@setOnClickListener

            findNavController().navigate(
                R.id.articleFragment,
                Bundle().apply { putInt("articleId", dayNumber) }
            )
        }

        btnBackToDays.setOnClickListener {
            findNavController().popBackStack(R.id.daysFragment, false)
        }
    }
}
