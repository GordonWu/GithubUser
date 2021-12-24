package gordon.lab.searchuser.customized.ui.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import gordon.lab.searchuser.R
import gordon.lab.searchuser.data.model.UserDetail
import gordon.lab.searchuser.databinding.FragmentUserDetailBinding
import gordon.lab.searchuser.util.MainIntent
import gordon.lab.searchuser.util.MainState
import gordon.lab.searchuser.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserDetailFragment:Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var binding: FragmentUserDetailBinding? = null
    private val args: UserDetailFragmentArgs by navArgs()
    private var userDetail: UserDetail? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            initToolbar()
            initUserLayout()
            initViewModelObserve()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun FragmentUserDetailBinding.initToolbar() {
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun FragmentUserDetailBinding.initUserLayout() {
        args.username.let {
            lifecycleScope.launch {
                sharedViewModel.userIntent.send(MainIntent.FetchUserDetail(it))
            }
        }
    }

    private fun FragmentUserDetailBinding.initViewModelObserve() {
        lifecycleScope.launch {
            sharedViewModel.state.collect {
                when(it){
                    is MainState.Idle->{
                        //do nothing~
                    }
                    is MainState.Loading->{
                        progressBar.isVisible = it.isLoading
                    }
                    is MainState.DetailFetched->{
                        progressBar.isVisible = it.isLoading

                        Glide.with(this@UserDetailFragment)
                            .load(it.result.avatarUrl)
                            .circleCrop()
                            .into(imgAvatar)
                        tvUserLogin.text = String.format(getString(R.string.str_detail_login_placeholder),it.result.login)
                        tvUserName.text = it.result.name
                        tvUserEmail.text = it.result.email
                        tvUserLocation.text = it.result.location?:getString(R.string.str_detail_location_unset)
                        tvUserCompany.text = it.result.company?:getString(R.string.str_detail_company_unset)
                        tvUserBio.text = it.result.bio?:getString(R.string.str_detail_bio_unset)

                    }
                    is MainState.Error->{

                        progressBar.isVisible = it.isLoading
                    }
                }
            }
        }
    }
}