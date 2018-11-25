package maubray.ami.brocantedumarais;

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
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Set;
import java.util.TreeMap;


public class MainActivity extends AppCompatActivity {

    // Décrypter le QRCode
    public static String QrCodeRead;

    // Les request code
    private final int QR_REQUEST_CODE = 81;
    private final int READ_REQUEST_CODE = 82;
    private final int WRITE_REQUEST_CODE = 83;

    // Les bases de données
    private ClassHandler database;

    // Le numéro de l'entrée sélectionnée
    private String entrySelected;

    // Récupération de l'EditText
    private EditText numEmplacement;

    // Récupération des Boutons
    private RadioButton[] entries = new RadioButton[5];

    // Récupération des TextView
    private TextView totalNumber;
    private TextView scanNumber;

    //  Vibreur
    private Vibrator vib;

    // Vibration duration
    private final int VIB_DURATION_SUCCESS = 150; // 0.2 seconde
    private final int VIB_DURATION_FAILED = 800; // 0.6 seconde

    // Classe pour jouer des sons
    private SoundPool soundPool;
    private int soundRight, soundWrong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupération du EditText
        numEmplacement = findViewById(R.id.number_text);

        // Récupération des Boutons
        entries[0] = findViewById(R.id.entree_0);
        entries[1] = findViewById(R.id.entree_1);
        entries[2] = findViewById(R.id.entree_2);
        entries[3] = findViewById(R.id.entree_3);
        entries[4] = findViewById(R.id.entree_4);

        // Récupération des TextView
        totalNumber = findViewById(R.id.total);
        scanNumber = findViewById(R.id.scan);

        // Création et ouverture de la base de données
        database = new ClassHandler(this);
        database.open();

        // Récupération du vibreur
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Initilisation du son
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        soundRight = soundPool.load(this, R.raw.right, 1);
        soundWrong = soundPool.load(this, R.raw.wrong, 1);

