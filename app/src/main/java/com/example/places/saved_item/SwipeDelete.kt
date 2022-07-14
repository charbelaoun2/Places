package com.example.places.saved_item

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.places.R

abstract class SwipeDelete(context : Context) : ItemTouchHelper.Callback() {
    private var deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)!!
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FD1100"))

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlag = ItemTouchHelper.RIGHT
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
        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

        if (dX > 0) {
            swipeBackground.setBounds(
                itemView.left,
                itemView.top,
                dX.toInt(),
                itemView.bottom
            )
            deleteIcon.setBounds(
                itemView.left + iconMargin,
                itemView.top + iconMargin,
                itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                itemView.bottom - iconMargin
            )
        } else {
            swipeBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            deleteIcon.setBounds(
                itemView.right - iconMargin - deleteIcon.intrinsicWidth,
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
        deleteIcon.draw(c)
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