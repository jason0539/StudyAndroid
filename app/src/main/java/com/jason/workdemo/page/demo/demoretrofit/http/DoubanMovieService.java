package com.jason.workdemo.page.demo.demoretrofit.http;


import com.jason.workdemo.page.demo.demoretrofit.entity.Subject;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liuzhenhui on 2016/10/27.
 */
public interface DoubanMovieService {
    @GET("top250")
    Observable<DoubanHttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);
}
