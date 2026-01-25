package ru.netology.faceyoga.ui.days

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.faceyoga.R
import ru.netology.faceyoga.databinding.ItemDayBinding
import ru.netology.faceyoga.ui.common.localizedDayLevelTitle
import kotlin.math.roundToInt

class DaysAdapter(
    private val onClick: (DayUi) -> Unit,
    private val onLockedClick: (DayUi) -> Unit,
) : ListAdapter<DayUi, DaysAdapter.VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding, onClick, onLockedClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemDayBinding,
        private val onClick: (DayUi) -> Unit,
        private val onLockedClick: (DayUi) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DayUi) {
            val ctx = itemView.context

            // --- всегда сбрасываем базовый стейт ---
            binding.lockIcon.isVisible = false
            binding.chevron.isVisible = true
            binding.root.setOnClickListener(null)

            // Заголовок: День X
            binding.title.text = ctx.getString(R.string.day_title, item.dayNumber)

            // Подзаголовок: Уровень • N упражнений
            val level = ctx.localizedDayLevelTitle(item.dayNumber)
            val exercises = ctx.resources.getQuantityString(
                R.plurals.exercises_count,
                item.exercisesCount,
                item.exercisesCount
            )
            binding.subtitle.text = ctx.getString(R.string.day_level_format, level, exercises)

            // Прогресс
            val percent = if (item.isCompleted) 100 else item.progressPercent
            binding.progress.progress = percent.coerceIn(0, 100)
            binding.status.text = ctx.getString(R.string.progress_percent, percent)

            // ===== LOCK UI =====
            val locked = item.isLocked

            // 1) Фон карточки — через tint (стабильно, без “переезда” при скролле)
            val bgColor = ContextCompat.getColor(
                ctx,
                if (locked) R.color.surface_card_locked else R.color.surface_card
            )
            binding.card.backgroundTintList = ColorStateList.valueOf(bgColor)

            // 2) Контент — альфа (а не сама card)
            binding.content.alpha = if (locked) 0.45f else 1f

            // Иконки
            binding.lockIcon.isVisible = locked
            binding.chevron.isVisible = !locked

            // Клик
            binding.card.setOnClickListener {
                if (locked) onLockedClick(item) else onClick(item)
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<DayUi>() {
        override fun areItemsTheSame(oldItem: DayUi, newItem: DayUi): Boolean =
            oldItem.programDayId == newItem.programDayId

        override fun areContentsTheSame(oldItem: DayUi, newItem: DayUi): Boolean =
            oldItem == newItem
    }
}
