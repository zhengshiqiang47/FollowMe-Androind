package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.UserUtil;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/12/23.
 */

public class SignUpActivity extends Activity {

    @Bind(R.id.sign_up_error)
    TextView erroText;
    @Bind(R.id.sign_up_username_edit)
    EditText usernameEdit;
    @Bind(R.id.sign_up_password_edit)
    EditText passwordEdit;
    @Bind(R.id.sign_up_button)
    TextView signUpButton;
    @Bind(R.id.sign_up_nick_edit)
    EditText nickNameEdit;

    String userName;
    String password;
    String nickName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameEdit.getText()!=null&&passwordEdit.getText()!=null){
                    userName=usernameEdit.getText().toString();
                    password=passwordEdit.getText().toString();
                    nickName=nickNameEdit.getText().toString();
                    String reg="[a-z0-9_-]*";
                    Pattern pattern=Pattern.compile(reg);
                    if(pattern.matcher(userName).matches()){
                        new signup().execute();
                    }else {
                        erroText.setText("用户名不符合要求");
                    }
                    Log.i("SignUp",userName+" "+password);
                }
            }
        });
    }

    private class signup extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            UserUtil.signUp(getApplicationContext(),userName,"111222333",nickName);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(LoginActivity.RESULT_OK);
            finish();
        }
    }
}
