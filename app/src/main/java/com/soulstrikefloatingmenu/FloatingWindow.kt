package com.soulstrikefloatingmenu

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class FloatingWindow(private val context: Context) {
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var isExpanded = false

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
            setPadding(16, 16, 16, 16)
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

        // Title
        val title = TextView(context).apply {
            text = "Soul Strike Floating Menu"
            textSize = 16f
            setTextColor(0xFFFFFFFF.toInt())
        }

        // Menu buttons
        val button1 = Button(context).apply {
            text = "Feature 1"
            setOnClickListener { 
                Toast.makeText(context, "Feature 1 clicked!", Toast.LENGTH_SHORT).show()
            }
        }

        val button2 = Button(context).apply {
            text = "Feature 2"
            setOnClickListener { 
                Toast.makeText(context, "Feature 2 clicked!", Toast.LENGTH_SHORT).show()
            }
        }

        val button3 = Button(context).apply {
            text = "Feature 3"
            setOnClickListener { 
                Toast.makeText(context, "Feature 3 clicked!", Toast.LENGTH_SHORT).show()
            }
        }

        val closeButton = Button(context).apply {
            text = "Close"
            setOnClickListener { collapseMenu() }
        }

        val hideButton = Button(context).apply {
            text = "Hide Menu"
            setOnClickListener { hide() }
        }

        expandedView.addView(title)
        expandedView.addView(button1)
        expandedView.addView(button2)
        expandedView.addView(button3)
        expandedView.addView(closeButton)
        expandedView.addView(hideButton)

        layout.addView(collapsedView)
        layout.addView(expandedView)

        // Store references for later use
        layout.tag = mapOf(
            "collapsed" to collapsedView,
            "expanded" to expandedView
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

    fun hide() {
        try {
            windowManager?.removeView(floatingView)
            floatingView = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
