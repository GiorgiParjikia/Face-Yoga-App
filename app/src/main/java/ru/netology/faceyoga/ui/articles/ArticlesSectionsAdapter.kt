package ru.netology.faceyoga.ui.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.faceyoga.R
import ru.netology.faceyoga.ui.articles.model.ArticleCardUi
import ru.netology.faceyoga.ui.articles.model.ArticleSectionUi

class ArticlesSectionsAdapter(
    private val onCardClick: (ArticleCardUi) -> Unit
) : RecyclerView.Adapter<ArticlesSectionsAdapter.VH>() {

    private var items: List<ArticleSectionUi> = emptyList()

    fun submit(list: List<ArticleSectionUi>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_section_carousel, parent, false)
        return VH(v, onCardClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    class VH(
        itemView: View,
        onCardClick: (ArticleCardUi) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.sectionTitle)
        private val carousel: RecyclerView = itemView.findViewById(R.id.carousel)

        private val cardsAdapter = ArticleCardsAdapter(onCardClick)

        init {
            carousel.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            carousel.adapter = cardsAdapter
        }

        fun bind(section: ArticleSectionUi) {
            title.text = itemView.context.getString(section.titleRes)
            cardsAdapter.submit(section.items)
        }
    }
}
