package com.codepath.instagram_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class CreatePostActivity extends AppCompatActivity {

    public final String APP_TAG = "PersonalInstagram";

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PHOTO_WIDTH = 300;

    public String photoFileName = "photo.jpg";
    File photoFile;

    @BindView(R.id.postBtn) Button postBtn;
    @BindView(R.id.takePictureBtn) Button takePictureBtn;
    @BindView(R.id.preview) ImageView preview;
    @BindView(R.id.description) EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
    }

    // get the image from the camera using Intents
    public void onLaunchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(CreatePostActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, PHOTO_WIDTH);

                // Load the taken image into a preview
                ImageView preview = (ImageView) findViewById(R.id.preview);
                preview.setImageBitmap(takenImage);

                try {
                    // compress the image further and write to a new file
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    File resizedFile = getPhotoFileUri(photoFileName + "_resized");
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);
                    fos.write(bytes.toByteArray());
                    fos.close();
                } catch (IOException e) {
                    Log.i("CaptureImage", "Failed updating resized photo image");
                    e.getMessage();
                }
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.postBtn)
    public void postImage() {
        final String postDescription = description.getText().toString();
    }

    @OnClick(R.id.takePictureBtn)
    public void takePicture() {

    }
}
