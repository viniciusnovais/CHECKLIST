package br.com.pdasolucoes.checklist.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.model.Todo;

/**
 * Created by PDA on 19/05/2017.
 */

public class ServiceTodoPost {

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

    private static String URL = "http://179.184.159.52/wsforms/wstodo.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/SetToDo ";

    private static String NAMESPACE = "http://tempuri.org/";

    public static boolean postTodo(List<Todo> lista) {
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "SetToDo ");

        try {
            SoapObject soapLista = new SoapObject(NAMESPACE, "lista");
            for (Todo t : lista) {
                soapLista.addProperty("ToDoEO", t);
            }
            request.addSoapObject(soapLista);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            MarshalDate md = new MarshalDate();
            md.register(envelope);

            envelope.addMapping(NAMESPACE, "ToDoEO", new Todo().getClass());
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


            androidHttpTransport.call(SOAP_ACTION, envelope);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //Return booleam to calling object
        return true;
    }

}
