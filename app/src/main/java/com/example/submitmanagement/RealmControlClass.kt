package com.example.submitmanagement

import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmObject
import java.lang.NullPointerException

class RealmControl(
    _submit: Submit = Submit("","","","",null)
) : AppCompatActivity() ,SubmitDBInterface{
    private val realm : Realm = Realm.getDefaultInstance()

    var submitData: Submit = _submit
    override var submitName: String = submitData.submitName
    override var submitContents: String = submitData.submitContents
    override var submitTerm: String = submitData.submitTerm
    override var remarks: String = submitData.remarks
    override var bitmap: ByteArray? = submitData.bitmap

    companion object{
        const val ID = "id"
        const val SUBMIT_NAME = "submitName"
        const val SUBMIT_CONTENTS = "submitContents"
        const val SUBMIT_TERM_YEAR = "submitTerm"
        const val REMARKS = "remarks"
        const val ANY_BITMAPS = "anyBitmaps"

        const val DATABASE_ID_ZERO = 0
    }

    private fun getObj(id: Int) : RealmObject?{
        return realm.where(SubmitDB::class.java)
            .equalTo(ID,id)
            .findFirst()
    }

    /**
     * コンストラクタに代入された[Submit]に基づき、データベースへレコード挿入する処理を開始します。
     */
    fun controlSubmitDB(){
        realm.executeTransaction{ realm
            searchInsertPlace()
        }
    }

    private fun createRecord(id: Int): RealmObject {
        val obj = getObj(id)
        if (obj == null) realm.createObject(SubmitDB::class.java, id)
        return getObj(id)!!
    }

    /**
     * DB更新フロー
     *
     * 1.DBが存在しないなら作成してid=0にレコードを挿入
     *
     * 2.id=0より取得レコードが小さいときDBの最大値+1にレコードを作成して、id=0に取得レコードを挿入
     *
     * 3.取得レコードが{id=DBの最大値}より大きい場合、最大値にレコードを作成して挿入
     *
     * 4.取得レコードより該当レコードが小さいときにDB操作レコードIDに+!する
     *
     * 5.取得レコードが該当レコードより大きい場合、該当レコードから上を一個ずつずらして
     *  元々該当レコードがあるレコードに挿入
     * 
     * 6.取得レコードと該当レコードが同じ値の場合該当レコードの一個後のレコードに取得レコードを挿入
     *
     * フローはこの順番で更新される
     */
    private fun searchInsertPlace(){
        if(isRealmNull() == null) {
            createRecord(DATABASE_ID_ZERO)
            insertSubmitDB(DATABASE_ID_ZERO)
            return
        }

        val termArray : Array<Long> = getTermToArray()
        val ymdHm = submitTerm.toLong()

        for(i in termArray.indices){
            when{
                i == DATABASE_ID_ZERO && termArray[i] > ymdHm -> {
                    //DB内で一個目より小さい時
                    shiftRecord(DATABASE_ID_ZERO)
                    insertSubmitDB(DATABASE_ID_ZERO)
                    return
                }
                ymdHm > termArray[i] && i == getRealmMax() -> {
                    //termがDB内で最大の時
                    shiftRecord(getRealmMax())
                    insertSubmitDB(getRealmMax())
                    return
                }
                ymdHm > termArray[i] && ymdHm < termArray[i + 1] -> {
                    //DB検索で該当レコードより一個上のレコードのtermが大きい時
                    shiftRecord(i)
                    insertSubmitDB(i + 1)
                    return
                }
                ymdHm == termArray[i] -> {
                    //同じの時 //todo まだ未完成
                    shiftRecord(getRealmMax())
                    insertSubmitDB(i)
                    return
                }
            }
        }
    }

    /**
     * DBのid=0からmaxまでのsubmitTermをLong型のarrayにして返します。
     *
     * @return Array<Long>
     */
    private fun getTermToArray():Array<Long> {
        var returnArray : Array<Long> = arrayOf()
        for(i in 0..getRealmMax()){
            val obj = realm.where(SubmitDB::class.java)
                .equalTo(ID,i)
                .findFirst()

            if (obj != null) returnArray += obj.submitTerm.toLong()
        }
        return returnArray
    }

    private fun shiftRecord(inputRecordId: Int){
        createRecord(getRealmMax() + 1)

        for(i in getRealmMax() - 1 downTo inputRecordId){
            val objRemoveValue = realm.where(SubmitDB::class.java)
                .equalTo(ID,i + 1)
                .findFirst()!!

            val objMoveValue = realm.where(SubmitDB::class.java)
                .equalTo(ID,i)
                .findFirst()!!

            val moveValueData = Submit(
                objMoveValue.submitName,
                objMoveValue.submitContents,
                objMoveValue.submitTerm,
                objMoveValue.remarks,
                objMoveValue.anyBitmaps
            )

            RealmObjectMove(moveValueData,objRemoveValue).realmReplace()
        }
    }

    private fun insertSubmitDB(id: Int){
        val obj = realm.where(SubmitDB::class.java)
            .equalTo(ID,id)
            .findFirst()!!

        RealmObjectMove(submitData,obj).realmReplace()
    }

    /**
     * この関数で得られる [SubmitDB] の"Max"は最大のIDでありレコードの個数ではありません。
     * レコードの個数を得たい場合はこの関数に +1 をしてください。最小のIDは 0 です。
     * DBが存在しない場合、-1を返します。
     *
     * @return テーブルに存在する [SubmitDB] の最大のIDを返す。
     * @throws NullPointerException レコードが存在しない場合
     *
     * throw NullPointerException("The database is empty.")
     */
    fun getRealmMax(): Int {
        val objMax = realm.where(SubmitDB::class.java)
            .max(ID) ?:-1
        return objMax.toInt()
    }

    /**
     * この関数で [@param] のidのレコードのカラムが取得できます。
     * @param id 欲しいデータのあるid
     * @return 戻り値はデータクラス [Submit] でreturnされます。
     */
    fun getSubmitDB(id: Int) : Submit{
        val obj = realm.where(SubmitDB::class.java)
            .equalTo(ID,id)
            .findFirst() ?: return submitData

        val ymdHm = obj.submitTerm

        return Submit(
            obj.submitName,
            obj.submitContents,
            ymdHm,
            obj.remarks,
            obj.anyBitmaps
        )
    }

    private fun isRealmNull(): Int? {
        val minId = 0
        return realm.where(SubmitDB::class.java)
            .equalTo(ID,minId)
            .findFirst()
            ?.id
    }

    private fun deleteRealmObject(id: Int){
        realm.executeTransaction { realm
            realm.where(SubmitDB::class.java)
                .equalTo(ID, id)
                .findFirst()?.deleteFromRealm()
        }
    }


    /**
     * データベース [SubmitDB] のテーブルを全て消去します。
     */
    fun realmDataBaseInit() {
        val obj = realm.where(SubmitDB::class.java)
            .findAll()
        realm.executeTransaction { realm
            obj.deleteAllFromRealm()
        }
    }

    /**
     * このクラスはdataBaseの入れ替えを行います。　詳細は[@param]にて
     * @param _beforeData 挿入したいレコード。　型は [Submit]
     * @param _afterObj 挿入されるレコード。　　型は [SubmitDB]
     */
    inner class RealmObjectMove(
        _beforeData : Submit,
        _afterObj : SubmitDB
        ) : SubmitDBInterface{
        private val beforeData = _beforeData
        private var afterObj = _afterObj

        override var submitName: String = beforeData.submitName
        override var submitContents: String = beforeData.submitContents
        override var submitTerm: String = beforeData.submitTerm
        override var remarks: String = beforeData.remarks
        override var bitmap: ByteArray? = beforeData.bitmap

        fun realmReplace(){
            mAPI.logTest("replace success ${afterObj.submitTerm} <- ${submitTerm}")
            afterObj.submitName = submitName
            afterObj.submitContents = submitContents
            afterObj.submitTerm = submitTerm
            afterObj.remarks = remarks
            afterObj.anyBitmaps = bitmap
        }
    }
}