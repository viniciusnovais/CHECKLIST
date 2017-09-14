package br.com.pdasolucoes.checklist.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**
 * Created by PDA on 14/08/2017.
 */

public class ServiceVerificaForm {

    private static String URL = "http://179.184.159.52/wsforms/wsform.asmx";

    private static String SOAP_ACTION = "http://tempuri.org/setFormAlterado";

    private static String NAMESPACE = "http://tempuri.org/";

    public static int verifica(int idForm) {
        SoapObject response = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "setFormAlterado");
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        // Set Username
        unamePI.setName("id");
        // Set Value
        unamePI.setValue(idForm);
        // Set dataType
        unamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(unamePI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        int status = 0;
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // Get the response
            response = (SoapObject) envelope.bodyIn;

            status = Integer.parseInt(response.getProperty("int").toString());

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
            e.printStackTrace();
        }
        //Return status to calling object
        return status;
    }
}
