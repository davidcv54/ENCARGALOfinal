package mx.com.encargalo.http;

import mx.com.encargalo.http.models.RequestUser;
import mx.com.encargalo.http.models.ResponseUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("register_users.php")
    Call<ResponseUser> register(@Body RequestUser requests);


}
