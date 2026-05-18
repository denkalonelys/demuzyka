package com.demuzyka.tv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.demuzyka.tv.ui.theme.DeMuzykaTvTheme

class MainTvActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeMuzykaTvTheme {
                DeMuzykaTvApp()
            }
        }
    }
}
