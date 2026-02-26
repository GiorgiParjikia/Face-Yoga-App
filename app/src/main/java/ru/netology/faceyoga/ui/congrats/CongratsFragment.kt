package ru.netology.faceyoga.ui.congrats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.analytics.AnalyticsEvents
import ru.netology.faceyoga.analytics.AnalyticsLogger
import ru.netology.faceyoga.data.repository.ProgressRepository
import javax.inject.Inject

@AndroidEntryPoint
class CongratsFragment : Fragment(R.layout.fragment_congrats) {

    @Inject lateinit var progressRepo: ProgressRepository
    @Inject lateinit var analytics: AnalyticsLogger

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val crash = FirebaseCrashlytics.getInstance()

        val btnOpenArticle = view.findViewById<MaterialButton>(R.id.btnOpenArticle)
        val btnBackToDays = view.findViewById<MaterialButton>(R.id.btnBackToDays)

        // 👉 номер дня, который только что был завершён
        val dayNumber = arguments?.getInt("dayNumber", -1) ?: -1

        // ✅ Crashlytics
        if (dayNumber > 0) crash.setCustomKey("day_number", dayNumber)
        crash.log("congrats_open day=$dayNumber")

        // ✅ Analytics: тренировка завершена (дошёл до Congrats)
        if (dayNumber > 0) {
            analytics.log(
                AnalyticsEvents.WORKOUT_FINISH,
                Bundle().apply { putInt("day_number", dayNumber) }
            )
        } else {
            analytics.log(AnalyticsEvents.WORKOUT_FINISH)
        }

        // ✅ сохраняем прогресс сразу при показе экрана Congrats
        if (dayNumber > 0) {
            viewLifecycleOwner.lifecycleScope.launch {
                progressRepo.setLastCompletedDay(dayNumber)
            }
        }

        btnOpenArticle.setOnClickListener {
            if (dayNumber <= 0) return@setOnClickListener

            crash.log("open_article_from_congrats day=$dayNumber")

            findNavController().navigate(
                R.id.articleFragment,
                Bundle().apply {
                    putInt("articleId", dayNumber)
                    putInt("dayNumber", dayNumber)          // ✅ важно: честный day_number
                    putBoolean("fromCongrats", true)        // ✅ важно для аналитики
                }
            )
        }

        btnBackToDays.setOnClickListener {
            crash.log("back_to_days_from_congrats day=$dayNumber")
            findNavController().popBackStack(R.id.daysFragment, false)
        }
    }
}