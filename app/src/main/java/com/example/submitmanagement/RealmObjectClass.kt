package com.example.submitmanagement

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SubmitDB : RealmObject(){
    @PrimaryKey
    var id : Int = 0
    var submitName : String = ""
    var submitContents : String = ""
    var submitTerm : String = "000000000000" //YYYYMMDDHHMM
    var remarks : String = ""
    var anyBitmaps : ByteArray? = null
}

/**
 * このインターフェースは [SubmitDB] で追加されているid以外の変数を用意しています。
 *
 * @author RealmObjectClass.kt
 */
@FunctionalInterface
interface SubmitDBInterface{
    var submitName : String
    var submitContents : String
    var submitTerm : String
    var remarks : String
    var bitmap : ByteArray?
}