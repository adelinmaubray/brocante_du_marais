package be.ami.maubray.brocantedumarais.scanner.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Vibrator;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import be.ami.maubray.brocantedumarais.scanner.database.ClassHandler;
import be.ami.maubray.brocantedumarais.scanner.database.DatabaseHandler;
import be.ami.maubray.brocantedumarais.scanner.models.Emplacement;
import be.ami.maubray.brocantedumarais.scanner.models.Entry;
import be.ami.maubray.brocantedumarais.scanner.scanner.Scanner;
import maubray.ami.brocantedumarais.R;


public class MainActivity extends AppCompatActivity {

    // The result code
    public static final int RESULT_CODE = 1;
    // The request code
    private static final int QR_REQUEST_CODE = 81;
    private static final int READ_REQUEST_CODE = 82;
    private static final int WRITE_REQUEST_CODE = 83;
    // Vibration duration
    private static final int VIB_DURATION_SUCCESS = 150; // 0.2 seconds
    private static final int VIB_DURATION_FAILED = 800; // 0.6 seconds
    // The content of the QR Code
    public static String QR_CODE_READ;
    // The databaseHandler
    private ClassHandler databaseHandler;
    // The selected entry
    private String entrySelected;
    // EditText
    private EditText editText;
    private TextView scanNumber;
    // Buttons
    private RadioButton[] entries = new RadioButton[5];
    // TextViews
    private TextView totalNumber;
    // Vibration
    private Vibrator vibrator;
    // Classes to play sounds
    private SoundPool soundPool;
    private int soundRight;
    private int soundWrong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get EditText
        editText = findViewById(R.id.input_spot);

        // Get Buttons
        entries[0] = findViewById(R.id.entree_0);
        entries[1] = findViewById(R.id.entree_1);
        entries[2] = findViewById(R.id.entree_2);
        entries[3] = findViewById(R.id.entree_3);
        entries[4] = findViewById(R.id.entree_4);

        // Get TextViews
        totalNumber = findViewById(R.id.total);
        scanNumber = findViewById(R.id.scan);

        // Create and open databaseHandler
        databaseHandler = new ClassHandler(this);
        databaseHandler.open();

        // Get Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Sound initialisation
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        soundRight = soundPool.load(this, R.raw.right, 1);
        soundWrong = soundPool.load(this, R.raw.wrong, 1);

