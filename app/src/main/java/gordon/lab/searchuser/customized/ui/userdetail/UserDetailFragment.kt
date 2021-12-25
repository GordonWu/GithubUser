package gordon.lab.searchuser.customized.ui.userdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import gordon.lab.searchuser.R
import gordon.lab.searchuser.customized.protocol.MainEvent
import gordon.lab.searchuser.databinding.FragmentUserDetailBinding
import gordon.lab.searchuser.viewmodel.UserDetailViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UserDetailFragment:Fragment() {

    private val viewModel: UserDetailViewModel by activityViewModels()
    private var binding: FragmentUserDetailBinding? = null
    private val args: UserDetailFragmentArgs by navArgs()

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

    private fun initUserLayout() {
        args.username.let {
            lifecycleScope.launch {
                viewModel.setEvent(MainEvent.FetchUserDetail(it))
            }
        }
    }

    private fun FragmentUserDetailBinding.initViewModelObserve() {
        lifecycleScope.launch {
            viewModel.viewState.collect {
                when(it.userDetailState){
                    is UserDetailState.Idle->{
                        //do nothing~
                    }
                    is UserDetailState.Loading->{
                        progressBar.isVisible = it.userDetailState.isLoading
                    }
                    is UserDetailState.Fetched->{
                        progressBar.isVisible = it.userDetailState.isLoading

                        val data = it.userDetailState.result

                        Glide.with(this@UserDetailFragment)
                            .load(data.avatarUrl)
                            .circleCrop()
                            .into(imgAvatar)

                        tvUserLogin.text = String.format(getString(R.string.str_detail_login_placeholder),data.login)
                        tvUserName.text = data.name
                        tvUserEmail.text = data.email
                        tvUserLocation.text = data.location?:getString(R.string.str_detail_location_unset)
                        tvUserCompany.text = data.company?:getString(R.string.str_detail_company_unset)
                        tvUserBio.text = data.bio?:getString(R.string.str_detail_bio_unset)
                        val url = data.blog
                            if(URLUtil.isValidUrl(url)){
                                linkWrapper.isVisible = true
                                tvUserLink.text = url
                                tvUserLink.setOnClickListener {
                                val intent = Intent()
                                intent.action = Intent.ACTION_VIEW
                                intent.data = Uri.parse(url)
                                startActivity(intent)
                                }
                            }
                    }
                    is UserDetailState.Error->{
                        progressBar.isVisible = it.userDetailState.isLoading
                        Toast.makeText(context,it.userDetailState.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}