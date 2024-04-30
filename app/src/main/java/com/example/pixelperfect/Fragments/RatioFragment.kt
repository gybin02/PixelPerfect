package com.example.pixelperfect.Fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Adapters.AspectAdapter
import com.example.pixelperfect.Adapters.AspectAdapter.OnNewSelectedListener
import com.example.pixelperfect.Adapters.BorderRatioAdapter
import com.example.pixelperfect.Adapters.BorderRatioAdapter.BackgroundBorderListener
import com.example.pixelperfect.Adapters.ColorRatioAdapter
import com.example.pixelperfect.Adapters.GradientRatioAdapter
import com.example.pixelperfect.Adapters.GradientRatioAdapter.BackgroundListener
import com.example.pixelperfect.Assets.FilterFileAsset.cloneBitmap
import com.example.pixelperfect.R
import com.example.pixelperfect.Utils.SystemUtil
import com.steelkiwi.cropiwa.AspectRatio

class RatioFragment : DialogFragment(), OnNewSelectedListener, BackgroundListener, ColorRatioAdapter.BackgroundColorListener, BackgroundBorderListener {
    private lateinit var relative_layout_loading: RelativeLayout
    private var bitmap: Bitmap? = null
    private var blurBitmap: Bitmap? = null
    private lateinit var image_view_blur: ImageView
    private var aspectRatio: AspectRatio? = null
    lateinit var recycler_view_ratio: RecyclerView
    lateinit var recycler_view_color: RecyclerView
    lateinit var ratioSaveListener: RatioSaveListener
    lateinit var constraint_layout_padding: RecyclerView
    lateinit var image_view_ratio: ImageView
    private lateinit var constraint_layout_ratio: ConstraintLayout
    lateinit var recycler_view_background: RecyclerView
    lateinit var frame_layout_wrapper: FrameLayout
    lateinit var textViewValue: TextView
    lateinit var imageViewCrop: ImageView
    lateinit var imageViewGradient: ImageView
    lateinit var imageViewBorder: ImageView
    lateinit var imageViewColor: ImageView

    interface RatioSaveListener {
        fun ratioSavedBitmap(bitmap: Bitmap?)
    }

