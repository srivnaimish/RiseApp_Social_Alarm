package com.riseapps.riseapp.executor.Network;

import com.riseapps.riseapp.model.LoginRequest;
import com.riseapps.riseapp.model.MessageRequest;
import com.riseapps.riseapp.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by naimish on 9/11/17.
 */

public interface RequestInterface {
    @POST("RiseApp/index.php")
    Call<ServerResponse> operation(@Body LoginRequest request);

    @POST("RiseApp/index.php")
    Call<ServerResponse> chat(@Body MessageRequest request);
}