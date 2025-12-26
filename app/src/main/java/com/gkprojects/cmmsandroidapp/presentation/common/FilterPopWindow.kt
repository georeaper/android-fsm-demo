package com.gkprojects.cmmsandroidapp.presentation.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class filterPopWindow : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(layoutId: Int, onViewCreated: (View) -> Unit): filterPopWindow {
            val fragment = filterPopWindow()
            fragment.layoutId = layoutId
            fragment.onViewCreatedCallback = onViewCreated
            return fragment
        }
    }

    private var layoutId: Int = 0
    private lateinit var onViewCreatedCallback: (View) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FilterPopWindow", "onViewCreated called")
        onViewCreatedCallback(view)
    }
}