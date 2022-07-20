package com.example.places

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeGesture(context: Context) : ItemTouchHelper.Callback() {
    private var saveIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_save_24)!!
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#0371C9"))

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlag = ItemTouchHelper.LEFT
        return makeMovementFlags(0, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val iconMargin = (itemView.height - saveIcon.intrinsicHeight) / 2

        if (dX > 0) {
            swipeBackground.setBounds(
                itemView.left,
                itemView.top,
                dX.toInt(),
                itemView.bottom
            )
            saveIcon.setBounds(
                itemView.left + iconMargin,
                itemView.top + iconMargin,
                itemView.left + iconMargin + saveIcon.intrinsicWidth,
                itemView.bottom - iconMargin
            )
        } else {
            swipeBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            saveIcon.setBounds(
                itemView.right - iconMargin - saveIcon.intrinsicWidth,
                itemView.top + iconMargin,
                itemView.right - iconMargin,
                itemView.bottom - iconMargin
            )
        }
        swipeBackground.draw(c)
        if (dX > 0) {
            c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
        } else {
            c.clipRect(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        }
        saveIcon.draw(c)
        c.restore()

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )

    }

}