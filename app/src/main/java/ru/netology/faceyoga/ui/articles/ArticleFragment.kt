package ru.netology.faceyoga.ui.articles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.articles.ArticlesRepository
import ru.netology.faceyoga.data.articles.LanguageResolver
import ru.netology.faceyoga.ui.articles.adapter.ArticleAdapter
import ru.netology.faceyoga.ui.articles.parser.ArticleTextParser

class ArticleFragment : Fragment(R.layout.fragment_article) {

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

        // --- Data ---
        val articleId = arguments?.getInt("articleId", -1) ?: -1
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