        showButtons();
    }

    @Override
    protected void onDestroy() {
        // Fermer la base de données quand l'activité se ferme
        super.onDestroy();
        database.close();
    }

    // Gère les résultats d'activités
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // QR Code
            case QR_REQUEST_CODE:
                if (resultCode == 1) {
                    QrCodeRead = QrCodeRead.substring(0, QrCodeRead.length() - 2);
                    Emplacement emp = database.selectByCode(QrCodeRead);
                    check(emp, QrCodeRead);
                }
                break;
            // Importation des données
            case READ_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    // Get the path
                    String path = uri.getPath();
                    try {
                        read(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // Exportation des données
            case WRITE_REQUEST_CODE:
                Uri uri = data.getData();
                try {
                    write(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    // Modifier l'entrée quand on sélectionne un nouveau RadioButton
    public void changeEntry(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        vib.vibrate(VIB_DURATION_SUCCESS);
        soundPool.play(soundRight, 1, 1, 1, 0, 1);
        switch (view.getId()) {
            case R.id.entree_0:
                if (checked) {
                    String[] tmp = entries[0].getText().toString().split(" ");
                    entrySelected = tmp[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entree_1:
                if (checked) {
                    String[] tmp = entries[1].getText().toString().split(" ");
                    entrySelected = tmp[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entree_2:
                if (checked) {
                    String[] tmp = entries[2].getText().toString().split(" ");
                    entrySelected = tmp[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entree_3:
                if (checked) {
                    String[] tmp = entries[3].getText().toString().split(" ");
                    entrySelected = tmp[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entree_4:
                if (checked) {
                    String[] tmp = entries[4].getText().toString().split(" ");
                    entrySelected = tmp[1];
                    Toast.makeText(this, "Entrée " + entrySelected + " sélectionnée", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        setNumbers();
    }

    // Ouvrir l'activité du Scanneur de QR Code et donc faire une recherche par code
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void QrScanner(View view) {
        startActivityForResult(new Intent(this, Scanner.class), QR_REQUEST_CODE);
    }


    // Rechercher par numéro (via l'interface)
    public void search(View view) {
        String edit = numEmplacement.getText().toString().toUpperCase();
        if (edit.length() < 3) {
            check(null, edit);
        } else {
            String numero = edit.substring(0, edit.length() - 2);
            String verif = edit.substring(edit.length() - 2, edit.length());
            Emplacement emp = database.selectByNumber(numero);
            if (emp != null) {
                if (verif.equals(emp.getCode().substring(emp.getCode().length() - 2, emp.getCode().length()))) {
                    check(emp, edit);
                } else {
                    check(null, edit);
                }
            } else {
                check(null, edit);
            }
        }
        numEmplacement.setText("");
    }

    // Vérification de la validité des données (code ou numéro)
    public void check(Emplacement emp, String code) {
        // Variable qui dit si les données sont acceptables
        boolean check = false;

        // Création de l'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String titleText;

        // Aucun emplacement trouvé avec ce code
        if (emp == null) {
            vib.vibrate(VIB_DURATION_FAILED);
            soundPool.play(soundWrong, 1, 1, 1, 0, 1);
            titleText = "Donnée invalide";
            builder.setMessage("Aucun emplacement n'a comme code : " + code);

            // Un emplacement trouvé avec ce code
        } else {

            // Est-ce la bonne entrée ?
            if (!emp.getEntry().equals(entrySelected)) {
                vib.vibrate(VIB_DURATION_FAILED);
                soundPool.play(soundWrong, 1, 1, 1, 0, 1);
                titleText = "Mauvaise entrée";
                builder.setMessage("Emplacement n°" + emp.getNumber() + "\nVeuillez vous présentez à l'entrée " + emp.getEntry());
            }

            // A-t-il déjà été scanné ?
            else if (emp.getScan() != null) {
                vib.vibrate(VIB_DURATION_FAILED);
                soundPool.play(soundWrong, 1, 1, 1, 0, 1);
                titleText = "Déjà entré";
                builder.setMessage("Entrée " + emp.getEntry() + "\nEmplacement n°" + emp.getNumber() + "\nEntré à " + emp.getScan() + "\nDéjà refusé : " + emp.getRefus());
                emp.setRefus(emp.getRefus() + 1);
                database.updateEmplacement(emp);
            }

            // Bon emplacement et bonne entrée
            else {
                vib.vibrate(VIB_DURATION_SUCCESS);
                soundPool.play(soundRight, 1, 1, 1, 0, 1);
                titleText = "Bonne Journée";
                builder.setMessage("Emplacement n°" + emp.getNumber());

                // Récupérer la date et l'heure actuelle
                Calendar c = Calendar.getInstance();
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String strDate = sdf.format(c.getTime());
                emp.setScan(strDate);
                emp.setRefus(0);
                database.updateEmplacement(emp);
                check = true;

                // Décrémenter les nombres
                Entry ent = database.selectOneEntry(entrySelected + "_scan");
                ent.setValue(ent.getValue() +

                        1);
                database.updateEntry(ent);
                setNumbers();
            }
        }

        // Ce qui suit permet de personnalisé l'AlertDialog
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

        // Affichage de l'AlertDialog
        builder.setTitle(ssBuilder);
        AlertDialog alert = builder.create();
        alert.show();

        // Modification de la taille et de la couleur du message
        TextView textView = alert.findViewById(android.R.id.message);
        textView.setTextSize(32);
        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    // Ouvrir le File Chooser pour importer des données
    public void importData(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), READ_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_LONG).show();
            vib.vibrate(VIB_DURATION_SUCCESS);
        }
    }

    // Lecture du fichier à importer
    public void read(Uri uri) throws IOException {
        String dataRead;
        InputStream is = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Emplacement emp;
        Map<String, Integer> ent = new TreeMap<String, Integer>();
        int comptEmp = 0;
        int comptEntNew = 0;
        int comptEntUpd = 0;
        if (is != null) {
            while ((dataRead = reader.readLine()) != null) {
                emp = new Emplacement();
                String[] datas = dataRead.split("\t");
                emp.setNumber(datas[0]);
                emp.setCode(datas[1]);
                emp.setEntry(datas[2]);

                // Insérer l'emplacement seulement si le numéro n'est pas présent dans la base de données
                Emplacement temp = database.selectByNumber(emp.getNumber());
                if (temp == null) {
                    comptEmp++;
                    database.insertEmplacement(emp);
                }

                // Répertorier les entrées
                if (ent.containsKey(datas[2])) {
                    ent.put(datas[2], ent.get(datas[2]) + 1);
                } else {
                    ent.put(datas[2], 1);
                }
            }
            is.close();

            // Insérer les entrés dans la base de données
            for (Map.Entry<String, Integer> entry : ent.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();

                Entry temp = database.selectOneEntry(key + "_total");

                if (temp == null) {
                    comptEntNew++;

                    // La partie totale
                    Entry entry_total = new Entry();
                    entry_total.setId(key + "_total");
                    entry_total.setLibelle(key);
                    entry_total.setValue(value);
                    database.insertEntry(entry_total);

                    //La partie scan
                    Entry entry_scan = new Entry();
                    entry_scan.setId(key + "_scan");
                    entry_scan.setLibelle(key);
                    entry_scan.setValue(0);
                    database.insertEntry(entry_scan);
                } else {
                    temp.setValue(value);
                    database.updateEntry(temp);
                }

            }

            Toast.makeText(this, comptEmp + " données importées\n" + comptEntNew + " entrées trouvées\n" + comptEntUpd + " entrées modifiées", Toast.LENGTH_SHORT).show();
            vib.vibrate(VIB_DURATION_SUCCESS);

            showButtons();
            setNumbers();
        }
    }

    // Afficher les Boutons (lors d'ouverture ou de l'import)
    public void showButtons() {
        for (int i = 0; i < entries.length; i++) {
            entries[i].setVisibility(View.GONE);
        }

        Set<String> ent = database.selectAllEntry();
        Iterator<String> it = ent.iterator();
        int compt = 0;
        while (it.hasNext()) {
            entries[compt].setVisibility(View.VISIBLE);
            entries[compt].setText("Entrée " + it.next());
            compt++;
        }
    }

    // Changer les nuémros quand on sélectionne une entrée
    public void setNumbers() {
        String scan = entrySelected + "_scan";
        String total = entrySelected + "_total";

        Entry tmp = database.selectOneEntry(scan);
        if (tmp == null) {
            scanNumber.setText("...");
        } else {
            scanNumber.setText(Integer.toString(tmp.getValue()));
        }

        tmp = database.selectOneEntry(total);
        if (tmp == null) {
            totalNumber.setText("...");
        } else {
            totalNumber.setText(Integer.toString(tmp.getValue()));
        }
    }

    // Ouvrir le File Chooser pour les données à exporter
    public void exportData(View view) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = sdf.format(c.getTime());

        // Nom du fichier
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

    // Ecrire les données à exporter
    public void write(Uri uri) throws IOException {
        int compt = 0;

        ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "w");
        FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());

        // Sélectionner tous les emplacements pour les écrire
        List<Emplacement> table = database.selectAllEmplacement();
        fileOutputStream.write(("N°\tQRCode\tHeure\tRefus\r\n").getBytes());
        for (int i = 0; i < table.size(); i++) {
            fileOutputStream.write((table.get(i).getNumber() + "\t" + table.get(i).getCode() + "\t" + table.get(i).getScan() + "\t" + table.get(i).getRefus() + "\t\r\n").getBytes());
            compt++;
        }

        fileOutputStream.close();
        pfd.close();

        Toast.makeText(this, compt + " données ont été exportées", Toast.LENGTH_SHORT).show();
        vib.vibrate(VIB_DURATION_SUCCESS);
    }

    // AlerDialog pour s'assurer qu'on veuille effacer la DB
    public void deleteDBAsk(View view) {
        //Old version of AlertDialog (no confirmation)
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        String message = "Etes-vous sûr ?";
////
////        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(message);
////
////        // Initialize a new relative size span instance
////        // Double the text size on this span
////        RelativeSizeSpan largeSizeText = new RelativeSizeSpan(2.5f);
////
////        // Initialize a new foreground color span instance (green or red)
////        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark));
////
////        // Apply the foreground color span
////        ssBuilder.setSpan(
////                foregroundColorSpan, // Span to add
////                0, // Start of the span
////                message.length(), // End of the span
////                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
////        );
////
////        // Apply the relative size span
////        ssBuilder.setSpan(
////                largeSizeText, // Span to add
////                0, // Start of the span
////                message.length(), // End of the span
////                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
////        );
////
////        builder.setTitle(ssBuilder);
////        builder.setMessage("");
////        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                deleteDB();
////            }
////        });
////        builder.setNeutralButton("Non", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                dialog.cancel();
////            }
////        });
////        AlertDialog alert = builder.create();
////        alert.show();
////
////        // Get the buttons
////        Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
////        Button neutralButton = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
////
////        // Change the button colors
////        positiveButton.setTextColor(getResources().getColor(R.color.green));
////        neutralButton.setTextColor(getResources().getColor(R.color.red));
////
////        // Change the button sizes
////        positiveButton.setTextSize(30);
////        neutralButton.setTextSize(30);

        // new version of AlertDialog

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
                    deleteDB();
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
        vib.vibrate(VIB_DURATION_FAILED);

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
        vib.vibrate(VIB_DURATION_FAILED);
        switch (cas) {
            case 1:
                Toast.makeText(this, "Code incorrect\nDonnées non effacées", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Données non effacées", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // Supprimer tous les emplacements de la base de données
    public void deleteDB() {
        long cntEmp = DatabaseUtils.queryNumEntries(ClassHandler.db, DatabaseHandler.TABLE_NAME_EMPLACEMENT);
        long cntEnt = DatabaseUtils.queryNumEntries(ClassHandler.db, DatabaseHandler.TABLE_NAME_ENTRY);
        database.deleteAll();
        Toast.makeText(this, (int) cntEmp + " données effacées\n" + cntEnt / 2 + " entrées réinitialisées", Toast.LENGTH_SHORT).show();
        vib.vibrate(VIB_DURATION_SUCCESS);

        showButtons();
        setNumbers();
    }
}