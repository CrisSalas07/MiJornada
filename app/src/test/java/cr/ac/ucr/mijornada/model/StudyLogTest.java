package cr.ac.ucr.mijornada.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for {@link StudyLog}: adding, removing and the computed count and total.
 */
public class StudyLogTest {

    @Test
    public void threeAdds_giveCountAndTotal() {
        StudyLog log = new StudyLog();
        log.add(new ActividadEstudio("Cálculo", 30));
        log.add(new ActividadEstudio("Programación", 45));
        log.add(new ActividadEstudio("Inglés", 25));

        assertEquals(3, log.getCount());
        assertEquals(100, log.getTotalMinutes());
    }

    @Test
    public void removeLast_removesMostRecentActivity() {
        StudyLog log = new StudyLog();
        log.add(new ActividadEstudio("Cálculo", 30));
        log.add(new ActividadEstudio("Programación", 45));

        assertTrue(log.removeLast());
        assertEquals(1, log.getCount());
        assertEquals(30, log.getTotalMinutes());
        assertEquals("Cálculo", log.getActivities().get(0).getNombre());
    }

    @Test
    public void removeLast_onEmptyLog_returnsFalse() {
        StudyLog log = new StudyLog();

        assertFalse(log.removeLast());
        assertEquals(0, log.getCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getActivities_cannotBeModified() {
        StudyLog log = new StudyLog();
        log.getActivities().add(new ActividadEstudio("Cálculo", 30));
    }
}
