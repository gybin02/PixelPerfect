package com.example.pixelperfect.Editor

import android.graphics.Color
import android.graphics.Shader

class Text {
    var backgroundAlpha = 0
    var backgroundBorder = 0
    var backgroundColor = 0
    var backgroundColorIndex = 0
    var fontIndex = 0
    var fontName: String? = null
    var isShowBackground = false
    var paddingHeight = 0
    var paddingWidth = 0
    var text: String? = null
    var textAlign = 0
    var textAlpha = 0
    var textColor = 0
    var textColorIndex = 0
    var textHeight = 0
    var textShader: Shader? = null
    var textShaderIndex = 0
    var textShadow: TextShadow? = null
    var textShadowIndex = 0
    var textSize = 0
    var textWidth = 0

    class TextShadow internal constructor(@JvmField var radius: Int, val dx: Int, val dy: Int, val colorShadow: Int)

    companion object {
        val textShadowList: List<TextShadow>
            get() {
                val arrayList = ArrayList<TextShadow>()
                arrayList.add(TextShadow(0, 0, 0, -16711681))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#FFFFFF")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#000000")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#FF0000")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#FF3C00")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#FFAA00")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#D9FF00")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#08FF00")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#00FFA6")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#0099FF")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#0022FF")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#7700FF")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#D900FF")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#FF0099")))
                arrayList.add(TextShadow(8, 4, 4, Color.parseColor("#FF0048")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#FFFFFF")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#000000")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#FF0000")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#FF3C00")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#FFAA00")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#D9FF00")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#08FF00")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#00FFA6")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#0099FF")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#0022FF")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#7700FF")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#D900FF")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#FF0099")))
                arrayList.add(TextShadow(8, -4, -4, Color.parseColor("#FF0048")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#FFFFFF")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#000000")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#FF0000")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#FF3C00")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#FFAA00")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#D9FF00")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#08FF00")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#00FFA6")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#0099FF")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#0022FF")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#7700FF")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#D900FF")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#FF0099")))
                arrayList.add(TextShadow(8, -4, 4, Color.parseColor("#FF0048")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#FFFFFF")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#000000")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#FF0000")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#FF3C00")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#FFAA00")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#D9FF00")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#08FF00")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#00FFA6")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#0099FF")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#0022FF")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#7700FF")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#D900FF")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#FF0099")))
                arrayList.add(TextShadow(8, 4, -4, Color.parseColor("#FF0048")))
                return arrayList
            }
        val defaultProperties: Text
            get() {
                val polishText = Text()
                polishText.textSize = 30
                polishText.textAlign = 4
                polishText.fontName = "36.ttf"
                polishText.textColor = -1
                polishText.textAlpha = 255
                polishText.backgroundAlpha = 255
                polishText.paddingWidth = 12
                polishText.textShaderIndex = 7
                polishText.backgroundColorIndex = 21
                polishText.textColorIndex = 16
                polishText.fontIndex = 0
                polishText.isShowBackground = false
                polishText.backgroundBorder = 8
                polishText.textAlign = 4
                return polishText
            }
    }
}
