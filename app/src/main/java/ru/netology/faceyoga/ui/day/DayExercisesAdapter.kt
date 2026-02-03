package ru.netology.faceyoga.ui.day

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.media.VideoUrlResolver
import ru.netology.faceyoga.databinding.ItemDayExerciseBinding
import ru.netology.faceyoga.ui.common.VerticalCropTransformation
import ru.netology.faceyoga.ui.common.localizedExerciseTitle
import ru.netology.faceyoga.ui.common.localizedExerciseType
import ru.netology.faceyoga.ui.common.localizedItemName
import ru.netology.faceyoga.ui.common.localizedZone
import java.util.concurrent.ConcurrentHashMap

class DayExercisesAdapter(
    private val videoUrlResolver: VideoUrlResolver,
    private val scope: CoroutineScope,
    private val onClick: (DayExerciseUi) -> Unit = {}
) : ListAdapter<DayExerciseUi, DayExercisesAdapter.VH>(Diff) {

    // cache: gs://... -> https://...
    private val previewCache = ConcurrentHashMap<String, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDayExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding, videoUrlResolver, scope, previewCache, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemDayExerciseBinding,
        private val resolver: VideoUrlResolver,
        private val scope: CoroutineScope,
        private val cache: ConcurrentHashMap<String, String>,
        private val onClick: (DayExerciseUi) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var current: DayExerciseUi? = null
        private var boundPreviewKey: String? = null

        init {
            binding.root.setOnClickListener {
                current?.let(onClick)
            }
        }

        fun bind(item: DayExerciseUi) {
            current = item
            val ctx = itemView.context

            // -------- Title --------
            binding.title.text = ctx.localizedExerciseTitle(item.title)

            // -------- Subtitle --------
            val zoneText = ctx.localizedZone(item.zone)
            val typeText = ctx.localizedExerciseType(item.type)
            binding.subtitle.text =
                ctx.getString(R.string.exercise_subtitle, zoneText, typeText)

            // -------- Right info --------
            binding.rightInfo.text = item.rightInfo

            // -------- Item icon --------
            binding.itemIcon.isVisible = item.requiresItem
            if (item.requiresItem) {
                binding.itemIcon.setImageResource(R.drawable.ic_item_pencil)
                binding.itemIcon.contentDescription =
                    ctx.localizedItemName(item.requiredItemKey)
            }

            // -------- Preview --------
            val uri = item.previewImageUri
            boundPreviewKey = uri

            // сбрасываем старую картинку при реюзе
            binding.preview.load(null) {
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_image_error)
            }

            if (uri.isNullOrBlank()) return

            // http(s) — грузим сразу
            if (uri.startsWith("http")) {
                binding.preview.loadPreview(uri)
                return
            }

            // gs:// — проверяем кэш
            cache[uri]?.let { https ->
                binding.preview.loadPreview(https)
                return
            }

            // gs:// — резолвим асинхронно
            scope.launch {
                val https = withContext(Dispatchers.IO) {
                    runCatching { resolver.resolve(uri) }.getOrNull()
                } ?: return@launch

                cache[uri] = https

                // защита от реюза ViewHolder
                if (boundPreviewKey != uri) return@launch

                binding.preview.loadPreview(https)
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<DayExerciseUi>() {
        override fun areItemsTheSame(
            oldItem: DayExerciseUi,
            newItem: DayExerciseUi
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DayExerciseUi,
            newItem: DayExerciseUi
        ): Boolean = oldItem == newItem
    }
}

/**
 * Extension для единообразной загрузки превью
 */
private fun ImageView.loadPreview(url: String?) {
    load(url) {
        crossfade(true)
        placeholder(R.drawable.ic_placeholder)
        error(R.drawable.ic_image_error)
        transformations(VerticalCropTransformation(0.18f))
    }
}
