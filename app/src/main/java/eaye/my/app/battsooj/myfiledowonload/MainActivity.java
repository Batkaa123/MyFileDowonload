package eaye.my.app.battsooj.myfiledowonload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_STORAGE_CODE = 1000;

    EditText mUrlEt;
    Button mDowanloadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrlEt = findViewById(R.id.urlEt);
        mDowanloadBtn = findViewById(R.id.downloadUrl);

        mDowanloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_STORAGE_CODE);

                    } else {
                        startDownloading();
                    }
                } else {
                    startDownloading();
                }

            }
        });
    }

    private void startDownloading() {

    String url = mUrlEt.getText().toString().trim();

    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
            DownloadManager.Request.NETWORK_MOBILE);
    request.setTitle("Download");
    request.setDescription("Downloadind file...");
    request.allowScanningByMediaScanner();
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+ System.currentTimeMillis());

    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    manager.enqueue(request);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    startDownloading();
                }
                else {
                    Toast.makeText(this,"Permissic denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}