package ghazimoradi.soheil.weather.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors.fromApplication
import ghazimoradi.soheil.weather.databinding.ActivityMainBinding
import ghazimoradi.soheil.weather.di.font.ViewPumpEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper.Companion.wrap

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun attachBaseContext(newBase: Context) {
        val viewPump = fromApplication(
            newBase.applicationContext,
            ViewPumpEntryPoint::class.java
        ).viewPump()

        super.attachBaseContext(wrap(newBase, viewPump))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}