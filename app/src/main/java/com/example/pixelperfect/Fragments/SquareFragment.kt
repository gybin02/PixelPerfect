package com.example.pixelperfect.Fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Adapters.SquareAdapter
import com.example.pixelperfect.Adapters.SquareAdapter.SplashChangeListener
import com.example.pixelperfect.Assets.FilterFileAsset
import com.example.pixelperfect.Assets.StickerFileAsset.loadBitmapFromAssets
import com.example.pixelperfect.Editor.SquareView
import com.example.pixelperfect.R
import com.example.pixelperfect.Sticker.SplashSticker

/**
 * 遮罩UI
 */
class SquareFragment : DialogFragment(), SplashChangeListener {
    lateinit var imageViewBackground: ImageView
    var bitmap: Bitmap? = null
    private var blurBitmap: Bitmap? = null
    private lateinit var frameLayout: FrameLayout
    var isSplashView = false
    lateinit var recyclerViewBlur: RecyclerView
    lateinit var textVewTitle: TextView
    var blurSquareBgListener: SplashDialogListener? = null
    private var polishSplashSticker: SplashSticker? = null
    lateinit var polishSplashView: SquareView

    //    private lateinit var viewGroup: ViewGroup
    lateinit var textViewValue: TextView
    private lateinit var seekbar_brush: SeekBar

    interface SplashDialogListener {
        fun onSaveBlurBackground(bitmap: Bitmap?)
    }


    fun setBlurBitmap(bitmap2: Bitmap?) {
        blurBitmap = bitmap2
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setRetainInstance(true)
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
//        Dialog dialog = getDialog();
//        Window window = dialog.getWindow();
//        window.requestFeature(Window.FEATURE_NO_TITLE);
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val inflate = layoutInflater.inflate(R.layout.fragment_square, viewGroup, false)
//        viewGroup = viewGroup2
        imageViewBackground = inflate.findViewById(R.id.image_view_background)
        polishSplashView = inflate.findViewById(R.id.splash_view)
        frameLayout = inflate.findViewById(R.id.frame_layout)
        textViewValue = inflate.findViewById(R.id.textViewValue)
        imageViewBackground.setImageBitmap(bitmap)
        textVewTitle = inflate.findViewById(R.id.textViewTitle)
        if (isSplashView) {
            polishSplashView.setImageBitmap(blurBitmap)
        }
        recyclerViewBlur = inflate.findViewById(R.id.recycler_view_splash)
        recyclerViewBlur.setLayoutManager(LinearLayoutManager(context, 0, false))
        recyclerViewBlur.setHasFixedSize(true)
        recyclerViewBlur.setAdapter(SquareAdapter(requireContext(), this, isSplashView))
        if (isSplashView) {
            polishSplashSticker = SplashSticker(loadBitmapFromAssets(requireContext(), "blur/image_mask_1.webp"), loadBitmapFromAssets(requireContext(), "blur/image_frame_1.webp"))
            polishSplashView.addSticker(polishSplashSticker!!)
        }
        seekbar_brush = inflate.findViewById(R.id.seekbar_brush)
        seekbar_brush.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                LoadBlurBitmap(progress.toFloat()).execute(*arrayOfNulls<Void>(0))
                val value = progress.toString()
                textViewValue.text = value
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        polishSplashView.refreshDrawableState()
        polishSplashView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        textVewTitle.setOnClickListener(View.OnClickListener {
            polishSplashView.setCurrentSplashMode(0)
            recyclerViewBlur.setVisibility(View.VISIBLE)
            polishSplashView.refreshDrawableState()
            polishSplashView.invalidate()
        })
        inflate.findViewById<View>(R.id.image_view_save).setOnClickListener {
            blurSquareBgListener!!.onSaveBlurBackground(polishSplashView.getBitmap(bitmap))
            dismiss()
        }
        inflate.findViewById<View>(R.id.image_view_close).setOnClickListener { dismiss() }
        return inflate
    }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
    }

    internal inner class LoadBlurBitmap(private val intensity: Float) : AsyncTask<Void?, Bitmap?, Bitmap>() {
        public override fun onPreExecute() {}
        override fun doInBackground(vararg voidArr: Void?): Bitmap {
            return FilterFileAsset.getBlurImageFromBitmap(bitmap, intensity)
        }

        public override fun onPostExecute(bitmap: Bitmap) {
            polishSplashView.setImageBitmap(bitmap)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        polishSplashView.sticker.release()
        if (blurBitmap != null) {
            blurBitmap!!.recycle()
        }
        blurBitmap = null
        bitmap = null
    }

    override fun onSelected(splashSticker2: SplashSticker?) {
        polishSplashView.addSticker(splashSticker2!!)
    }

    companion object {
        private const val TAG = "SplashFragment"

        @JvmStatic
        fun show(appCompatActivity: AppCompatActivity, bitmap: Bitmap?, blurBitmap: Bitmap?, blurSquareBgListener: SplashDialogListener?, isSplashView: Boolean): SquareFragment {
            val splashDialog = SquareFragment()
            splashDialog.bitmap=(bitmap)
            splashDialog.setBlurBitmap(blurBitmap)
            splashDialog.blurSquareBgListener =(blurSquareBgListener)
            splashDialog.isSplashView = (isSplashView)
            splashDialog.show(appCompatActivity.supportFragmentManager, TAG)
            return splashDialog
        }
    }
}
