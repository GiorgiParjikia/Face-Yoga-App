package ru.netology.faceyoga.ui.articles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.netology.faceyoga.R
import ru.netology.faceyoga.analytics.AnalyticsEvents
import ru.netology.faceyoga.analytics.AnalyticsLogger
import ru.netology.faceyoga.data.articles.ArticlesRepository
import ru.netology.faceyoga.data.articles.LanguageResolver
import ru.netology.faceyoga.ui.articles.adapter.ArticleAdapter
import ru.netology.faceyoga.ui.articles.parser.ArticleTextParser
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {

    @Inject lateinit var analytics: AnalyticsLogger

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val crash = FirebaseCrashlytics.getInstance()

        // --- Toolbar ---
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // --- RecyclerView ---
        val list = view.findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(requireContext())

        // --- Args ---
        val articleId = arguments?.getInt("articleId", -1) ?: -1
        val fromCongrats = arguments?.getBoolean("fromCongrats", false) ?: false

        // ✅ dayNumber берём из args (правильно), если нет — fallback на articleId (чтобы не ломать текущую схему)
        val dayNumber = (arguments?.getInt("dayNumber", -1) ?: -1).let { dn ->
            if (dn > 0) dn else articleId
        }

        // ✅ Crashlytics: keys + breadcrumb
        crash.setCustomKey("article_id", articleId)
        if (dayNumber > 0) crash.setCustomKey("day_number", dayNumber)
        crash.log("article_open id=$articleId source=${if (fromCongrats) "congrats" else "articles"} day=$dayNumber")

        // ✅ Analytics: открытие статьи
        val params = Bundle().apply {
            putInt("article_id", articleId)
            putString("source", if (fromCongrats) "congrats" else "articles")
            if (dayNumber > 0) putInt("day_number", dayNumber)
        }

        if (fromCongrats) {
            analytics.log(AnalyticsEvents.ARTICLE_OPEN_AFTER_WORKOUT, params)
        } else {
            analytics.log(AnalyticsEvents.ARTICLE_OPEN, params)
        }

        // --- Data ---
        val repo = ArticlesRepository(requireContext().applicationContext)
        val lang = LanguageResolver.currentLangKey(requireContext())

        // --- Load article ---
        viewLifecycleOwner.lifecycleScope.launch {
            val article = withContext(Dispatchers.IO) {
                repo.getById(articleId)
            }

            if (article == null) {
                list.adapter = ArticleAdapter(emptyList())
                return@launch
            }

            val blocks = ArticleTextParser.parse(
                text = article.textFor(lang),
                captions = article.captionsFor(lang)
            )

            list.adapter = ArticleAdapter(blocks)
        }
    }
}