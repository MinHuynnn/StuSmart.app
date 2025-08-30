package com.app.stusmart.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QRCodeUtils {
    /**
     * Trả về Bitmap QR (hoặc null nếu content rỗng/encode lỗi).
     * - Có viền (MARGIN=1) và ErrorCorrection M để dễ quét
     * - ARGB_8888 cho chất lượng tốt
     */
    fun generateQRCode(content: String, width: Int = 512, height: Int = 512): Bitmap? {
        if (content.isBlank() || width <= 0 || height <= 0) return null
        return try {
            val hints = hashMapOf<EncodeHintType, Any>(
                EncodeHintType.MARGIN to 1,
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
                // EncodeHintType.CHARACTER_SET to "UTF-8" // bật nếu có Unicode phức tạp
            )
            val matrix: BitMatrix = MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints
            )
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (matrix.get(x, y)) Color.BLACK else Color.WHITE
                }
            }
            Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
        } catch (_: Exception) {
            null
        }
    }
}
