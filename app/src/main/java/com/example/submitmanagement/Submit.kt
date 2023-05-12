package com.example.submitmanagement

/**
 * インスタンス変数として 各[@param] を取得出来ます。
 *
 * @param _nameData 講義名
 * @param _contentsData 提出物内容
 * @param _ymdHmData 提出期限 : 年月日時分
 * @param _remarksData 備考
 * @param _bitmap 参考画像
 */
class Submit(
    _nameData: String,
    _contentsData : String,
    _ymdHmData : String,
    _remarksData : String,
    _bitmap : ByteArray? = null
) : SubmitDBInterface{
    override var submitName: String = _nameData
    override var submitContents: String = _contentsData
    override var submitTerm: String = _ymdHmData
    override var remarks: String = _remarksData
    override var bitmap: ByteArray? = _bitmap

    fun getYMD() : String{
        val ymdStr = selectYMD()
        val y = ymdStr.substring(0..3)
        val m = ymdStr.substring(4..5)
        val d = ymdStr.substring(6..7)
        return y + "年" + m + "月" + d + "日"
    }

    fun getHM() : String{
        val hmStr = selectHM()
        val h = hmStr.substring(0..1)
        val m = hmStr.substring(2..3)
        return h + "時" + m + "分 "
    }

    private fun selectYMD() : String{
        return submitTerm.substring(0..7)
    }

    private fun selectHM() : String{
        return submitTerm.substring(8..11)
    }
}