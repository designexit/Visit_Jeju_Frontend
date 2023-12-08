package com.example.visit_jeju_app.login

import android.R
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.visit_jeju_app.R
import com.example.visit_jeju_app.databinding.ActivityTestFragmentBinding


class TestFragmentActivity : AppCompatActivity() {

    lateinit var binding : ActivityTestFragmentBinding

    private val Fragment_1 = 1
    private val Fragment_2 = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        findViewById<View>(com.example.visit_jeju_app.R.id.btn1).setOnClickListener(() {
            fun onClick(view: View?) {
                FragmentView(Fragment_1)
            }
        })
        findViewById<View>(com.example.visit_jeju_app.R.id.btn2).setOnClickListener(() {
            fun onClick(view: View?) {
                FragmentView(Fragment_2)
            }
        })
        FragmentView(Fragment_1)
    }

    private fun FragmentView(fragment: Int) {

        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        when (fragment) {
            1 -> {
                // 첫번 째 프래그먼트 호출
                val fragment1 = Fragment1()
                transaction.replace(com.example.visit_jeju_app.R.id.fragment_container, fragment1)
                transaction.commit()
            }

            2 -> {
                // 두번 째 프래그먼트 호출
                val fragment2 = Fragment2()
                transaction.replace(com.example.visit_jeju_app.R.id.fragment_container, fragment2)
                transaction.commit()
            }
        }
    }
}
