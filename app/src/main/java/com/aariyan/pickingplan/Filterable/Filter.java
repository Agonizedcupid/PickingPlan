package com.aariyan.pickingplan.Filterable;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.aariyan.pickingplan.Model.PlanModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;

public class Filter {

    private Context context;

    public Filter(Context context) {
        this.context = context;
    }

    public List<PlanModel> getFilteredData(List<PlanModel> listOfPlans, int flag) {
        List<PlanModel> list = new ArrayList<>();
        //Observable<PlanModel> observable = Observable.fromIterable(listOfPlans).filter(planModel -> planModel.getToLoad().equals("0") || TextUtils.isEmpty(planModel.getToLoad()));
        Observable<PlanModel> observable = Observable.
                fromIterable(listOfPlans)
                .filter(new Predicate<PlanModel>() {
                    @Override
                    public boolean test(PlanModel planModel) throws Throwable {
                        if (flag == 1) {
                            //Remaining
                            return planModel.getToLoad().equals("0") || TextUtils.isEmpty(planModel.getToLoad());
                        } else {
                            //Loaded
                            return Integer.parseInt(planModel.getToLoad()) > 0;
                        }
                    }
                });

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                list.add((PlanModel) o);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        return list;
    }
}
