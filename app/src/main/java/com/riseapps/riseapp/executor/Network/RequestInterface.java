package com.riseapps.riseapp.executor.Network;

import com.riseapps.riseapp.model.Pojo.Server.ContactRequest;
import com.riseapps.riseapp.model.Pojo.Server.ContactsResponse;
import com.riseapps.riseapp.model.Pojo.Server.LoginRequest;
import com.riseapps.riseapp.model.Pojo.Server.MessageRequest;
import com.riseapps.riseapp.model.Pojo.Server.ServerResponse;

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

    @POST("RiseApp/index.php")
    Call<ContactsResponse> contacts(@Body ContactRequest request);
}