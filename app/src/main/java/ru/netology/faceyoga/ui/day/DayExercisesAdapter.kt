package ru.netology.faceyoga.ui.day

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.media.VideoUrlResolver
import ru.netology.faceyoga.databinding.ItemDayExerciseBinding
import ru.netology.faceyoga.ui.common.VerticalCropTransformation
import ru.netology.faceyoga.ui.common.localizedExerciseTitle
import ru.netology.faceyoga.ui.common.localizedExerciseType
import ru.netology.faceyoga.ui.common.localizedZone
import java.util.concurrent.ConcurrentHashMap

class DayExercisesAdapter(
    private val videoUrlResolver: VideoUrlResolver,
    private val onClick: (DayExerciseUi) -> Unit = {}
) : ListAdapter<DayExerciseUi, DayExercisesAdapter.VH>(Diff) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    // cache: gs://... -> https://...
    private val previewCache = ConcurrentHashMap<String, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDayExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding, onClick, videoUrlResolver, scope, previewCache)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        job.cancel()
    }

    class VH(
        private val binding: ItemDayExerciseBinding,
        private val onClick: (DayExerciseUi) -> Unit,
        private val resolver: VideoUrlResolver,
        private val scope: CoroutineScope,
        private val cache: ConcurrentHashMap<String, String>
    ) : RecyclerView.ViewHolder(binding.root) {

        private var current: DayExerciseUi? = null

        // защита от реюза viewHolder при асинхронной загрузке
        private var boundPreviewKey: String? = null

        init {
            binding.root.setOnClickListener {
                current?.let(onClick)
            }
        }

        fun bind(item: DayExerciseUi) {
            current = item
            val ctx = itemView.context

            // title
            binding.title.text = ctx.localizedExerciseTitle(item.title)

            // subtitle (через ресурсы)
            val zoneText = ctx.localizedZone(item.zone)
            val typeText = ctx.localizedExerciseType(item.type)
            binding.subtitle.text = ctx.getString(R.string.exercise_subtitle, zoneText, typeText)

            // rightInfo
            binding.rightInfo.text = item.rightInfo

            // ===== PREVIEW =====
            val uri = item.previewImageUri
            boundPreviewKey = uri

            // placeholder сразу (сброс старой картинки при реюзе)
            binding.preview.load(null) {
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_image_error)
            }

            if (uri.isNullOrBlank()) return

            // https -> грузим сразу
            if (uri.startsWith("http")) {
                binding.preview.load(uri) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_image_error)
                    transformations(VerticalCropTransformation(0.18f))
                }
                return
            }

            // gs:// -> проверяем кэш
            cache[uri]?.let { https ->
                binding.preview.load(https) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_image_error)
                    transformations(VerticalCropTransformation(0.18f))
                }
                return
            }

            // gs:// -> резолвим в https и кэшируем
            scope.launch {
                val https = withContext(Dispatchers.IO) {
                    runCatching { resolver.resolve(uri) }.getOrNull()
                } ?: return@launch

                cache[uri] = https

                // защита от реюза viewHolder
                if (boundPreviewKey != uri) return@launch

                binding.preview.load(https) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_image_error)
                    transformations(VerticalCropTransformation(0.18f))
                }
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<DayExerciseUi>() {
        override fun areItemsTheSame(oldItem: DayExerciseUi, newItem: DayExerciseUi): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DayExerciseUi, newItem: DayExerciseUi): Boolean =
            oldItem == newItem
    }
}
