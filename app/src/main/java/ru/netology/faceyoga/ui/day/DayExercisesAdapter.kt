package ru.netology.faceyoga.ui.day

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val previewController: VideoPreviewController,
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
        return VH(
            binding = binding,
            resolver = videoUrlResolver,
            scope = scope,
            cache = previewCache,
            previewController = previewController,
            onClick = onClick
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemDayExerciseBinding,
        private val resolver: VideoUrlResolver,
        private val scope: CoroutineScope,
        private val cache: ConcurrentHashMap<String, String>,
        private val previewController: VideoPreviewController,
        private val onClick: (DayExerciseUi) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var current: DayExerciseUi? = null
        private var boundPreviewKey: String? = null

        // ⏱ job для задержки показа превью
        private var previewJob: Job? = null

        init {
            // обычный клик
            binding.root.setOnClickListener {
                current?.let(onClick)
            }

            // 🔥 LONG PRESS VIDEO PREVIEW (с задержкой)
            binding.root.setOnTouchListener { _, event ->
                val item = current
                val videoUri = item?.videoUri

                if (videoUri.isNullOrBlank()) return@setOnTouchListener false

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        previewJob?.cancel()

                        val localId = current?.id
                        previewJob = scope.launch {
                            delay(1_500) // ⏱ 1.5 секунды

                            // защита от реюза ViewHolder
                            if (current?.id == localId) {
                                val ctx = binding.root.context
                                val title = ctx.localizedExerciseTitle(item.title)
                                previewController.show(videoUri, title)
                            }
                        }
                        true
                    }


                    MotionEvent.ACTION_UP -> {
                        previewJob?.cancel()
                        previewJob = null
                        previewController.hide()
                        binding.root.performClick() // 👈 ВАЖНО
                        true
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        previewJob?.cancel()
                        previewJob = null
                        previewController.hide()
                        true
                    }

                    else -> false
                }
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

            // -------- Preview image --------
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
    if (url.isNullOrBlank()) {
        load(null) {
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_image_error)
        }
        return
    }

    // 56dp -> px (под твой контейнер 56dp x 56dp)
    val px = (56f * resources.displayMetrics.density).toInt()

    load(url) {
        crossfade(true)
        placeholder(R.drawable.ic_placeholder)
        error(R.drawable.ic_image_error)

        // КРИТИЧНО: иначе трансформация может "ломать" отрисовку (HARDWARE bitmap)
        allowHardware(false)

        // КРИТИЧНО: чтобы не декодировать 1080px ради 56dp
        size(px, px)

        // твой фокус (под лицо)
        transformations(VerticalCropTransformation(0.18f))

        // Если ты заменил JPG -> WebP "по тому же URL" и хочешь проверить, что не кэш:
        // memoryCachePolicy(CachePolicy.DISABLED)
        // diskCachePolicy(CachePolicy.DISABLED)
    }
}