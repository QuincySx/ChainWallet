package com.smallraw.chain.wallet.feature.wallets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smallraw.chain.wallet.databinding.FragmentSelectWalletBinding

class WalletSelectListFragment : Fragment() {
    private lateinit var mBinding: FragmentSelectWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSelectWalletBinding.inflate(inflater, container, false)
            .also { mBinding = it }.root
    }
}