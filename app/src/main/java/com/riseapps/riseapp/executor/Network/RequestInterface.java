package com.riseapps.riseapp.executor.Network;

import com.riseapps.riseapp.model.Pojo.Server.ContactRequest;
import com.riseapps.riseapp.model.Pojo.Server.ContactsResponse;
import com.riseapps.riseapp.model.Pojo.Server.LoginRequest;
import com.riseapps.riseapp.model.Pojo.Server.MessageRequest;
import com.riseapps.riseapp.model.Pojo.Server.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by naimish on 9/11/17.
 */

public interface RequestInterface {
    @POST("/index.php")
    Call<ServerResponse> operation(@Body LoginRequest request);

    @POST("/index.php")
    Call<ServerResponse> chat(@Body MessageRequest request);

    @POST("/index.php")
    Call<ContactsResponse> contacts(@Body ContactRequest request);

    @Multipart
    @POST("/dp_client.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
}