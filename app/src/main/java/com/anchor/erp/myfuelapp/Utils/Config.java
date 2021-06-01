package com.anchor.erp.myfuelapp.Utils;

import android.content.Context;
import android.util.Base64;

import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.anchor.erp.myfuelapp.Models.OffersForMobile;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {

    public static final String baseUrl_local = "http://192.168.0.111:8080/myFuelAPI/";
    public static final String baseurl_local_vd = "http://10.0.2.2:8080/myFuelAPI/";
    public static final String baseUrl_demo = "http://172.104.147.162:8282/myFuelAPI-1.0/";
    public static final String safaricomauth = "https://sandbox.safaricom.co.ke/oauth/v1/generate";
    public static final String safaricomstkprocessrequest = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/";
    public static final String safaricomstkpushquery = "https://sandbox.safaricom.co.ke/mpesa/stkpushquery/v1/";
    public static final String directionsApiUrl = "https://maps.googleapis.com/maps/api/directions/json";
    public static final String distanceMatrixApiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String CONSUMER_KEY = "aGwO62wwMEfGK8pWX6jWoxnejrTkCllk";
    private static final String CONSUMER_SECRET = "X729si71RDapKBGW";
    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String ACCOUNT_REFERENCE = "Shell Vivo Test";
    public static final String latitude = "Latitude";
    public static final String longitude = "Longitude";
    public static final String lastLocation = "lastLocation";
    public static final String TRANSACTION_TYPE = "CustomerPayBillOnline";
    public static final String CALL_BACK_URL = "http://192.168.1.11:8080/myFuelAPI";
    public static final String key_checkoutRequestId = "checkoutRequestId";
    public static final String key_purchase_status = "purchase_status";
    private static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";

    public static String generateMpesaPassword(){
        return android.util.Base64.encodeToString((BUSINESS_SHORT_CODE+PASSKEY+generateTimeStamp()).getBytes(), android.util.Base64.NO_WRAP);
    }

    public static String generateTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        return simpleDateFormat.format(new Date());
    }

    public interface SmsListener {
        void bindSmsListener(String code, String phone, Context context);
        void unbindSmsListener();
        void messagesRecieved(String[] message);
    }

    public interface ContactsListener{
        void contactSelected();
    }

    public interface TransactionsListener{
        void transactionSelected(FuelCar fuelCar);
    }

    public interface RecieverFromContacts{
        void readContact();
    }

    public interface BundleSelection{
        void bundleselected(FuelPackage fuelPackage);
    }

    public static NetClient getNetClient(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(60,TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl_demo).addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit.create(NetClient.class);
    }

    public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars,salt,iterations,64*8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
        return iterations+":"+toHex(salt)+":"+toHex(hash);
    }

    private static String toHex(byte[] hash) {
        BigInteger bigInteger = new BigInteger(1,hash);
        String hex = bigInteger.toString(16);
        int paddingLength = (hash.length * 2) - hex.length();
        if (paddingLength > 0){
            return String.format("%0"+paddingLength+"d",0)+hex;
        } else {
            return hex;
        }
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static boolean validatePassword(String entered,String frmdb) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = frmdb.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(entered.toCharArray(),salt,iterations,hash.length * 8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++){
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String part) {
        byte[] bytes = new byte[part.length() / 2];
        for (int i = 0;i < bytes.length; i++){
            bytes[i] = (byte) Integer.parseInt(part.substring(2*i,2*i+2),16);
        }
        return bytes;
    }

    public static String authenticate() throws IOException, JSONException {
        String appKeySecret = CONSUMER_KEY + ":" + CONSUMER_SECRET;
        byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
        String encoded = Base64.encodeToString(bytes,Base64.NO_WRAP);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader("authorization", "Basic "+encoded)
                .addHeader("cache-control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonObject=new JSONObject(response.body().string());
        System.out.println(jsonObject.getString("access_token"));
        return jsonObject.getString("access_token");
    }

    public interface OffersListener {
        public void offerSelected(OffersForMobile offer);
    }
}
