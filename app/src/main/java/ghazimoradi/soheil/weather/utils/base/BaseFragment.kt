package ghazimoradi.soheil.weather.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import ghazimoradi.soheil.weather.R.string.checkYourNetwork
import ghazimoradi.soheil.weather.utils.other.doWorkOnLifecycle
import ghazimoradi.soheil.weather.utils.other.showToast
import ghazimoradi.soheil.weather.viewmodels.CheckInternetViewModel

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    //Binding
    protected abstract val bindingInflater: (inflater: LayoutInflater) -> T
    private var _binding: T? = null
    protected val binding: T get() = requireNotNull(_binding)

    private val internetViewModel: CheckInternetViewModel by viewModels()

    protected var isNetworkAvailable = true

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}