package com.vincent.baseproject.ui.city_picker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.zaaach.citypicker.db.DBConfig
import com.zaaach.citypicker.db.DBManager
import com.zaaach.citypicker.model.City
import java.io.File
import java.util.*

/**
 * 创建日期：2019/4/2 0002on 上午 10:14
 * 描述：通过db文件获取自定义数据
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */

//由于是构造函数初始化调用copyDBFile方法，因此所有字段需要提前进行赋值，所以此处设置成静态，
private const val tbName = "cities.db"
private const val tableName = "targetcity"
class FileDBManager( ctx: Context) : DBManager(ctx) {

    /**
     * assets文件夹下数据库文件名称
     */

    override fun copyDBFile() {
        val dir = File(DB_PATH)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        createDbFile(tbName)
    }

    override fun getAllCities(): MutableList<City> {
        val db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + tbName, null)
        val cursor = db.rawQuery("select * from $tableName", null)
        val result = mutableListOf<City>()
        var city: City
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(DBConfig.COLUMN_C_NAME))
            val province = cursor.getString(cursor.getColumnIndex(DBConfig.COLUMN_C_PROVINCE))
            val pinyin = cursor.getString(cursor.getColumnIndex(DBConfig.COLUMN_C_PINYIN))
            val code = cursor.getString(cursor.getColumnIndex(DBConfig.COLUMN_C_CODE))
            city = City(name, province, pinyin, code)
            result.add(city)
        }
        cursor.close()
        db.close()
        Collections.sort(result, CityComparator())
        return result
    }

    override fun searchCity(keyword: String?): MutableList<City> {
        val sql = ("select * from " + tableName + " where "
                + DBConfig.COLUMN_C_NAME + " like ? " + "or "
                + DBConfig.COLUMN_C_PINYIN + " like ? ")
        val db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + tbName, null)
        val cursor = db.rawQuery(sql, arrayOf("%$keyword%", "$keyword%"))

        val result = mutableListOf<City>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(DBConfig.COLUMN_C_NAME))
            val province = cursor.getString(cursor.getColumnIndex(DBConfig.COLUMN_C_PROVINCE))
            val pinyin = cursor.getString(cursor.getColumnIndex(DBConfig.COLUMN_C_PINYIN))
            val code = cursor.getString(cursor.getColumnIndex(DBConfig.COLUMN_C_CODE))
            val city = City(name, province, pinyin, code)
            result.add(city)
        }
        cursor.close()
        db.close()
        val comparator = CityComparator()
        Collections.sort(result, comparator)
        return result
    }
}