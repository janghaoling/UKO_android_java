package com.mobile.uko.network;

import com.mobile.uko.model.ChangePassword;
import com.mobile.uko.model.EventStatusUpdate;
import com.mobile.uko.model.EventsAll;
import com.mobile.uko.model.EventsIdModel;
import com.mobile.uko.model.ExplorerResponse;
import com.mobile.uko.model.GetLeaseAgreementUrl;
import com.mobile.uko.model.GetProfileImagePath;
import com.mobile.uko.model.LeaseAgreementHome;
import com.mobile.uko.model.LocationAllModel;
import com.mobile.uko.model.LocationIdModel;
import com.mobile.uko.model.LoginToken;
import com.mobile.uko.model.Maintenance;
import com.mobile.uko.model.Profile;
import com.mobile.uko.model.RegisterModel;
import com.mobile.uko.model.UserDetails;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("api/v1/user/signup")
    Call<ResponseBody> register(@Body RegisterModel body);

    @Headers("Content-Type: application/json")
    @POST("api/v1/user/signin")
    Call<LoginToken> login(@Body LoginToken body);

    @GET("api/v1/propertyLocations/all")
    Call<List<LocationAllModel>> getLocationAll();

    @GET("api/v1/propertyLocations/{id}")
    Call<LocationIdModel> getLocationId(@Path("id") Integer id);

    @GET("api/v1/verifyPasswordResetToken/{token}")
    Call<ResponseBody> verifyToken(@Path("token") String token);

    @GET("api/v1/explores")
    Call<ExplorerResponse> getExplorer();

    @Headers("Content-Type: application/json")
    @POST("api/v1/forgotPassword")
    Call<ResponseBody> forgotPassword(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("api/v1/resetPassword")
    Call<ResponseBody> resetPassword(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("api/v1/inquiry")
    Call<ResponseBody> enquirySignUp(@Body String body);

    @GET("api/v1/user/download/leaseAgreement")
    Call<ResponseBody> downloadLeaseAgreement();

    @GET("api/v1/user/getPresignedUrl/leaseAgreement")
    Call<GetLeaseAgreementUrl> getPresignedUrl();

    @GET("api/v1/user/leaseAgreementUpdateStatus")
    Call<ResponseBody> leaseAgreementUpdate();

    @POST("api/v1/user/leaseAgreementPath")
    Call<ResponseBody> leaseAgreementPath(@Body String body);

    @GET("api/v1/user/getProfile")
    Call<Profile> getProfile();

    @PUT("api/v1/user/updateProfile")
    Call<UserDetails> updateProfile(@Body UserDetails body);

    @PUT("api/v1/user/profileImagePath")
    Call<GetProfileImagePath> getProfileImagePath(@Body GetProfileImagePath path);

    @GET("api/v1/user/notices/all")
    Call<ResponseBody> getNoticesAll();

    @GET("api/v1/user/getPresignedUrl/profileImage")
    Call<GetLeaseAgreementUrl> getProfilePresignedUrl(@Query("fileName") String fileName, @Query("contentType") String contentType);

    @GET("api/v1/user/events/all")
    Call<List<EventsAll>> getEventsAll();

    @GET("api/v1/user/events/detail/{id}")
    Call<EventsIdModel> getEventsId(@Path("id") Integer id);

    @PUT("api/v1/user/event/statusUpdate")
    Call<EventStatusUpdate> updateEventStatus(@Body EventStatusUpdate body);

    @PUT("api/v1/user/changePassword")
    Call<ChangePassword> changePassword(@Body ChangePassword body);

    @GET("api/v1/user/home")
    Call<LeaseAgreementHome> getAgreementHome();

    @GET("api/v1/user/tickets/all")
    Call<List<Maintenance>> getMaintenanceAll(@Query("status") String status);
}
