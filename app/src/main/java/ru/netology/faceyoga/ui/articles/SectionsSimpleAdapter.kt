package ru.netology.faceyoga.ui.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.netology.faceyoga.R
import ru.netology.faceyoga.ui.articles.model.ArticleCardUi
import ru.netology.faceyoga.ui.articles.model.ArticleSectionUi

class SectionsSimpleAdapter(
    private val onArticleClick: (ArticleCardUi) -> Unit
) : RecyclerView.Adapter<SectionsSimpleAdapter.VH>() {

    private var items: List<ArticleSectionUi> = emptyList()

    fun submit(list: List<ArticleSectionUi>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_section_simple, parent, false)
        return VH(v, onArticleClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VH(
        itemView: View,
        private val onArticleClick: (ArticleCardUi) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.sectionTitle)
        private val container: LinearLayout = itemView.findViewById(R.id.itemsContainer)

        fun bind(section: ArticleSectionUi) {
            title.text = itemView.context.getString(section.titleRes)

            container.removeAllViews()
            val inflater = LayoutInflater.from(itemView.context)

            section.items.forEach { article ->
                val row = inflater.inflate(R.layout.item_article_row_simple, container, false)
                val t = row.findViewById<TextView>(R.id.articleTitle)
                val locked = row.findViewById<TextView>(R.id.articleLocked)

                // Заголовок + маркер замка
                t.text = if (article.isLocked) "🔒 ${article.title}" else article.title

                // Подпись "Откроется после дня N" — только если locked
                if (article.isLocked) {
                    locked.visibility = View.VISIBLE
                    val day = article.lockedAfterDay ?: 0
                    locked.text = itemView.context.getString(R.string.articles_unlock_after_day, day)
                } else {
                    locked.visibility = View.GONE
                }

                // Клик
                row.setOnClickListener { onArticleClick(article) }

                container.addView(row)
            }
        }
    }
}
