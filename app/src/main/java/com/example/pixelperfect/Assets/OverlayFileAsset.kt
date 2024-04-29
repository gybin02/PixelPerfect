package com.example.pixelperfect.Assets

import android.graphics.Bitmap
import org.wysaid.common.SharedContext
import org.wysaid.nativePort.CGEImageHandler

object OverlayFileAsset {
    @JvmField
    val OVERLAY_EFFECTS = arrayOf(
        OverlayCode(""),
        OverlayCode("#unpack @krblend sr overlay/blend_1.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_2.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_3.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_4.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_5.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_6.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_7.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_8.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_9.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_10.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_11.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_12.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_13.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_14.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_15.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_16.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_17.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_18.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_19.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_20.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_21.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_22.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_23.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_24.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_25.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_26.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_27.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_28.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_29.webp 100"),
        OverlayCode("#unpack @krblend sr overlay/blend_30.webp 100")
    )

    @JvmStatic
    fun getListBitmapOverlayEffect(bitmap: Bitmap?): List<Bitmap?> {
        val arrayList = mutableListOf<Bitmap?>()
        val sharedContext = SharedContext.create()
        sharedContext.makeCurrent()
        val cgeImageHandler = CGEImageHandler()
        cgeImageHandler.initWithBitmap(bitmap)
        for (filterBean in OVERLAY_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.image)
            cgeImageHandler.processFilters()
            val resultBitmap = cgeImageHandler.resultBitmap
            arrayList.add(resultBitmap)
        }
        sharedContext.release()
        return arrayList
    }

    class OverlayCode(var image: String)
}
