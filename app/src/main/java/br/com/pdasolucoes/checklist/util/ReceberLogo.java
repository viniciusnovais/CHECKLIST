package br.com.pdasolucoes.checklist.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by PDA on 10/08/2017.
 */

public class ReceberLogo {

    private static String URL = "http://179.184.159.52/wsforms/wslogo.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/GetLogo ";

    private static String NAMESPACE = "http://tempuri.org/";

    public static Bitmap logo(int idUsuario) {
        byte[] imagem = null;
        Bitmap bmp = null;

        SoapObject response = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetLogo ");
        // Property which holds input parameters
        PropertyInfo id = new PropertyInfo();
        // Set Username
        id.setName("idUsuario");
        // Set Value
        id.setValue(idUsuario);
        // Set dataType
        id.setType(Integer.class);
        // Add the property to request object
        request.addProperty(id);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        new MarshalBase64().register(envelope);
        envelope.encodingStyle = SoapEnvelope.ENC;

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // Get the response
            response = (SoapObject) envelope.bodyIn;

            String result = response.getProperty("GetLogoResult").toString();

            if (result != "") {
                imagem = Base64.decode(result, Base64.DEFAULT);
                bmp = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Return byte to calling object
        return bmp;
    }
}
