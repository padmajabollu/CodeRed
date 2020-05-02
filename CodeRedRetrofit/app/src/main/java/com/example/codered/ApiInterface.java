package com.example.codered;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("CodeRedApi.php")
    Call<List<status>> addStudent(

            @Field("choice") String choice,
            @Field("name") String name,
            @Field("phno") String phno,
            @Field("title") String title,
            @Field("media1") String media1
    );

    @FormUrlEncoded
    @POST("CodeRedApi.php")
    Call<List<status>> addPayment(
            @Field("choice") String choice,

            @Field("distributor") String distributor,
            @Field("customer") String customer,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("CodeRedApi.php")
    Call<List<status>> add(
            @Field("add") String add
    );

    @FormUrlEncoded
    @POST("CodeRedApi.php")
    Call<List<studentData>> getStudent(
            @Field("choice") String choice
    );

    @FormUrlEncoded
    @POST("CodeRedApi.php")
    Call<List<status>> deleteStudent(
            @Field("choice") String choice,
            @Field("student_id") String student_id
    );

    @FormUrlEncoded
    @POST("CodeRedApi.php")
    Call<List<paymentData>> getPayment(
            @Field("choice") String choice
    );


}