        showButtons();
    }

    @Override
    protected void onDestroy() {
        // Close the databaseHandler when the activity is closed
        super.onDestroy();
        databaseHandler.close();
    }

    /**
     * Handle the results of the activities
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // QR Code
            case QR_REQUEST_CODE:
                if (resultCode == RESULT_CODE) {
                    QR_CODE_READ = QR_CODE_READ.substring(0, QR_CODE_READ.length() - 2);
                    Emplacement emplacement = databaseHandler.selectByCode(QR_CODE_READ);
                    check(emplacement, QR_CODE_READ);
                }
                break;

            // Data importation
            case READ_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    readFile(uri);
                }
                break;

            // Data exportation
            case WRITE_REQUEST_CODE:
                Uri uri = data.getData();
                write(uri);
                break;
            default:
                break;
        }
    }

    /**
     * Modification of the entry when a RadioButton is pressed
     *
     * @param view The View
     */
    public void changeEntryClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        vibrator.vibrate(VIB_DURATION_SUCCESS);
        soundPool.play(soundRight, 1, 1, 1, 0, 1);
        switch (view.getId()) {
            case R.id.entree_0:
                if (checked) {
                    String[] entryNumber = entries[0].getText().toString().split(" ");
                    entrySelected = entryNumber[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entree_1:
                if (checked) {
                    String[] entryNumber = entries[1].getText().toString().split(" ");
                    entrySelected = entryNumber[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entree_2:
                if (checked) {
                    String[] entryNumber = entries[2].getText().toString().split(" ");
                    entrySelected = entryNumber[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entree_3:
                if (checked) {
                    String[] entryNumber = entries[3].getText().toString().split(" ");
                    entrySelected = entryNumber[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entree_4:
                if (checked) {
                    String[] entryNumber = entries[4].getText().toString().split(" ");
                    entrySelected = entryNumber[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        setNumbers();
    }

    /**
     * Open the scanner activity
     *
     * @param view The view
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void QrScannerClicked(View view) {
        startActivityForResult(new Intent(this, Scanner.class), QR_REQUEST_CODE);
    }


    /**
     * Search an emplacement (via the UI, using the EditText)
     *
     * @param view The View
     */
    public void searchClicked(View view) {
        String emplacementCode = editText.getText().toString().toUpperCase();
        if (emplacementCode.length() < 3) {
            check(null, emplacementCode);
        } else {
            String number = emplacementCode.substring(0, emplacementCode.length() - 2);
            String verification = emplacementCode.substring(emplacementCode.length() - 2);
            Emplacement emplacement = databaseHandler.selectByNumber(number);
            if (emplacement != null) {
                if (verification.equals(emplacement.getCode().substring(emplacement.getCode().length() - 2))) {
                    check(emplacement, emplacementCode);
                } else {
                    check(null, emplacementCode);
                }
            } else {
                check(null, emplacementCode);
            }
        }
        editText.setText("");
    }

    /**
     * Check the validity of the databaseHandler (number or code)
     *
     * @param emplacement The Emplacement to check
     * @param code        The retrived code to check
     */
    public void check(Emplacement emplacement, String code) {
        // Variable which check if databaseHandler are valid
        boolean check = false;

        // Creation of AlterDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String titleText;

        // No emplacement has been found in the databaseHandler
        if (emplacement == null) {
            vibrator.vibrate(VIB_DURATION_FAILED);
            soundPool.play(soundWrong, 1, 1, 1, 0, 1);
            titleText = "Donnée invalide";
            builder.setMessage("Aucun emplacement n'a comme code : " + code);

            // An emplacement has been found
        } else {

            // Is it the good entry?
            if (!emplacement.getEntry().equals(entrySelected)) {
                vibrator.vibrate(VIB_DURATION_FAILED);
                soundPool.play(soundWrong, 1, 1, 1, 0, 1);
                titleText = "Mauvaise entrée";
                builder.setMessage("Emplacement n°" +
                        emplacement.getNumber() +
                        "\nVeuillez vous présentez à l'entrée " +
                        emplacement.getEntry());
            }

            // Does the emplacement has already been scanned?
            else if (emplacement.getScan() != null) {
                vibrator.vibrate(VIB_DURATION_FAILED);
                soundPool.play(soundWrong, 1, 1, 1, 0, 1);
                titleText = "Déjà entré";
                builder.setMessage("Entrée " +
                        emplacement.getEntry() +
                        "\nEmplacement n°" + emplacement.getNumber() +
                        "\nEntré à " + emplacement.getScan() + "\nDéjà refusé : " +
                        emplacement.getRefusal());
                emplacement.setRefusal(emplacement.getRefusal() + 1);
                databaseHandler.updateEmplacement(emplacement);
            }

            // It is a good emplacement and good entry
            else {
                vibrator.vibrate(VIB_DURATION_SUCCESS);
                soundPool.play(soundRight, 1, 1, 1, 0, 1);
                titleText = "Bonne Journée";
                builder.setMessage("Emplacement n°" + emplacement.getNumber());

                // Get the time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                String date = format.format(c.getTime());
                emplacement.setScan(date);
                emplacement.setRefusal(0);
                databaseHandler.updateEmplacement(emplacement);
                check = true;

                // Decrease the number for the entry
                Entry entry = databaseHandler.selectOneEntry(entrySelected + "_scan");
                entry.setValue(entry.getValue() + 1);
                databaseHandler.updateEntry(entry);
                setNumbers();
            }
        }

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        // Initialize a new relative size span instance
        RelativeSizeSpan largeSizeText = new RelativeSizeSpan(2.5f);

        // Initialize a new foreground color span instance (green or red)
        ForegroundColorSpan foregroundColorSpan;

        if (check) {
            foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.green));
        } else {
            foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.red));
        }

        // Apply the foreground color span
        ssBuilder.setSpan(
                foregroundColorSpan, // Span to add
                0, // Start of the span
                titleText.length(), // End of the span
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
        );

        // Apply the relative size span
        ssBuilder.setSpan(
                largeSizeText, // Span to add
                0, // Start of the span
                titleText.length(), // End of the span
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
        );

        // Display AlertDialog
        builder.setTitle(ssBuilder);
        AlertDialog alert = builder.create();
        alert.show();

        // Modification of the size and the color of texts
        TextView textView = alert.findViewById(android.R.id.message);
        textView.setTextSize(32);
        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    /**
     * Open File Chooser to import the data
     *
     * @param view The View
     */
    public void importDataClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), READ_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_LONG).show();
            vibrator.vibrate(VIB_DURATION_SUCCESS);
        }
    }

    /**
     * Read the file to import
     *
     * @param uri The URI of the file
     */
    public void readFile(Uri uri) {
        try (InputStream stream = getContentResolver().openInputStream(uri)) {

            String readData;
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Emplacement emplacement;
            Map<String, Integer> entry = new TreeMap<String, Integer>();
            int emplacementCompt = 0;
            int newEntryCompt = 0;
            int updateEntryCompt = 0;

            while ((readData = reader.readLine()) != null) {
                emplacement = new Emplacement();
                String[] data = readData.split("\t");
                emplacement.setNumber(data[0]);
                emplacement.setCode(data[1]);
                emplacement.setEntry(data[2]);

                // Insert the emplacement only of the number is not present in the database
                Emplacement temp = databaseHandler.selectByNumber(emplacement.getNumber());
                if (temp == null) {
                    emplacementCompt++;
                    databaseHandler.insertEmplacement(emplacement);
                }

                // Index the entries
                if (entry.containsKey(data[2])) {
                    entry.put(data[2], entry.get(data[2]) + 1);
                } else {
                    entry.put(data[2], 1);
                }
            }

            // Insert the entries in the database
            for (Map.Entry<String, Integer> entrySet : entry.entrySet()) {
                String key = entrySet.getKey();
                Integer value = entrySet.getValue();

                Entry temp = databaseHandler.selectOneEntry(key + "_total");

                if (temp == null) {
                    newEntryCompt++;

                    // Total part
                    Entry entryTotal = new Entry();
                    entryTotal.setId(key + "_total");
                    entryTotal.setDescription(key);
                    entryTotal.setValue(value);
                    databaseHandler.insertEntry(entryTotal);

                    // Scan part
                    Entry entryScan = new Entry();
                    entryScan.setId(key + "_scan");
                    entryScan.setDescription(key);
                    entryScan.setValue(0);
                    databaseHandler.insertEntry(entryScan);
                } else {
                    temp.setValue(value);
                    databaseHandler.updateEntry(temp);
                }

            }

            Toast.makeText(this, emplacementCompt + " données importées\n" + newEntryCompt + " entrées trouvées\n" + updateEntryCompt + " entrées modifiées", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(VIB_DURATION_SUCCESS);

            showButtons();
            setNumbers();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Display the entry RadioButton (when data are imported)
     */
    public void showButtons() {
        for (RadioButton entry : entries) {
            entry.setVisibility(View.GONE);
        }

        Set<String> allEntries = databaseHandler.selectAllEntry();
        Iterator<String> it = allEntries.iterator();
        int compt = 0;
        while (it.hasNext()) {
            entries[compt].setVisibility(View.VISIBLE);
            entries[compt].setText("Entrée " + it.next());
            compt++;
        }
    }

    /**
     * Set numbers at the top right (when an entry is selected)
     */
    public void setNumbers() {
        String scan = entrySelected + "_scan";
        String total = entrySelected + "_total";

        Entry tmp = databaseHandler.selectOneEntry(scan);
        if (tmp == null) {
            scanNumber.setText("...");
        } else {
            scanNumber.setText(Integer.toString(tmp.getValue()));
        }

        tmp = databaseHandler.selectOneEntry(total);
        if (tmp == null) {
            totalNumber.setText("...");
        } else {
            totalNumber.setText(Integer.toString(tmp.getValue()));
        }
    }

    /**
     * Open the File Chooser to set the destination of the file
     *
     * @param view The View
     */
    public void exportDataClicked(View view) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = sdf.format(c.getTime());

        // Filename
        String filename = strDate + " export";
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /**
     * Write the date to export
     *
     * @param uri The URI of the file to write
     */
    public void write(Uri uri) {
        try (ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "w");
             FileOutputStream fileOutputStream = new FileOutputStream(Objects.requireNonNull(pfd).getFileDescriptor())) {

            int compt = 0;
            // Select all the emplacements to write them
            List<Emplacement> emplacements = databaseHandler.selectAllEmplacement();
            fileOutputStream.write(("N°\tQRCode\tHeure\tRefus\r\n").getBytes());
            for (Emplacement emplacement : emplacements) {
                fileOutputStream.write((emplacement.getNumber() + "\t" + emplacement.getCode() + "\t" + emplacement.getScan() + "\t" + emplacement.getRefusal() + "\t\r\n").getBytes());
                compt++;
            }

            Toast.makeText(this, compt + " données ont été exportées", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(VIB_DURATION_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * AlterDialog to confirm the delete the database
     *
     * @param view The View
     */
    public void deleteDbAskClicked(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "Entrez le code secret";

        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(message);

        // Initialize a new relative size span instance
        // Double the text size on this span
        RelativeSizeSpan largeSizeText = new RelativeSizeSpan(2.0f);

        // Initialize a new foreground color span instance (green or red)
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark));

        // Apply the foreground color span
        ssBuilder.setSpan(
                foregroundColorSpan, // Span to add
                0, // Start of the span
                message.length(), // End of the span
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
        );

        // Apply the relative size span
        ssBuilder.setSpan(
                largeSizeText, // Span to add
                0, // Start of the span
                message.length(), // End of the span
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
        );

        builder.setTitle(ssBuilder);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (text.equals("4738")) {
                    deleteDb();
                } else {
                    makeToast(1);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                makeToast(2);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        vibrator.vibrate(VIB_DURATION_FAILED);

        // Get the buttons
        Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        Button neutralButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);

        // Change the button colors
        positiveButton.setTextColor(getResources().getColor(R.color.green));
        neutralButton.setTextColor(getResources().getColor(R.color.red));

        // Change the button sizes
        positiveButton.setTextSize(30);
        neutralButton.setTextSize(30);

    }

    public void makeToast(int cas) {
        vibrator.vibrate(VIB_DURATION_FAILED);
        switch (cas) {
            case 1:
                Toast.makeText(this, "Code incorrect\nDonnées non effacées", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Données non effacées", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * Delete all the data from the database
     */
    public void deleteDb() {
        long cntEmp = DatabaseUtils.queryNumEntries(databaseHandler.database, DatabaseHandler.TABLE_NAME_EMPLACEMENT);
        long cntEnt = DatabaseUtils.queryNumEntries(databaseHandler.database, DatabaseHandler.TABLE_NAME_ENTRY);
        databaseHandler.deleteAll();
        Toast.makeText(this, (int) cntEmp + " données effacées\n" + cntEnt / 2 + " entrées réinitialisées", Toast.LENGTH_SHORT).show();
        vibrator.vibrate(VIB_DURATION_SUCCESS);

        showButtons();
        setNumbers();
    }
}