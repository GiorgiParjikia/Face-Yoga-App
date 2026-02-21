package ru.netology.faceyoga.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.netology.faceyoga.R
import java.util.Locale

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Insets: опускаем контент ниже статус-бара + чуть воздуха
        val root = view.findViewById<View>(R.id.settingsRoot)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val statusBarsTop = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val extraTop = (12 * v.resources.displayMetrics.density).toInt()
            v.updatePadding(top = statusBarsTop + extraTop)
            insets
        }

        val cardLanguage = view.findViewById<View>(R.id.cardLanguage)
        val tvLanguageValue = view.findViewById<TextView>(R.id.tvLanguageValue)
        val btnEmail = view.findViewById<MaterialButton>(R.id.btnEmail)

        fun renderLang() {
            val selected = LanguageManager.getSelectedLang(requireContext())

            if (selected == "en") {
                tvLanguageValue.text = getString(R.string.settings_language_english)
                return
            }
            if (selected == "ru") {
                tvLanguageValue.text = getString(R.string.settings_language_russian)
                return
            }
            if (selected == "ka") {
                tvLanguageValue.text = getString(R.string.settings_language_georgian)
                return
            }

            // system -> показываем язык телефона (как одно из трёх)
            val sys = resources.configuration.locales[0]?.language
                ?.lowercase(Locale.US)
                ?: "en"

            tvLanguageValue.text = when (sys) {
                "ru" -> getString(R.string.settings_language_russian)
                "ka", "geo" -> getString(R.string.settings_language_georgian)
                else -> getString(R.string.settings_language_english)
            }
        }

        renderLang()

        // Только 3 языка (без "System" в списке)
        cardLanguage.setOnClickListener {
            val labels = arrayOf(
                getString(R.string.settings_language_english),
                getString(R.string.settings_language_russian),
                getString(R.string.settings_language_georgian)
            )
            val values = arrayOf("en", "ru", "ka")

            val current = LanguageManager.getSelectedLang(requireContext())
            val checked = values.indexOf(current).let { if (it >= 0) it else 0 } // system -> English

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.settings_language_title)
                .setSingleChoiceItems(labels, checked) { dialog, which ->
                    LanguageManager.setSelectedLang(requireContext(), values[which])
                    dialog.dismiss()
                    renderLang()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        btnEmail.setOnClickListener {
            val email = "parjikia5@gmail.com"
            val subject = "Giorgi Parjikia"
            val body = buildString {
                appendLine(getString(R.string.settings_email_body_hint))
                appendLine()
                appendLine("---")
                appendLine("Android: ${android.os.Build.VERSION.RELEASE}")
                appendLine("Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
            }

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }

            startActivity(Intent.createChooser(intent, getString(R.string.settings_email_chooser)))
        }
    }
}