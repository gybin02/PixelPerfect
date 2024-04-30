package com.example.pixelperfect.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Adapters.FontAdapter
import com.example.pixelperfect.Adapters.FontAdapter.ItemClickListener
import com.example.pixelperfect.Adapters.ShadowAdapter
import com.example.pixelperfect.Adapters.ShadowAdapter.ShadowItemClickListener
import com.example.pixelperfect.Adapters.TextBackgroundAdapter
import com.example.pixelperfect.Adapters.TextColorAdapter
import com.example.pixelperfect.Adapters.TextColorAdapter.ColorListener
import com.example.pixelperfect.Assets.FontFileAsset.listFonts
import com.example.pixelperfect.Assets.FontFileAsset.setFontByName
import com.example.pixelperfect.Editor.EditText
import com.example.pixelperfect.Editor.Text
import com.example.pixelperfect.R
import com.example.pixelperfect.Utils.PreferenceUtil
import com.example.pixelperfect.Utils.SystemUtil

class TextFragment : DialogFragment(), View.OnClickListener, ItemClickListener, ShadowItemClickListener, ColorListener, TextBackgroundAdapter.BackgroundColorListener {
    lateinit var linear_layout_edit_text_tools: ConstraintLayout
    var polishText: Text = Text.defaultProperties
    lateinit var seekbar_radius: SeekBar
    lateinit var seekbar_height: SeekBar
    lateinit var seekbar_background_opacity: SeekBar
    lateinit var seekbar_width: SeekBar
    lateinit var image_view_color: ImageView
    lateinit var textViewFont: TextView
    lateinit var textViewColor: TextView
    lateinit var textViewBg: TextView
    lateinit var textViewShadow: TextView
    lateinit var relativeLayoutBg: RelativeLayout
    lateinit var image_view_adjust: ImageView
    lateinit var scroll_view_change_font_adjust: ScrollView
    lateinit var scroll_view_change_font_layout: LinearLayout
    lateinit var image_view_align_left: ImageView
    lateinit var image_view_align_center: ImageView
    lateinit var image_view_align_right: ImageView
    lateinit var recycler_view_color: RecyclerView
    lateinit var recycler_view_background: RecyclerView
    private var fontAdapter: FontAdapter? = null
    private var shadowAdapter: ShadowAdapter? = null
    lateinit var linear_layout_preview: LinearLayout
    lateinit var recycler_view_fonts: RecyclerView
    lateinit var recycler_view_shadow: RecyclerView
    lateinit var add_text_edit_text: EditText
    private lateinit var inputMethodManager: InputMethodManager
    lateinit var text_view_preview_effect: TextView
    lateinit var textViewSeekBarSize: TextView
    lateinit var textViewSeekBarColor: TextView
    lateinit var textViewSeekBarBackground: TextView
    lateinit var textViewSeekBarRadius: TextView
    lateinit var textViewSeekBarWith: TextView
    lateinit var textViewSeekBarHeight: TextView
    lateinit var image_view_save_change: ImageView
    lateinit var image_view_keyboard: ImageView
    lateinit var checkbox_background: CheckBox
    private lateinit var textEditor: TextEditor
    private var textFunctions: List<ImageView>? = null
    lateinit var seekbar_text_size: SeekBar
    lateinit var textColorOpacity: SeekBar

    interface TextEditor {
        fun onBackButton()
        fun onDone(polishText: Text?)
    }

    override fun onItemClick(view: View?, i: Int) {
        setFontByName(requireContext(), text_view_preview_effect, listFonts[i])
        polishText.fontName = listFonts[i]
        polishText.fontIndex = i
    }

    override fun onShadowItemClick(view: View?, position: Int) {
        val textShadow = Text.textShadowList[position]
        text_view_preview_effect.setShadowLayer(textShadow.radius.toFloat(), textShadow.dx.toFloat(), textShadow.dy.toFloat(), textShadow.colorShadow)
        text_view_preview_effect.invalidate()
        polishText.textShadow = textShadow
        polishText.textShadowIndex = position
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
//        Dialog dialog = getDialog();
//        Window window = dialog.getWindow();
//        window.requestFeature(Window.FEATURE_NO_TITLE);
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return layoutInflater.inflate(R.layout.fragment_text, viewGroup, false)
    }

