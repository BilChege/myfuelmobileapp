package com.anchor.erp.myfuelapp.Network;

import com.anchor.erp.myfuelapp.Models.DealerRating;
import com.anchor.erp.myfuelapp.Models.MobileDealer;
import com.anchor.erp.myfuelapp.MpesaModels.StkPushQuery;
import com.anchor.erp.myfuelapp.MpesaModels.StkPushQuerySuccess;
import com.anchor.erp.myfuelapp.MpesaModels.StkRequest;
import com.anchor.erp.myfuelapp.MpesaModels.StkRequestSuccess;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.anchor.erp.myfuelapp.Models.MobileRedemptions;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.OffersForMobile;
import com.anchor.erp.myfuelapp.Models.Purchase;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Models.VehicleMake;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetClient {

    @POST("signupmobileuser")
    Call<MobileUser> signup(@Body MobileUser user);

    @POST("verifyphone/{phone}")
    Call<String> verifyphone(@Path("phone") String phone);

    @GET("verifyemail")
    Call<String> verifyEmail(@Query("address") String address);

    @GET("allpackages")
    Call<List<FuelPackage>> allpackages();

    @GET("checkuser")
    Call<MobileUser> checkUser(@Query("email") String email);

    @POST("addvehicle")
    Call<Vehicle> addvehicle(@Body Vehicle v);

    @POST("makepurchase")
    Call<Purchase> buybundle(@Body Purchase f);

    @Headers("Accept-Charset: utf-8")
    @GET("login")
    Call<MobileUser> loginuser(@Query("email") String email,@Query("rsps") String password);

    @GET("vehiclesforcorporate/{id}")
    Call<List<Vehicle>> vehiclesForCorporate(@Path("id") int id);

    @GET("verifypin/{id}")
    Call<Boolean> verifyPin(@Path("id") int id,@Query("encodedPin") String pin);

    @GET("verifypass/{id}")
    Call<Boolean> verifyPass(@Path("id") int id,@Query("encodedPass") String passWord);

    @POST("fuelcar")
    Call<FuelCar> fuelmycar(@Body FuelCar fuelCar);

    @PUT("updateuserdetails")
    Call<MobileUser> updateuser (@Body MobileUser user);

    @GET("balancesfor/{id}")
    Call<Balances> getbalances (@Path("id") int id);

    @GET("usages/{userid}")
    Call<List<FuelCar>> usagesforuser (@Path("userid") int id);

    @GET("alloffers")
    Call<List<OffersForMobile>> alloffers();

    @POST("redeempointsforpromo")
    Call<Balances> redeempointsforpromo(@Body MobileRedemptions redemption);

    @GET("allmakes")
    Call<List<VehicleMake>> allmakes ();

    @GET("alldealers/{id}")
    Call<List<MobileDealer>> alldealers(@Path("id") int id);

    @PUT("updatevehicle")
    Call<Vehicle> updateCar(@Body Vehicle vehicle);

    @GET("verifyuser")
    Call<MobileUser> verifySystemUser(@Query("phone") String phone);

    @POST("sambaza")
    Call<Balances> sambazaPackege(@Query("sentfrom") String sentfrom,@Query("recipientphone") String recipientphone,@Query("amount") String amount);

    @PUT("userfeedback")
    Call<FuelCar> giveUserFeedback(@Body FuelCar fuelCar);

    @POST("processrequest")
    Call<StkRequestSuccess> promptUser(@Body StkRequest stkRequest);

    @POST("query")
    Call<StkPushQuerySuccess> confirmPayment(@Body StkPushQuery stkPushQuery);

    @POST("ratestation")
    Call<DealerRating> doRating(@Body DealerRating dealerRating);
}
