package com.aariyan.pickingplan.Interface;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApis {

    @FormUrlEncoded
    @POST("users.php")
    Observable<ResponseBody> logInResponse(@Field("pincode") String pinCode);

    @GET("GetPlan.php?")
    Observable<ResponseBody> getPlan(@Query("ref") String qrCode);

    @FormUrlEncoded
    @POST("PostPickedQty.php")
    Observable<ResponseBody> postPickedQty(@Field("intPickingId") int itemId, @Field("Qty") int quantity,
                                           @Field("userId") int userId);
}
