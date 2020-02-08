package com.example.app.ourapplication.rest.api;

import com.example.app.ourapplication.rest.model.request.CommentFeedReqModel;
import com.example.app.ourapplication.rest.model.request.HomeFeedReqModel;
import com.example.app.ourapplication.rest.model.request.ProfileFeedReqModel;
import com.example.app.ourapplication.rest.model.request.ProfileUpdateModel;
import com.example.app.ourapplication.rest.model.request.SignInReqModel;
import com.example.app.ourapplication.rest.model.request.TokenReqModel;
import com.example.app.ourapplication.rest.model.response.CompleteFeedModel;
import com.example.app.ourapplication.rest.model.response.ComposeRespModel;
import com.example.app.ourapplication.rest.model.response.GetDataRespModel;
import com.example.app.ourapplication.rest.model.response.Person;
import com.example.app.ourapplication.rest.model.response.ProfileRespModel;
import com.example.app.ourapplication.rest.model.response.SignInRespModel;
import com.example.app.ourapplication.rest.model.request.SignUpReqModel;
import com.example.app.ourapplication.rest.model.response.SuccessRespModel;
import com.example.app.ourapplication.rest.model.response.SubscriberDataRespModel;
import com.example.app.ourapplication.rest.model.request.SubscribeReqModel;

import com.example.app.ourapplication.rest.model.request.ChatPostReqModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sarumugam on 15/01/17.
 */
public interface RestApi {

    @POST("/signin")
    Call<SignInRespModel> signIn(@Body SignInReqModel signInReqModel);

    @POST("/signup")
    Call<Void> signUp(@Body SignUpReqModel signUpReqModel);

    @POST("/updateprofile")
    Call<SubscriberDataRespModel> updateProfile(@Body ProfileUpdateModel profileReqModel);

    @POST("/feedquery/home")
    Call<GetDataRespModel> queryHomeFeed(@Body HomeFeedReqModel homeFeedReqModel);

    @POST("/feedquery/profile")
    Call<GetDataRespModel> queryProfileFeed(@Body ProfileFeedReqModel profileFeedReqModel);

    @POST("/feedquery/comment")
    Call<GetDataRespModel> queryCommentFeed(@Body CommentFeedReqModel commentFeedReqModel);

    @POST("/postfeed")
    Call<ComposeRespModel> ComposeFeed(@Body CompleteFeedModel completeFeedModel);

    @POST("/updatetoken")
    Call<SuccessRespModel> UpdateToken(@Body TokenReqModel tokenReqModel);

    @POST("/postchat")
    Call<SuccessRespModel> ComposeChat(@Body Person person);

    @POST("/subscriber/subscribe")
    Call<SuccessRespModel> SubscribeFeed(@Body SubscribeReqModel subscribereqmodel);

    @POST("/subscriber/unsubscribe")
    Call<SuccessRespModel> UnSubscribeFeed(@Body SubscribeReqModel subscribereqmodel);

    @POST("/subscriber/dataquery")
    Call<SubscriberDataRespModel> querySubscriberData();
}