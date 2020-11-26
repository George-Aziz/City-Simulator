/**************************************************************************
 * Author: George Aziz
 * Date Created: 02/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Activity that displays details of a selected structure
 **************************************************************************/
/* Camera Functionality taken from MAD Lecture 5: App Interaction */

package curtin.edu.au.assignment2.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Email;

import javax.sql.CommonDataSource;

import curtin.edu.au.assignment2.models.GameMap;
import curtin.edu.au.assignment2.models.MapElement;
import curtin.edu.au.assignment2.R;

public class DetailScreenActivity extends AppCompatActivity
{
    private EditText editName;
    private TextView rowVal, colVal, structType;
    private Button cameraBtn, saveBtn;
    private ImageView structImg;
    private GameMap gameMap;
    private MapElement element;

    private static final int REQUEST_THUMBNAIL = 1;
    private Intent thumbnailPhotoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        //Receive Intent
        Intent intent = getIntent();
        int row = intent.getIntExtra("row",-1);
        int col = intent.getIntExtra("col",-1);
        gameMap = GameMap.get();
        element = gameMap.get(row,col);

        //Find All Views in Activity
        cameraBtn = findViewById(R.id.cameraBtn);
        rowVal = findViewById(R.id.rowVal);
        colVal = findViewById(R.id.colVal);
        editName = findViewById(R.id.editName);
        structType = findViewById(R.id.structTypeLabel);
        structImg = findViewById(R.id.structImg);
        saveBtn = findViewById(R.id.saveBtn);
        thumbnailPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Set up for all views on activity
        structImg.setImageResource(element.getStructure().getDrawableId());
        structType.setText(element.getStructure().getLabel());
        rowVal.setText(String.valueOf(row));
        colVal.setText(String.valueOf(col));

        //If there is no editName available then by default it is the structure Label
        if(element.getEditName() == null)
        {
            editName.setText(element.getStructure().getLabel());
        }
        else
        {
            editName.setText(element.getEditName());
        }

        if (element.getCustomImage() != null) //If custom image is found then display custom image
        {
            structImg.setImageBitmap(element.getCustomImage());
        }
        else //Display structure only if custom image not found
        {
            structImg.setImageResource(element.getStructure().getDrawableId());
        }

        final Activity curActivity = this;
        cameraBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(curActivity, new String[] {Manifest.permission.CAMERA}, REQUEST_THUMBNAIL);
                }
                else
                {
                    startActivityForResult(thumbnailPhotoIntent, REQUEST_THUMBNAIL);
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                element.setEditName(editName.getText().toString());
                gameMap.updateMapElement(element);
                startActivity(new Intent(DetailScreenActivity.this, MapScreenActivity.class));
            }
        });
    }

    //Gets the Image from camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            element.setCustomImage(thumbnail);
            structImg.setImageBitmap(thumbnail); //Set New Photo to Custom Image
        }
    }

    //Goes back to Map Screen but starts new activity and not default back button to prevent issues
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(DetailScreenActivity.this, MapScreenActivity.class));
    }



}