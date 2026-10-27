package cr.ac.ucr.mijornada;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import cr.ac.ucr.mijornada.model.ActividadEstudio;
import cr.ac.ucr.mijornada.model.ActivityValidator;
import cr.ac.ucr.mijornada.model.ActivityValidator.ValidationResult;
import cr.ac.ucr.mijornada.model.StudyLog;

/**
 * The only screen of the app. It reads the form, asks {@link ActivityValidator} whether the input is valid,
 * stores valid activities in a {@link StudyLog} and shows the list and summary. All rules live in the model.
 */
public class MainActivity extends AppCompatActivity {

    private static final String KEY_NAMES = "names";
    private static final String KEY_MINUTES = "minutes";

    private final StudyLog studyLog = new StudyLog();

    private EditText editName;
    private EditText editMinutes;
    private TextView textSummary;
    private TextView textActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // ime() is included so the ScrollView shrinks above the keyboard; edge-to-edge disables adjustResize.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editName = findViewById(R.id.editName);
        editMinutes = findViewById(R.id.editMinutes);
        textSummary = findViewById(R.id.textSummary);
        textActivities = findViewById(R.id.textActivities);

        TextView labelMinutes = findViewById(R.id.labelMinutes);
        labelMinutes.setText(getString(R.string.label_minutes,
                ActivityValidator.MIN_MINUTES, ActivityValidator.MAX_MINUTES));

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> registerActivity());

        Button buttonDeleteLast = findViewById(R.id.buttonDeleteLast);
        buttonDeleteLast.setOnClickListener(v -> deleteLastActivity());

        // Null on the first launch; non-null when Android recreates the Activity, e.g. after a rotation.
        if (savedInstanceState != null) {
            restoreActivities(savedInstanceState);
        }
        render();
    }

    /**
     * Saves the activities before the Activity is destroyed by a configuration change such as a rotation.
     * A Bundle cannot hold our own objects directly, so each field goes into its own list in the same order.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> minutes = new ArrayList<>();
        for (ActividadEstudio activity : studyLog.getActivities()) {
            names.add(activity.getNombre());
            minutes.add(activity.getMinutos());
        }
        outState.putStringArrayList(KEY_NAMES, names);
        outState.putIntegerArrayList(KEY_MINUTES, minutes);
    }

    /**
     * Rebuilds the activities from the two parallel lists saved in {@link #onSaveInstanceState}.
     */
    private void restoreActivities(Bundle savedInstanceState) {
        ArrayList<String> names = savedInstanceState.getStringArrayList(KEY_NAMES);
        ArrayList<Integer> minutes = savedInstanceState.getIntegerArrayList(KEY_MINUTES);
        if (names == null || minutes == null) {
            return;
        }
        // Position i of both lists belongs to the same activity, so the original order is kept.
        for (int i = 0; i < names.size(); i++) {
            studyLog.add(new ActividadEstudio(names.get(i), minutes.get(i)));
        }
    }

    /**
     * Validates the form and, only if it is valid, adds the activity, refreshes the screen and resets the form.
     */
    private void registerActivity() {
        String name = editName.getText().toString();
        String minutesText = editMinutes.getText().toString();

        ValidationResult result = ActivityValidator.validate(name, minutesText);
        switch (result) {
            case EMPTY_NAME:
                showError(editName, getString(R.string.error_empty_name));
                return;
            case INVALID_MINUTES:
                showError(editMinutes, getString(R.string.error_invalid_minutes));
                return;
            case OUT_OF_RANGE:
                showError(editMinutes, getString(R.string.error_out_of_range,
                        ActivityValidator.MIN_MINUTES, ActivityValidator.MAX_MINUTES));
                return;
            default:
                break;
        }

        // Safe to parse: the validator already confirmed it is a whole number in range.
        studyLog.add(new ActividadEstudio(name.trim(), Integer.parseInt(minutesText.trim())));
        render();

        editName.getText().clear();
        editMinutes.getText().clear();
        editName.requestFocus();
    }

    /**
     * Removes the most recent activity, or tells the user there is nothing to remove.
     */
    private void deleteLastActivity() {
        if (studyLog.removeLast()) {
            render();
        } else {
            Toast.makeText(this, R.string.toast_empty_list, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Marks a field as wrong and moves the cursor there so the user can fix it right away.
     */
    private void showError(EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }

    /**
     * Rebuilds the numbered list and the summary from the collection, the single source of truth.
     */
    private void render() {
        List<ActividadEstudio> activities = studyLog.getActivities();

        if (activities.isEmpty()) {
            textActivities.setText(R.string.list_empty);
        } else {
            StringBuilder list = new StringBuilder();
            for (int i = 0; i < activities.size(); i++) {
                ActividadEstudio activity = activities.get(i);
                if (i > 0) {
                    list.append('\n');
                }
                // Positions start at 1 for the user, while list indexes start at 0.
                list.append(getString(R.string.list_item, i + 1, activity.getNombre(), activity.getMinutos()));
            }
            textActivities.setText(list);
        }

        int count = studyLog.getCount();
        textSummary.setText(getResources().getQuantityString(
                R.plurals.summary, count, count, studyLog.getTotalMinutes()));
    }
}
