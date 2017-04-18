package com.neemathu.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity
  extends AppCompatActivity
{
  public MainActivity() {}
  
  protected void onCreate(final Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968602);
    setSupportActionBar((Toolbar)findViewById(2131492973));
    paramBundle = (EditText)findViewById(2131492976);
    paramBundle.setFocusable(true);
    paramBundle.setFocusableInTouchMode(true);
    paramBundle.requestFocus();
    paramBundle = (EditText)findViewById(2131492976);
    paramBundle.setTransformationMethod(new PasswordTransformationMethod());
    final CheckBox localCheckBox = (CheckBox)findViewById(2131492979);
    localCheckBox.setText("Show Password");
    localCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          paramBundle.setTransformationMethod(null);
          localCheckBox.setText("Hide Password1");
          return;
        }
        paramBundle.setTransformationMethod(new PasswordTransformationMethod());
        localCheckBox.setText("Show Password1");
      }
    });
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    return true;
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == 2131493008) {
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  public void setText(View paramView)
  {
    paramView = (TextView)findViewById(2131492974);
    EditText localEditText1 = (EditText)findViewById(2131492975);
    EditText localEditText2 = (EditText)findViewById(2131492976);
    if (localEditText1.getText().toString().trim().equals(""))
    {
      localEditText1.setError("Please enter phone number");
      localEditText1.setFocusableInTouchMode(true);
      localEditText1.requestFocus();
      return;
    }
    if (localEditText2.getText().toString().trim().equals(""))
    {
      localEditText2.setError("Please enter your email");
      return;
    }
    paramView.setText(localEditText2.getText().toString());
    Log.println(4, "Test1", "Test");
  }
}
