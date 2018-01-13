package com.ryanfung.cnct.network;

import com.ryanfung.cnct.models.AccessToken;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.models.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CnctApi {

    @POST("oauth/token?grant_type=client_credentials")
    Observable<AccessToken> clientCredentials(
            @Header("Authorization") String authorization);

    @POST("oauth/token?grant_type=password")
    @FormUrlEncoded
    Observable<AccessToken> authenticate(
            @Header("Authorization") String authorization,
            @Field("email") String email,
            @Field("password") String password);

    @POST("api/v1/users")
    @FormUrlEncoded
    Observable<User> createUser(
            @Header("Authorization") String authorization,
            @Field("user[username]") String username,
            @Field("user[email]") String email,
            @Field("user[password]") String password);

    @PUT("api/v1/users/{id}")
    @FormUrlEncoded
    Observable<User> updateUser(
            @Path("id") String userId,
            @FieldMap Map<String, String> params);

    @GET("api/v1/me")
    Observable<User> me();

    @POST("api/v1/devices")
    @FormUrlEncoded
    Observable<Device> createDevice(
            @Field("device[uuid]") String uuid);

    @POST("api/v1/networks")
    @FormUrlEncoded
    Observable<Network> createNetwork(
            @FieldMap Map<String, String> params);

    @DELETE("api/v1/networks/{id}")
    Observable<Network> deleteNetwork(
            @Path("id") String id);

    @PUT("api/v1/networks/{id}")
    @FormUrlEncoded
    Observable<Network> updateNetwork(
            @Path("id") String id,
            @FieldMap Map<String, String> params);

}
