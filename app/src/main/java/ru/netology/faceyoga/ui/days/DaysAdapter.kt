package ru.netology.faceyoga.ui.days

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.faceyoga.databinding.ItemLessonBinding

class LessonsAdapter(
    private val onClick: (LessonUi) -> Unit
) : RecyclerView.Adapter<LessonsAdapter.VH>() {

    private var items: List<LessonUi> = emptyList()

    fun submit(list: List<LessonUi>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLessonBinding.inflate(inflater, parent, false)
        return VH(binding, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VH(
        private val binding: ItemLessonBinding,
        private val onClick: (LessonUi) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var current: LessonUi? = null

        init {
            binding.root.setOnClickListener {
                current?.let(onClick)
            }
        }

        fun bind(item: LessonUi) {
            current = item
            binding.title.text = item.title
            binding.subtitle.text = item.subtitle
        }
    }
}
