package ru.netology.faceyoga

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.faceyoga.data.seed.DbSeeder
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var seeder: DbSeeder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHost.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        // ✅ Скрываем bottom bar во время тренировки + Congrats
        val hideBottomNavOn = setOf(
            R.id.countdownFragment,
            R.id.videoPlayerFragment,
            R.id.congratsFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.isVisible = destination.id !in hideBottomNavOn

            // ✅ ФИКС: корректная подсветка вкладки без навигации (не используем selectedItemId!)
            val tabId = destination.findBottomTabId()
            if (tabId != null) {
                val item = bottomNav.menu.findItem(tabId)
                if (item != null && !item.isChecked) {
                    item.isChecked = true
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            seeder.seedIfNeeded()
        }
    }

    /**
     * Возвращаем id пункта bottom nav (у тебя они совпадают с id фрагментов).
     * Весь workout flow считаем частью вкладки "Упражнения" (daysFragment).
     */
    private fun NavDestination.findBottomTabId(): Int? {
        return when (id) {
            // Вкладка "Упражнения"
            R.id.daysFragment,
            R.id.dayExercisesFragment,
            R.id.countdownFragment,
            R.id.videoPlayerFragment,
            R.id.congratsFragment -> R.id.daysFragment

            // Остальные вкладки
            R.id.articlesFragment -> R.id.articlesFragment
            R.id.progressFragment -> R.id.progressFragment
            R.id.settingsFragment -> R.id.settingsFragment

            else -> null
        }
    }
}