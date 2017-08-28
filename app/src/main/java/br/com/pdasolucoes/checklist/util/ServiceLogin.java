package br.com.pdasolucoes.checklist.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.pdasolucoes.checklist.model.Usuario;

/**
 * Created by PDA on 06/02/2017.
 */

public class ServiceLogin {

    private static String URL = "http://179.184.159.52/wsforms/wsusuario.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/Login";

    private static String NAMESPACE = "http://tempuri.org/";


    public static JSONObject Login(Usuario usuario) {

        String resposta = "";
        JSONObject jsonObject = null;
        Gson gson = new Gson();
        String json = gson.toJson(usuario);

        try {
            URL url = new URL(WebService.URL + "/login");
            HttpURLConnection conexao = null;
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestProperty("Content-Type", "application/json");
            conexao.setRequestMethod("POST");
            conexao.setDoInput(true);
            conexao.setDoOutput(true);

            conexao.connect();

            OutputStream out = conexao.getOutputStream();

            out.write(json.getBytes("UTF-8"));
            out.flush();
            out.close();
            InputStream in = null;
            int status = conexao.getResponseCode();
            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                Log.w("ERRO", "Error code: " + status);
                in = conexao.getErrorStream();
            } else {
                in = conexao.getInputStream();
            }

            resposta = WebService.readStream(in);
            in.close();
            Log.w("resposta", resposta);
            jsonObject = new JSONObject(resposta);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static SoapObject invokeLoginWS(Usuario usuario) {
        SoapObject response = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "Login");
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        PropertyInfo passPI = new PropertyInfo();
        // Set Username
        unamePI.setName("nome");
        // Set Value
        unamePI.setValue(usuario.getEmail());
        // Set dataType
        unamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(unamePI);
        //Set Password
        passPI.setName("senha");
        //Set dataType
        passPI.setValue(usuario.getSenha());
        //Set dataType
        passPI.setType(String.class);
        //Add the property to request object
        request.addProperty(passPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // Get the response
            response = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
            //LoginActivity.errored = true;
            e.printStackTrace();
        }
        //Return booleam to calling object
        return response;
    }
}
