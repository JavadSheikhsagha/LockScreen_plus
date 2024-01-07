package com.a2mp.lockscreen.LockScreen

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.log


class OnSwipeTouchListener(ctx: Context? , var onTopSwap : ()-> Unit , var onBottomSwap : ()-> Unit) : OnTouchListener {
    // Fields:
    /** Whether a swipe motion has been detected  */
    protected var isSwipeDetected = false
    private val gestureDetector: GestureDetector

    companion object {
        private const val HORIZONTAL_SWIPE_THRESHOLD = 0
        private const val HORIZONTAL_SWIPE_VELOCITY_THRESHOLD = 0
        private const val VERTICAL_SWIPE_THRESHOLD = 100
        private const val VERTICAL_SWIPE_VELOCITY_THRESHOLD = 100
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {    // More of a horizontal movement
                    if (Math.abs(diffX) > Companion.HORIZONTAL_SWIPE_THRESHOLD && Math.abs(velocityX) > Companion.HORIZONTAL_SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            isSwipeDetected = true
                            onSwipeRight()
                        } else {
                            isSwipeDetected = true
                            onSwipeLeft()
                        }
                    }
                    result = true
                } else {    // Vertical movement
                    if (Math.abs(diffY) > Companion.VERTICAL_SWIPE_THRESHOLD && Math.abs(velocityY) > Companion.VERTICAL_SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            isSwipeDetected = true
                            onSwipeBottom()
                            Log.i("TAG11111111111111123213", "onFling: aslfnsalknflsaf")
                        } else {
                            isSwipeDetected = true
                            onSwipeTop()
                            Log.i("TAG11111111111111123213", "onFling: aslfnsalknflsaf")
                        }
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }


    }

    fun onSwipeRight() {

    }
    fun onSwipeLeft() {

    }
    fun onSwipeTop() {
        onTopSwap.invoke()
    }
    fun onSwipeBottom() {
        onBottomSwap.invoke()
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!)
    }

    // Constructors:
    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }
}