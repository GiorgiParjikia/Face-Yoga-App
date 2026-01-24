package ru.netology.faceyoga.ui.congrats

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import ru.netology.faceyoga.R

class CongratsFragment : Fragment(R.layout.fragment_congrats) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnOpenArticle = view.findViewById<MaterialButton>(R.id.btnOpenArticle)
        val btnBackToDays = view.findViewById<MaterialButton>(R.id.btnBackToDays)

        btnOpenArticle.setOnClickListener {
            // пока заглушка (статьи ещё не готовы)
            Toast.makeText(requireContext(), "Статьи скоро будут доступны", Toast.LENGTH_SHORT).show()
        }

        btnBackToDays.setOnClickListener {
            // MVP: вернуться на экран дней (корень вкладки "Упражнения")
            findNavController().popBackStack(R.id.daysFragment, false)
        }
    }
}