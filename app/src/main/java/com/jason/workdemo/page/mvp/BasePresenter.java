package com.jason.workdemo.page.mvp;


import com.jason.common.utils.MLog;

import java.util.LinkedList;

import rx.Subscription;

/**
 * Created by liuzhenhui on 2016/11/1.
 */
public abstract class BasePresenter<T> {
    private static final String TAG = BasePresenter.class.getSimpleName();

    LinkedList<Subscription> allSubscription = null;
    LinkedList<BaseModel> allModels = null;
    BaseView mBaseView;

    private BasePresenter() {

    }

    public BasePresenter(T t) {
        attachView((BaseView) t);
    }

    protected void attachView(BaseView view) {
        mBaseView = view;
    }

    /**
     * UI页面销毁时，调用该方法，释放Presenter中持有的View引用，防止内存泄露
     * 注意释放view后，之前发出的耗时操作回调中操作view引起空指针
     * 同时，尽量在该方法中反注册、取消一切耗时监听器（view销毁了，数据取回来已经无意义）
     */
    public void detachView() {
        if (allSubscription != null) {
            for (Subscription subscription : allSubscription) {
                if (subscription != null && subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
            allSubscription.clear();
            allSubscription = null;
        }

        if (allModels != null) {
            for (BaseModel allModel : allModels) {
                if (allModel != null) {
                    allModel.destroy();
                }
            }
            allModels.clear();
            allModels = null;
        }

        mBaseView = null;
    }

    protected T getView() {
        if (mBaseView == null) {
            MLog.e(MLog.TAG_MVP, TAG + "->" + " Presenter getView == null, Please call attachView method!");
        }
        return (T) mBaseView;
    }

    /**
     * 收集所有subscription，退出时反注册
     */
    protected void addSubscription(Subscription subscription) {
        if (subscription != null) {
            if (allSubscription == null) {
                allSubscription = new LinkedList<>();
            }
            allSubscription.add(subscription);
            logSubscriptionMsg();
        }
    }

    protected void removeSubscription(Subscription subscription) {
        if (subscription != null && allSubscription != null) {
            allSubscription.remove(subscription);
            logSubscriptionMsg();
        }
    }

    private void logSubscriptionMsg() {
        String subsStackInfos = "subscription in stack: [";
        if (allSubscription != null) {
            int size = allSubscription.size();
            for (int i = 0; i < size; i++) {
                subsStackInfos += allSubscription.get(i).getClass().getSimpleName() + "@" + allSubscription.get(i).hashCode();
                if (i < allSubscription.size() - 1)
                    subsStackInfos += ", ";
            }
        }
        subsStackInfos += "]";
        MLog.d(MLog.TAG_MVP, TAG + "-> " + subsStackInfos);
    }

    protected void addModel(BaseModel model) {
        if (model != null) {
            if (allModels == null) {
                allModels = new LinkedList<>();
            }
            allModels.add(model);
            logModelsMsg();
        }
    }

    protected void removeModel(BaseModel model) {
        if (model != null && allModels != null) {
            allModels.remove(model);
            logModelsMsg();
        }
    }

    protected void logModelsMsg() {
        String modelsStackStr = "model in stack: [";
        if (allModels != null) {
            int size = allModels.size();
            for (int i = 0; i < size; i++) {
                modelsStackStr += allModels.get(i).getClass().getSimpleName() + "@" + allModels.get(i).hashCode();
                if (i < allModels.size() - 1)
                    modelsStackStr += ", ";
            }
        }
        modelsStackStr += "]";
        MLog.d(MLog.TAG_MVP, TAG + "-> " + modelsStackStr);
    }

}
