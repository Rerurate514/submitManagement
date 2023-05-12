package com.example.submitmanagement

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import java.io.ByteArrayOutputStream

val mAPI = MyAppBaseAPI()

class MyAppBaseAPI {
    /**
     * ログを簡単出力
     */
    fun logTest(msg: String = "log is outed"){
        Log.d("test",msg)
    }

    /**
     * viewの表示, 非表示をmodeで切り替える
     * @param view 任意のview
     * @param mode trueなら表示, falseなら非表示
     */
    fun visibleView(view: View, mode: Boolean){
        when(mode){
            true -> view.visibility = View.VISIBLE
            false -> view.visibility = View.INVISIBLE
        }
    }

    /**
     * コールバック関数
     */
    fun runCompleted(callback: () -> Unit) {
        callback.invoke()
    }

    /**
     * クロージャを使用したカウンタ, インスタンスごとに値を保持
     * @param mode plusかminus デフォルトではplus minusはカウンタから１減らすだけ
     */
    fun countClosure(mode :String = "plus") : () -> Int{
        var count = 0
        val counter : () -> Int = {
            when (mode) {
                "plus" -> count++
                "minus" -> count--
            }
            count
        }
        return counter
    }

    fun castByteArrayFromBitmap(bmp: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun castBitmapFromByteArray(byteArray: ByteArray): Bitmap? {
        val opt = BitmapFactory.Options()
        opt.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, opt)
    }
}