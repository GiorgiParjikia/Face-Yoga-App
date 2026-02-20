package ru.netology.faceyoga.data.model

import androidx.annotation.StringRes
import ru.netology.faceyoga.R

enum class ArticleCategory(
    val key: String,
    @param:StringRes val titleRes: Int
) {
    BASE_CARE("base_care", R.string.articles_cat_base_care),
    PROCESS_TECH("process_tech", R.string.articles_cat_process_tech),
    FACE_SYSTEM("face_system", R.string.articles_cat_face_system),
    LIFESTYLE("lifestyle", R.string.articles_cat_lifestyle),
    EXTRA_USEFUL("extra_useful", R.string.articles_cat_extra_useful),
    SOCIAL_PSY("social_psy", R.string.articles_cat_social_psy),
    FINAL("final", R.string.articles_cat_final);

    companion object {
        fun fromKey(key: String): ArticleCategory =
            entries.firstOrNull { it.key == key } ?: BASE_CARE
    }
}