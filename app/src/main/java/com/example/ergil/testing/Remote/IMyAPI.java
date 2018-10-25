package com.example.ergil.testing.Remote;

import com.example.ergil.testing.Model.APIResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyAPI {
    @FormUrlEncoded
    @POST("login.php")
    Call<APIResponse> loginUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<APIResponse> registerUser(@Field("name") String name, @Field("lastname") String lastname, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("forgot_pass.php")
    Call<APIResponse> forgotPass(@Field("email") String email);

    @FormUrlEncoded
    @POST("reset_pass.php")
    Call<APIResponse> resetPass(@Field("token") String token, @Field("email") String email, @Field("password") String password);

    @POST("logout.php")
    Call<APIResponse> logout();

    @POST("get_info.php")
    Call<APIResponse> getInfo();

    @POST("delete.php")
    Call<APIResponse> delete();
}
