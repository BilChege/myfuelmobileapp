package com.anchor.erp.myfuelapp.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Models.Contact;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.anchor.erp.myfuelapp.Models.MobileDealer;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.OffersForMobile;
import com.anchor.erp.myfuelapp.Models.Purchase;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Models.VehicleMake;
import com.anchor.erp.myfuelapp.Models.VehicleModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class SessionPreferences {

    private Context context;
    private SimpleDateFormat simpleDateFormat;
    private static final String key_name = "name";
    private static final String key_phone = "phone";
    private static final String purchase_to_be_made = "purchase to be made";
    private static final String selected_contact = "selected contact";
    private static final String selected_transaction = "selected transaction";
    private static final String key_id = "id";
    private static final String key_amt = "amount";
    private static final String key_date = "date";
    private static final String purchase_date = "purchase date";
    private static final String expiry_date = "expiry date";
    private static final String user = "user";
    private static final String user_feedback = "user_feedback";
    private static final String rated_app = "rated_app";
    private static final String rating = "rating";
    private static final String key_price = "price";
    private static final String _package = "package";
    private static final String key_station = "station";
    private static final String key_vehicle = "vehicle";
    private static final String key_promoname = "promoname";
    private static final String key_promocode = "promocode";
    private static final String key_promodesc = "promodesc";
    private static final String key_points = "points";
    private static final String first_time_status = "first_time_status";
    private static final String corporate_user_status_file = "corporate_user_status_file";
    private static final String selected_promotion = "selected promotion";
    private static final String role = "role";
    private SharedPreferences contacts_pref, transactions_pref, purchase_pref, promo_pref, corporate_first_time;
    private SharedPreferences.Editor contacts_pref_edit, transactions_pref_edit, purchase_pref_edit, promo_pref_edit,corporate_first_time_edit;

    public SessionPreferences(Context context) {
        this.context = context;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        contacts_pref = context.getSharedPreferences(selected_contact,Context.MODE_PRIVATE);
        contacts_pref_edit = contacts_pref.edit();
        transactions_pref = context.getSharedPreferences(selected_transaction,Context.MODE_PRIVATE);
        transactions_pref_edit = transactions_pref.edit();
        purchase_pref = context.getSharedPreferences(purchase_to_be_made,Context.MODE_PRIVATE);
        purchase_pref_edit = purchase_pref.edit();
        promo_pref = context.getSharedPreferences(selected_promotion,Context.MODE_PRIVATE);
        promo_pref_edit = promo_pref.edit();
        corporate_first_time = context.getSharedPreferences(corporate_user_status_file,Context.MODE_PRIVATE);
        corporate_first_time_edit = corporate_first_time.edit();
    }

    public void saveFirstTimeStatus(String status){
        SharedPreferences sharedPreferences = context.getSharedPreferences("first time",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status",status);
        editor.commit();
    }

    public Boolean getFirstTimeStatus(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("first time",Context.MODE_PRIVATE);
        return Boolean.valueOf(sharedPreferences.getString("status","true"));
    }

    public void setCorporateFirstTime(boolean status){
        corporate_first_time_edit.putBoolean(first_time_status,status);
        corporate_first_time_edit.commit();
    }

    public boolean getCorporateFirstTime(){
        return corporate_first_time.getBoolean(first_time_status,true);
    }

    public void setLoggedInStatus (String status){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Log in",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status",status);
        editor.commit();
    }

    public boolean getLoggedInStatus (){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Log in",Context.MODE_PRIVATE);
        return Boolean.valueOf(sharedPreferences.getString("status","true"));
    }

    public void setPurchase_to_be_made(Purchase purchase){
        purchase_pref_edit.putString(purchase_date,simpleDateFormat.format(purchase.getDatePurchased()));
        purchase_pref_edit.putString(expiry_date,simpleDateFormat.format(purchase.getExpiryDate()));
        purchase_pref_edit.putInt(user,purchase.getUser().getId());
        purchase_pref_edit.putString(key_price, String.valueOf(purchase.getaPackage().getPriceOfPackage()));
        purchase_pref_edit.putInt(key_points,purchase.getaPackage().getPoints());
        purchase_pref_edit.putInt(_package,purchase.getaPackage().getId());
        purchase_pref_edit.commit();
    }

    public Purchase getPurchaseToBeMade() throws ParseException {
        Purchase purchase = new Purchase();
        purchase.setDatePurchased(simpleDateFormat.parse(purchase_pref.getString(purchase_date,null)));
        purchase.setExpiryDate(simpleDateFormat.parse(purchase_pref.getString(expiry_date,null)));
        MobileUser mobileUser = new MobileUser();
        mobileUser.setId(purchase_pref.getInt(user,0));
        purchase.setUser(mobileUser);
        FuelPackage fuelPackage = new FuelPackage();
        fuelPackage.setId(purchase_pref.getInt(_package,0));
        fuelPackage.setPriceOfPackage(Double.parseDouble(purchase_pref.getString(key_price,null)));
        fuelPackage.setPoints(purchase_pref.getInt(key_points,0));
        purchase.setaPackage(fuelPackage);
        return purchase;
    }

    public void setSelected_promotion(OffersForMobile offers){
        promo_pref_edit.putInt(key_id,offers.getId());
        promo_pref_edit.putString(key_promoname,offers.getPromoname());
        promo_pref_edit.putString(key_promocode,offers.getPromocode());
        promo_pref_edit.putString(key_promodesc,offers.getPromodesc());
        promo_pref_edit.putInt(key_points,offers.getPoints());
        promo_pref_edit.putString(expiry_date,simpleDateFormat.format(offers.getExpiry()));
        promo_pref_edit.commit();
    }

    public OffersForMobile getSelectedPromotion() throws ParseException {
        OffersForMobile offers = new OffersForMobile();
        offers.setId(promo_pref.getInt(key_id,0));
        offers.setPromoname(promo_pref.getString(key_promoname,null));
        offers.setPromodesc(promo_pref.getString(key_promodesc,null));
        offers.setPromocode(promo_pref.getString(key_promocode,null));
        offers.setPoints(promo_pref.getInt(key_points,0));
        offers.setExpiry(simpleDateFormat.parse(promo_pref.getString(expiry_date,null)));
        return offers;
    }

    public int createFuelPackages(List<FuelPackage> fuelPackages){

        int i = 0;
        for (FuelPackage f:fuelPackages){

            SharedPreferences sharedPreferences = context.getSharedPreferences("buypackage"+i,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id",f.getId());
            editor.putString("amount", String.valueOf(f.getAmount()));
            editor.putString("price", String.valueOf(f.getPriceOfPackage()));
            editor.putInt("points",f.getPoints());
            editor.putString("typeofpackage",f.getTypeOfPackage());
            editor.putString("expirydays", String.valueOf(f.getExpirydays()));
            editor.commit();
            Log.e(TAG, "createFuelPackages: "+f.getId()+" "+f.getAmount()+" "+f.getTypeOfPackage()+" "+f.getExpirydays() );
            i += 1;
        }
        return i;
    }

    public List<FuelPackage> getFuelpackages(int i){

        List<FuelPackage> response = new ArrayList<>();
        for (int y = 0;y < i;y++){

            SharedPreferences sharedPreferences = context.getSharedPreferences("buypackage"+y,Context.MODE_PRIVATE);
            FuelPackage fuelPackage = new FuelPackage();
            fuelPackage.setId(sharedPreferences.getInt("id",-1));
            fuelPackage.setAmount(Double.parseDouble(sharedPreferences.getString("amount",null)));
            fuelPackage.setPriceOfPackage(Double.parseDouble(sharedPreferences.getString("price",null)));
            fuelPackage.setTypeOfPackage(sharedPreferences.getString("typeofpackage",null));
            fuelPackage.setPoints(sharedPreferences.getInt("points",0));
            fuelPackage.setExpirydays(Integer.parseInt(sharedPreferences.getString("expirydays",null)));
            response.add(fuelPackage);
            Log.e(TAG, "getFuelpackages: "+fuelPackage.getId()+" "+fuelPackage.getAmount()+" "+fuelPackage.getPriceOfPackage()+" "+fuelPackage.getTypeOfPackage()+" "+fuelPackage.getExpirydays() );
        }
        return response;

    }

    public void setSelectedContact(Contact contact){
        contacts_pref_edit.putString(key_name,contact.getName());
        contacts_pref_edit.putString(key_phone,contact.getPhone());
        contacts_pref_edit.commit();
    }

    public Contact getSelectedContact(){
        Contact contact = new Contact();
        contact.setName(contacts_pref.getString(key_name,null));
        contact.setPhone(contacts_pref.getString(key_phone,null));
        return contact;
    }

    public void setSelectedVehicle(Vehicle vehicle){
        SharedPreferences sharedPreferences = context.getSharedPreferences("selected vehicle",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",vehicle.getId());
        editor.putString("regno",vehicle.getRegno());
        editor.putString("make",vehicle.getMake());
        editor.putBoolean("active",vehicle.isActive());
        editor.putInt("makeid",vehicle.getMakeid());
        editor.putInt("modelid",vehicle.getModelid());
        editor.putString("CCs",vehicle.getCCs());
        editor.putString("enginetype",vehicle.getEnginetype());
        editor.commit();
    }

    public Vehicle getSelectedVehicle(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("selected vehicle",Context.MODE_PRIVATE);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(sharedPreferences.getInt("id",0));
        vehicle.setMake(sharedPreferences.getString("make",null));
        vehicle.setActive(sharedPreferences.getBoolean("active",false));
        vehicle.setRegno(sharedPreferences.getString("regno",null));
        vehicle.setMakeid(sharedPreferences.getInt("makeid",0));
        vehicle.setModelid(sharedPreferences.getInt("modelid",0));
        vehicle.setCCs(sharedPreferences.getString("CCs",null));
        vehicle.setEnginetype(sharedPreferences.getString("enginetype",null));
        return vehicle;
    }

    public void insertLoggedInUser(MobileUser user){
        SharedPreferences sp = context.getSharedPreferences("login session user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
         editor.putInt("id",user.getId());
         editor.putString("First Name",user.getFirstName());
         editor.putString("Last Name",user.getLastName());
         editor.putString("email",user.getEmail());
         editor.putString("phone",user.getPhone());
         editor.putBoolean(rated_app,user.isRatedapp());
//         editor.putInt(rating,user.getRating());
         editor.putString(user_feedback,user.getUserfeedback());
         editor.putString(role,user.getRole());
         editor.putString("accountPassword",user.getAccountPassword());
         editor.putString("pin",user.getPin());
         editor.commit();
        Log.e(TAG, "insertLoggedInUser: "+user.getFirstName()+" "+user.getLastName()+" "+user.getId() );
    }

    public MobileUser getLoggedInUser(){

        SharedPreferences sp = context.getSharedPreferences("login session user",Context.MODE_PRIVATE);
        MobileUser user = new MobileUser();
        user.setId(sp.getInt("id",-1));
        user.setFirstName(sp.getString("First Name",""));
        user.setLastName(sp.getString("Last Name",""));
        user.setEmail(sp.getString("email",""));
        user.setPhone(sp.getString("phone",null));
        user.setAccountPassword(sp.getString("accountPassword",null));
        user.setPin(sp.getString("pin",null));
        user.setRole(sp.getString(role,null));
        user.setUserfeedback(sp.getString(user_feedback,null));
        user.setRatedapp(sp.getBoolean(rated_app,false));
        user.setRating(sp.getInt(rating,-1));
        return user;
    }

    public int addsessionpurchases(List<Purchase> purchases){
        int counter = 0;

        for (Purchase p:purchases){

            SharedPreferences sharedPreferences = context.getSharedPreferences("purchase"+counter,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id",p.getId());
            editor.putInt("package",p.getaPackage().getId());
            editor.putInt("amount", (int) p.getaPackage().getAmount());
            editor.putInt("price", (int) p.getaPackage().getPriceOfPackage());
            editor.putInt("user",p.getUser().getId());
            editor.commit();
            counter += 1;
        }
        return counter;
    }

    public void setnumpurchases(int numpurchases){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numpurchases",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number of purchases",numpurchases);
        editor.commit();
    }

    public int addsessionvehicles(List<Vehicle> vehicles){
        int counter = 0;

        for (Vehicle v:vehicles){

            SharedPreferences sp = context.getSharedPreferences("vehicle"+counter,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("id",v.getId());
            editor.putString("regno",v.getRegno());
            editor.putString("ccs",v.getCCs());
            editor.putInt("makeid",v.getMakeid());
            editor.putInt("modelid",v.getModelid());
            editor.putBoolean("active",v.isActive());
            editor.putString("engine type",v.getEnginetype());
            editor.putString("make",v.getMake());
            editor.commit();
            counter += 1;
//            Log.e(TAG, "addsessionvehicles: "+v.getMake()+" "+v.getRegno() );
        }

        return counter;
    }

    public int addSessionUsages (List<FuelCar> usages){
        int counter = 0;
        for (FuelCar f:usages){
            SharedPreferences sharedPreferences = context.getSharedPreferences("usages"+counter,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id",f.getId());
            editor.putFloat("amount", (float) f.getAmountOfFuel());
//            editor.putInt("user",f.getUser().getId());
            editor.putInt("vehicle",f.getVehicle().getId());
            editor.putString("regno",f.getVehicle().getRegno());
            editor.putString("make",f.getVehicle().getMake());
            editor.putString("CCs",f.getVehicle().getCCs());
            editor.putString("stationid",f.getStationid());
            editor.commit();
            counter += 1;
            Log.e(TAG, "addSessionUsages: "+f.getVehicle().getRegno()+" "+f.getAmountOfFuel() );
        }
        return counter;
    }

    public void setNumUsages(int i){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numusages",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number of usages",i);
        editor.commit();
    }

    public int createSessionOffers(List<OffersForMobile> offers){

        int count = 0;
        for (OffersForMobile o:offers){
            SharedPreferences sharedPreferences = context.getSharedPreferences("offer"+count,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id",o.getId());
            editor.putString("name",o.getPromoname());
            editor.putString("desc",o.getPromodesc());
            editor.putInt("pts",o.getPoints());
            editor.putString("expiry", String.valueOf(o.getExpiry()));
            editor.commit();
            count += 1;
            Log.e(TAG, "createSessionOffers: "+o.getPromoname()+" "+o.getPromodesc() );
        }
        return count;
    }

    public void setNumOffers(int i){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numoffers",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("numberofoffers",i);
        editor.commit();
    }

    public int getNumOffers(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numoffers",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("numberofoffers",-1);
    }

    public List<OffersForMobile> getoffers(int i) throws ParseException {
        List<OffersForMobile> offers = new ArrayList<>();
        for (int j = 0;j < i;j++){
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
            SharedPreferences sharedPreferences = context.getSharedPreferences("offer"+j,Context.MODE_PRIVATE);
            OffersForMobile offer = new OffersForMobile();
            offer.setId(sharedPreferences.getInt("id",0));
            offer.setPromoname(sharedPreferences.getString("name",null));
            offer.setPromodesc(sharedPreferences.getString("desc",null));
            offer.setPoints(sharedPreferences.getInt("pts",0));
            offer.setExpiry(dateFormat.parse(sharedPreferences.getString("expiry",null)));
            offers.add(offer);
            Log.e(TAG, "getoffers: "+offer.getPromoname()+" "+offer.getPromodesc() );
        }
        return offers;
    }

    public List<FuelCar> getUsages (int i){
        List<FuelCar> usages = new ArrayList<>();
        for (int y = 0;y < i;y++){
            SharedPreferences sharedPreferences = context.getSharedPreferences("usages"+y,Context.MODE_PRIVATE);
            FuelCar fuelCar = new FuelCar();
            fuelCar.setId(sharedPreferences.getInt("id",0));
            fuelCar.setStationid(sharedPreferences.getString("stationid",null));
            fuelCar.setAmountOfFuel(sharedPreferences.getFloat("amount",0));
            MobileUser user = new MobileUser();
            user.setId(sharedPreferences.getInt("user",0));
            fuelCar.setUser(user);
            Vehicle vehicle = new Vehicle();
            vehicle.setId(sharedPreferences.getInt("vehicle",0));
            vehicle.setRegno(sharedPreferences.getString("regno",null));
//            vehicle.setMake(sharedPreferences.getString("make",null));
            vehicle.setCCs(sharedPreferences.getString("CCs",null));
            fuelCar.setVehicle(vehicle);
            usages.add(fuelCar);
            Log.e(TAG, "getUsages: "+fuelCar.getVehicle().getRegno()+" "+fuelCar.getAmountOfFuel() );
        }
        return usages;
    }

    public int getNumUsages(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numusages",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("number of usages",0);
    }

    public void setnumvehicles(int num){

        SharedPreferences sharedPreferences = context.getSharedPreferences("numvehicles",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number of vehicles",num);
        editor.commit();

    }

    public int getnumvehicles(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numvehicles",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("number of vehicles",-1);
    }

    public int getnumpurchases(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numpurchases",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("number of purchases",0);
    }

    public List<Vehicle> getSessionvehicles(int i){

        List<Vehicle> vehicles = new ArrayList<>();
        for (int y = 0;y < i;y++){

            SharedPreferences sharedPreferences = context.getSharedPreferences("vehicle"+y,Context.MODE_PRIVATE);
            Vehicle vehicle = new Vehicle();
            vehicle.setId(sharedPreferences.getInt("id",-1));
            vehicle.setRegno(sharedPreferences.getString("regno",null));
            vehicle.setCCs(sharedPreferences.getString("ccs",null));
            vehicle.setActive(sharedPreferences.getBoolean("active",false));
            vehicle.setMakeid(sharedPreferences.getInt("makeid",0));
            vehicle.setModelid(sharedPreferences.getInt("modelid",0));
            vehicle.setMake(sharedPreferences.getString("make",null));
            vehicle.setEnginetype(sharedPreferences.getString("enginetype",null));
            vehicles.add(vehicle);
//            Log.e(TAG, "getSessionvehicles: "+vehicle.getRegno()+" "+vehicle.getMake() );
        }
        return vehicles;
    }

    public int addVehicleMakes (List<VehicleMake> vehicleMakes){
        int count = 0;
        for (VehicleMake vehicleMake: vehicleMakes){
            SharedPreferences sharedPreferences = context.getSharedPreferences("make"+count,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id",vehicleMake.getId());
            editor.putString("make",vehicleMake.getVehicleMake());
            int modelcount = 0;
            List<VehicleModel> models = vehicleMake.getModels();
            for (VehicleModel vmodel : models){
                SharedPreferences sharedPreferences1 = context.getSharedPreferences("model"+modelcount+"formake"+count,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putInt("id",vmodel.getId());
                editor1.putString("model",vmodel.getModel());
                editor1.commit();
                modelcount += 1;
            }
            editor.putInt("modelscount",modelcount);
            count += 1;
            editor.commit();
            Log.e(TAG, "addvehiclemakes: @@@@@@@@@@@@@@@@@@@@@@    "+vehicleMake.getVehicleMake() );
        }
        return count;
    }

    public void setnumvmakes (int i){
        SharedPreferences sharedPreferences = context.getSharedPreferences("nummakes",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number of makes",i);
        Log.e(TAG, "setnumvmakes: NUMBER OF MAKES !!!!!!!!!!!!!!      @@@@@@@@@@@@@@@@@@  "+i );
        editor.commit();
    }

    public int getnumvmakes (){
        SharedPreferences sharedPreferences = context.getSharedPreferences("nummakes",Context.MODE_PRIVATE);
        Log.e(TAG, "setnumvmakes: NUMBER OF MAKES !!!!!!!!!!!!!!      @@@@@@@@@@@@@@@@@@  "+sharedPreferences.getInt("number of makes",0) );
        return sharedPreferences.getInt("number of makes",0);
    }

    public List<VehicleMake> makes(int i){
        List<VehicleMake> result = new ArrayList<>();
        for (int j = 0;j < i;j++){
            SharedPreferences sharedPreferences = context.getSharedPreferences("make"+j,Context.MODE_PRIVATE);
            VehicleMake vehicleMake = new VehicleMake();
            vehicleMake.setId(sharedPreferences.getInt("id",0));
            vehicleMake.setVehicleMake(sharedPreferences.getString("make",null));
            List<VehicleModel> models = new ArrayList<>();
            for (int k = 0;k < sharedPreferences.getInt("modelscount",0);k++){
                SharedPreferences sharedPreferences1 = context.getSharedPreferences("model"+k+"formake"+j,Context.MODE_PRIVATE);
                Integer in = sharedPreferences1.getInt("id",0);
                String model = sharedPreferences1.getString("model",null);
                VehicleModel vehicleModel = new VehicleModel();
                vehicleModel.setId(in);
                vehicleModel.setModel(model);
                models.add(vehicleModel);
            }
            vehicleMake.setModels(models);
            result.add(vehicleMake);
            Log.e(TAG, "makes: @@@@@@@@@@@@@@@@@@@@@@    "+vehicleMake.getVehicleMake() );
        }
        return result;
    }

    public List<Purchase> getPurchases(int i){
        List<Purchase> purchases = new ArrayList<>();
        for (int y = 0;y < i;y++){

            SharedPreferences sharedPreferences = context.getSharedPreferences("purchase"+y,Context.MODE_PRIVATE);
            Purchase p = new Purchase();
            p.setId(sharedPreferences.getInt("id",0));
            FuelPackage fuelPackage = new FuelPackage();
            fuelPackage.setId(sharedPreferences.getInt("package",0));
            fuelPackage.setAmount(sharedPreferences.getInt("amount",0));
            fuelPackage.setPriceOfPackage(sharedPreferences.getInt("price",0));
            p.setaPackage(fuelPackage);
            MobileUser user = new MobileUser();
            user.setId(sharedPreferences.getInt("user",0));
            p.setUser(user);
            purchases.add(p);
        }

        return purchases;
    }

    public void insertBalances (Balances b){
        SharedPreferences sharedPreferences = context.getSharedPreferences("balances",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("bundle", (int) b.getBundle());
        editor.putInt("account", (int) b.getAccount());
        editor.putInt("points", b.getPoints());
        editor.putString("testval", String.valueOf(56.75));
        editor.commit();
        Log.e(TAG, "doInBackground: @@@@@@@@@@@@@@@@@@@@ !!!!!!!!!!!!!!!!!!!!!  "+b.getBundle() );
    }

    public int addDealers (List<MobileDealer> dealers){

        int count = 0;
        for (MobileDealer mobileDealer:dealers){
            SharedPreferences sharedPreferences = context.getSharedPreferences("dealer"+count,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id",mobileDealer.getId());
            editor.putString("name",mobileDealer.getName());
            editor.putString("latitude", String.valueOf(mobileDealer.getLatitude()));
            editor.putString("longitude", String.valueOf(mobileDealer.getLongitude()));
            editor.commit();
            count += 1;
        }
        return count;
    }

    public void setNumDealers(int num){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numdealers",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number of Dealers",num);
        editor.commit();
    }

    public int getnumdealers (){
        SharedPreferences sharedPreferences = context.getSharedPreferences("numdealers",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("number of Dealers",0);
    }

    public List<MobileDealer> getSessionDealers(int num){
        List<MobileDealer> mobileDealers = new ArrayList<>();
        for (int i = 0;i < num; i++){
            SharedPreferences sharedPreferences = context.getSharedPreferences("dealer"+i,Context.MODE_PRIVATE);
            MobileDealer mobileDealer = new MobileDealer();
            mobileDealer.setId(sharedPreferences.getInt("id",0));
            mobileDealer.setName(sharedPreferences.getString("name",null));
            mobileDealer.setLatitude(Double.valueOf(sharedPreferences.getString("latitude","0")));
            mobileDealer.setLongitude(Double.valueOf(sharedPreferences.getString("longitude","0")));
            mobileDealers.add(mobileDealer);
        }
        return mobileDealers;
    }

    public Balances getBalances (){
        SharedPreferences sharedPreferences = context.getSharedPreferences("balances",Context.MODE_PRIVATE);
        Balances balances = new Balances();
//        balances.setBundle( sharedPreferences.getFloat("bundle",0));
        balances.setAccount(sharedPreferences.getInt("account", 0));
        balances.setPoints(sharedPreferences.getInt("points",0));
        return balances;
    }

    public void clearpreviousbalances(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("balances",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void clearsessionVehicles(int i){
        for (int j = 0; j < i;j++){
            SharedPreferences sharedPreferences = context.getSharedPreferences("vehicle"+j,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }

    public void setSelected_transaction(FuelCar fuelCar){
        transactions_pref_edit.putInt(key_id,fuelCar.getId());
        transactions_pref_edit.putString(key_amt, String.valueOf(fuelCar.getAmount()));
        transactions_pref_edit.putString(key_date,simpleDateFormat.format(fuelCar.getDateFueled()));
        transactions_pref_edit.putString(key_station,fuelCar.getStationid());
        transactions_pref_edit.putString(key_vehicle,fuelCar.getVehicle().getRegno());
        transactions_pref_edit.commit();
    }

    public FuelCar getSelectedTransaction() throws ParseException {
        FuelCar fuelCar = new FuelCar();
        fuelCar.setId(transactions_pref.getInt(key_id,0));
        fuelCar.setAmount(Double.parseDouble(transactions_pref.getString(key_amt,null)));
        fuelCar.setDateFueled(simpleDateFormat.parse(transactions_pref.getString(key_date,null)));
        fuelCar.setStationid(transactions_pref.getString(key_station,null));
        Vehicle vehicle = new Vehicle();
        vehicle.setRegno(transactions_pref.getString(key_vehicle,null));
        fuelCar.setVehicle(vehicle);
        return fuelCar;
    }
}
