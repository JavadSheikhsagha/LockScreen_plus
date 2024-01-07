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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.min

class LargeEmojiView (context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var emojiList = mutableListOf<String>("\uD83D\uDE00", "\uD83D\uDE03")

    private val numColumns = 2
    private val numRows = ceil(53.toDouble() / numColumns / 1.6).toInt()

    init {
        emojiPaint.textSize = resources.getDimension(R.dimen.emoji_size2)
        emojiPaint.color = Color.BLACK
        emojiPaint.textAlign = Paint.Align.CENTER
        emojiPaint.setTypeface(ResourcesCompat.getFont(context ,R.font.emoji))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        emojiList = MainLockScreenData.getEmojiList(context)!!

        val cellWidth = width.toFloat() / numColumns
        val cellHeight = height.toFloat() / numRows

        val emojiSize = min(cellWidth, cellHeight) * 1.38f
        emojiPaint.textSize = emojiSize

        var emojiIndex = 0
        for (row in 0 until numRows) {

            val numColumnsForRow = when {
                row % 2 == 0 -> 3 // Top row with three emojis
                else -> 2 // Middle rows with two emojis
            }

            val rowWidth = numColumnsForRow * cellWidth
            val rowOffset = (width - rowWidth) / 2 // Offset to center the row

            for (col in 0 until numColumnsForRow) {
                if (emojiIndex < 54) {
                    val emoji = emojiList[emojiIndex % emojiList.size]

                    val centerX = rowOffset + (col + 0.5f) * cellWidth
                    val centerY = (row + 0.5f) * cellHeight

                    canvas?.drawText(emoji, centerX, centerY, emojiPaint)
                }
                emojiIndex++
            }
        }
    }


    fun reloadData() {
        emojiList = MainLockScreenData.getEmojiList(context)!!
        this.invalidate()
    }
}