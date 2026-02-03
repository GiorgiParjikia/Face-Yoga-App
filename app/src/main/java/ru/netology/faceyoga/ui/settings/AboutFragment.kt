package ru.netology.faceyoga.ui.settings

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.faceyoga.R

class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ─────────────────────────────
        // Insets — чтобы Toolbar не залезал под статус-бар
        // ─────────────────────────────
        val root = view.findViewById<View>(R.id.root)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, bars.top, 0, 0)
            insets
        }

        // ─────────────────────────────
        // Toolbar: кнопка "Назад"
        // ─────────────────────────────
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // ─────────────────────────────
        // Версия приложения
        // ─────────────────────────────
        val versionText = view.findViewById<TextView>(R.id.about_version)

        val pInfo = requireContext()
            .packageManager
            .getPackageInfo(requireContext().packageName, 0)

        val versionName = pInfo.versionName

        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            pInfo.versionCode.toLong()
        }

        versionText.text = getString(
            R.string.about_version,
            versionName,
            versionCode
        )
    }
}
