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
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.ui.common.FySnack

@AndroidEntryPoint
class ArticlesFragment : Fragment(R.layout.fragment_articles) {

    private val vm: ArticlesViewModel by viewModels()

    private var adapter: ArticlesSectionsAdapter? = null
    private var list: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<RecyclerView>(R.id.sectionsList)
        this.list = list

        val anchor = requireActivity().findViewById<View>(R.id.bottom_nav)

        val adapter = ArticlesSectionsAdapter { article ->
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
        this.adapter = adapter

        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.sections.collect { adapter.submit(it) }
            }
        }

        vm.start()
    }

    override fun onDestroyView() {
        // важно: разорвать RecyclerView -> Adapter -> View ссылки
        list?.adapter = null
        list = null

        adapter = null

        super.onDestroyView()
    }
}