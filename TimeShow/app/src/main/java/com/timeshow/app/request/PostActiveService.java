package com.timeshow.app.request;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by peibin on 18-2-12.
 */

public interface PostActiveService {
    @GET ("/active/add")
    Call<String> add (@Query ( "token" ) String token,
                      @Query ( "title" ) String title,
                      @Query ( "profile" ) String profile,
                      @Query ( "cost" ) String cost,
                      @Query ( "address" ) String address,
                      @Query ( "details" ) String details,
                      @Query ( "time" ) String time,
                      @Query ( "url" ) String url,
                      @Query ( "kind" ) String kind);
}
