package com.riseapps.riseapp.executor.Network;

import com.riseapps.riseapp.model.LoginRequest;
import com.riseapps.riseapp.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by naimish on 6/11/17.
 */

public interface RequestInterface {
    @POST("RiseApp/index.php")
    Call<LoginResponse> operation(@Body LoginRequest request);
}
