package ru.netology.faceyoga.ui.articles

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R

@AndroidEntryPoint
class ArticlesFragment : Fragment(R.layout.fragment_articles) {

    private val vm: ArticlesViewModel by viewModels()
    private lateinit var adapter: ArticlesSectionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.sectionsList)

        adapter = ArticlesSectionsAdapter { article ->
            if (article.isLocked) {
                val day = article.lockedAfterDay ?: 0
                Toast.makeText(
                    requireContext(),
                    getString(R.string.articles_unlock_after_day, day),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(
                    R.id.action_articlesFragment_to_articleFragment,
                    bundleOf("articleId" to article.id)
                )
            }
        }

        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.sections.collect { adapter.submit(it) }
            }
        }

        vm.load()
    }

    override fun onResume() {
        super.onResume()
        // ✅ если язык/прогресс поменялись пока ты был в другом экране — обновим
        vm.load()
    }
}
