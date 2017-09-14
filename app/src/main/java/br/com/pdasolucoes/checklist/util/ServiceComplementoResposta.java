package br.com.pdasolucoes.checklist.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**
 * Created by PDA on 04/09/2017.
 */

public class ServiceComplementoResposta {

    private static String URL = "http://179.184.159.52/wsforms/wslogo.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/UploadArquivo";

    private static String NAMESPACE = "http://tempuri.org/";

    public static boolean postResposta(byte[] imagem) {

        try {
            // Create request
            SoapObject request = new SoapObject(NAMESPACE, "UploadArquivo");
            request.addProperty("nomeDoArquivo", "teste");
            request.addProperty("arquivoByte", imagem);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            MarshalBase64 md = new MarshalBase64();
            md.register(envelope);

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
