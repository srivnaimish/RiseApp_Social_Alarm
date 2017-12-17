package com.riseapps.riseapp.view.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.Pojo.Server.ServerResponse;
import com.riseapps.riseapp.view.ui.activity.MainActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.riseapps.riseapp.Components.AppConstants.RC_GALLERY;

public class Settings extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "AUTH";
    private SharedPreferenceSingelton sharedPreferenceSingleton = new SharedPreferenceSingelton();
    private Dialog dialog;

    public static Settings newInstance() {
        return new Settings();
    }

    Tasks tasks = new Tasks();
    ImageView pic, method;
    TextView name, phone;
    FirebaseUser firebaseUser;
    Switch theme_switch;
    CardView method_card, rate, share, theme;

    ImageButton edit_pic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        pic = view.findViewById(R.id.profile_pic);
        method = view.findViewById(R.id.setting_method_image);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        share = view.findViewById(R.id.setting_share);
        rate = view.findViewById(R.id.setting_rate);
        theme_switch = view.findViewById(R.id.setting_theme_switch);
        theme = view.findViewById(R.id.setting_theme);
        method_card = view.findViewById(R.id.setting_alarm_method);
        edit_pic = view.findViewById(R.id.edit_pic);

        method_card.setOnClickListener(this);
        edit_pic.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        theme.setOnClickListener(this);
        theme_switch.setOnCheckedChangeListener(this);

        if (tasks.getCurrentTheme(getContext()) == 1) {
            theme_switch.setChecked(true);
        }

        assert ((MainActivity)getActivity()) != null;
        firebaseUser = ((MainActivity)getActivity()).currentUser;

        String username = sharedPreferenceSingleton.getSavedString(getContext(),"Name");
        name.setText(username);
        phone.setText(firebaseUser.getPhoneNumber());

        Glide.with(getContext())
                .load(AppConstants.getProfileImage(firebaseUser.getPhoneNumber()))
                .dontAnimate()
                .skipMemoryCache(true)
                .placeholder(R.drawable.default_user)
                .error(R.drawable.default_user)
                .centerCrop()
                .into(pic);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_alarm_method:
                method.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.vibrate));

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.method_dialog);
                try {
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                } catch (Exception e) {
                }
                LinearLayout simple = dialog.findViewById(R.id.simple);
                LinearLayout math = dialog.findViewById(R.id.math);
                simple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferenceSingleton.saveAs(getContext(), "Method", 0);
                        dialog.dismiss();
                    }
                });
                math.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferenceSingleton.saveAs(getContext(), "Method", 1);
                        dialog.dismiss();
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                }, 100);

                break;

            case R.id.setting_rate:
                break;
            case R.id.setting_share:
                break;
            case R.id.setting_theme:
                theme_switch.toggle();
                restartApp();
                break;

            case R.id.edit_pic:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RC_GALLERY);
            break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            sharedPreferenceSingleton.saveAs(getContext(), "Theme", 1);
        } else
            sharedPreferenceSingleton.saveAs(getContext(), "Theme", 0);
        //restartApp();
    }

    public void restartApp() {
        Intent i = getActivity().getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        assert i != null;
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RC_GALLERY && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String mediaPath = cursor.getString(columnIndex);
                Glide.with(getContext())
                        .load(mediaPath)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.default_user)
                        .centerCrop()
                        .into(pic);
                // Set the Image in ImageView for Previewing the Media
                cursor.close();

                uploadFile(mediaPath);

            } else {
                Toast.makeText(getContext(), "No image picked", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadFile(String mediaPath) {
        //progressDialog.show();

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        String extension=mediaPath.substring(mediaPath.lastIndexOf('.'));

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", firebaseUser.getPhoneNumber()+".jpg", requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), firebaseUser.getPhoneNumber()+".jpg"+extension);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        Call<ServerResponse> call = requestInterface.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getResult().equalsIgnoreCase("Successful")) {
                        Toast.makeText(getContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }
}
