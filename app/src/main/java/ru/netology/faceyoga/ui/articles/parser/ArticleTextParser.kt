package ru.netology.faceyoga.ui.articles.parser

import ru.netology.faceyoga.ui.articles.model.ArticleBlock

object ArticleTextParser {

    // [IMAGE: path | caption]
    private val imageRegex =
        Regex("""\[IMAGE:\s*([^|\]]+)(?:\s*\|\s*([^\]]+))?]""")

    fun parse(
        text: String,
        captions: List<String>
    ): List<ArticleBlock> {

        val result = mutableListOf<ArticleBlock>()
        var lastIndex = 0
        var imageIndex = 0

        imageRegex.findAll(text).forEach { match ->

            // --- ТЕКСТ ДО КАРТИНКИ ---
            val before = text.substring(lastIndex, match.range.first)
            addTextBlocks(before, result)

            // --- КАРТИНКА ---
            val name = match.groupValues[1].trim()
            val captionFromText = match.groupValues.getOrNull(2)?.trim()
            val captionFromList = captions.getOrNull(imageIndex)

            val caption = when {
                !captionFromText.isNullOrBlank() -> captionFromText
                !captionFromList.isNullOrBlank() -> captionFromList
                else -> null
            }

            result += ArticleBlock.Image(name, caption)

            imageIndex++
            lastIndex = match.range.last + 1
        }

        // --- ХВОСТОВОЙ ТЕКСТ ---
        val tail = text.substring(lastIndex)
        addTextBlocks(tail, result)

        return result
    }

    private fun addTextBlocks(
        raw: String,
        result: MutableList<ArticleBlock>
    ) {
        raw
            .split("\n\n") // 🔥 ключевой момент
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { paragraph ->
                result += ArticleBlock.Text(paragraph)
            }
    }
}