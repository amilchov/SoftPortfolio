package com.dl.softportfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPortfolioActivity extends AppCompatActivity {

    private static final String FIELD_EMPTY = "Полето е задължително!";

    @BindView(R.id.et_name)
    EditText editTextName;

    @BindView(R.id.et_email)
    EditText editTextEmail;

    @BindView(R.id.et_phone)
    EditText editTextPhone;

    @BindView(R.id.et_school)
    EditText editTextSchool;

    @BindView(R.id.et_work)
    EditText editTextWork;

    @BindView(R.id.et_technologies)
    EditText editTextTechnolohies;

    String name, email, phone, school, work, technologies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Добави портфолио!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.btn_confirm})
    public void onClick() {
        createPortfolio();
    }

    private void createPortfolio() {
        name = editTextName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();
        school = editTextSchool.getText().toString().trim();
        work = editTextWork.getText().toString().trim();
        technologies = editTextTechnolohies.getText().toString().trim();

        if (name.isEmpty())
            editTextName.setError(FIELD_EMPTY);
        else if (email.isEmpty())
            editTextEmail.setError(FIELD_EMPTY);
        else if (phone.isEmpty())
            editTextPhone.setError(FIELD_EMPTY);
        else if (school.isEmpty())
            editTextSchool.setError(FIELD_EMPTY);
        else if (work.isEmpty())
            editTextWork.setError(FIELD_EMPTY);
        else if (technologies.isEmpty())
            editTextTechnolohies.setError(FIELD_EMPTY);
        else {
            initSharedPreferences();
            startActivity(new Intent(this, MainActivity.class));
//            super.onBackPressed();
        }
    }

    private void initSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear().apply();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("school", school);
        editor.putString("work", work);
        editor.putString("technologies", technologies);
        editor.apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
