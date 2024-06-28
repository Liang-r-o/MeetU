package com.example.meet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.framework.cloud.CloudManager;
import com.example.framework.entry.Constants;
import com.example.meet.R;

public class ChatActivity extends AppCompatActivity {

    public static void startActivity(Context mContext, String userId,
                                     String userName, String userPhoto) {
//        if (!CloudManager.getInstance().isConnect()) {
//            Toast.makeText(mContext, mContext.getString(R.string.text_server_status), Toast.LENGTH_SHORT).show();
//            return;
//        }
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID, userId);
        intent.putExtra(Constants.INTENT_USER_NAME, userName);
        intent.putExtra(Constants.INTENT_USER_PHOTO, userPhoto);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}