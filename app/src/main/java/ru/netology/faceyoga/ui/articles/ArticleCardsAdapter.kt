package ru.netology.faceyoga.ui.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ru.netology.faceyoga.R
import ru.netology.faceyoga.ui.articles.model.ArticleCardUi

class ArticleCardsAdapter(
    private val onClick: (ArticleCardUi) -> Unit
) : RecyclerView.Adapter<ArticleCardsAdapter.VH>() {

    private var items: List<ArticleCardUi> = emptyList()

    fun submit(list: List<ArticleCardUi>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_card, parent, false)
        return VH(v, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VH(
        itemView: View,
        private val onClick: (ArticleCardUi) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val card: MaterialCardView = itemView.findViewById(R.id.card)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val lockedText: TextView = itemView.findViewById(R.id.lockedText)
        private val overlay: View = itemView.findViewById(R.id.lockedOverlay)
        private val arrowIcon: ImageView = itemView.findViewById(R.id.arrowIcon)

        fun bind(item: ArticleCardUi) {
            val ctx = itemView.context

            title.text = item.title

            val unlockedBg = ContextCompat.getColor(ctx, R.color.green_primary_dark)
            val lockedBg = ContextCompat.getColor(ctx, R.color.article_card_green_locked)
            val lockedArrow = ContextCompat.getColor(ctx, R.color.article_arrow_locked)

            if (item.isLocked) {
                // фон карточки чуть "серее"
                card.setCardBackgroundColor(lockedBg)

                // стрелка приглушенная
                arrowIcon.setColorFilter(lockedArrow)

                // подпись про день
                val day = item.lockedAfterDay ?: 0
                lockedText.visibility = View.VISIBLE
                lockedText.text = ctx.getString(R.string.articles_unlock_after_day, day)

                // лёгкое затемнение (очень слабое)
                overlay.visibility = View.VISIBLE
                overlay.alpha = 0.08f
            } else {
                // обычный фон
                card.setCardBackgroundColor(unlockedBg)

                // стрелка как раньше (в цвет карточки)
                arrowIcon.setColorFilter(unlockedBg)

                lockedText.visibility = View.GONE
                overlay.visibility = View.GONE
            }

            itemView.setOnClickListener { onClick(item) }
        }
    }
}
