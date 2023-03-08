package com.smallraw.chain.wallet.feature.wallets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.smallraw.chain.wallet.databinding.FragmentSelectWalletBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletSelectListFragment : Fragment() {
    private lateinit var mBinding: FragmentSelectWalletBinding
    private val mViewModel by viewModels<WalletSelectListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSelectWalletBinding.inflate(inflater, container, false)
            .also {
                mBinding = it
                it.vm = mViewModel
            }.root
    }
}