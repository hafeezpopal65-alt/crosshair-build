package com.shivam.crosshairstudio

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // ── Crosshair state ──
    private var stateA1 = 0
    private var stateA2 = 90
    private var stateA3 = 180
    private var stateA4 = 270
    private var stateRadius = 10
    private var stateLength = 20
    private var stateThickness = 3
    private var activeColor = Color.parseColor("#00E5FF")

    private lateinit var crosshairView: CrosshairView
    private lateinit var liveCode: TextView
    private lateinit var panelModify: View
    private lateinit var panelPresets: RecyclerView
    private lateinit var tabModify: TextView
    private lateinit var tabPresets: TextView
    private lateinit var colorBar: LinearLayout

    // Slider refs for updating from presets
    private val seekBars = mutableMapOf<String, SeekBar>()
    private val valueLabels = mutableMapOf<String, TextView>()

    data class SliderDef(val label: String, val key: String, val max: Int)

    private val sliderDefs = listOf(
        SliderDef("ANGLE 1",   "a1",        360),
        SliderDef("ANGLE 2",   "a2",        360),
        SliderDef("ANGLE 3",   "a3",        360),
        SliderDef("ANGLE 4",   "a4",        360),
        SliderDef("GAP",       "radius",    250),
        SliderDef("LENGTH",    "length",    250),
        SliderDef("THICKNESS", "thickness", 250),
    )

    private val colors = listOf(
        "#00E5FF" to "CYAN",
        "#39FF14" to "GREEN",
        "#F0C040" to "GOLD",
        "#FF3A5C" to "RED",
        "#FFFFFF" to "WHITE",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        crosshairView = findViewById(R.id.crosshairView)
        liveCode      = findViewById(R.id.liveCode)
        panelModify   = findViewById(R.id.panelModify)
        panelPresets  = findViewById(R.id.panelPresets)
        tabModify     = findViewById(R.id.tabModify)
        tabPresets    = findViewById(R.id.tabPresets)
        colorBar      = findViewById(R.id.colorBar)

        buildSliders()
        buildColors()
        buildPresets()
        updateCodeDisplay()

        // Tabs
        tabModify.setOnClickListener { switchTab(true) }
        tabPresets.setOnClickListener { switchTab(false) }

        // Telegram
        findViewById<View>(R.id.btnTelegram).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/ShivamMaurya09")))
        }
    }

    // ────────────────────────── SLIDERS ──────────────────────────
    private fun buildSliders() {
        val container = findViewById<LinearLayout>(R.id.sliderContainer)
        val inflater = LayoutInflater.from(this)

        for (def in sliderDefs) {
            val itemView = inflater.inflate(R.layout.item_slider, container, false)
            val labelTv = itemView.findViewById<TextView>(R.id.sliderLabel)
            val valueTv = itemView.findViewById<TextView>(R.id.sliderValue)
            val seekBar = itemView.findViewById<SeekBar>(R.id.seekBar)

            labelTv.text = def.label
            val initVal = getStateVal(def.key)
            seekBar.max = def.max
            seekBar.progress = initVal
            valueTv.text = initVal.toString()

            seekBars[def.key] = seekBar
            valueLabels[def.key] = valueTv

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        setStateVal(def.key, progress)
                        valueTv.text = progress.toString()
                        refreshCrosshair()
                    }
                }
                override fun onStartTrackingTouch(sb: SeekBar) {}
                override fun onStopTrackingTouch(sb: SeekBar) {}
            })

            container.addView(itemView)
        }
    }

    // ────────────────────────── COLORS ──────────────────────────
    private var colorDots = mutableListOf<View>()

    private fun buildColors() {
        val dp32 = (32 * resources.displayMetrics.density).toInt()
        val dp6  = (6  * resources.displayMetrics.density).toInt()
        val dp8  = (8  * resources.displayMetrics.density).toInt()

        for ((i, pair) in colors.withIndex()) {
            val (hex, _) = pair
            val dot = View(this)
            val params = LinearLayout.LayoutParams(dp32, dp32)
            params.marginEnd = dp6
            dot.layoutParams = params
            dot.setBackgroundColor(Color.parseColor(hex))

            // Rounded corners via background
            val bg = android.graphics.drawable.GradientDrawable()
            bg.shape = android.graphics.drawable.GradientDrawable.RECTANGLE
            bg.cornerRadius = dp6.toFloat()
            bg.setColor(Color.parseColor(hex))
            dot.background = bg

            dot.setOnClickListener {
                activeColor = Color.parseColor(hex)
                colorDots.forEachIndexed { idx, v ->
                    val d = v.background as android.graphics.drawable.GradientDrawable
                    d.setStroke(if (idx == i) dp6/2 else 0, Color.WHITE)
                }
                refreshCrosshair()
            }

            colorBar.addView(dot)
            colorDots.add(dot)
        }
        // Default select first
        val firstBg = colorDots[0].background as android.graphics.drawable.GradientDrawable
        firstBg.setStroke((3 * resources.displayMetrics.density).toInt(), Color.WHITE)
    }

    // ────────────────────────── PRESETS ──────────────────────────
    private fun buildPresets() {
        val adapter = PresetAdapter(CrosshairDatabase.presets) { preset ->
            applyPreset(preset)
        }
        panelPresets.layoutManager = LinearLayoutManager(this)
        panelPresets.adapter = adapter
    }

    private fun applyPreset(p: CrosshairPreset) {
        stateA1        = p.a1
        stateA2        = p.a2
        stateA3        = p.a3
        stateA4        = p.a4
        stateRadius    = p.radius
        stateLength    = p.length
        stateThickness = p.thickness

        // Update all seekbars
        for (def in sliderDefs) {
            val v = getStateVal(def.key)
            seekBars[def.key]?.progress = v
            valueLabels[def.key]?.text  = v.toString()
        }
        refreshCrosshair()
    }

    // ────────────────────────── TABS ──────────────────────────
    private fun switchTab(modifyActive: Boolean) {
        if (modifyActive) {
            panelModify.visibility = View.VISIBLE
            panelPresets.visibility = View.GONE
            tabModify.setBackgroundResource(R.drawable.tab_selected_bg)
            tabModify.setTextColor(Color.parseColor("#E8EAF6"))
            tabPresets.setBackgroundColor(Color.TRANSPARENT)
            tabPresets.setTextColor(Color.parseColor("#5C6382"))
        } else {
            panelModify.visibility = View.GONE
            panelPresets.visibility = View.VISIBLE
            tabPresets.setBackgroundResource(R.drawable.tab_selected_bg)
            tabPresets.setTextColor(Color.parseColor("#E8EAF6"))
            tabModify.setBackgroundColor(Color.TRANSPARENT)
            tabModify.setTextColor(Color.parseColor("#5C6382"))
        }
    }

    // ────────────────────────── HELPERS ──────────────────────────
    private fun getStateVal(key: String) = when (key) {
        "a1"        -> stateA1
        "a2"        -> stateA2
        "a3"        -> stateA3
        "a4"        -> stateA4
        "radius"    -> stateRadius
        "length"    -> stateLength
        "thickness" -> stateThickness
        else        -> 0
    }

    private fun setStateVal(key: String, v: Int) = when (key) {
        "a1"        -> stateA1        = v
        "a2"        -> stateA2        = v
        "a3"        -> stateA3        = v
        "a4"        -> stateA4        = v
        "radius"    -> stateRadius    = v
        "length"    -> stateLength    = v
        "thickness" -> stateThickness = v
        else        -> Unit
    }

    private fun refreshCrosshair() {
        crosshairView.a1 = stateA1
        crosshairView.a2 = stateA2
        crosshairView.a3 = stateA3
        crosshairView.a4 = stateA4
        crosshairView.radius = stateRadius
        crosshairView.length = stateLength
        crosshairView.thickness = stateThickness
        crosshairView.activeColor = activeColor
        crosshairView.updateAndRedraw()
        updateCodeDisplay()
    }

    private fun updateCodeDisplay() {
        liveCode.text = "[ $stateA1 · $stateA2 · $stateA3 · $stateA4 ]"
    }
}
