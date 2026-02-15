package ru.netology.faceyoga.ui.articles

import android.os.Bundle
import android.view.View
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
import ru.netology.faceyoga.ui.common.FySnack

@AndroidEntryPoint
class ArticlesFragment : Fragment(R.layout.fragment_articles) {

    private val vm: ArticlesViewModel by viewModels()
    private lateinit var adapter: ArticlesSectionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.sectionsList)
        val anchor = requireActivity().findViewById<View>(R.id.bottom_nav)

        adapter = ArticlesSectionsAdapter { article ->
            if (article.isLocked) {
                val day = article.lockedAfterDay ?: 0
                FySnack.show(
                    rootView = view,
                    message = getString(R.string.articles_unlock_after_day, day),
                    anchor = anchor
                )
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

        vm.start()
    }
}
