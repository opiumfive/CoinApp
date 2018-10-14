package com.opiumfive.coinapp.ui.feature.main.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.ui.base.BaseFragment

class TestFragment : BaseFragment() {

    private var ida: String = ""

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    companion object {
        fun newInstance(id: String): TestFragment {
            return TestFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ida = arguments?.getString("id") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply { text = ida }
    }
}