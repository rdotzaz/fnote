package rdotzaz.footnote.animations

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Math.hypot

class CircleAnim
{
    fun viewAnim(layout : ConstraintLayout) {
        val x = layout!!.width / 2
        val y = layout!!.height / 2

        val startRadius = 0;
        val endRadius = hypot(layout!!.width.toDouble(), layout!!.height.toDouble()).toInt()

        val anim : Animator = ViewAnimationUtils.createCircularReveal(
                layout,
                x,
                y,
                startRadius.toFloat(),
                endRadius.toFloat()
        )
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
            }
            override fun onAnimationEnd(animator: Animator) {
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        anim.duration = 800
        anim.start()


    }
}