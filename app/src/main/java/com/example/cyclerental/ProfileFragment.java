package com.example.cyclerental;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ProfileFragment extends Fragment {

    private EditText etName, etContact, etPassword;
    private ImageView ivProfileImage;
    private Button btnUpdate, btnHistory, btnLogout;
    private DBHelper dbHelper;
    private String email, imagePath;
    private TextView tvUserEmail;

    private static final int PICK_IMAGE_REQUEST = 1;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etName = view.findViewById(R.id.etName);
        etContact = view.findViewById(R.id.etContact);
        etPassword = view.findViewById(R.id.etPassword);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnHistory = view.findViewById(R.id.btnHistory);
        btnLogout = view.findViewById(R.id.btnLogout);

        dbHelper = new DBHelper(getContext());


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Activity.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");


        tvUserEmail.setText(email);

        loadUserProfile();

        ivProfileImage.setOnClickListener(v -> openGallery());

        btnUpdate.setOnClickListener(v -> updateUserProfile());

        btnHistory.setOnClickListener(v -> {
            Intent intentToHistory = new Intent(requireContext(), HistoryActivity.class);
            intentToHistory.putExtra("USER_EMAIL", email);
            startActivity(intentToHistory);
        });


        btnLogout.setOnClickListener(v -> {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();


            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserProfile() {
        Cursor cursor = dbHelper.getUserDetails(email);
        if (cursor != null && cursor.moveToFirst()) {
            etName.setText(cursor.getString(0));
            etContact.setText(cursor.getString(1));
            etPassword.setText(cursor.getString(2));
            imagePath = cursor.getString(3);

            if (imagePath != null && !imagePath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                ivProfileImage.setImageBitmap(bitmap);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                ivProfileImage.setImageBitmap(bitmap);
                imagePath = saveImageToInternalStorage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        File directory = requireActivity().getFilesDir();
        String filename = "profile" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    private void updateUserProfile() {
        String newName = etName.getText().toString().trim();
        String newContact = etContact.getText().toString().trim();
        String newPassword = etPassword.getText().toString().trim();

        if (newName.isEmpty() || newContact.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbHelper.updateUserProfile(email, newName, newContact, newPassword, imagePath);
        if (isUpdated) {
            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}