package com.aariyan.pickingplan.Interface;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestApis {

    @FormUrlEncoded
    @POST("users.php")
    Observable<ResponseBody> logInResponse(@Field("pincode") String pinCode);
}
