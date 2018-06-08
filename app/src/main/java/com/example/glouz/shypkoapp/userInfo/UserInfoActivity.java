package com.example.glouz.shypkoapp.userInfo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.glouz.shypkoapp.R;
import com.example.glouz.shypkoapp.data.DataImages;
import com.example.glouz.shypkoapp.data.DataSetting;
import com.yandex.metrica.YandexMetrica;

public class UserInfoActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    DataSetting dataSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataSetting = new DataSetting(this);
        if (dataSetting.checkTheme()) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setFlagOnEditors(false);
        setTextInEditors();
        ImageView imageView = (ImageView) findViewById(R.id.avatar_user_info);
        Bitmap avatar = DataImages.getAvatar(false, this);
        if (avatar != null) {
            imageView.setImageBitmap(avatar);
        }
    }

    public void editPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onBackPressed() {
        saveTextFromEditors();
        Button editButton = findViewById(R.id.editButton);
        if (!editButton.getText().toString().equals(getString(R.string.startEditing))) {
            dataSetting.setFlagNewInfo(true);
        }
        super.onBackPressed();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            DataImages.saveNewAvatar(data.getData(), this);
            ImageView imageView = (ImageView) findViewById(R.id.avatar_user_info);
            Bitmap avatar = DataImages.getAvatar(false, this);
            if (avatar != null) {
                imageView.setImageBitmap(avatar);
            }
        }
        dataSetting.setFlagNewInfo(true);
    }

    public void callPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        EditText editText = findViewById(R.id.editPhone);
        String phone = editText.getText().toString();
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
        YandexMetrica.reportEvent("Открыто окно звонка");
    }

    public void openEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        EditText editText = findViewById(R.id.editEmail);
        String email = editText.getText().toString();
        intent.setData(Uri.parse("mailto:" + email));
        startActivity(intent);
        YandexMetrica.reportEvent("Открыто окно почты");
    }

    public void openGithub(View view) {
        EditText editText = findViewById(R.id.editGithub);
        try {
            Uri address = Uri.parse(editText.getText().toString());
            Intent openlink = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlink);
            YandexMetrica.reportEvent("Открыта страничка github'а");
        } catch (ActivityNotFoundException exception) {
            Toast toast = Toast.makeText(this, R.string.errorFormatURI, Toast.LENGTH_SHORT);
            toast.show();
            editText.setText(getString(R.string.linkGithub));
        }
    }

    public void openMap(View view) {
        EditText editText = findViewById(R.id.editLocation);
        String location = editText.getText().toString();
        Uri address = Uri.parse("geo:0,0?q=" + location);
        Intent openLink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openLink);
        YandexMetrica.reportEvent("Открыта карта");
    }

    public void openVK(View view) {
        EditText editText = findViewById(R.id.editSocialNetworkVK);
        try {
            String vk = editText.getText().toString();
            Uri address = Uri.parse(vk);
            Intent openlink = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlink);
            YandexMetrica.reportEvent("Открыта страничка VK");
        } catch (ActivityNotFoundException exception) {
            Toast toast = Toast.makeText(this, R.string.errorFormatURI, Toast.LENGTH_SHORT);
            toast.show();
            editText.setText(getString(R.string.socialNetworkVK));
        }
    }

    public void editFields(View view) {
        Button editButton = findViewById(R.id.editButton);
        String text = editButton.getText().toString();
        String newText = getString(R.string.startEditing);
        boolean flagEnable;
        if (!text.equals(newText)) {
            editButton.setText(newText);
            flagEnable = false;
        } else {
            editButton.setText(getString(R.string.finishEditing));
            flagEnable = true;
            dataSetting.setFlagNewInfo(true);
        }
        setFlagOnEditors(flagEnable);
        if (!flagEnable) {
            saveTextFromEditors();
        }
    }

    private void setFlagOnEditors(boolean flag) {
        setFlagOnEditor(flag, R.id.editEmail);
        setFlagOnEditor(flag, R.id.editGithub);
        setFlagOnEditor(flag, R.id.editName);
        setFlagOnEditor(flag, R.id.editPhone);
        setFlagOnEditor(flag, R.id.editLocation);
        setFlagOnEditor(flag, R.id.editSocialNetworkVK);
    }

    private void setFlagOnEditor(boolean flag, int idView) {
        View view = findViewById(idView);
        view.setClickable(flag);
        view.setFocusableInTouchMode(flag);
        view.setFocusable(flag);
        view.setLongClickable(flag);
    }

    private void saveTextFromEditors() {
        saveEmail();
        saveGithub();
        saveName();
        savePhone();
        saveLocation();
        saveVK();
    }

    private void setTextInEditors() {
        setEmail();
        setGithub();
        setName();
        setPhone();
        setLocation();
        setVk();
    }


    private void saveVK() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).edit();
        EditText view = findViewById(R.id.editSocialNetworkVK);
        editor.putString(getString(R.string.keyVK), view.getText().toString());
        editor.apply();
    }

    private void saveName() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).edit();
        EditText view = findViewById(R.id.editName);
        editor.putString(getString(R.string.keyName), view.getText().toString());
        editor.apply();
    }

    private void savePhone() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).edit();
        EditText view = findViewById(R.id.editPhone);
        editor.putString(getString(R.string.keyPhone), view.getText().toString());
        editor.apply();
    }

    private void saveGithub() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).edit();
        EditText view = findViewById(R.id.editGithub);
        editor.putString(getString(R.string.keyGithub), view.getText().toString());
        editor.apply();
    }

    private void saveEmail() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).edit();
        EditText view = findViewById(R.id.editEmail);
        editor.putString(getString(R.string.keyEmail), view.getText().toString());
        editor.apply();
    }

    private void saveLocation() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).edit();
        EditText view = findViewById(R.id.editLocation);
        editor.putString(getString(R.string.keyLocation), view.getText().toString());
        editor.apply();
    }


    private void setVk() {
        String value = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE)
                .getString(getString(R.string.keyVK), getString(R.string.socialNetworkVK));
        EditText view = findViewById(R.id.editSocialNetworkVK);
        view.setText(value);
    }

    private void setName() {
        String value = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).
                getString(getString(R.string.keyName), getString(R.string.myName));
        EditText view = findViewById(R.id.editName);
        view.setText(value);
    }

    private void setPhone() {
        String value = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).
                getString(getString(R.string.keyPhone), getString(R.string.number_phone));
        EditText view = findViewById(R.id.editPhone);
        view.setText(value);
    }

    private void setGithub() {
        String value = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).
                getString(getString(R.string.keyGithub), getString(R.string.linkGithub));
        EditText view = findViewById(R.id.editGithub);
        view.setText(value);
    }

    private void setEmail() {
        String value = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).
                getString(getString(R.string.keyEmail), getString(R.string.email));
        EditText view = findViewById(R.id.editEmail);
        view.setText(value);
    }

    private void setLocation() {
        String value = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE).
                getString(getString(R.string.keyLocation), getString(R.string.location));
        EditText view = findViewById(R.id.editLocation);
        view.setText(value);
    }

}
