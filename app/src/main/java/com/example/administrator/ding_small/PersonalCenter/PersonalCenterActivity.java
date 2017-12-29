package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.ding_small.AccountBookActivity;
import com.example.administrator.ding_small.ContactsActivity;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.NotepadActivity;
import com.example.administrator.ding_small.NotepadBtnActivity;
import com.example.administrator.ding_small.R;

/**
 * Created by Administrator on 2017/11/6.
 */

public class PersonalCenterActivity extends Activity implements View.OnClickListener{
    private ImageView f_account,f_contacts,f_center,f_notepad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center);
        findViewById(R.id.perfect).setOnClickListener(this);
        findViewById(R.id.instructions).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.setting).setOnClickListener(this);
        findViewById(R.id.binding_company).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        findViewById(R.id.edit_pass).setOnClickListener(this);
        f_account=findViewById(R.id.f_account);
        f_contacts=findViewById(R.id.f_contacts);
        f_center=findViewById(R.id.f_center);
        f_notepad=findViewById(R.id.f_notepad);
        f_center.setImageResource(R.drawable.center_yes);
        f_contacts.setImageResource(R.drawable.contacts_no);

        f_contacts.setOnClickListener(this);
        f_account.setOnClickListener(this);
        f_notepad.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.perfect:
                intent=new Intent(PersonalCenterActivity.this,PersonalCenterPerfectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.instructions:
                intent=new Intent(PersonalCenterActivity.this,InstructionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.setting:
                intent=new Intent(PersonalCenterActivity.this,SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.about:
                intent=new Intent(PersonalCenterActivity.this,AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.binding_company:
                intent=new Intent(PersonalCenterActivity.this,BindingCompanyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.add:
                intent=new Intent(PersonalCenterActivity.this,NotepadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_account:
                intent=new Intent(PersonalCenterActivity.this,AccountBookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_notepad:
                intent=new Intent(PersonalCenterActivity.this,NotepadBtnActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_contacts:
                intent=new Intent(PersonalCenterActivity.this,ContactsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.exit:
                intent=new Intent(PersonalCenterActivity.this, LoginAcitivity.class);
                startActivity(intent);
                System.exit(0);
                break;
            case R.id.edit_pass:
                intent=new Intent(PersonalCenterActivity.this,EditPassWordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
