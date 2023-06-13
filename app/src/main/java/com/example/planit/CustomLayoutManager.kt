package com.example.planit

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class CustomLayoutManager(context: Context, size: Int): GridLayoutManager(context, size)
{
    override fun supportsPredictiveItemAnimations(): Boolean
    {
        return false
    }
}