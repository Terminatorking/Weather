package ghazimoradi.soheil.weather.ui.fragments.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.R.id.actionToMain
import ghazimoradi.soheil.weather.R.string.go_to_settings
import ghazimoradi.soheil.weather.R.string.location_permission_needed
import ghazimoradi.soheil.weather.R.string.cancel
import ghazimoradi.soheil.weather.databinding.FragmentSplashBinding
import ghazimoradi.soheil.weather.utils.base.BaseFragment
import ghazimoradi.soheil.weather.utils.other.doWorkOnLifecycle
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
@Suppress("DEPRECATION")
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val requestPermissionCode = 1
    private var isNavigating = false // Prevent multiple navigations

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGotoSettings.setOnClickListener {
            goToSettings()
        }

        // Check if permission is already granted BEFORE requesting
        if (hasLocationPermission()) {
            navigateToMain()
        } else {
            requestPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // Only request if not already granted
        if (!hasLocationPermission()) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestPermissionCode
            )
        } else {
            navigateToMain()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            requestPermissionCode -> {
                // Check if permission was granted
                val isGranted = grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED

                if (isGranted) {
                    navigateToMain()
                } else {
                    // Check if we should show rationale or if "Don't ask again" was checked
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showDialog()
                        binding.permissionLay.isVisible = false
                    } else {
                        binding.permissionLay.isVisible = true
                    }
                }
            }
        }
    }

    private fun navigateToMain() {
        // Prevent multiple navigation calls
        if (isNavigating) return
        isNavigating = true

        doWorkOnLifecycle {
            delay(2000.milliseconds)
            findNavController().navigate(actionToMain)
        }
    }

    private fun showDialog() {

        if (isNavigating) return

        AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(getString(location_permission_needed))
            .setPositiveButton(getString(go_to_settings)) { _, _ ->
                goToSettings()
                activity?.finish()
            }
            .setNegativeButton(getString(cancel)) { _, _ ->
                activity?.finish()
            }
            .show()
    }

    private fun goToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }
}