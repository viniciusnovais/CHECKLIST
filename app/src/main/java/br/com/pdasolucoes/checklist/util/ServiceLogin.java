package br.com.pdasolucoes.checklist.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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


    public static JSONObject Login(Usuario usuario) {

        String resposta = "";
        JSONObject jsonObject=null;
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
            Log.w("resposta",resposta);
            jsonObject = new JSONObject(resposta);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
