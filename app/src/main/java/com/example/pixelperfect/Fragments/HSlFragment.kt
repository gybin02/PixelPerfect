package com.example.pixelperfect.Fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.pixelperfect.R
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import org.wysaid.nativePort.CGENativeLibrary
/**
 * 调色面板
 */
class HSlFragment : DialogFragment() {
    var bitmap: Bitmap? = null

    //public HSLSaveListener ratioSaveListener;
    lateinit var image_view_ratio: ImageView
    private lateinit var seekbarIntensityHue: IndicatorSeekBar
    private lateinit var seekbarIntensitySaturation: IndicatorSeekBar
    private lateinit var seekbarIntensityLightness: IndicatorSeekBar
    lateinit var colorselectionRg: RadioGroup

    private var tempbitmap: Bitmap? = null
    var onFilterSavePhoto: OnFilterSavePhoto? = null

    interface OnFilterSavePhoto {
        fun onSaveFilter(bitmap: Bitmap?)
    }


    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setRetainInstance(true)
    }

    /* public void setRatioSaveListener(HSLSaveListener iRatioSaveListener) {
        this.ratioSaveListener = iRatioSaveListener;
    }*/
    @SuppressLint("WrongConstant")
    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
//        Dialog dialog = getDialog();
//        Window window = dialog.getWindow();
//        window.requestFeature(Window.FEATURE_NO_TITLE);
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val inflate = layoutInflater.inflate(R.layout.fragment_hsl, viewGroup, false)
        seekbarIntensityHue = inflate.findViewById(R.id.hue)
        seekbarIntensitySaturation = inflate.findViewById(R.id.sat)
        seekbarIntensityLightness = inflate.findViewById(R.id.light)
        colorselectionRg = inflate.findViewById(R.id.colorselection)
        image_view_ratio = inflate.findViewById(R.id.imageViewRatio)
        image_view_ratio.setImageBitmap(bitmap)
        inflate.findViewById<View>(R.id.imageViewCloseRatio).setOnClickListener { dismiss() }
        inflate.findViewById<View>(R.id.imageViewSaveRatio).setOnClickListener {
            onFilterSavePhoto!!.onSaveFilter((image_view_ratio.getDrawable() as BitmapDrawable).bitmap)
            dismiss()
        }
        val seekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                applyEffect()
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }
        seekbarIntensityHue.onSeekChangeListener = seekChangeListener
        seekbarIntensitySaturation.onSeekChangeListener = seekChangeListener
        colorselectionRg.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i -> tempbitmap = null })
        seekbarIntensityLightness.onSeekChangeListener = seekChangeListener
        return inflate
    }

    /**
     * 应用效果
     */
    private fun applyEffect() {
        val colorMap = mapOf(
            R.id.red to "red",
            R.id.green to "green",
            R.id.blue to "blue",
            R.id.mergenta to "magenta",
            R.id.yellow to "yellow",
            R.id.cyan to "cyan",
            R.id.white to "white"
        )

        val checkedRadioButtonId = colorselectionRg.checkedRadioButtonId
        val selectedColor = colorMap[checkedRadioButtonId]

        selectedColor?.let { color ->
            val ruleString = "@selcolor $color(${seekbarIntensityHue.progressFloat / 100},${seekbarIntensitySaturation.progressFloat / 100},${seekbarIntensityLightness.progressFloat / 100},${seekbarIntensityLightness.progressFloat / 100})"
            // 使用 ruleString 进行后续操作
            if (tempbitmap == null) {
                tempbitmap = getBitmapFromView(image_view_ratio)
            }
            val tempbitmap2 = CGENativeLibrary.filterImage_MultipleEffects(tempbitmap, ruleString, seekbarIntensityHue.getProgressFloat() / 200)
            image_view_ratio.setImageBitmap(tempbitmap2)
        }
    }


    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bitmap = null
    }

    fun getBitmapFromView(view: View?): Bitmap? {
        view ?: return null
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    companion object {
        private const val TAG = "HSlFragment"

        /**
         * 显示
         */
        @JvmStatic
        fun show(appCompatActivity: AppCompatActivity, onFilterSavePhoto: OnFilterSavePhoto?, mBitmap: Bitmap?): HSlFragment {
            val ratioFragment = HSlFragment()
            ratioFragment.bitmap = mBitmap
            ratioFragment.onFilterSavePhoto = onFilterSavePhoto
            ratioFragment.show(appCompatActivity.supportFragmentManager, TAG)
            return ratioFragment
        }
    }
}
