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
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Adapters.AspectAdapter
import com.example.pixelperfect.Adapters.AspectAdapter.OnNewSelectedListener
import com.example.pixelperfect.R
import com.github.flipzeus.FlipDirection
import com.github.flipzeus.ImageFlipper
import com.isseiaoki.simplecropview.CropImageView
import com.steelkiwi.cropiwa.AspectRatio


class CropperFragment : DialogFragment(), OnNewSelectedListener {
    private var bitmap: Bitmap? = null
    lateinit var crop_image_view: CropImageView
    var onCropPhoto: OnCropPhoto? = null
    lateinit var relative_layout_loading: RelativeLayout

    interface OnCropPhoto {
        fun finishCrop(bitmap: Bitmap?)
    }

    fun setBitmap(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setRetainInstance(true)
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

    @SuppressLint("WrongConstant")
    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
        val dialog = dialog
        //        Window window = dialog.getWindow();
//        window.requestFeature(Window.FEATURE_NO_TITLE);
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val inflate = layoutInflater.inflate(R.layout.fragment_cropper, viewGroup, false)
        val aspectRatioPreviewAdapter = AspectAdapter()
        aspectRatioPreviewAdapter.listener = this
        val recycler_view_ratio = inflate.findViewById<RecyclerView>(R.id.recycler_view_ratio)
        recycler_view_ratio.layoutManager = LinearLayoutManager(context, 0, false)
        recycler_view_ratio.adapter = aspectRatioPreviewAdapter
        crop_image_view = inflate.findViewById(R.id.crop_image_view)
        crop_image_view.setCropMode(CropImageView.CropMode.FREE)
        inflate.findViewById<View>(R.id.imageViewRotateLeft).setOnClickListener { crop_image_view.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D) }
        inflate.findViewById<View>(R.id.imageViewRotateRight).setOnClickListener { crop_image_view.rotateImage(CropImageView.RotateDegrees.ROTATE_90D) }
        inflate.findViewById<View>(R.id.imageViewFlipV).setOnClickListener { ImageFlipper.flip(crop_image_view, FlipDirection.VERTICAL) }
        inflate.findViewById<View>(R.id.imageViewFlipH).setOnClickListener { ImageFlipper.flip(crop_image_view, FlipDirection.HORIZONTAL) }
        inflate.findViewById<View>(R.id.imageViewSaveCrop).setOnClickListener { OnSaveCrop().execute(*arrayOfNulls<Void>(0)) }
        relative_layout_loading = inflate.findViewById(R.id.relative_layout_loading)
        relative_layout_loading.setVisibility(View.GONE)
        inflate.findViewById<View>(R.id.imageViewCloseCrop).setOnClickListener { dismiss() }
        return inflate
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        crop_image_view = view.findViewById(R.id.crop_image_view)
        crop_image_view.setImageBitmap(bitmap)
    }

    override fun onNewAspectRatioSelected(aspectRatio: AspectRatio?) {
        if (aspectRatio!!.width == 10 && aspectRatio.height == 10) {
            crop_image_view.setCropMode(CropImageView.CropMode.FREE)
        } else {
            crop_image_view.setCustomRatio(aspectRatio.width, aspectRatio.height)
        }
    }

    internal inner class OnSaveCrop : AsyncTask<Void?, Bitmap?, Bitmap>() {
        public override fun onPreExecute() {
            mLoading(true)
        }

        override fun doInBackground(vararg voidArr: Void?): Bitmap {
            return crop_image_view.croppedBitmap
        }

        public override fun onPostExecute(bitmap: Bitmap) {
            mLoading(false)
            onCropPhoto!!.finishCrop(bitmap)
            dismiss()
        }
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
        private const val TAG = "CropFragment"
        @JvmStatic
        fun show(appCompatActivity: AppCompatActivity, onCropPhoto2: OnCropPhoto?, bitmap2: Bitmap?): CropperFragment {
            val cropDialogFragment = CropperFragment()
            cropDialogFragment.setBitmap(bitmap2)
            cropDialogFragment.onCropPhoto = (onCropPhoto2)
            cropDialogFragment.show(appCompatActivity.supportFragmentManager, TAG)
            return cropDialogFragment
        }
    }
}