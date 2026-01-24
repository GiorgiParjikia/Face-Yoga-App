package ru.netology.faceyoga.ui.days

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.faceyoga.R
import ru.netology.faceyoga.databinding.ItemDayBinding
import ru.netology.faceyoga.ui.common.localizedDayLevelTitle

class DaysAdapter(
    private val onClick: (DayUi) -> Unit
) : ListAdapter<DayUi, DaysAdapter.VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemDayBinding,
        private val onClick: (DayUi) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var current: DayUi? = null

        init {
            binding.root.setOnClickListener { current?.let(onClick) }
        }

        fun bind(item: DayUi) {
            current = item
            val ctx = itemView.context

            // Заголовок: День X
            binding.title.text = ctx.getString(R.string.day_title, item.dayNumber)

// Подзаголовок: Уровень • N упражнений
            val level = ctx.localizedDayLevelTitle(item.dayNumber) // после правки будет "Лёгкий"
            val exercises = ctx.resources.getQuantityString(
                R.plurals.exercises_count,
                item.exercisesCount,
                item.exercisesCount
            )
            binding.subtitle.text = ctx.getString(
                R.string.day_level_format,
                level,
                exercises
            )


            // Прогресс
            val percent = if (item.isCompleted) 100 else item.progressPercent
            binding.progress.progress = percent
            binding.status.text = ctx.getString(R.string.progress_percent, percent)
        }
    }

    private object Diff : DiffUtil.ItemCallback<DayUi>() {
        override fun areItemsTheSame(oldItem: DayUi, newItem: DayUi): Boolean =
            oldItem.programDayId == newItem.programDayId

        override fun areContentsTheSame(oldItem: DayUi, newItem: DayUi): Boolean =
            oldItem == newItem
    }
}