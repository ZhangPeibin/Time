package com.timeshow.app.request;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by peibin on 18-2-12.
 */

public interface UpdateUserInfoService {
    @GET ("/user/setinfor")
    Call<String> update (@Query ( "token" ) String token,
                         @Query ( "title" ) String title,
                         @Query ( "profile") String profile,
                         @Query ( "head") String head,
                         @Query ( "type") String type,
                         @Query ( "mobile") String mobile,
                         @Query ( "card") String card);
}
