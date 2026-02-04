package ru.netology.faceyoga.ui.congrats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import ru.netology.faceyoga.R

class CongratsFragment : Fragment(R.layout.fragment_congrats) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnOpenArticle = view.findViewById<MaterialButton>(R.id.btnOpenArticle)
        val btnBackToDays = view.findViewById<MaterialButton>(R.id.btnBackToDays)

        // 👉 номер дня, который только что был завершён
        val dayNumber = arguments?.getInt("dayNumber", -1)

        btnOpenArticle.setOnClickListener {

            // защита от кривых аргументов (на всякий случай)
            if (dayNumber == null || dayNumber <= 0) {
                // если вдруг day не передался — ничего не делаем
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                // статья дня = номер дня
                putInt("articleId", dayNumber)
            }

            findNavController().navigate(
                R.id.articleFragment,
                bundle
            )
        }

        btnBackToDays.setOnClickListener {
            findNavController().popBackStack(R.id.daysFragment, false)
        }
    }
}
