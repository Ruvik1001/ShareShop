package com.grishina.shareshop.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.grishina.data.share.ShareRepositoryImpl
import com.grishina.domain.data.ProductList
import com.grishina.shareshop.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}