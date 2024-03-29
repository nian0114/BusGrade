package com.lyk.busgrade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by JohnTsai on 16/1/31.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private Button mLoginButton;
    private TextInputLayout mTextInputLayoutName;
    private TextInputLayout mTextInputLayoutPswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linesearch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("线路查询");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mTextInputLayoutName= (TextInputLayout) findViewById(R.id.textInputLayoutStartName);
        mTextInputLayoutPswd = (TextInputLayout) findViewById(R.id.textInputLayoutEndName);

        mEditTextName = (EditText) findViewById(R.id.editStartName);
        mTextInputLayoutName.setErrorEnabled(true);
        mEditTextPassword = (EditText) findViewById(R.id.editEndName);
        mTextInputLayoutPswd.setErrorEnabled(true);

        mLoginButton = (Button) findViewById(R.id.buttonLogin);
        mLoginButton.setOnClickListener(this);
        mEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkName(s.toString(),false);
            }
        });

        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPswd(s.toString(),false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonLogin){
            hideKeyBoard();
            if(!checkName(mEditTextName.getText(),true))
                return;
            if(!checkPswd(mEditTextPassword.getText(),true))
                return;
            Intent intent = new Intent();
            intent.setClass(SearchActivity.this, SchemeActivity.class);
            intent.putExtra("qidian", mEditTextName.getText().toString());
            intent.putExtra("zongdian", mEditTextPassword.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    private boolean checkPswd(CharSequence pswd,boolean isLogin) {
        if(TextUtils.isEmpty(pswd)) {
            if(isLogin) {
                mTextInputLayoutPswd.setError(getString(R.string.error_pswd_empty));
                return false;
            }
        }else{
            mTextInputLayoutPswd.setError(null);
        }
        return true;
    }

    private boolean checkName(CharSequence name,boolean isLogin) {
        if(TextUtils.isEmpty(name)) {
            if(isLogin) {
                mTextInputLayoutName.setError(getString(R.string.error_login_empty));
                return false;
            }
        }else{
            mTextInputLayoutName.setError(null);
        }
        return true;
    }

    private void hideKeyBoard() {
        View view = getCurrentFocus();
        if(view!=null){
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
              .hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
