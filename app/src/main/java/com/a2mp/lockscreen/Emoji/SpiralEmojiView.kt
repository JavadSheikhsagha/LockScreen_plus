package com.a2mp.lockscreen.Emoji

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import java.lang.Float

class SpiralEmojiView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val spiralPath = Path()
    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var emojiList = mutableListOf<String>("\uD83D\uDE00", "\uD83D\uDE03")

    init {
        emojiPaint.textSize = resources.getDimension(R.dimen.emoji_size1)
        emojiPaint.color = Color.BLACK
        emojiPaint.textAlign = Paint.Align.CENTER
        emojiPaint.setTypeface(ResourcesCompat.getFont(context ,R.font.emoji))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        emojiList = MainLockScreenData.getEmojiList(context)!!

        val centerX = width / 2f
        val centerY = height / 2f

        val maxRadius = Float.min(centerX, centerY)

        spiralPath.reset()

        var angle = 10.0
        var radius = 10.0
        var x: Double
        var y: Double
        var counter = 1
        while (counter <= 200) {

            emojiPaint.textSize = resources.getDimension(R.dimen.emoji_size1) + counter

            counter++
            angle += 10
            radius += angle / counter / 3

            x = centerX + radius * Math.cos(angle) * 2
            y = centerY + radius * Math.sin(angle) * 2
            if (radius < maxRadius) {
                spiralPath.addCircle(x.toFloat(), y.toFloat(), 0f, Path.Direction.CW)

                val emoji = emojiList[(radius * emojiList.size / maxRadius).toInt()]
                canvas?.drawText(emoji, x.toFloat(), y.toFloat(), emojiPaint)
            }
        }

        canvas?.drawPath(spiralPath, emojiPaint)
    }


    fun reloadData() {
        emojiList = MainLockScreenData.getEmojiList(context)!!
        this.invalidate()
    }

}