package com.soulstrikefloatingmenu

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*

class SimpleFloatingMenu(private val context: Context) {
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var isExpanded = false
    
    // Menu state variables
    private var godMode = false
    private var infiniteAmmo = false
    private var autoAim = false
    private var showFPS = true
    private var playerSpeed = 1.0f
    private var menuAlpha = 0.8f

    fun show() {
        try {
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            
            val inflater = LayoutInflater.from(context)
            floatingView = createFloatingView(inflater)
            
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            
            params.gravity = Gravity.TOP or Gravity.START
            params.x = 100
            params.y = 100
            
            windowManager?.addView(floatingView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createFloatingView(inflater: LayoutInflater): View {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0x80000000.toInt()) // Semi-transparent black
            setPadding(32, 32, 32, 32)
        }

        // Title
        val title = TextView(context).apply {
            text = "Soul Strike Menu"
            textSize = 18f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
        }

        // Collapsed view (just a button)
        val collapsedView = Button(context).apply {
            text = "Soul Strike Menu"
            setOnClickListener { expandMenu() }
        }

        // Expanded view
        val expandedView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            visibility = View.GONE
        }

        // God Mode checkbox
        val godModeCheck = CheckBox(context).apply {
            text = "God Mode"
            setOnCheckedChangeListener { _, isChecked ->
                godMode = isChecked
                Toast.makeText(context, "God Mode: ${if (isChecked) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
            }
        }

        // Infinite Ammo checkbox
        val infiniteAmmoCheck = CheckBox(context).apply {
            text = "Infinite Ammo"
            setOnCheckedChangeListener { _, isChecked ->
                infiniteAmmo = isChecked
                Toast.makeText(context, "Infinite Ammo: ${if (isChecked) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
            }
        }

        // Auto Aim checkbox
        val autoAimCheck = CheckBox(context).apply {
            text = "Auto Aim"
            setOnCheckedChangeListener { _, isChecked ->
                autoAim = isChecked
                Toast.makeText(context, "Auto Aim: ${if (isChecked) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
            }
        }

        // Show FPS checkbox
        val showFPSCheck = CheckBox(context).apply {
            text = "Show FPS"
            isChecked = showFPS
            setOnCheckedChangeListener { _, isChecked ->
                showFPS = isChecked
                Toast.makeText(context, "Show FPS: ${if (isChecked) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
            }
        }

        // Player Speed slider
        val speedLabel = TextView(context).apply {
            text = "Player Speed: $playerSpeed"
            setTextColor(0xFFFFFFFF.toInt())
        }

        val speedSeekBar = SeekBar(context).apply {
            max = 500 // 0.5x to 5.0x speed
            progress = 100 // 1.0x speed
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    playerSpeed = progress / 100.0f
                    speedLabel.text = "Player Speed: $playerSpeed"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        // Action buttons
        val applyButton = Button(context).apply {
            text = "Apply All Changes"
            setOnClickListener {
                applyAllChanges()
            }
        }

        val resetButton = Button(context).apply {
            text = "Reset All Settings"
            setOnClickListener {
                resetAllSettings()
            }
        }

        val closeButton = Button(context).apply {
            text = "Close Menu"
            setOnClickListener {
                collapseMenu()
            }
        }

        val hideButton = Button(context).apply {
            text = "Hide Menu"
            setOnClickListener {
                hide()
            }
        }

        // Add all views to expanded layout
        expandedView.addView(title)
        expandedView.addView(godModeCheck)
        expandedView.addView(infiniteAmmoCheck)
        expandedView.addView(autoAimCheck)
        expandedView.addView(showFPSCheck)
        expandedView.addView(speedLabel)
        expandedView.addView(speedSeekBar)
        expandedView.addView(applyButton)
        expandedView.addView(resetButton)
        expandedView.addView(closeButton)
        expandedView.addView(hideButton)

        // Add views to main layout
        layout.addView(collapsedView)
        layout.addView(expandedView)

        // Store references for later use
        layout.tag = mapOf(
            "collapsed" to collapsedView,
            "expanded" to expandedView,
            "godMode" to godModeCheck,
            "infiniteAmmo" to infiniteAmmoCheck,
            "autoAim" to autoAimCheck,
            "showFPS" to showFPSCheck,
            "speedLabel" to speedLabel,
            "speedSeekBar" to speedSeekBar
        )

        return layout
    }

    private fun expandMenu() {
        val layout = floatingView as? LinearLayout ?: return
        val views = layout.tag as? Map<String, View> ?: return
        
        val collapsedView = views["collapsed"] as? View
        val expandedView = views["expanded"] as? View
        
        collapsedView?.visibility = View.GONE
        expandedView?.visibility = View.VISIBLE
        isExpanded = true
    }

    private fun collapseMenu() {
        val layout = floatingView as? LinearLayout ?: return
        val views = layout.tag as? Map<String, View> ?: return
        
        val collapsedView = views["collapsed"] as? View
        val expandedView = views["expanded"] as? View
        
        collapsedView?.visibility = View.VISIBLE
        expandedView?.visibility = View.GONE
        isExpanded = false
    }

    private fun applyAllChanges() {
        // Apply all enabled features
        if (godMode) applyFeature("God Mode")
        if (infiniteAmmo) applyFeature("Infinite Ammo")
        if (autoAim) applyFeature("Auto Aim")
        if (showFPS) applyFeature("Show FPS")
        
        Toast.makeText(context, "All changes applied!", Toast.LENGTH_SHORT).show()
    }
    
    private fun resetAllSettings() {
        val layout = floatingView as? LinearLayout ?: return
        val views = layout.tag as? Map<String, View> ?: return
        
        // Reset all toggles
        (views["godMode"] as? CheckBox)?.isChecked = false
        (views["infiniteAmmo"] as? CheckBox)?.isChecked = false
        (views["autoAim"] as? CheckBox)?.isChecked = false
        (views["showFPS"] as? CheckBox)?.isChecked = true
        (views["speedSeekBar"] as? SeekBar)?.progress = 100
        
        godMode = false
        infiniteAmmo = false
        autoAim = false
        showFPS = true
        playerSpeed = 1.0f
        
        Toast.makeText(context, "All settings reset!", Toast.LENGTH_SHORT).show()
    }
    
    private fun applyFeature(featureName: String) {
        // Here you would implement the actual game modifications
        // This is where you'd hook into game functions and apply changes
        when (featureName) {
            "God Mode" -> {
                // Hook into health/damage functions
                // Set health to maximum or make damage 0
            }
            "Infinite Ammo" -> {
                // Hook into ammo consumption functions
                // Prevent ammo from decreasing
            }
            "Auto Aim" -> {
                // Hook into aiming functions
                // Automatically target nearest enemy
            }
            "Show FPS" -> {
                // Hook into rendering functions
                // Display FPS counter
            }
        }
    }

    fun hide() {
        try {
            windowManager?.removeView(floatingView)
            floatingView = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
