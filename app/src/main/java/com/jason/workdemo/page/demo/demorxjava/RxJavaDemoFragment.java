package com.jason.workdemo.page.demo.demorxjava;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.jason.common.utils.ImageUtils;
import com.jason.common.utils.MLog;
import com.jason.common.utils.MToast;
import com.jason.workdemo.R;
import com.jason.workdemo.page.demo.demorxjava.bean.Course;
import com.jason.workdemo.page.demo.demorxjava.bean.Student;
import com.jason.workdemo.page.fragment.ContentFragment;
import com.jason.workdemo.page.mvp.BasePresenter;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liuzhenhui on 2016/10/28.
 */
public class RxJavaDemoFragment extends ContentFragment {
    public static final String TAG = RxJavaDemoFragment.class.getSimpleName();

    @BindView(R.id.btn_rxjava_print)
    Button btnPrint;

    @BindView(R.id.iv_rxjava_appicon)
    ImageView ivAppIcon;

    @BindView(R.id.iv_rxjava_fileimage)
    ImageView ivFileImage;

    @BindView(R.id.btn_rxjava_rxbind)
    Button btnRxBind;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rxjava;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onInitView() {
        RxView.clicks(btnRxBind)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MToast.show("RxBind");
                    }
                });
    }

    @OnClick(R.id.btn_rxjava_print)
    public void print() {
        // 打印数组
        String[] names = new String[4];
        names[0] = "jason";
        names[1] = "jason1";
        names[2] = "jason2";
        names[3] = "jason3";
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        MLog.d(MLog.TAG_TEST_RXJAVA, TAG + "->" + "call " + s);
                    }
                });

        // 加载图片资源
        final int drawableRes = R.mipmap.ic_launcher;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getActivity().getResources().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                // 指定subscribe()发生在io线程
                .subscribeOn(Schedulers.io())
                // 指定Subscriber的回调发生在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        ivAppIcon.setImageDrawable(drawable);
                    }

                    @Override
                    public void onCompleted() {
                        MLog.d(MLog.TAG_TEST_RXJAVA, TAG + "->" + "onCompleted ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.d(MLog.TAG_TEST_RXJAVA, TAG + "->" + "onError err = " + e.toString());
                    }
                });

        // API转换
        Observable.just(Environment.getExternalStorageDirectory() + File.separator + "test.png")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) {
                        return ImageUtils.decodeImage(filePath);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        ivFileImage.setImageBitmap(bitmap);
                    }
                });

        // flatmap
        Student[] students = new Student[5];
        students[0] = new Student("s1");
        students[1] = new Student("s2");
        students[2] = new Student("s3");
        students[3] = new Student("s4");
        students[4] = new Student("s5");
        Subscriber<Course> courseSubscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {
                MLog.d(MLog.TAG_TEST_RXJAVA, TAG + "->" + "onCompleted ");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                MLog.d(MLog.TAG_TEST_RXJAVA, TAG + "->" + "onNext course name = " + course.getName());
            }
        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                }).subscribe(courseSubscriber);
    }
}
