package ghazimoradi.soheil.weather.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}