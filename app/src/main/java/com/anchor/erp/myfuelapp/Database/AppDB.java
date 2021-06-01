package com.anchor.erp.myfuelapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.anchor.erp.myfuelapp.Models.MobileDealer;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.Purchase;
import com.anchor.erp.myfuelapp.Models.Vehicle;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AppDB extends SQLiteOpenHelper {

    private static final String tb_vehicle = "vehicles";
    private static final String vehicle_col_id = "id";
    private static final String vehicle_col_reg = "reg_no";
    private static final String vehicle_col_makeid = "make_id";
    private static final String vehicle_col_active = "active";
    private static final String vehicle_col_modelid = "model_id";
    private static final String vehicle_col_make = "make";
    private static final String vehicle_col_engtype = "engine_type";
    private static final String vehicle_col_user = "user";
    private static final String vehicle_col_cc = "CCs";

    private static final String tb_purchases  = "purchases";
    private static final String purchases_col_id = "id";
    private static final String purchases_col_user = "user";
    private static final String purchases_col_bundle = "bundle";
    private static final String purchases_col_cash = "cash";
    private static final String purchases_col_pts = "points";

    private static final String tb_usages = "usages";
    private static final String usages_col_user = "user";
    private static final String usages_col_cash_amt = "cash_amt";
    private static final String usages_col_vehivle = "vehicle";
    private static final String usages_vehicle_reg = "vehicle_reg";
    private static final String usages_vehivle_make = "vehicle_make";

    private static final String tb_dealers = "dealers";
    private static final String dealers_col_id = "id";
    private static final String dealers_col_name = "name";
    private static final String dealers_col_lat = "lat";
    private static final String dealers_col_lon = "lon";
    private static final String station_id = "stationid";
    private static final String rating = "rating";

    private static final String db_name = "MyFuelAppDB";

    private static final String create_table_vehicles = "CREATE TABLE IF NOT EXISTS "+
            tb_vehicle+"("+vehicle_col_id+" INT,"+
            vehicle_col_reg+" VARCHAR(50),"+
            vehicle_col_make+" VARCHAR(50),"+
            vehicle_col_engtype+" VARCHAR(50),"+
            vehicle_col_modelid+" INT,"+
            vehicle_col_makeid+" INT,"+
            vehicle_col_active+" INT,"+
            vehicle_col_cc+" INT,"+
            vehicle_col_user+" INT);";

    private static final String create_table_dealers = "CREATE TABLE IF NOT EXISTS " +
            tb_dealers+" ("+dealers_col_id+" INT, " +
            dealers_col_name+" VARCHAR(50), " +
            station_id+" VARCHAR(50), "+
            rating+" REAL,"+
            dealers_col_lat+" REAL, " +
            dealers_col_lon+" REAL);";

    private static final String create_table_purchases = "CREATE TABLE IF NOT EXISTS "+
            tb_purchases+"("+purchases_col_id+" INT,"+
            purchases_col_user+" VARCHAR(50),"+
            purchases_col_bundle+" INT,"+
            purchases_col_cash+" INT,"+
            purchases_col_pts+" INT);";

    private static final String create_table_usages = "CREATE TABLE IF NOT EXISTS "+tb_usages+" ("+
            usages_col_user+" INT,"+
            usages_col_vehivle+" INT,"+
            usages_vehicle_reg+" VARCHAR(50),"+
            usages_vehivle_make+" VARCHAR(50),"+
            usages_col_cash_amt+" INT);";

    private static final String droptable_vehicle = "DROP TABLE IF EXISTS "+tb_vehicle;
    private static final String droptable_purchase = "DROP TABLE IF EXISTS "+tb_purchases;
    private static final String droptable_usages = "DROP TABLE IF EXISTS "+tb_usages;
    private static final String droptable_dealers = "DROP TABLE IF EXISTS "+tb_dealers;

    public AppDB(Context context){
        super(context,db_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_table_vehicles);
        sqLiteDatabase.execSQL(create_table_purchases);
        sqLiteDatabase.execSQL(create_table_usages);
        sqLiteDatabase.execSQL(create_table_dealers);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(droptable_vehicle);
        sqLiteDatabase.execSQL(droptable_purchase);
        sqLiteDatabase.execSQL(droptable_usages);
        sqLiteDatabase.execSQL(droptable_dealers);

        onCreate(sqLiteDatabase);
    }

    public MobileDealer getDealer(int id){
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM "+tb_dealers+" WHERE "+dealers_col_id+" = "+id;
        MobileDealer dealer = null;
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()){
            dealer = new MobileDealer();
            dealer.setId(cursor.getInt(cursor.getColumnIndex(dealers_col_id)));
            dealer.setName(cursor.getString(cursor.getColumnIndex(dealers_col_name)));
            dealer.setStationId(cursor.getString(cursor.getColumnIndex(station_id)));
            dealer.setLongitude(cursor.getDouble(cursor.getColumnIndex(dealers_col_lon)));
            dealer.setLatitude(cursor.getDouble(cursor.getColumnIndex(dealers_col_lat)));
            dealer.setUserrating(cursor.getDouble(cursor.getColumnIndex(rating)));
        }
        return dealer;
    }

    public MobileDealer getDealerId(String stationId){
        Log.i(TAG, "getDealerId: REQUESTED DEALER @@@@@@@@@@@@@@@@@  :  "+stationId );
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM "+tb_dealers+" WHERE "+station_id+" = '"+stationId+"'";
        MobileDealer mobileDealer = null;
        Cursor cursor = database.rawQuery(query,null);
        Log.i(TAG, "getDealerId: NUMBER OF RESULTS FOUND : "+cursor.getCount());
        if (cursor.moveToFirst()){
            mobileDealer = new MobileDealer();
            mobileDealer.setId(cursor.getInt(cursor.getColumnIndex(dealers_col_id)));
            mobileDealer.setName(cursor.getString(cursor.getColumnIndex(dealers_col_name)));
            mobileDealer.setLatitude(cursor.getDouble(cursor.getColumnIndex(dealers_col_lat)));
            mobileDealer.setUserrating(cursor.getInt(cursor.getColumnIndex(rating)));
            mobileDealer.setLongitude(cursor.getDouble(cursor.getColumnIndex(dealers_col_lon)));
            mobileDealer.setStationId(cursor.getString(cursor.getColumnIndex(station_id)));
        }
        return mobileDealer;
    }

    public void updateDealer(MobileDealer dealer){
        Log.i(TAG, "updateDealer: @@@@@@@@@@@@@@@@@"+dealer.toString());
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dealers_col_id,dealer.getId());
        values.put(dealers_col_name,dealer.getName());
        values.put(station_id,dealer.getStationId());
        values.put(rating,dealer.getUserrating());
        values.put(dealers_col_lat,dealer.getLatitude());
        values.put(dealers_col_lon,dealer.getLongitude());

        database.update(tb_dealers,values,dealers_col_id +" = "+dealer.getId(),null);
        database.close();
    }

    public void addDealer(MobileDealer dealer){
        Log.i(TAG, "addDealer: @@@@@@@@@@@@@@@@@@@"+dealer.toString());
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dealers_col_id,dealer.getId());
        values.put(dealers_col_name,dealer.getName());
        values.put(station_id,dealer.getStationId());
        values.put(rating,dealer.getUserrating());
        values.put(dealers_col_lat,dealer.getLatitude());
        values.put(dealers_col_lon,dealer.getLongitude());

        database.insert(tb_dealers,null,values);
        database.close();
    }

    public void addVehicle (Vehicle vehicle){

//        Log.e(TAG, "addVehicle: "+vehicle.getMake()+" "+vehicle.getRegno() );

        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(vehicle_col_id,vehicle.getId());
        values.put(vehicle_col_reg,vehicle.getRegno());
        values.put(vehicle_col_make,vehicle.getMake());
        values.put(vehicle_col_cc,vehicle.getCCs());
        values.put(vehicle_col_makeid,vehicle.getMakeid());
        values.put(vehicle_col_modelid,vehicle.getModelid());
        values.put(vehicle_col_active,(vehicle.isActive()?1:0));
        values.put(vehicle_col_engtype,vehicle.getEnginetype());
        values.put(vehicle_col_user,vehicle.getOwner().getId());
        database.insert(tb_vehicle,null,values);
        database.close();
        Log.e(TAG, "addVehicle: ################   @@@@@@@@@@@@@@@@@  VEHICLE CREATE :"+vehicle.getOwner().getId() );
    }

    public int updateVehicle(Vehicle vehicle){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(vehicle_col_reg,vehicle.getRegno());
        contentValues.put(vehicle_col_make,vehicle.getMake());
        contentValues.put(vehicle_col_cc,vehicle.getCCs());
        contentValues.put(vehicle_col_active,(vehicle.isActive()?1:0));
        contentValues.put(vehicle_col_makeid,vehicle.getMakeid());
        contentValues.put(vehicle_col_modelid,vehicle.getModelid());
        contentValues.put(vehicle_col_engtype,vehicle.getEnginetype());
        contentValues.put(vehicle_col_user,vehicle.getOwner().getId());
        return database.update(tb_vehicle,contentValues,vehicle_col_id+" = "+vehicle.getId(),null);
    }

    public void cleartablevehicles(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from "+tb_vehicle);
        database.execSQL(droptable_vehicle);
    }

    public void cleartabledealers(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from "+tb_dealers);
        database.execSQL(droptable_dealers);
    }

    public List<MobileDealer> getDealers(){
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM "+tb_dealers;
        Cursor cursor = database.rawQuery(query,null);
        List<MobileDealer> dealers = new ArrayList<>();

        if (cursor.moveToFirst()){

            do{
                MobileDealer dealer = new MobileDealer();
                dealer.setId(cursor.getInt(cursor.getColumnIndex(dealers_col_id)));
                dealer.setName(cursor.getString(cursor.getColumnIndex(dealers_col_name)));
                dealer.setStationId(cursor.getString(cursor.getColumnIndex(station_id)));
                dealer.setLongitude(cursor.getDouble(cursor.getColumnIndex(dealers_col_lon)));
                dealer.setLatitude(cursor.getDouble(cursor.getColumnIndex(dealers_col_lat)));
                dealers.add(dealer);
            } while (cursor.moveToNext());
        }
        return dealers;
    }

    public List<Vehicle> getUserVehicles(int id, boolean checkactivestatus){

        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM "+tb_vehicle+" WHERE "+vehicle_col_user+" = "+id+" AND "+vehicle_col_id+" > 0";
        if (checkactivestatus){
            query += " AND "+vehicle_col_active+" = 1";
        }
        Cursor cursor = database.rawQuery(query,null);
        List<Vehicle> vehicles = null;

        if (cursor.moveToFirst() ){
            vehicles = new ArrayList<>();
            Log.e(TAG, "getVehicles: @@@@@@@@@@@@@@      #############   CALL MADE TO FETCH VEHICLE FROM DB PAST IF OUTSIDE DO:" );
            do{
                Vehicle v = new Vehicle();
                v.setId(cursor.getInt(cursor.getColumnIndex(vehicle_col_id)));
                v.setRegno(cursor.getString(cursor.getColumnIndex(vehicle_col_reg)));
                v.setCCs(cursor.getString(cursor.getColumnIndex(vehicle_col_cc)));
                v.setMakeid(cursor.getInt(cursor.getColumnIndex(vehicle_col_makeid)));
                v.setModelid(cursor.getInt(cursor.getColumnIndex(vehicle_col_modelid)));
                v.setActive(cursor.getInt(cursor.getColumnIndex(vehicle_col_active)) == 1);
                v.setEnginetype(cursor.getString(cursor.getColumnIndex(vehicle_col_engtype)));
                v.setMake(cursor.getString(cursor.getColumnIndex(vehicle_col_make)));
                MobileUser m = new MobileUser();
                m.setId(cursor.getInt(cursor.getColumnIndex(vehicle_col_user)));
                v.setOwner(m);
                vehicles.add(v);
//                Log.e(TAG, "getVehicles: "+v.getRegno()+" "+v.getMake() );
            }while (cursor.moveToNext());
        }
        return vehicles;
    }

    public void addPurchase(Purchase purchase){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(purchases_col_id,purchase.getId());
        contentValues.put(purchases_col_user,purchase.getUser().getId());
        contentValues.put(purchases_col_bundle,purchase.getaPackage().getAmount());
        contentValues.put(purchases_col_cash,purchase.getaPackage().getPriceOfPackage());
        contentValues.put(purchases_col_pts,purchase.getaPackage().getPoints());

        database.insert(tb_purchases,null,contentValues);
        Log.e(TAG, "addPurchase: @@@@@@@@@@@@@@@@@@@@@@@    "+purchase.getId()+" "+purchase.getaPackage().getPriceOfPackage() );
        database.close();
    }

    public List<Purchase> purchases (){
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM "+tb_purchases;
        Cursor cursor = database.rawQuery(query,null);
        List<Purchase> purchases = new ArrayList<>();

        if (cursor.moveToFirst()){

            do{
                Purchase p = new Purchase();
                p.setId(cursor.getInt(cursor.getColumnIndex(purchases_col_id)));
                FuelPackage f = new FuelPackage();
                f.setAmount(cursor.getInt(cursor.getColumnIndex(purchases_col_bundle)));
                f.setPriceOfPackage(cursor.getInt(cursor.getColumnIndex(purchases_col_cash)));
                f.setPoints(cursor.getInt(cursor.getColumnIndex(purchases_col_pts)));
                p.setaPackage(f);
                MobileUser user = new MobileUser();
                user.setId(cursor.getInt(cursor.getColumnIndex(purchases_col_user)));
                p.setUser(user);
                purchases.add(p);
                Log.e(TAG, "purchases: @@@@@@@@@@@@@@@@@@@@@@@@@@@@          "+p.getId()+" "+p.getaPackage().getPriceOfPackage() );
            } while (cursor.moveToNext());

        }
        return purchases;
    }

    public void insertUsage (FuelCar fuelCar){

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(usages_col_user,fuelCar.getUser().getId());
        contentValues.put(usages_col_cash_amt,fuelCar.getAmountOfFuel());
        contentValues.put(usages_col_vehivle,fuelCar.getVehicle().getId());
        contentValues.put(usages_vehicle_reg,fuelCar.getVehicle().getRegno());
        contentValues.put(usages_vehivle_make,fuelCar.getVehicle().getMake());
        database.insert(tb_usages,null,contentValues);
        database.close();

    }

    public List<FuelCar> usages(){
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM "+tb_usages;
        Cursor cursor = database.rawQuery(query,null);
        List<FuelCar> usages = new ArrayList<>();
        if (cursor.moveToFirst()){

            do{

                FuelCar fuelCar = new FuelCar();
                MobileUser user = new MobileUser();
                user.setId(cursor.getInt(cursor.getColumnIndex(usages_col_user)));
                fuelCar.setUser(user);
                Vehicle vehicle = new Vehicle();
                vehicle.setId(cursor.getInt(cursor.getColumnIndex(usages_col_vehivle)));
                vehicle.setMake(cursor.getString(cursor.getColumnIndex(usages_vehivle_make)));
                vehicle.setRegno(cursor.getString(cursor.getColumnIndex(usages_vehicle_reg)));
                fuelCar.setVehicle(vehicle);
                fuelCar.setAmountOfFuel(cursor.getInt(cursor.getColumnIndex(usages_col_cash_amt)));
                usages.add(fuelCar);
            } while (cursor.moveToNext());

        }
        return usages;
    }

}
