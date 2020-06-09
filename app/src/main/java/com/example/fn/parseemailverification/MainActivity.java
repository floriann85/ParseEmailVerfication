package com.example.fn.parseemailverification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    // Ui Components
    // globalen EditText anlegen
    private EditText edtEmail, edtUserName, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // den Title für die Activity setzen
        setTitle("Welcome");

        // Information einer Installation durch einen User auf einen Gerät,
        // diese wird auf dem Server/ Backend gespeichert
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // initialisieren
        edtEmail = findViewById(R.id.edtEmail);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);

    }

    // Methodde anlegen für die Funktion Button
    public void signupIsPressed(View btnView) {
        Toast.makeText(this, "Sign Up Pressed", Toast.LENGTH_SHORT).show();

        try {

            // Sign up with Parse
            ParseUser user = new ParseUser();
            user.setEmail(edtEmail.getText().toString());
            user.setUsername(edtUserName.getText().toString());
            user.setPassword(edtPassword.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    // Abfrage ob kein Fehler aufgetreten ist
                    if (e == null) {
                        ParseUser.logOut();
                        alertDisplayer("Account Greated Successfully!",
                                "Please verify your email before Login", false);
                    } else {
                        ParseUser.logOut();
                        alertDisplayer("Error Account Creation failed",
                                "Account could not be created" + " :" + e.getMessage(), true);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Methoden anlegen für die Funktion Benachrichtigung
    private void alertDisplayer(String title, String message, final boolean error) {
        // AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
        // <-- wenn die Activities in der App vorhanden
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (!error) {
                            // Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            // <-- wenn die Activities in der App vorhanden
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

}