    fun setBitmap(bitmap: Bitmap?) {
        this.bitmap = bitmap
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
        val view = layoutInflater.inflate(R.layout.fragment_ratio, viewGroup, false)
        val aspectRatioPreviewAdapter = AspectAdapter()
        aspectRatioPreviewAdapter.listener = this
        relative_layout_loading = view.findViewById(R.id.relative_layout_loading)
        relative_layout_loading.setVisibility(View.GONE)
        recycler_view_ratio = view.findViewById(R.id.recycler_view_ratio)
        recycler_view_ratio.setLayoutManager(LinearLayoutManager(context, 0, false))
        recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter)
        aspectRatio = AspectRatio(1, 1)
        recycler_view_background = view.findViewById(R.id.recycler_view_background)
        recycler_view_background.setLayoutManager(LinearLayoutManager(context, 0, false))
        recycler_view_background.setAdapter(GradientRatioAdapter(requireContext(), this))
        recycler_view_color = view.findViewById(R.id.recycler_view_color)
        recycler_view_color.setLayoutManager(LinearLayoutManager(context, 0, false))
        recycler_view_color.setAdapter(ColorRatioAdapter(requireContext(), this))
        textViewValue = view.findViewById(R.id.textViewValue)
        imageViewCrop = view.findViewById(R.id.imageViewCrop)
        imageViewGradient = view.findViewById(R.id.imageViewGradient)
        constraint_layout_padding = view.findViewById(R.id.recycler_vew_border)
        imageViewBorder = view.findViewById(R.id.imageViewBorder)
        imageViewColor = view.findViewById(R.id.imageViewColor)
        imageViewCrop.setOnClickListener(View.OnClickListener {
            recycler_view_ratio.setVisibility(View.VISIBLE)
            recycler_view_background.setVisibility(View.GONE)
            constraint_layout_padding.setVisibility(View.GONE)
            recycler_view_color.setVisibility(View.GONE)
            imageViewCrop.setBackgroundResource(R.drawable.background_selected_color)
            imageViewCrop.setColorFilter(resources.getColor(R.color.mainColor))
            imageViewGradient.setBackgroundResource(R.drawable.background_unslelected)
            imageViewGradient.setColorFilter(resources.getColor(R.color.white))
            imageViewBorder.setBackgroundResource(R.drawable.background_unslelected)
            imageViewBorder.setColorFilter(resources.getColor(R.color.white))
            imageViewColor.setBackgroundResource(R.drawable.background_unslelected)
            imageViewColor.setColorFilter(resources.getColor(R.color.white))
        })
        imageViewGradient.setOnClickListener(View.OnClickListener {
            recycler_view_ratio.setVisibility(View.GONE)
            recycler_view_background.setVisibility(View.VISIBLE)
            constraint_layout_padding.setVisibility(View.GONE)
            recycler_view_color.setVisibility(View.GONE)
            imageViewCrop.setBackgroundResource(R.drawable.background_unslelected)
            imageViewCrop.setColorFilter(resources.getColor(R.color.white))
            imageViewGradient.setBackgroundResource(R.drawable.background_selected_color)
            imageViewGradient.setColorFilter(resources.getColor(R.color.mainColor))
            imageViewBorder.setBackgroundResource(R.drawable.background_unslelected)
            imageViewBorder.setColorFilter(resources.getColor(R.color.white))
            imageViewColor.setBackgroundResource(R.drawable.background_unslelected)
            imageViewColor.setColorFilter(resources.getColor(R.color.white))
        })
        imageViewBorder.setOnClickListener(View.OnClickListener {
            constraint_layout_padding.setVisibility(View.VISIBLE)
            recycler_view_ratio.setVisibility(View.GONE)
            recycler_view_background.setVisibility(View.GONE)
            recycler_view_color.setVisibility(View.GONE)
            imageViewCrop.setBackgroundResource(R.drawable.background_unslelected)
            imageViewCrop.setColorFilter(resources.getColor(R.color.white))
            imageViewGradient.setBackgroundResource(R.drawable.background_unslelected)
            imageViewGradient.setColorFilter(resources.getColor(R.color.white))
            imageViewBorder.setBackgroundResource(R.drawable.background_selected_color)
            imageViewBorder.setColorFilter(resources.getColor(R.color.mainColor))
            imageViewColor.setBackgroundResource(R.drawable.background_unslelected)
            imageViewColor.setColorFilter(resources.getColor(R.color.white))
        })
        imageViewColor.setOnClickListener(View.OnClickListener {
            recycler_view_color.setVisibility(View.VISIBLE)
            constraint_layout_padding.setVisibility(View.GONE)
            recycler_view_ratio.setVisibility(View.GONE)
            recycler_view_background.setVisibility(View.GONE)
            imageViewCrop.setBackgroundResource(R.drawable.background_unslelected)
            imageViewCrop.setColorFilter(resources.getColor(R.color.white))
            imageViewGradient.setBackgroundResource(R.drawable.background_unslelected)
            imageViewGradient.setColorFilter(resources.getColor(R.color.white))
            imageViewBorder.setBackgroundResource(R.drawable.background_unslelected)
            imageViewBorder.setColorFilter(resources.getColor(R.color.white))
            imageViewColor.setBackgroundResource(R.drawable.background_selected_color)
            imageViewColor.setColorFilter(resources.getColor(R.color.mainColor))
        })
        constraint_layout_padding.setLayoutManager(LinearLayoutManager(context, 0, false))
        constraint_layout_padding.setHasFixedSize(true)
        constraint_layout_padding.setAdapter(BorderRatioAdapter(requireContext(), this))
        (view.findViewById<View>(R.id.seekbarPadding) as SeekBar).setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
                val dpToPx = SystemUtil.dpToPx(this@RatioFragment.context, i)
                val layoutParams = image_view_ratio!!.layoutParams as FrameLayout.LayoutParams
                layoutParams.setMargins(dpToPx, dpToPx, dpToPx, dpToPx)
                image_view_ratio!!.setLayoutParams(layoutParams)
                val value = i.toString()
                textViewValue.setText(value)
            }
        })
        image_view_ratio = view.findViewById(R.id.image_view_ratio)
        image_view_ratio.setImageBitmap(bitmap)
        image_view_ratio.setAdjustViewBounds(true)
        val defaultDisplay = requireActivity().windowManager.defaultDisplay
        val point = Point()
        defaultDisplay.getSize(point)
        constraint_layout_ratio = view.findViewById(R.id.constraint_layout_ratio)
        image_view_blur = view.findViewById(R.id.image_view_blur)
        image_view_blur.setImageBitmap(blurBitmap)
        frame_layout_wrapper = view.findViewById(R.id.frame_layout_wrapper)
        frame_layout_wrapper.setLayoutParams(ConstraintLayout.LayoutParams(point.x, point.x))
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraint_layout_ratio)
        constraintSet.connect(frame_layout_wrapper.getId(), 3, constraint_layout_ratio.getId(), 3, 0)
        constraintSet.connect(frame_layout_wrapper.getId(), 1, constraint_layout_ratio.getId(), 1, 0)
        constraintSet.connect(frame_layout_wrapper.getId(), 4, constraint_layout_ratio.getId(), 4, 0)
        constraintSet.connect(frame_layout_wrapper.getId(), 2, constraint_layout_ratio.getId(), 2, 0)
        constraintSet.applyTo(constraint_layout_ratio)
        view.findViewById<View>(R.id.image_view_close).setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.image_view_save).setOnClickListener { SaveRatioView().execute(getBitmapFromView(frame_layout_wrapper)) }
        return view
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

    private fun calculateWidthAndHeight(aspectRatio: AspectRatio?, point: Point): IntArray {
        val height = constraint_layout_ratio!!.height
        if (aspectRatio!!.height > aspectRatio.width) {
            val mRatio = (aspectRatio.ratio * height.toFloat()).toInt()
            return if (mRatio < point.x) {
                intArrayOf(mRatio, height)
            } else intArrayOf(point.x, (point.x.toFloat() / aspectRatio.ratio).toInt())
        }
        val iRatio = (point.x.toFloat() / aspectRatio.ratio).toInt()
        return if (iRatio > height) {
            intArrayOf((height.toFloat() * aspectRatio.ratio).toInt(), height)
        } else intArrayOf(point.x, iRatio)
    }

    override fun onNewAspectRatioSelected(aspectRatio: AspectRatio?) {
        val defaultDisplay = requireActivity().windowManager.defaultDisplay
        val point = Point()
        defaultDisplay.getSize(point)
        this.aspectRatio = aspectRatio
        val calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point)
        frame_layout_wrapper!!.setLayoutParams(ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]))
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraint_layout_ratio)
        constraintSet.connect(frame_layout_wrapper!!.id, 3, constraint_layout_ratio!!.id, 3, 0)
        constraintSet.connect(frame_layout_wrapper!!.id, 1, constraint_layout_ratio!!.id, 1, 0)
        constraintSet.connect(frame_layout_wrapper!!.id, 4, constraint_layout_ratio!!.id, 4, 0)
        constraintSet.connect(frame_layout_wrapper!!.id, 2, constraint_layout_ratio!!.id, 2, 0)
        constraintSet.applyTo(constraint_layout_ratio)
    }

    internal inner class SaveRatioView : AsyncTask<Bitmap?, Bitmap?, Bitmap>() {
        public override fun onPreExecute() {
            mLoading(true)
        }

        override fun doInBackground(vararg bitmapArr: Bitmap?): Bitmap {
            val cloneBitmap = cloneBitmap(bitmapArr[0])
            bitmapArr[0]?.recycle()
//            bitmapArr[0] = null
            return cloneBitmap
        }

        public override fun onPostExecute(bitmap: Bitmap) {
            mLoading(false)
            ratioSaveListener.ratioSavedBitmap(bitmap)
            dismiss()
        }
    }

    override fun onBackgroundColorSelected(pos: Int, squareView: ColorRatioAdapter.SquareView?) {
        if (squareView!!.isColor) {
            frame_layout_wrapper.setBackgroundColor(squareView.drawableId)
            image_view_blur.setVisibility(View.GONE)
        } else if (squareView.text == "Blur") {
            image_view_blur.setVisibility(View.VISIBLE)
        } else {
            frame_layout_wrapper.setBackgroundResource(squareView.drawableId)
            image_view_blur.setVisibility(View.GONE)
        }
        frame_layout_wrapper.invalidate()
    }

    override fun onBackgroundSelected(item: Int, squareView: GradientRatioAdapter.SquareView?) {
        if (squareView!!.isGradient) {
            frame_layout_wrapper.setBackgroundColor(squareView.drawableId)
        } else if (squareView.text == "Blur") {
            image_view_blur.setVisibility(View.VISIBLE)
        } else {
            frame_layout_wrapper.setBackgroundResource(squareView.drawableId)
            image_view_blur.setVisibility(View.GONE)
        }
        frame_layout_wrapper.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (blurBitmap != null) {
            blurBitmap!!.recycle()
            blurBitmap = null
        }
        bitmap = null
    }

    override fun onBackgroundBorderSelected(item: Int, squareView: BorderRatioAdapter.SquareView?) {
        if (squareView!!.isColor) {
            image_view_ratio!!.setBackgroundColor(squareView.drawableId)
            val dpToPx = SystemUtil.dpToPx(context, 3)
            image_view_ratio!!.setPadding(dpToPx, dpToPx, dpToPx, dpToPx)
            return
        } else if (squareView.text == "Blur") {
            image_view_ratio!!.setPadding(0, 0, 0, 0)
        } else {
            val dpToPx = SystemUtil.dpToPx(context, 3)
            image_view_ratio!!.setPadding(dpToPx, dpToPx, dpToPx, dpToPx)
            image_view_ratio!!.setBackgroundColor(squareView.drawableId)
            return
        }
        frame_layout_wrapper!!.invalidate()
    }

    fun getBitmapFromView(view: View?): Bitmap? {
        view?:return null
        val bitmap = Bitmap.createBitmap(requireView().width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun mLoading(showLoading: Boolean) {
        if (showLoading) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            relative_layout_loading.visibility = View.VISIBLE
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            relative_layout_loading.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "RatioFragment"
        /**
         * 显示
         */
        @JvmStatic
        fun show(appCompatActivity: AppCompatActivity, ratioSaveListener: RatioSaveListener, mBitmap: Bitmap?, blurBitmap: Bitmap?): RatioFragment {
            val ratioFragment = RatioFragment()
            ratioFragment.setBitmap(mBitmap)
            ratioFragment.setBlurBitmap(blurBitmap)
            ratioFragment.ratioSaveListener=(ratioSaveListener)
            ratioFragment.show(appCompatActivity.supportFragmentManager, TAG)
            return ratioFragment
        }
    }
}
