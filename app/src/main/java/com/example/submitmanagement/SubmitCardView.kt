package com.example.submitmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import java.security.SecureRandom
import kotlin.random.Random

@SuppressLint("InflateParams", "ViewConstructor")
class SubmitCardView(context: Context, attrs: AttributeSet?, _submitData : Submit): CardView(context, attrs), SubmitDBInterface {
    private var submitData: Submit = _submitData
    override var submitName: String = submitData.submitName
    override var submitContents: String = submitData.submitContents
    override var submitTerm: String = submitData.submitTerm
    override var remarks: String = submitData.remarks
    override var bitmap: ByteArray? = submitData.bitmap
    private var termDay : String = submitData.getYMD()
    private var termHour : String = submitData.getHM()

    companion object{
        const val MARGIN = 12
        const val SUBMIT_NAME_SIZE = 24f
        const val HEADLINE_SIZE = 16f
        const val FOOTER_HEIGHT = 128
        const val SPACE_MARGIN = 16
    }

    private val allLinear = LinearLayout(context).also{
        it.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        it.orientation = LinearLayout.VERTICAL
    }

    private val cardView = CardView(context).also{
        it.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        it.radius = 32f
    }

    private val headerLinear = LinearLayout(context).also{
        it.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        it.orientation = LinearLayout.VERTICAL
    }

    private val contentsTermLinear = LinearLayout(context).also{
        it.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        it.orientation = LinearLayout.HORIZONTAL
    }

    private val contentsLinear = LinearLayout(context).also{
        it.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f
        )
        it.orientation = LinearLayout.VERTICAL
    }

    private val termLinear = LinearLayout(context).also{
        it.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f
        )
        it.orientation = LinearLayout.VERTICAL
    }

    private val remarksLinear = LinearLayout(context).also{
        it.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        it.orientation = LinearLayout.VERTICAL
    }

    private val footerLinear = LinearLayout(context).also{
        it.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f
        )
        it.orientation = LinearLayout.HORIZONTAL
    }

    private val textParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    @SuppressLint("ResourceAsColor")
    private val showSubmitNameInCard = TextView(context).also {
        it.textSize = SUBMIT_NAME_SIZE
        it.layoutParams = textParams
        it.gravity = Gravity.CENTER
        it.text = submitName
        it.setTextColor(Color.WHITE)
    }

    private val textSubmitContentsInCard = TextView(context).also{
        it.textSize = HEADLINE_SIZE
        it.layoutParams = textParams
        it.gravity = Gravity.CENTER
        it.setText(R.string.submitContents)
    }

    private val showSubmitContentsInCard = TextView(context).also{
        it.layoutParams = textParams
        it.gravity = Gravity.CENTER
        it.text = submitContents
    }

    private val textSubmitTermInCard = TextView(context).also{
        it.textSize = HEADLINE_SIZE
        it.layoutParams = textParams
        it.gravity = Gravity.CENTER
        it.setText(R.string.submitTermAll)
    }

    private val showSubmitTermDayInCard = TextView(context).also{
        it.layoutParams = textParams
        it.gravity = Gravity.CENTER
        it.text = termDay
    }

    private val showSubmitTermHourInCard = TextView(context).also{
        it.layoutParams = textParams
        it.gravity = Gravity.CENTER
        it.text = termHour
    }

    private val textRemarksInCard = TextView(context).also{
        it.textSize = HEADLINE_SIZE
        it.layoutParams = textParams
        it.gravity = Gravity.CENTER
        it.setText(R.string.remarks)
    }

    private val showRemarksInCard = TextView(context).also{
        it.layoutParams = textParams
        it.gravity = Gravity.CENTER
        it.text = remarks
    }

    private val footerParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        FOOTER_HEIGHT,
        1f
    )

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    private val editButton = ImageButton(context).also{
        it.layoutParams = footerParams
        it.setImageResource(R.drawable.submit_ui_button_edit)
        it.setBackgroundColor(R.color.orange)
        it.scaleType = ImageView.ScaleType.FIT_CENTER

        it.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_BUTTON_PRESS -> {
                    true
                }
                MotionEvent.ACTION_DOWN -> {
                    it.alpha = 0.5f
                    true
                }
                MotionEvent.ACTION_UP ->
                {
                    it.alpha = 1f
                    false
                }
                else -> {
                    false
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val submitCompleteButton = ImageButton(context).also{
        it.layoutParams = footerParams
        it.setImageResource(R.drawable.submit_ui_button_submitcomplete)
        it.setBackgroundColor(Color.RED)
        it.scaleType = ImageView.ScaleType.FIT_CENTER

        it.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_BUTTON_PRESS -> {
                    true
                }
                MotionEvent.ACTION_DOWN -> {
                    it.alpha = 0.5f
                    true
                }
                MotionEvent.ACTION_UP ->
                {
                    it.alpha = 1f
                    false
                }
                else -> {
                    false
                }
            }
        }
    }

    private val spaceParams = ViewGroup.LayoutParams(
        SPACE_MARGIN, SPACE_MARGIN
    )

    private val spaceTop = Space(context).also{
        it.layoutParams = spaceParams
    }

    private val spaceBottom = Space(context).also{
        it.layoutParams = spaceParams
    }

    init{
        headerLinear.setBackgroundColor(randomColor())

        textParams.setMargins(MARGIN)
    }

    fun generateView(){
        headerLinear.removeAllViews()
        //headerLinear.addView(spaceTop)
        headerLinear.addView(showSubmitNameInCard)

        contentsLinear.removeAllViews()
        contentsLinear.addView(textSubmitContentsInCard)
        contentsLinear.addView(showSubmitContentsInCard)

        termLinear.removeAllViews()
        termLinear.addView(textSubmitTermInCard)
        termLinear.addView(showSubmitTermDayInCard)
        termLinear.addView(showSubmitTermHourInCard)

        remarksLinear.removeAllViews()
        remarksLinear.addView(textRemarksInCard)
        remarksLinear.addView(showRemarksInCard)

        footerLinear.removeAllViews()
        footerLinear.addView(editButton)
        footerLinear.addView(submitCompleteButton)

        contentsTermLinear.removeAllViews()
        contentsTermLinear.addView(contentsLinear)
        contentsTermLinear.addView(termLinear)

        allLinear.removeAllViews()
        allLinear.addView(headerLinear)
        allLinear.addView(contentsTermLinear)
        allLinear.addView(remarksLinear)
        allLinear.addView(spaceBottom)
        allLinear.addView(footerLinear)

        cardView.removeAllViews()
        cardView.addView(allLinear)

        addView(cardView)
    }

    private fun randomColor(): Int{
        val random = SecureRandom()
        val max = 200
        return Color.rgb(random.nextInt(max),random.nextInt(max),random.nextInt(max))
    }


}