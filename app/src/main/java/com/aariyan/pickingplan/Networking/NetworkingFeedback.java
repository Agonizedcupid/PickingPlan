package com.aariyan.pickingplan.Networking;

import android.content.Context;

import com.aariyan.pickingplan.Interface.GetPLanInterface;
import com.aariyan.pickingplan.Interface.LogInInterface;
import com.aariyan.pickingplan.Interface.RestApis;
import com.aariyan.pickingplan.Model.AuthenticationModel;
import com.aariyan.pickingplan.Model.PlanModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class NetworkingFeedback {

    private Context context;
    ApiClient apiClient;
    RestApis apis;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<AuthenticationModel> listOfUsers;
    private List<PlanModel> listOfPlans;

    public NetworkingFeedback(Context context) {
        this.context = context;
        apis = ApiClient.getClient().create(RestApis.class);
        listOfUsers = new ArrayList<>();
        listOfPlans = new ArrayList<>();
    }

    public void postLogInResponse(LogInInterface logInInterface, String pinCode) {
        compositeDisposable.add(apis.logInResponse(pinCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray root = new JSONArray(responseBody.string());
                        listOfUsers.clear();
                        for (int i = 0; i < root.length(); i++) {
                            JSONObject single = root.getJSONObject(i);
                            int userId = single.getInt("UserId");
                            int locationId = single.getInt("LocationId");
                            String pickingTeams = single.getString("strPickingTeams");

                            AuthenticationModel model = new AuthenticationModel(userId, locationId, pickingTeams);
                            listOfUsers.add(model);
                        }
                        logInInterface.checkLogIn(listOfUsers);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        logInInterface.onError(throwable.getMessage());
                    }
                }));
    }

    public void getPlan(GetPLanInterface getPLanInterface, String qrCode) {
        compositeDisposable.add(apis.getPlan(qrCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray root = new JSONArray(responseBody.string());
                        listOfPlans.clear();
                        for (int i = 0; i < root.length(); i++) {
                            JSONObject single = root.getJSONObject(i);
                            int intAutoPicking = single.getInt("intAutoPicking");
                            String Storename = single.getString("Storename");
                            String Quantity = single.getString("Quantity");
                            String ItemCode = single.getString("ItemCode");
                            String Description = single.getString("Description");
                            String SalesOrderNo = single.getString("SalesOrderNo");
                            int OrderId = single.getInt("OrderId");
                            String mass = single.getString("mass");
                            int LineNos = single.getInt("LineNos");
                            String weights = single.getString("weights");
                            String OrderDate = single.getString("OrderDate");
                            String Instruction = single.getString("Instruction");
                            String Area = single.getString("Area");
                            String Toinvoice = single.getString("Toinvoice");

                            PlanModel model = new PlanModel(intAutoPicking,Storename,Quantity,ItemCode,Description,
                                    SalesOrderNo, OrderId, mass, LineNos, weights, OrderDate, Instruction,Area,Toinvoice);
                            listOfPlans.add(model);
                        }
                        getPLanInterface.gotPlan(listOfPlans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        getPLanInterface.error(throwable.getMessage());
                    }
                }));
    }
}
