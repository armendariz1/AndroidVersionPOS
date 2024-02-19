package cacean.sorteos;

/**
 * Created by CARLOSANTONIO on 09/04/2015.
 */
public class Apuesta {

    private String idApuesta;
    private String numero;
    private String montoPrimero;
    private String montoSegundo;
    private String montoTercero;


    //Constructor
    public Apuesta(String idApuesta, String numero, String montoPrimero, String montoSegundo, String montoTercero) {
        super();
        this.idApuesta = idApuesta;
        this.numero = numero;
        this.montoPrimero = montoPrimero;
        this.montoSegundo = montoSegundo;
        this.montoTercero = montoTercero;
        
    }
    public String getIdApuesta() {
        return idApuesta;
    }

    public void setIdApuesta(String idApuesta) {
        this.idApuesta = idApuesta;
    }

    public String getMontoPrimero() {
        return montoPrimero;
    }

    public void setMontoPrimero(String montoPrimero) {
        this.montoPrimero = montoPrimero;
    }

    public String getMontoSegundo() {
        return montoSegundo;
    }

    public void setMontoSegundo(String montoSegundo) {
        this.montoSegundo = montoSegundo;
    }

    public String getMontoTercero() {
        return montoTercero;
    }

    public void setMontoTercero(String montoTercero) {
        this.montoTercero = montoTercero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
