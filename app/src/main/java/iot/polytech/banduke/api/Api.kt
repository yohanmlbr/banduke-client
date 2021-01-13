package iot.polytech.banduke.api

import retrofit2.Call
import retrofit2.http.*
import iot.polytech.banduke.models.*

interface Api {

    //auth
    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    fun signUpUser(@Body suu: SignUpUser):Call<TokenResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun logInUser(@Body liu: LogInUser):Call<TokenResponse>


    //user
    @Headers("Content-Type: application/json")
    @GET("user/{id}")
    fun getUserDetailsById(@Path("id") id: Int, @Header("Authorization") token: String):Call<UserDetails>

    @Headers("Content-Type: application/json")
    @GET("user/list")
    fun getAllUsers(@Header("Authorization") token: String):Call<List<User>>

    @Headers("Content-Type: application/json")
    @PUT("user/update")
    fun editUser(@Body eu: EditUser, @Header("Authorization") token: String):Call<EditUser>


    //friends
    @Headers("Content-Type: application/json")
    @GET("friends/{id}")
    fun getFriendsByUserId(@Path("id") id: Int, @Header("Authorization") token: String):Call<List<User>>

    @Headers("Content-Type: application/json")
    @GET("friends/{id}/isFriend/{friend}")
    fun isFriend(@Path("id") id: Int, @Path("friend") friend: Int, @Header("Authorization") token: String):Call<ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("friends/{id}/add/{friend}")
    fun addFriend(@Path("id") id: Int, @Path("friend") friend: Int, @Header("Authorization") token: String):Call<ApiResponse>

    @Headers("Content-Type: application/json")
    @DELETE("friends/{id}/delete/{friend}")
    fun deleteFriend(@Path("id") id: Int, @Path("friend") friend: Int, @Header("Authorization") token: String):Call<ApiResponse>


    //sessions
    @Headers("Content-Type: application/json")
    @GET("session/list/{id}")
    fun getSessionsByUserId(@Path("id") id: Int, @Header("Authorization") token: String):Call<List<SessionIdData>>

    @Headers("Content-Type: application/json")
    @GET("session/{id}")
    fun getSessionById(@Path("id") id: Int, @Header("Authorization") token: String):Call<Session>

    @Headers("Content-Type: application/json")
    @GET("session/{id}/content")
    fun getSessionContentById(@Path("id") id: Int, @Header("Authorization") token: String):Call<SessionContent>

    @Headers("Content-Type: application/json")
    @PUT("session/{id}/rename")
    fun renameSessionsById(@Path("id") id: Int, @Body newName: String, @Header("Authorization") token: String):Call<ApiResponse>
}