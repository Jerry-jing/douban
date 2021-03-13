package com.example.douban.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.douban.R;
import com.example.douban.adapter.TitleAdapter;
import com.example.douban.db.Login;
import com.example.douban.fragment.PoetryFragment;
import com.example.douban.fragment.BookFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView headImage;
    private Uri imageUri;
    private String account;
    private TextView headname;

    private String[] titles = {"每日一句","书影音"};
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        account = intent.getStringExtra("account");

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        fragments.add(new PoetryFragment());
        fragments.add(new BookFragment());

        TitleAdapter adapter = new TitleAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        View headerLayout = navigationView.getHeaderView(0);
        headImage = headerLayout.findViewById(R.id.nv_head);
        List<Login> logins = DataSupport.findAll(Login.class);
        for(int i = 0; i < logins.size(); i++) {
            Login login = logins.get(i);
            if (login.getAccount().equals(account)) {
                if(login.getHeadshot() != null) {
                    byte[] images = login.getHeadshot();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(images, 0, images.length);
                    headImage.setImageBitmap(bitmap);
                }
            }
        }
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        headname = headerLayout.findViewById(R.id.username);
        headname.setText(account);

    }

    public void changeImage(){
        final String font[] = { "拍照", "从图库选择" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(font, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(font[which]){
                    case "拍照":
                        File outputImage = new File(getExternalCacheDir(),
                                "output_image.jpg");
                        try{
                            if(outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(HomeActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 6);
                        break;
                    case "从图库选择":
                        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                        }
                }
            }
        }).setNegativeButton("Cancle", null)
                .setPositiveButton("OK", null).create().show();

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 7);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 6:
                try{
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    headImage.setImageBitmap(bitmap);

                    byte[]images = img(bitmap);

                    Login login = new Login();
                    login.setHeadshot(images);
                    login.updateAll("account = ?", account);

                    /*List<Login> logins = DataSupport.findAll(Login.class);
                    for(int i = 0; i < logins.size(); i++) {
                        Login login = logins.get(i);
                        if (login.getAccount().equals(account)) {
                            login.setHeadshot(images);
                            login.save();
                        }
                    }*/

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                handleImageOnKitKat(data);
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection,null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            headImage.setImageBitmap(bitmap);

            byte[]images = img(bitmap);
            List<Login> logins = DataSupport.findAll(Login.class);
            for(int i = 0; i < logins.size(); i++) {
                Login login = logins.get(i);
                if (login.getAccount().equals(account)) {
                    login.setHeadshot(images);
                    login.save();
                }
            }

            Toast.makeText(this, "更换成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.message:
                Toast.makeText(this, "点击了提示按钮", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "你没有设置权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    /*
     * 将图片转换为字节
     */
    private byte[]img(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}