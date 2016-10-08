package coolweather.com.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import coolweather.com.app.db.CoolWeatherOpenHelper;

/**
 * Created by 31798 on 2016/10/8.
 */
public class CoolWeatherDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    /**
     * 将构造器私有化
     *
     * @param context
     */
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例
     *
     * @param context
     * @return
     */
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将Province实例存储到数据库
     * @param province
     */
    public void saveProvince(Province province) {
        if (province != null) {
//            ContentValues values = new ContentValues();
//            values.put("province_name", province.getProvinceName());
//            values.put("province_code", province.getProvinceCode());
//            db.insert("Province", null, values);
            db.execSQL("insert into Province (province_name, province_code) values(?, ?)",
                    new String[]{province.getProvinceName(), province.getProvinceCode()});
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     * @return
     */
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Province", null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(
                        cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(
                        cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将City实例存储到数据库
     * @param city
     */
    public void saveCity(City city) {
        if (city != null) {
            db.execSQL("insert into City (city_name, city_code, province_id) values(?, ?, ?)",
                    new String[]{city.getCityName(), city.getCityCode(),
                            String.valueOf(city.getProvinceId())});
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息
     * @param provinceId
     * @return
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from City where province_id = ?",
                new String[]{String.valueOf(provinceId)});
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将County实例存储到数据库
     * @param county
     */
    public void saveCounty(County county) {
        if (county != null) {
            db.execSQL("insert into county (county_name, county_code, city_id) values(?, ?, ?)",
                    new String[]{county.getCountyName(), county.getCountyCode(),
                            String.valueOf(county.getCityId())});
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息
     * @param cityId
     * @return
     */
    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from County where city_id = ?",
                new String[]{String.valueOf(cityId)});
        while (cursor.moveToNext()) {
            County county = new County();
            county.setId(cursor.getInt(cursor.getColumnIndex("id")));
            county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
            county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
            county.setCityId(cityId);
            list.add(county);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}
