package com.aariyan.pickingplan.Networking;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.aariyan.pickingplan.Constant.Constant;
import com.aariyan.pickingplan.Database.DatabaseAdapter;
import com.aariyan.pickingplan.Interface.GetPLanInterface;
import com.aariyan.pickingplan.Interface.LogInInterface;
import com.aariyan.pickingplan.Interface.RestApis;
import com.aariyan.pickingplan.Model.AuthenticationModel;
import com.aariyan.pickingplan.Model.PlanModel;
import com.aariyan.pickingplan.Model.PostModel;
import com.aariyan.pickingplan.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
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
    DatabaseAdapter databaseAdapter;
    private Activity activity;

    public NetworkingFeedback(Context context, Activity activity) {
        this.context = context;
        apis = ApiClient.getClient().create(RestApis.class);
        listOfUsers = new ArrayList<>();
        listOfPlans = new ArrayList<>();
        databaseAdapter = new DatabaseAdapter(context);
        this.activity = activity;
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

    public void getPlan(GetPLanInterface getPLanInterface, String qrCode, CoordinatorLayout snackBarLayout) {
        listOfPlans.clear();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listOfPlans = databaseAdapter.getPlansByReference(qrCode);
            }
        });
        if (listOfPlans.size() > 0) {
            getPLanInterface.gotPlan(listOfPlans);
            Snackbar.make(snackBarLayout, "Data showing from local storage", Snackbar.LENGTH_SHORT).show();

        } else {
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

                                PlanModel model = new PlanModel(intAutoPicking, Storename, Quantity, ItemCode, Description,
                                        SalesOrderNo, OrderId, mass, LineNos, weights, OrderDate, Instruction, Area, Toinvoice,
                                        "0", qrCode);
//                                model.setReference(qrCode);
//                                model.setToLoad("0");
                                listOfPlans.add(model);
                            }
                            getPLanInterface.gotPlan(listOfPlans);
                            insertIntoSqliteDatabase(listOfPlans, qrCode, snackBarLayout);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Throwable {
                            getPLanInterface.error(throwable.getMessage());
                        }
                    }));
        }
    }

    //Insert into SQLite database:
    private void insertIntoSqliteDatabase(List<PlanModel> listOfPlans, String reference, CoordinatorLayout snackBarLayout) {

        Observable<PlanModel> observable = Observable.fromIterable(listOfPlans)
                .subscribeOn(Schedulers.io());
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(Object o) {
                PlanModel model = (PlanModel) o;
                long id = databaseAdapter.insertPlans(model.getIntAutoPicking(), model.getStorename(), model.getQuantity(),
                        model.getItemCode(), model.getDescription(), model.getSalesOrderNo(), model.getOrderId(),
                        model.getMass(), model.getLineNos(), model.getWeights(), model.getOrderDate(), model.getInstruction(),
                        model.getArea(), model.getToinvoice(), model.getToLoad(), reference);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Snackbar.make(snackBarLayout, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Snackbar.make(snackBarLayout, "Saved on local storage", Snackbar.LENGTH_SHORT).show();
            }
        };
        observable.subscribe(observer);
    }

    //Post on server database:
    public void postDataOnServer(List<PostModel> listOfPostData, ConstraintLayout snackBarLayout, int userId) {

        Observable<PostModel> observable = Observable.fromIterable(listOfPostData)
                .subscribeOn(Schedulers.io());
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
//                if (d.isDisposed()) {
//                    Snackbar.make(snackBarLayout, "Data uploaded successfully!", Snackbar.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onNext(Object o) {
                PostModel model = (PostModel) o;
                compositeDisposable.add(apis
                        .postPickedQty(model.getPickingId(),
                                Integer.parseInt(model.getQuantity()),
                                userId)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody responseBody) throws Throwable {
                                //JSONArray data = new JSONArray(responseBody.string());
                                Snackbar.make(snackBarLayout, "Data is uploading", Snackbar.LENGTH_SHORT).show();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.d("ERROR_TESTING", throwable.getMessage());
                                Snackbar.make(snackBarLayout, "Error: " + throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Snackbar.make(snackBarLayout, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Snackbar.make(snackBarLayout, "Data posted successfully", Snackbar.LENGTH_SHORT).show();
            }
        };
        observable.subscribe(observer);
    }
}
