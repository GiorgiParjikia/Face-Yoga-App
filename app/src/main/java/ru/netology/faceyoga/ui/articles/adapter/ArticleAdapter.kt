package ru.netology.faceyoga.ui.articles.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import ru.netology.faceyoga.databinding.ItemArticleImageBinding
import ru.netology.faceyoga.databinding.ItemArticleTextBinding
import ru.netology.faceyoga.ui.articles.model.ArticleBlock
import coil.load
import android.text.method.LinkMovementMethod


class ArticleAdapter(
    private val items: List<ArticleBlock>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is ArticleBlock.Text -> TYPE_TEXT
            is ArticleBlock.Image -> TYPE_IMAGE
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_TEXT -> TextVH(
                ItemArticleTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            TYPE_IMAGE -> ImageVH(
                ItemArticleImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> error("Unknown viewType: $viewType")
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (val item = items[position]) {
            is ArticleBlock.Text -> (holder as TextVH).bind(item)
            is ArticleBlock.Image -> (holder as ImageVH).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    // ---------- TEXT ----------

    class TextVH(
        private val b: ItemArticleTextBinding
    ) : RecyclerView.ViewHolder(b.root) {

        init {
            b.text.movementMethod = LinkMovementMethod.getInstance()
        }

        fun bind(item: ArticleBlock.Text) {
            val raw = item.text.trim()

            when {
                raw.startsWith("## ") -> {
                    b.text.text = raw.removePrefix("## ").trim()
                    b.text.setTypeface(null, Typeface.BOLD)
                    b.text.textSize = 18f
                }
                else -> {
                    b.text.text = raw
                    b.text.setTypeface(null, Typeface.NORMAL)
                    b.text.textSize = 14f
                }
            }
        }
    }

    // ---------- IMAGE ----------

    class ImageVH(
        private val b: ItemArticleImageBinding
    ) : RecyclerView.ViewHolder(b.root) {

        fun bind(item: ArticleBlock.Image) {
            b.caption.text = item.caption
            b.caption.visibility =
                if (item.caption.isNullOrBlank()) View.GONE else View.VISIBLE

            val storageRef = FirebaseStorage
                .getInstance()
                .reference
                .child(item.name)

            // Firebase Storage -> downloadUrl -> Coil
            storageRef.downloadUrl
                .addOnSuccessListener { uri ->
                    b.image.load(uri) {
                        crossfade(true)
                    }
                }
                .addOnFailureListener {
                    b.image.setImageDrawable(null)
                }
        }
    }


    companion object {
        private const val TYPE_TEXT = 0
        private const val TYPE_IMAGE = 1
    }
}
