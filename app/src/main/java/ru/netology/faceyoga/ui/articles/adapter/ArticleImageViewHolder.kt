package ru.netology.faceyoga.ui.articles.adapter

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.netology.faceyoga.R
import ru.netology.faceyoga.ui.articles.model.ArticleBlock

class ArticleImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val image = view.findViewById<ImageView>(R.id.image)
    private val caption = view.findViewById<TextView>(R.id.caption)

    fun bind(item: ArticleBlock.Image) {
        val ctx = itemView.context

        // грузим из assets по имени (например: "Anatomy Head.H03.2k.png")
        ctx.assets.open(item.name).use { input ->
            val bmp = BitmapFactory.decodeStream(input)
            image.setImageBitmap(bmp)
        }

        if (item.caption.isNullOrBlank()) {
            caption.visibility = View.GONE
        } else {
            caption.visibility = View.VISIBLE
            caption.text = item.caption
        }
    }
}
