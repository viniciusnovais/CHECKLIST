package br.com.pdasolucoes.checklist.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.pdasolucoes.checklist.model.Resposta;

/**
 * Created by PDA on 19/05/2017.
 */

public class ServiceRespostaPost {

    public static void post(Resposta r) throws JSONException {
        String resposta = "";


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idResposta", r.getIdResposta());
        jsonObject.put("txtResposta", r.getTxtResposta());
        jsonObject.put("idPergunta", r.getIdPergunta());
        jsonObject.put("idFormItem", r.getIdFormItem());
        jsonObject.put("idOpcao", r.getIdOpcao());
        jsonObject.put("idTodo", r.getTodo());

        try {
            URL url = new URL(WebService.URL_SHEETS + "f541bc46cf58");
            HttpURLConnection conexao = null;
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setDoInput(true);
            conexao.setDoOutput(true);
            conexao.setRequestProperty("Content-Type", "application/json");
            conexao.setRequestProperty("Accept", "application/json");
            conexao.setRequestMethod("POST");


            conexao.connect();
            OutputStreamWriter wr = new OutputStreamWriter(conexao.getOutputStream());
            wr.write(jsonObject.toString());

            OutputStream os = conexao.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
            //os.close();

            InputStream in;
            int status = conexao.getResponseCode();
            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                Log.d("ERRO", "Error code: " + status);
                in = conexao.getErrorStream();
            } else {
                in = conexao.getInputStream();
            }

            resposta = WebService.readStream(in);
            Log.w("Resposta", resposta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
