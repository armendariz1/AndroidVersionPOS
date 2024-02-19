package cacean.sorteos;

/**
 * Created by CARLOSANTONIO on 05/04/2015.
 */
public class Sorteo {
    private String configuracion;
    private String estatus;
    private String fechaSorteo;
    private String idSorteo;
    private String nombreSorteo;


    public String getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(String configuracion) {
        this.configuracion = configuracion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaSorteo() {
        return fechaSorteo;
    }

    public void setFechaSorteo(String fechaSorteo) {
        this.fechaSorteo = fechaSorteo;
    }

    public String getIdSorteo() {
        return idSorteo;
    }

    public void setIdSorteo(String idSorteo) {
        this.idSorteo = idSorteo;
    }

    public String getNombreSorteo() {
        return nombreSorteo;
    }

    public void setNombreSorteo(String nombreSorteo) {
        this.nombreSorteo = nombreSorteo;
    }
}
