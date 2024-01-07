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

class RingEmojiView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var emojiList = mutableListOf<String>("\uD83D\uDE00", "\uD83D\uDE03")

    init {
        emojiPaint.textSize = resources.getDimension(R.dimen.emoji_size5)
        emojiPaint.color = Color.BLACK
        emojiPaint.textAlign = Paint.Align.CENTER
        emojiPaint.setTypeface(ResourcesCompat.getFont(context ,R.font.emoji))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        emojiList = MainLockScreenData.getEmojiList(context)!!

        val centerX = width / 2f
        val centerY = height / 2f

        val maxRadius = Math.min(centerX, centerY)

        val totalRings = 6
        val ringRadiusIncrement = (maxRadius - emojiPaint.textSize) / totalRings

        for (i in 0 until totalRings) {
            val currentRadius = maxRadius - (i + 1) * ringRadiusIncrement
            val emojiCount = (2 * Math.PI * currentRadius * 1.1 / emojiPaint.textSize).toInt()

            val angleIncrement = 2 * Math.PI / emojiCount
            var currentAngle = 0.0

            for (j in 0 until emojiCount) {
                val x = centerX + (currentRadius * Math.cos(currentAngle) * 2.4).toFloat()
                val y = centerY + (currentRadius * Math.sin(currentAngle) * 2.4).toFloat()

                val emoji = emojiList[i % emojiList.size]
                canvas?.drawText(emoji, x, y, emojiPaint)

                currentAngle += angleIncrement
            }
        }

        // Draw the central emoji
        val centralX = centerX
        val centralY = centerY
        val centralEmoji = emojiList[0]
        canvas?.drawText(centralEmoji, centralX, centralY, emojiPaint)
    }


    fun reloadData() {
        emojiList = MainLockScreenData.getEmojiList(context)!!
        this.invalidate()
    }
}