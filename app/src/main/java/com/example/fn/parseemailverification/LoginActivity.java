package com.example.fn.parseemailverification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    // Ui Components
    // globalen EditText anlegen
    private EditText edtLoginUsername, edtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // den Title für die Activity setzen
        setTitle("Log In");

        // initialisieren
        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
    }

    // Methodde anlegen für die Funktion Button
    public void loginIsPressed(View btnView) {
        ParseUser.logInInBackground(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    if (parseUser.getBoolean("emailVerified")) {
                        alertDisplayer("Login Successful", "Welcome, " +
                                edtLoginUsername.getText().toString() + "!", false);
                    } else {
                        // User wird nicht eingeloggt bevor die Verfikation abgeschlossen wurde
                        ParseUser.logOut();
                        alertDisplayer("Login Fail", "Please Verify Your Email finish", true);
                    }
                } else {
                    // User wird nicht eingeloggt bevor keine Registrierung stattgefunden hat
                    ParseUser.logOut();
                    alertDisplayer("Login Fail", e.getMessage() + "Please re-try", true);
                }
            }
        });
    }

    // Methoden anlegen für die Funktion Benachrichtigung
    private void alertDisplayer(String title, String message, final boolean error) {
        // AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
        // <-- wenn die Activities in der App vorhanden
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (!error) {
                            // Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            // <-- wenn die Activities in der App vorhanden
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
