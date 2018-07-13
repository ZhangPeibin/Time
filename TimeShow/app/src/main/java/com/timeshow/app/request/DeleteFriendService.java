package com.timeshow.app.request;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by peibin on 18-2-12.
 */

public interface DeleteFriendService {
    @GET ("/user/deletefriend")
    Call<String> add (@Query ( "token" ) String token,
                      @Query ( "rphone" ) String title);
}
