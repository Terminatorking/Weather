package ghazimoradi.soheil.weather.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ghazimoradi.soheil.weather.R.string.checkYourNetwork
import ghazimoradi.soheil.weather.viewmodels.CheckInternetViewModel
import ghazimoradi.soheil.weather.R.style.RemoveDialogBackground
import ghazimoradi.soheil.weather.R.color.backShadow
import ghazimoradi.soheil.weather.utils.other.doWorkOnLifecycle
import ghazimoradi.soheil.weather.utils.other.showToast
import kotlin.getValue

abstract class BaseBottomSheetFragment<T : ViewBinding> : BottomSheetDialogFragment() {
    //Binding
    protected abstract val bindingInflater: (inflater: LayoutInflater) -> T
    private var _binding: T? = null
    protected val binding: T get() = requireNotNull(_binding)

    private val internetViewModel: CheckInternetViewModel by viewModels()

    protected var isNetworkAvailable = true

    override fun getTheme(): Int = RemoveDialogBackground

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doWorkOnLifecycle {
            internetViewModel.isNetworkAvailable.collect {
                isNetworkAvailable = it
                if (!it) {
                    requireContext().showToast(checkYourNetwork)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawableResource(backShadow)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}