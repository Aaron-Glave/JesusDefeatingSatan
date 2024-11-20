package com.example.jesusdefeatingsatan

import android.content.Context
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton

class ClickableScreen(context: Context): AppCompatButton(context) {
    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
    }
}