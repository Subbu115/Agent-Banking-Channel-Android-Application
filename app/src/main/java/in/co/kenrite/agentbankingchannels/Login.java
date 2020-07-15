package in.co.kenrite.agentbankingchannels;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private AutoCompleteTextView mMobileView;
    private EditText mPwdView;
    private TextView Errormsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mMobileView = (AutoCompleteTextView) findViewById(R.id.LMobile);
        mPwdView = (EditText) findViewById(R.id.LPassword);
        mPwdView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Errormsg =(TextView) findViewById(R.id.Errormsg);
        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        //Here we will validate saved preferences
        if (!new PrefManager(this).isUserLogedOut()) {
            //user's mobileno and pin both are saved in preferences
            startHomeActivity();
        }
    }
    private void attemptLogin() {

        mMobileView.setError(null);
        mPwdView.setError(null);

        // Store values at the time of the login attempt.
        final String mobileno = mMobileView.getText().toString();
        final String password = mPwdView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPwdView.setError(getString(R.string.error_invalid_password));
            focusView = mPwdView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mobileno)) {
            mMobileView.setError(getString(R.string.error_field_required));
            focusView = mMobileView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // save data in local shared preferences
        }
        RequestQueue queue= Volley.newRequestQueue(this);
        // final TextView mTextView = null;
        String url="http://192.168.43.40/edited/agent-banking-channel/m-validate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //mTextView.setText("Responce is: "+ response.substring(0, 500));
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            int valid = Integer.parseInt(obj.getString("Valid"));
                            String message = obj.getString("Message");
                            if(valid == 0)
                            {
                                //show error essage
                                Errormsg.setText(message);
                            }
                            else
                            {
                                saveJWT(message);
                                saveLoginDetailsProvider(mobileno, password);
                                startHomeActivity();
                            }
                        }
                        catch(Exception e)
                        {
                            // show error message
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //mTextView.setText("That did not work!");
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileno",mobileno);
                params.put("password",password);
                return params;
            }
        };
        queue.add(stringRequest);
        //startHomeActivity();
    }
    private void saveJWT(String message)
    {
        PrefManager pf = new PrefManager(this);
        pf.putString(PrefManager.JWT_TOKEN, message);
    }

    private void startHomeActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void saveLoginDetailsProvider(String mobileno, String password) {
        new PrefManager(this).saveLoginDetails(mobileno, password);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }
}
