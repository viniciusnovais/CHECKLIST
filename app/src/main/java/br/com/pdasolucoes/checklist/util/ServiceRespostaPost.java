package br.com.pdasolucoes.checklist.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.model.OpcaoResposta;
import br.com.pdasolucoes.checklist.model.Resposta;

/**
 * Created by PDA on 19/05/2017.
 */

public class ServiceRespostaPost {

//    public static void post(Resposta r) throws JSONException {
//        String resposta = "";
//
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("idResposta", r.getIdResposta());
//        jsonObject.put("txtResposta", r.getTxtResposta());
//        jsonObject.put("idPergunta", r.getIdPergunta().getIdPergunta());
//        jsonObject.put("idFormItem", r.getIdFormItem());
//        jsonObject.put("idOpcao", r.getIdOpcao());
//        jsonObject.put("idTodo", r.getTodo());
//
//
//        try {
//            URL url = new URL(WebService.URL_SHEETS + "f541bc46cf58");
//            HttpURLConnection conexao = null;
//            conexao = (HttpURLConnection) url.openConnection();
//            conexao.setDoInput(true);
//            conexao.setDoOutput(true);
//            conexao.setRequestProperty("Content-Type", "application/json");
//            conexao.setRequestProperty("Accept", "application/json");
//            conexao.setRequestMethod("POST");
//
//
//            conexao.connect();
//            OutputStreamWriter wr = new OutputStreamWriter(conexao.getOutputStream());
//            wr.write(jsonObject.toString());
//
//            OutputStream os = conexao.getOutputStream();
//            os.write(jsonObject.toString().getBytes("UTF-8"));
//            //os.close();
//
//            InputStream in;
//            int status = conexao.getResponseCode();
//            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
//                Log.d("ERRO", "Error code: " + status);
//                in = conexao.getErrorStream();
//            } else {
//                in = conexao.getInputStream();
//            }
//
//            resposta = WebService.readStream(in);
//            Log.w("Resposta", resposta);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static String URL = "http://179.184.159.52/wsforms/wsresposta.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/SetResposta";

    private static String NAMESPACE = "http://tempuri.org/";

    public static List<Resposta> postResposta(List<Resposta> lista) {

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "SetResposta");

        try {
            SoapObject soapLista = new SoapObject(NAMESPACE, "lista");
            for (Resposta r : lista) {
                soapLista.addProperty("RespostaFormsEO", r);
            }
            request.addSoapObject(soapLista);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            MarshalFloat md = new MarshalFloat();
            md.register(envelope);

            envelope.addMapping(NAMESPACE, "RespostaFormsEO", new Resposta().getClass());
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


            androidHttpTransport.call(SOAP_ACTION, envelope);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //Return booleam to calling object
        return lista;
    }

}
