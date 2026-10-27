package cr.ac.ucr.mijornada.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The ordered collection of study activities registered during the day.
 * Count and total are always computed from the collection so they can never get out of sync with it.
 */
public class StudyLog {

    private final ArrayList<ActividadEstudio> activities = new ArrayList<>();

    /**
     * Appends an activity at the end of the log.
     *
     * @param activity the activity to add
     */
    public void add(ActividadEstudio activity) {
        activities.add(activity);
    }

    /**
     * Removes the most recently added activity.
     *
     * @return {@code true} if an activity was removed, {@code false} if the log was already empty
     */
    public boolean removeLast() {
        if (activities.isEmpty()) {
            return false;
        }
        activities.remove(activities.size() - 1);
        return true;
    }

    /**
     * @return a read-only view of the activities in insertion order, so callers cannot bypass {@link #add}
     */
    public List<ActividadEstudio> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    /**
     * @return how many activities are in the log
     */
    public int getCount() {
        return activities.size();
    }

    /**
     * @return the sum of the minutes of every activity in the log
     */
    public int getTotalMinutes() {
        int total = 0;
        for (ActividadEstudio activity : activities) {
            total += activity.getMinutos();
        }
        return total;
    }
}
