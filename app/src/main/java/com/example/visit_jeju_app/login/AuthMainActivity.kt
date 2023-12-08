package com.example.visit_jeju_app.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.visit_jeju_app.R
import com.example.visit_jeju_app.databinding.ActivityAuthMainBinding

class AuthMainActivity : AppCompatActivity() {
    private val SignUpMainFragment = 1
    private val LoginFragment = 2

    lateinit var binding: ActivityAuthMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 fragment 이동
        binding.signInBtn.setOnClickListener {
            fragmentView(LoginFragment)
        }

        //회원가입 fragment 이동
        binding.signUpBtn.setOnClickListener {
            fragmentView(SignUpMainFragment)
        }

        // 기본적으로 Fragment_1을 보여주도록 설정
        //fragmentView(Fragment_1)
    }

    private fun fragmentView(fragment: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        when (fragment) {
            1 -> {
                // 첫번 째 프래그먼트 호출
                val fragment1 = SignUpMainFragment()
                transaction.replace(R.id.fragment_container, fragment1)
                transaction.commit()
            }
            2 -> {
                // 두번 째 프래그먼트 호출
                val fragment2 = LoginFragment()
                transaction.replace(R.id.fragment_container, fragment2)
                transaction.commit()
            }
        }
        transaction.commit()
    }
}

