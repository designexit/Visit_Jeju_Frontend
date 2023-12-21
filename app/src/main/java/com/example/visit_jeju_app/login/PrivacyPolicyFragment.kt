package com.example.visit_jeju_app.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.visit_jeju_app.R


class PrivacyPolicyFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)

        // 닫기 버튼 클릭 이벤트 처리
        val closeButton = view?.findViewById<Button>(R.id.closeBtn)
        closeButton?.setOnClickListener {
            // 현재 플래그먼트를 닫음
            dismiss()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}