package com.example.submitmanagement

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.submitmanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        openMainFragment()
    }

    private fun openMainFragment(){
        val tag = "mainFragment"
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if(fragment == null){
            fragment = MainFragment()
            supportFragmentManager.beginTransaction().apply{
                add(R.id.mainFragmentContainer, fragment, tag)
            }.commit()
        }
    }

    fun openSubmitResisterFragment(){
        val tag = "resisterSubmitFragment"
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if(fragment == null){
            fragment = ResisterSubmitFragment()
            supportFragmentManager.beginTransaction().apply{
                add(R.id.submitResisterFragmentContainer, fragment, tag)
            }.commit()
        }
    }

    @SuppressLint("CommitTransaction")
    fun showRealmRecordByCard(){
        val fragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as MainFragment
        fragment.showRealm()
    }
}