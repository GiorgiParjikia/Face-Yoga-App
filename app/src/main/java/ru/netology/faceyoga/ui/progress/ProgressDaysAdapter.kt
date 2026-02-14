package ru.netology.faceyoga.ui.progress


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ru.netology.faceyoga.R

class ProgressDaysAdapter(
    private val onDayClick: (ProgressDayUi) -> Unit
) : ListAdapter<ProgressDayUi, ProgressDaysAdapter.VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress_day, parent, false)
        return VH(v, onDayClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        itemView: View,
        private val onDayClick: (ProgressDayUi) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val card: MaterialCardView = itemView.findViewById(R.id.dayCard)
        private val tvDay: TextView = itemView.findViewById(R.id.tvDay)

        fun bind(item: ProgressDayUi) {
            tvDay.text = item.day.toString()

            val ctx = itemView.context

            when (item.state) {
                DayState.DONE -> {
                    card.strokeWidth = dp(ctx, 1)
                    card.strokeColor = ContextCompat.getColor(ctx, R.color.green_primary_dark)
                    card.setCardBackgroundColor(ContextCompat.getColor(ctx, android.R.color.transparent))
                    tvDay.setTextColor(ContextCompat.getColor(ctx, R.color.green_primary_dark))
                    tvDay.alpha = 1f
                }

                DayState.AVAILABLE -> {
                    card.strokeWidth = 0
                    card.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.green_primary_dark))
                    tvDay.setTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    tvDay.alpha = 1f
                }

                DayState.LOCKED -> {
                    card.strokeWidth = dp(ctx, 1)
                    card.strokeColor = ContextCompat.getColor(ctx, android.R.color.darker_gray)
                    card.setCardBackgroundColor(ContextCompat.getColor(ctx, android.R.color.transparent))
                    tvDay.setTextColor(ContextCompat.getColor(ctx, android.R.color.darker_gray))
                    tvDay.alpha = 0.55f
                }
            }

            itemView.setOnClickListener { onDayClick(item) }
        }

        private fun dp(ctx: android.content.Context, value: Int): Int {
            return (value * ctx.resources.displayMetrics.density).toInt()
        }
    }

    private object Diff : DiffUtil.ItemCallback<ProgressDayUi>() {
        override fun areItemsTheSame(oldItem: ProgressDayUi, newItem: ProgressDayUi): Boolean =
            oldItem.day == newItem.day

        override fun areContentsTheSame(oldItem: ProgressDayUi, newItem: ProgressDayUi): Boolean =
            oldItem == newItem
    }
}
