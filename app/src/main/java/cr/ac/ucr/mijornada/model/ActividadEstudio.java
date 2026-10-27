package cr.ac.ucr.mijornada.model;

/**
 * A single study activity: what was studied and for how many minutes.
 * Immutable so that once an activity is in the log nobody can change it behind the log's back.
 */
public class ActividadEstudio {

    private final String nombre;
    private final int minutos;

    /**
     * Creates an activity. Callers are expected to validate the values first with {@link ActivityValidator}.
     *
     * @param nombre  name of the activity
     * @param minutos duration in minutes
     */
    public ActividadEstudio(String nombre, int minutos) {
        this.nombre = nombre;
        this.minutos = minutos;
    }

    /**
     * @return the activity name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the duration in minutes
     */
    public int getMinutos() {
        return minutos;
    }
}