    fun dismissAndShowSticker() {
        textEditor.onBackButton()
        dismiss()
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        val context = requireContext()
        add_text_edit_text = view.findViewById(R.id.add_text_edit_text)
        image_view_keyboard = view.findViewById(R.id.image_view_keyboard)
        image_view_color = view.findViewById(R.id.image_view_color)
        image_view_align_left = view.findViewById(R.id.imageViewAlignLeft)
        image_view_align_center = view.findViewById(R.id.imageViewAlignCenter)
        image_view_align_right = view.findViewById(R.id.imageViewAlignRight)
        textViewFont = view.findViewById(R.id.textViewFont)
        textViewColor = view.findViewById(R.id.textViewColor)
        textViewBg = view.findViewById(R.id.textViewBg)
        textViewShadow = view.findViewById(R.id.textViewShadow)
        relativeLayoutBg = view.findViewById(R.id.relativeLayoutBg)
        textViewSeekBarSize = view.findViewById(R.id.seekbarSize)
        textViewSeekBarColor = view.findViewById(R.id.seekbarColor)
        textViewSeekBarBackground = view.findViewById(R.id.seekbarBackground)
        textViewSeekBarRadius = view.findViewById(R.id.seekbarRadius)
        textViewSeekBarWith = view.findViewById(R.id.seekbarWith)
        textViewSeekBarHeight = view.findViewById(R.id.seekbarHeight)
        image_view_adjust = view.findViewById(R.id.image_view_adjust)
        image_view_save_change = view.findViewById(R.id.image_view_save_change)
        scroll_view_change_font_layout = view.findViewById(R.id.scroll_view_change_font_layout)
        scroll_view_change_font_adjust = view.findViewById(R.id.scroll_view_change_color_adjust)
        linear_layout_edit_text_tools = view.findViewById(R.id.linear_layout_edit_text_tools)
        recycler_view_fonts = view.findViewById(R.id.recycler_view_fonts)
        recycler_view_shadow = view.findViewById(R.id.recycler_view_shadow)
        textColorOpacity = view.findViewById(R.id.seekbar_text_opacity)
        text_view_preview_effect = view.findViewById(R.id.text_view_preview_effect)
        linear_layout_preview = view.findViewById(R.id.linear_layout_preview)
        checkbox_background = view.findViewById(R.id.checkbox_background)
        seekbar_width = view.findViewById(R.id.seekbar_width)
        seekbar_height = view.findViewById(R.id.seekbar_height)
        seekbar_background_opacity = view.findViewById(R.id.seekbar_background_opacity)
        seekbar_text_size = view.findViewById(R.id.seekbar_text_size)
        seekbar_radius = view.findViewById(R.id.seekbar_radius)
        recycler_view_color = view.findViewById(R.id.recyclerViewColor)
        recycler_view_color.setLayoutManager(GridLayoutManager(context, 5))
        recycler_view_color.setAdapter(TextColorAdapter(context, this))
        recycler_view_background = view.findViewById(R.id.recyclerViewBg)
        recycler_view_background.setLayoutManager(GridLayoutManager(context, 5))
        recycler_view_background.setAdapter(TextBackgroundAdapter(context, this))
        add_text_edit_text.setTextFragment(this)
        initAddTextLayout()
        setDefaultStyleForEdittext()
        activity?.windowManager?.defaultDisplay?.getMetrics(DisplayMetrics())
        inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //打开输入框
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        recycler_view_fonts.layoutManager = GridLayoutManager(context, 5)
        fontAdapter = FontAdapter(context, listFonts)
        fontAdapter?.mClickListener = this
        recycler_view_fonts.adapter = fontAdapter
        recycler_view_shadow.layoutManager = GridLayoutManager(context, 5)
        shadowAdapter = ShadowAdapter(context, Text.textShadowList)
        shadowAdapter?.mClickListener = this
        recycler_view_shadow.adapter = shadowAdapter
        textColorOpacity.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val value = progress.toString()
                textViewSeekBarColor.text = value
                val i2 = 255 - progress
                polishText.textAlpha = i2
                text_view_preview_effect.setTextColor(Color.argb(i2, Color.red(polishText.textColor), Color.green(polishText.textColor), Color.blue(polishText.textColor)))
            }
        })
        add_text_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {}
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                text_view_preview_effect.text = charSequence.toString()
                polishText.text = charSequence.toString()
            }
        })
        checkbox_background.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                polishText.isShowBackground = false
                text_view_preview_effect.setBackgroundResource(0)
                text_view_preview_effect.setLayoutParams(LinearLayout.LayoutParams(-2, -2))
            } else if (checkbox_background.isPressed || polishText.isShowBackground) {
                polishText.isShowBackground = true
                initPreviewText()
            } else {
                checkbox_background.setChecked(false)
                polishText.isShowBackground = false
                initPreviewText()
            }
        }
        seekbar_width.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
                val value = i.toString()
                textViewSeekBarWith.setText(value)
                text_view_preview_effect.setPadding(SystemUtil.dpToPx(requireContext(), i), text_view_preview_effect.getPaddingTop(), SystemUtil.dpToPx(this@TextFragment.context, i), text_view_preview_effect.getPaddingBottom())
                polishText.paddingWidth = i
            }
        })
        seekbar_height.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
                val value = i.toString()
                textViewSeekBarHeight.setText(value)
                text_view_preview_effect.setPadding(text_view_preview_effect.getPaddingLeft(), SystemUtil.dpToPx(requireContext(), i), text_view_preview_effect.getPaddingRight(), SystemUtil.dpToPx(this@TextFragment.context, i))
                polishText.paddingHeight = i
            }
        })
        seekbar_background_opacity.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
                val value = i.toString()
                textViewSeekBarBackground.setText(value)
                polishText.backgroundAlpha = 255 - i
                val red = Color.red(polishText.backgroundColor)
                val green = Color.green(polishText.backgroundColor)
                val blue = Color.blue(polishText.backgroundColor)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.setColor(Color.argb(polishText.backgroundAlpha, red, green, blue))
                gradientDrawable.setCornerRadius(SystemUtil.dpToPx(requireContext(), polishText.backgroundBorder).toFloat())
                text_view_preview_effect.setBackground(gradientDrawable)
            }
        })
        seekbar_text_size.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
                val value = i.toString()
                textViewSeekBarSize.setText(value)
                var i2 = 15
                if (i >= 15) {
                    i2 = i
                }
                text_view_preview_effect.setTextSize(i2.toFloat())
                polishText.textSize = i2
            }
        })
        seekbar_radius.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
                val value = i.toString()
                textViewSeekBarRadius.setText(value)
                polishText.backgroundBorder = i
                val gradientDrawable = GradientDrawable()
                gradientDrawable.setCornerRadius(SystemUtil.dpToPx(requireContext(), i).toFloat())
                gradientDrawable.setColor(Color.argb(polishText.backgroundAlpha, Color.red(polishText.backgroundColor), Color.green(polishText.backgroundColor), Color.blue(polishText.backgroundColor)))
                text_view_preview_effect.setBackground(gradientDrawable)
            }
        })
        if (PreferenceUtil.getKeyboard(requireContext()) > 0) {
            updateAddTextBottomToolbarHeight(PreferenceUtil.getKeyboard(context))
        }
        initPreviewText()
    }

    @SuppressLint("WrongConstant")
    fun initPreviewText() {
        val context = requireContext()
        if (polishText.isShowBackground) {
            if (polishText.backgroundColor != 0) {
                text_view_preview_effect.setBackgroundColor(polishText.backgroundColor)
            }
            if (polishText.backgroundAlpha < 255) {
                text_view_preview_effect.setBackgroundColor(Color.argb(polishText.backgroundAlpha, Color.red(polishText.backgroundColor), Color.green(polishText.backgroundColor), Color.blue(polishText.backgroundColor)))
            }
            if (polishText.backgroundBorder > 0) {
                val gradientDrawable = GradientDrawable()
                gradientDrawable.setCornerRadius(SystemUtil.dpToPx(requireContext(), polishText.backgroundBorder).toFloat())
                gradientDrawable.setColor(Color.argb(polishText.backgroundAlpha, Color.red(polishText.backgroundColor), Color.green(polishText.backgroundColor), Color.blue(polishText.backgroundColor)))
                text_view_preview_effect.background = gradientDrawable
            }
        }
        if (polishText.paddingHeight > 0) {
            text_view_preview_effect.setPadding(text_view_preview_effect.getPaddingLeft(), polishText.paddingHeight, text_view_preview_effect.getPaddingRight(), polishText.paddingHeight)
            seekbar_height.progress = polishText.paddingHeight
        }
        if (polishText.paddingWidth > 0) {
            text_view_preview_effect.setPadding(polishText.paddingWidth, text_view_preview_effect.paddingTop, polishText.paddingWidth, text_view_preview_effect.paddingBottom)
            seekbar_width.progress = polishText.paddingWidth
        }
        if (polishText.text != null) {
            text_view_preview_effect.text = polishText.text
            add_text_edit_text.setText(polishText.text)
        }
        if (polishText.textShader != null) {
            text_view_preview_effect.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            text_view_preview_effect.paint.setShader(polishText.textShader)
        }
        when (polishText.textAlign) {
            4 -> {
                image_view_align_center.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_center_select))
            }

            3 -> {
                image_view_align_right.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_right))
            }

            2 -> {
                image_view_align_left.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_left))
            }
        }
        text_view_preview_effect.setPadding(SystemUtil.dpToPx(context, polishText.paddingWidth), text_view_preview_effect.paddingTop, SystemUtil.dpToPx(context, polishText.paddingWidth), text_view_preview_effect.paddingBottom)
        text_view_preview_effect.setTextColor(polishText.textColor)
        text_view_preview_effect.textAlignment = polishText.textAlign
        text_view_preview_effect.textSize = polishText.textSize.toFloat()
        setFontByName(context, text_view_preview_effect, polishText.fontName ?: "")
        text_view_preview_effect.invalidate()
    }

    private fun setDefaultStyleForEdittext() {
        add_text_edit_text.requestFocus()
        add_text_edit_text.textSize = 20.0f
        add_text_edit_text.textAlignment = View.TEXT_ALIGNMENT_CENTER
        add_text_edit_text.setTextColor(Color.parseColor("#ffffff"))
    }

    private fun initAddTextLayout() {
        textFunctions = getTextFunctions()
        image_view_keyboard.setOnClickListener(this)
        textViewFont.setOnClickListener(this)
        textViewColor.setOnClickListener(this)
        textViewBg.setOnClickListener(this)
        textViewShadow.setOnClickListener(this)
        image_view_adjust.setOnClickListener(this)
        image_view_color.setOnClickListener(this)
        image_view_save_change.setOnClickListener(this)
        image_view_align_left.setOnClickListener(this)
        image_view_align_center.setOnClickListener(this)
        image_view_align_right.setOnClickListener(this)
        scroll_view_change_font_layout.visibility = View.GONE
        scroll_view_change_font_adjust.visibility = View.GONE
        seekbar_width.progress = polishText.paddingWidth
    }

    override fun onColorSelected(position: Int, squareView: TextColorAdapter.SquareView?) {
        squareView ?: return
        if (squareView.isColor) {
            text_view_preview_effect.setTextColor(squareView.drawableId)
            polishText.textColor = squareView.drawableId
            text_view_preview_effect.paint.setShader(null)
            polishText.textShader = null
        } else {
            text_view_preview_effect.setTextColor(squareView.drawableId)
            polishText.textColor = squareView.drawableId
            text_view_preview_effect.paint.setShader(null)
            polishText.textShader = null
        }
    }

    override fun onBackgroundColorSelected(position: Int, squareView: TextBackgroundAdapter.SquareView?) {
        squareView ?: return
        if (squareView.isColor) {
            text_view_preview_effect.setBackgroundColor(squareView.drawableId)
            polishText.backgroundColor = squareView.drawableId
            seekbar_radius.setEnabled(true)
            polishText.isShowBackground = true
            if (!checkbox_background.isChecked) {
                checkbox_background.setChecked(true)
            }
            val red = Color.red(polishText.backgroundColor)
            val green = Color.green(polishText.backgroundColor)
            val blue = Color.blue(polishText.backgroundColor)
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(Color.argb(polishText.backgroundAlpha, red, green, blue))
            gradientDrawable.setCornerRadius(SystemUtil.dpToPx(requireContext(), polishText.backgroundBorder).toFloat())
            text_view_preview_effect.background = gradientDrawable
        } else {
            text_view_preview_effect.setBackgroundColor(squareView.drawableId)
            polishText.backgroundColor = squareView.drawableId
            seekbar_radius.setEnabled(true)
            polishText.isShowBackground = true
            if (!checkbox_background.isChecked) {
                checkbox_background.setChecked(true)
            }
            val red = Color.red(polishText.backgroundColor)
            val green = Color.green(polishText.backgroundColor)
            val blue = Color.blue(polishText.backgroundColor)
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(Color.argb(polishText.backgroundAlpha, red, green, blue))
            gradientDrawable.setCornerRadius(SystemUtil.dpToPx(requireContext(), polishText.backgroundBorder).toFloat())
            text_view_preview_effect.background = gradientDrawable
        }
    }

    override fun onResume() {
        super.onResume()
        val decorView = dialog?.window?.decorView ?: return
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { view, windowInsetsCompat ->
            ViewCompat.onApplyWindowInsets(
                decorView,
                windowInsetsCompat.inset(windowInsetsCompat.systemWindowInsetLeft, 0, windowInsetsCompat.systemWindowInsetRight, windowInsetsCompat.systemWindowInsetBottom)
            )
        }
    }

    fun updateAddTextBottomToolbarHeight(i: Int) {
        Handler().post {
            val layoutParams = linear_layout_edit_text_tools.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomMargin = i
            linear_layout_edit_text_tools.setLayoutParams(layoutParams)
            linear_layout_edit_text_tools.invalidate()
            scroll_view_change_font_layout.invalidate()
            scroll_view_change_font_adjust.invalidate()
            Log.i("HIHIH", i.toString() + "")
        }
    }

    fun setOnTextEditorListener(textEditor2: TextEditor) {
        textEditor = textEditor2
    }

    @SuppressLint("WrongConstant")
    override fun onClick(view: View) {
        val context = requireContext()
        when (view.id) {
            R.id.textViewFont -> {
                textViewFont.setBackgroundResource(R.drawable.background_selected)
                textViewShadow.setBackgroundResource(R.drawable.background_unslelected)
                textViewColor.setBackgroundResource(R.drawable.background_unslelected)
                textViewBg.setBackgroundResource(R.drawable.background_unslelected)
                recycler_view_fonts.visibility = View.VISIBLE
                recycler_view_shadow.visibility = View.GONE
                recycler_view_color.visibility = View.GONE
                relativeLayoutBg.visibility = View.GONE
                return
            }

            R.id.textViewShadow -> {
                textViewFont.setBackgroundResource(R.drawable.background_unslelected)
                textViewShadow.setBackgroundResource(R.drawable.background_selected)
                textViewColor.setBackgroundResource(R.drawable.background_unslelected)
                textViewBg.setBackgroundResource(R.drawable.background_unslelected)
                recycler_view_fonts.visibility = View.GONE
                recycler_view_shadow.visibility = View.VISIBLE
                recycler_view_color.visibility = View.GONE
                relativeLayoutBg.visibility = View.GONE
                return
            }

            R.id.textViewColor -> {
                textViewFont.setBackgroundResource(R.drawable.background_unslelected)
                textViewColor.setBackgroundResource(R.drawable.background_selected)
                textViewShadow.setBackgroundResource(R.drawable.background_unslelected)
                textViewBg.setBackgroundResource(R.drawable.background_unslelected)
                recycler_view_fonts.visibility = View.GONE
                recycler_view_shadow.visibility = View.GONE
                recycler_view_color.visibility = View.VISIBLE
                relativeLayoutBg.visibility = View.GONE
                return
            }

            R.id.textViewBg -> {
                textViewFont.setBackgroundResource(R.drawable.background_unslelected)
                textViewBg.setBackgroundResource(R.drawable.background_selected)
                textViewColor.setBackgroundResource(R.drawable.background_unslelected)
                textViewShadow.setBackgroundResource(R.drawable.background_unslelected)
                recycler_view_fonts.visibility = View.GONE
                recycler_view_shadow.visibility = View.GONE
                recycler_view_color.visibility = View.GONE
                relativeLayoutBg.visibility = View.VISIBLE
                return
            }

            R.id.imageViewAlignLeft -> {
                if (polishText.textAlign == 3 || polishText.textAlign == 4) {
                    polishText.textAlign = 2
                    image_view_align_left.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_left_select))
                    image_view_align_center.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_center))
                    image_view_align_right.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_right))
                }
                text_view_preview_effect.textAlignment = polishText.textAlign
                val textView = text_view_preview_effect
                textView.text = text_view_preview_effect.getText().toString().trim { it <= ' ' } + " "
                text_view_preview_effect.text = text_view_preview_effect.getText().toString().trim { it <= ' ' }
                return
            }

            R.id.imageViewAlignCenter -> {
                if (polishText.textAlign == 2 || polishText.textAlign == 3) {
                    polishText.textAlign = 4
                    image_view_align_center.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_center_select))
                    image_view_align_left.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_left))
                    image_view_align_right.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_right))
                }
                text_view_preview_effect.textAlignment = polishText.textAlign
                val textViews = text_view_preview_effect
                textViews.text = text_view_preview_effect.getText().toString().trim { it <= ' ' } + " "
                text_view_preview_effect.text = text_view_preview_effect.getText().toString().trim { it <= ' ' }
                return
            }

            R.id.imageViewAlignRight -> {
                if (polishText.textAlign == 4 || polishText.textAlign == 2) {
                    polishText.textAlign = 3
                    image_view_align_left.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_left))
                    image_view_align_center.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_center))
                    image_view_align_right.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_align_right_select))
                }
                text_view_preview_effect.textAlignment = polishText.textAlign
                val textViewz = text_view_preview_effect
                textViewz.text = text_view_preview_effect.getText().toString().trim { it <= ' ' } + " "
                text_view_preview_effect.text = text_view_preview_effect.getText().toString().trim { it <= ' ' }
                return
            }

            R.id.image_view_adjust -> {
                image_view_keyboard.setBackgroundResource(R.drawable.background_unslelected)
                image_view_adjust.setBackgroundResource(R.drawable.background_selected_color)
                image_view_color.setBackgroundResource(R.drawable.background_unslelected)
                image_view_keyboard.setColorFilter(resources.getColor(R.color.white))
                image_view_adjust.setColorFilter(resources.getColor(R.color.mainColor))
                image_view_color.setColorFilter(resources.getColor(R.color.white))
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                scroll_view_change_font_adjust.visibility = View.VISIBLE
                scroll_view_change_font_layout.visibility = View.GONE
                seekbar_background_opacity.progress = 255 - polishText.backgroundAlpha
                seekbar_text_size.progress = polishText.textSize
                seekbar_radius.progress = polishText.backgroundBorder
                seekbar_width.progress = polishText.paddingWidth
                seekbar_height.progress = polishText.paddingHeight
                textColorOpacity.progress = 255 - polishText.textAlpha
                toggleTextEditEditable(false)
                return
            }

            R.id.image_view_color -> {
                image_view_keyboard.setBackgroundResource(R.drawable.background_unslelected)
                image_view_adjust.setBackgroundResource(R.drawable.background_unslelected)
                image_view_color.setBackgroundResource(R.drawable.background_selected_color)
                image_view_keyboard.setColorFilter(resources.getColor(R.color.white))
                image_view_adjust.setColorFilter(resources.getColor(R.color.white))
                image_view_color.setColorFilter(resources.getColor(R.color.mainColor))
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                scroll_view_change_font_layout.visibility = View.VISIBLE
                scroll_view_change_font_adjust.visibility = View.GONE
                toggleTextEditEditable(false)
                add_text_edit_text.visibility = View.GONE
                shadowAdapter?.selectedItem = polishText.fontIndex
                fontAdapter?.selectedItem = polishText.fontIndex
                checkbox_background.setChecked(polishText.isShowBackground)
                checkbox_background.setChecked(polishText.isShowBackground)
                return
            }

            R.id.image_view_save_change -> {
                if (polishText.text == null || polishText.text?.length == 0) {
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    textEditor.onBackButton()
                    dismiss()
                    return
                }
                polishText.textWidth = text_view_preview_effect.measuredWidth
                polishText.textHeight = text_view_preview_effect.measuredHeight
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                textEditor.onDone(polishText)
                dismiss()
                return
            }

            R.id.image_view_keyboard -> {
                image_view_keyboard.setBackgroundResource(R.drawable.background_selected_color)
                image_view_adjust.setBackgroundResource(R.drawable.background_unslelected)
                image_view_color.setBackgroundResource(R.drawable.background_unslelected)
                image_view_keyboard.setColorFilter(resources.getColor(R.color.mainColor))
                image_view_adjust.setColorFilter(resources.getColor(R.color.white))
                image_view_color.setColorFilter(resources.getColor(R.color.white))
                toggleTextEditEditable(true)
                add_text_edit_text.visibility = View.VISIBLE
                add_text_edit_text.requestFocus()
                scroll_view_change_font_layout.visibility = View.GONE
                scroll_view_change_font_adjust.visibility = View.GONE
                linear_layout_edit_text_tools.invalidate()
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                return
            }

            else -> {}
        }
    }

    private fun toggleTextEditEditable(focus: Boolean) {
        add_text_edit_text.isFocusable = focus
        add_text_edit_text.setFocusableInTouchMode(focus)
        add_text_edit_text.isClickable = focus
    }

    private fun getTextFunctions(): List<ImageView> {
        val arrayList = mutableListOf<ImageView>()
        arrayList.add(image_view_keyboard)
        arrayList.add(image_view_color)
        arrayList.add(image_view_adjust)
        arrayList.add(image_view_save_change)
        return arrayList
    }

    companion object {
        const val EXTRA_COLOR_CODE = "extra_color_code"
        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val TAG = "TextFragment"

        @JvmOverloads
        fun show(appCompatActivity: AppCompatActivity, str: String = "Test", @ColorInt i: Int = ContextCompat.getColor(appCompatActivity, R.color.white)): TextFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_INPUT_TEXT, str)
            bundle.putInt(EXTRA_COLOR_CODE, i)
            val textFragment = TextFragment()
            textFragment.setArguments(bundle)
            textFragment.show(appCompatActivity.supportFragmentManager, TAG)
            return textFragment
        }

        @JvmStatic
        fun showFragment(appCompatActivity: AppCompatActivity, addTextProperties: Text? = null): TextFragment {
            val fragment = TextFragment()
            if (addTextProperties != null) {
                fragment.polishText = addTextProperties
            }

            fragment.show(appCompatActivity.supportFragmentManager, TAG)
            return fragment
        }
    }
}
