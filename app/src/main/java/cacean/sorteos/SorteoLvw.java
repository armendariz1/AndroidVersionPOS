package cacean.sorteos;

/**
 * Created by CARLOSANTONIO on 08/04/2015.
 */
public class SorteoLvw {
    String id;
    String nombre;
    //Constructor
    public SorteoLvw(String id, String nombre) {
        super();
        this.id = id;
        this.nombre = nombre;
    }
    @Override
    public String toString() {
        return nombre;
    }
    public String getId() {
        return id;
    }
}