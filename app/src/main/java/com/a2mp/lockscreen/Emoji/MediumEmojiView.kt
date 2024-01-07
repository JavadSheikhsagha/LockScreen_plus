package com.a2mp.lockscreen.Emoji

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R

class MediumEmojiView (context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var emojiList = mutableListOf<String>("\uD83D\uDE00", "\uD83D\uDE03")

    private val numRows = 12
    private val numColumns = 5

    init {
        emojiPaint.textSize = resources.getDimension(R.dimen.emoji_size2)
        emojiPaint.color = Color.BLACK
        emojiPaint.textAlign = Paint.Align.CENTER
        emojiPaint.setTypeface(ResourcesCompat.getFont(context , R.font.emoji))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        emojiList = MainLockScreenData.getEmojiList(context)!!


        val cellWidth = width.toFloat() / numColumns
        val cellHeight = height.toFloat() / numRows

        val emojiSize = Math.min(cellWidth, cellHeight) * 0.8f
        emojiPaint.textSize = emojiSize

        var emojiIndex = 0
        for (row in 0 until numRows) {
            for (col in 0 until numColumns) {
                if (emojiIndex < emojiList.size) {
                    val emoji = emojiList[emojiIndex]

                    val centerX = (col + 0.5f) * cellWidth
                    val centerY = (row + 0.5f) * cellHeight

                    canvas?.drawText(emoji, centerX, centerY, emojiPaint)
                }
                emojiIndex = (emojiIndex + 1) % emojiList.size
            }
        }
    }


    fun reloadData() {
        emojiList = MainLockScreenData.getEmojiList(context)!!
        this.invalidate()
    }
}