package com.dl.softportfolio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.glxn.qrgen.android.QRCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.cv_portfolio)
    CardView cardView;

    @BindView(R.id.tv_information)
    TextView textViewInformation;

    @BindView(R.id.tv_name)
    TextView textViewName;

    @BindView(R.id.tv_email)
    TextView textViewEmail;

    @BindView(R.id.tv_phone)
    TextView textViewPhone;

    @BindView(R.id.tv_school)
    TextView textViewSchool;

    @BindView(R.id.tv_work)
    TextView textViewWork;

    @BindView(R.id.tv_technologies)
    TextView textViewTechnologies;

    SharedPreferences pref;

    String name, email, phone, school, work, technologies;
    List<String> listTechnologies;
    View view;
    ImageView myImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Софтуерно Портфолио - Начало");
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }

        LayoutInflater factory = LayoutInflater.from(this);
        view = factory.inflate(R.layout.layout_qr, null);
        initSharedPreferences();
        myImg = findViewById(R.id.dialog_imageview);
    }

    private void initSharedPreferences() {
        pref = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
        name = pref.getString("name", null);
        email = pref.getString("email", null);
        phone = pref.getString("phone", null);
        school = pref.getString("school", null);
        work = pref.getString("work", null);
        technologies = pref.getString("technologies", null);

        if (name == null) {
            cardView.setVisibility(View.GONE);
            textViewInformation.setVisibility(View.VISIBLE);
        } else {
            listTechnologies = Arrays.asList(technologies.split(","));
            apendTextToTextView();
            Log.d("PRef", name);
            cardView.setVisibility(View.VISIBLE);
            textViewInformation.setVisibility(View.GONE);
        }
    }

    private void apendTextToTextView() {
        textViewName.append(name);
        textViewEmail.append(email);
        textViewPhone.append(phone);
        textViewSchool.append(school);
        textViewWork.append(work);

        for (int i=0; i<listTechnologies.size(); i++) {
            textViewTechnologies.append("* "+listTechnologies.get(i) + "\n");
        }
    }

    @OnClick({R.id.floatingActionButton})
    public void onClick() {
        startActivity(new Intent(this, AddPortfolioActivity.class));
        Log.d("GGG", listTechnologies.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK)
        {
            Log.d("QR","COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
            return;
        }
        if(requestCode == 101) {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("QR","Have scan result in your app activity :"+ result);
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.CAMERA}, 100))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_delete){
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.clear().apply();
            this.recreate();
        } else if(id == R.id.action_scan) {
            Intent i = new Intent(MainActivity.this, QrCodeActivity.class);
            startActivityForResult(i,101);
        } else if (id == R.id.action_qr) {
            createQrCode();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createQrCode() {
        ImageView image = new ImageView(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Име: " + name + System.lineSeparator());
        stringBuilder.append("Имейл: " + email + System.lineSeparator());
        stringBuilder.append("Телефонен номер: " + phone + System.lineSeparator());
        stringBuilder.append("Образование: " + school + System.lineSeparator());
        stringBuilder.append("Текуща работа: " + work + System.lineSeparator());
        stringBuilder.append("Технологии и езици за програмиране: " + technologies + System.lineSeparator());
        Bitmap myBitmap = QRCode.from(stringBuilder.toString())
                .withSize(500, 500)
                .withCharset("utf-8")
                .bitmap();
        image.setImageBitmap(myBitmap);

        image.setLayoutParams(
                new ViewGroup.LayoutParams(
                        // or ViewGroup.LayoutParams.WRAP_CONTENT
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        // or ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT ));

                AlertDialog.Builder builder =
                new AlertDialog.Builder(this).
                        setMessage("Сканирай!").
                        setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).
                        setView(image);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
    }
}