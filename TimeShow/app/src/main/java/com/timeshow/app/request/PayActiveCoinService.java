package com.timeshow.app.request;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by peibin on 18-2-12.
 */

public interface PayActiveCoinService {
    @GET ("/active/pay")
    Call<String> transfer (@Query ( "token" ) String token,
                           @Query ( "phone" ) String phone,
                           @Query ( "money" ) String money,
                           @Query ( "id" ) String id);
}
