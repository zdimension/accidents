package fr.ihm.accidents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoreDetails extends AppCompatActivity
{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PERMISSION = 2;
    private String currentPhotoPath;
    private TextView imgLocs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        Button restartButton = findViewById(R.id.restart);
        restartButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(MoreDetails.this, VictimeTemoin.class);
            startActivity(intent);
        });

        Button previousButton = findViewById(R.id.previous3);
        previousButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(MoreDetails.this, AccidentNature.class);
            startActivity(intent);
        });

        Button nextButton = findViewById(R.id.next_step);
        nextButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(MoreDetails.this, MapsActivity.class);
            startActivity(intent);
        });

        this.imgLocs = findViewById(R.id.photos_locations);

        Button photoButton = findViewById(R.id.take_photo);
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
        {
            photoButton.setOnClickListener(v ->
            {
                if (ContextCompat.checkSelfPermission(MoreDetails.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MoreDetails.this,
                        new String[] { Manifest.permission.CAMERA }, REQUEST_IMAGE_PERMISSION);
                    return;
                }
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try
                {
                    File imageFile = createImageFile();
                    Uri photoURI = FileProvider.getUriForFile(MoreDetails.this,
                        "fr.ihm.accidents.fileprovider", imageFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                catch (IOException e)
                {
                    Toast.makeText(MoreDetails.this, "Erreur lors de la creation du chemin",
                        Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            });
        }
        else
        {
            photoButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_IMAGE_PERMISSION && grantResults.length > 0)
        {
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            this.imgLocs.setText(this.imgLocs.getText() + "\n" + this.currentPhotoPath);
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");*/
        }
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File externalFilesDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", externalFilesDir);
        this.currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}