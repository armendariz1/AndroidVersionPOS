package cacean.sorteos;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by CARLOSANTONIO on 16/04/2015.
 */
public class MyWrapper{
    SoapObject soap;
    String data;
    Integer cant;
    String code;
    String message;

    public MyWrapper(SoapObject soap, String data, Integer cant, String code,String message) {
        this.soap = soap;
        this.data = data;
        this.cant = cant;
        this.code = code;
        this.message = message;
    }
}
