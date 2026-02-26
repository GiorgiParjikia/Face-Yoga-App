package ru.netology.faceyoga.ui.articles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
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

        // ✅ Analytics: открытие статьи
        val params = Bundle().apply {
            putInt("article_id", articleId)
            putInt("day_number", articleId) // у тебя articleId == dayNumber (если так и задумано)
            putString("source", if (fromCongrats) "congrats" else "articles")
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