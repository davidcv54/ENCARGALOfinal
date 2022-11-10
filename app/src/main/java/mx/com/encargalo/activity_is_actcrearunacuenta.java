package mx.com.encargalo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.function.ToIntFunction;

import mx.com.encargalo.http.Api;
import mx.com.encargalo.http.ApiServices;
import mx.com.encargalo.http.models.Data;
import mx.com.encargalo.http.models.RequestUser;
import mx.com.encargalo.http.models.ResponseUser;
import mx.com.encargalo.utils.Activitys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_is_actcrearunacuenta extends AppCompatActivity {

    private EditText is_edtnombre, is_edtapellido, is_edtcorreo, is_spcodigodelpais, is_numero_celular;
    ImageView img_user;
    Button btncrearcuenta;
    ProgressDialog progressDialog;
    Spinner is_sptipopersona;
    String nombre, apellido, email, profile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_actcrearunacuenta);

        img_user = findViewById(R.id.img_user);
        is_edtnombre = findViewById(R.id.is_edtnombre);
        is_edtapellido = findViewById(R.id.is_edtapellido);
        is_edtcorreo = findViewById(R.id.is_edtcorreo);
        is_spcodigodelpais = findViewById(R.id.is_spcodigodelpais);
        is_numero_celular = findViewById(R.id.is_numero_celular);
        is_sptipopersona = findViewById(R.id.is_sptipopersona);

        Intent intent = getIntent();
        this.email = intent.getStringExtra("email");
        this.nombre = intent.getStringExtra("name");
        this.profile = intent.getStringExtra("profile");

        Glide.with(this).
                load(profile).
                into(img_user);

        is_edtnombre.setText("" + nombre);
        is_edtcorreo.setText("" + email);

        btncrearcuenta = (Button) findViewById(R.id.is_ispbtnRegistrarse);
        btncrearcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codigodelpais = is_spcodigodelpais.getText().toString();
                String nombre = is_edtnombre.getText().toString();
                String apellidos = is_edtapellido.getText().toString();
                String celular = is_numero_celular.getText().toString();
                String correo = is_edtcorreo.getText().toString();
                String text = is_sptipopersona.getSelectedItem().toString();

                if (nombre.isEmpty()){
                    Toast.makeText(activity_is_actcrearunacuenta.this, "Nombre requerido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (apellidos.isEmpty()){
                    Toast.makeText(activity_is_actcrearunacuenta.this, "Apellidos requeridos", Toast.LENGTH_LONG).show();
                    return;
                }

                if (celular.isEmpty()){
                    Toast.makeText(activity_is_actcrearunacuenta.this, "Celular requerido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (codigodelpais.isEmpty()){
                    Toast.makeText(activity_is_actcrearunacuenta.this, "ID Identificacion requerido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (text.isEmpty()){
                    Toast.makeText(activity_is_actcrearunacuenta.this, "Debe de seleccionar el tipo de persona", Toast.LENGTH_LONG).show();
                    return;
                }

                RequestUser requestUser = new RequestUser();
                requestUser.setSp_usuCorreo(correo);
                requestUser.setSp_usuImagen(profile);
                requestUser.setNomsp_idRolUusuariobre("1");
                requestUser.setSp_idDocumentoPersona(codigodelpais);
                requestUser.setSp_perNombres(nombre);
                requestUser.setSp_perApellidos(apellidos);
                requestUser.setSp_perTipo(text);
                requestUser.setSp_perNumeroCelular(celular);
                registerAPI(requestUser);

            }
        });



    }


    private void registerAPI(RequestUser user){

        progressDialog = new ProgressDialog(activity_is_actcrearunacuenta.this);
        progressDialog.setMessage("Creando cuenta espere un momento....");
        progressDialog.show();

        String json = new Gson().toJson(user);
        Log.d("Register Users", json);

        Call<ResponseUser> singin = ApiServices.getClientRestrofit().create(Api.class).register(user);

        singin.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){

                    for ( Data item:response.body().getData()) {
                        if (item.getSuccess()){
                            Activitys.getSingleton(activity_is_actcrearunacuenta.this   , activity_is_actcrearunacuenta.class).muestraActividad();
                            finish();
                        }else {
                            Toast.makeText(activity_is_actcrearunacuenta.this, "" + item.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


    }





}
