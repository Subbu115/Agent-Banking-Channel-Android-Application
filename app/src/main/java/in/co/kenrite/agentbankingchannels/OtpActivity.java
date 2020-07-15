package in.co.kenrite.agentbankingchannels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class OtpActivity extends AppCompatActivity {
    String agent_acc_num,agent_mobile,customer_acc_num,customer_ifsc_code,customer_mobile;
    int customer_amount,customer_balance;
    EditText otpEdit;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        agent_acc_num = getIntent().getExtras().getString("agent_acc_num");
        agent_mobile = getIntent().getExtras().getString("agent_mobile");
        customer_acc_num = getIntent().getExtras().getString("customer_acc_num");
        customer_ifsc_code = getIntent().getExtras().getString("customer_ifsc_code");
        customer_balance = getIntent().getExtras().getInt("customer_balance");
        customer_amount = getIntent().getExtras().getInt("customer_amount");
        customer_mobile = getIntent().getExtras().getString("customer_mobile");
        generateOTP();
        otpEdit=(EditText) findViewById(R.id.otpEdit);
        button=(Button) findViewById(R.id.amt_transaction_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTransaction();
            }
        });
    }
    public void generateOTP()
    {
        RequestQueue queue= Volley.newRequestQueue(this);

        String url="http://192.168.43.40/edited/agent-banking-channel/m-script-generate-otp.php";
        Toast.makeText(getApplicationContext(),"" + url,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(SignupActivity.this,response,Toast.LENGTH_LONG).show();
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            String error_message = obj.getString("Message");
                            int COUNT = Integer.parseInt(obj.getString("COUNT"));
                            if(COUNT == 1)
                            {
                                //show error essage
                                Toast.makeText(getApplicationContext(),"Enter OTP",Toast.LENGTH_LONG).show();
                                //return;
                                /**Intent intent = new Intent(OtpVerification.this,MainActivity.class);
                                 startActivity(intent);
                                 finish();*/
                            }
                            else
                            {
                                //startHomeActivity();
                                Toast.makeText(getApplicationContext(),error_message.toString(), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        catch(Exception e)
                        {
                            // show error message
                        }}
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile",customer_mobile);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public void doTransaction()
    {
        final String otp = otpEdit.getText().toString();

        RequestQueue queue= Volley.newRequestQueue(this);

        String url="http://192.168.43.40/edited/agent-banking-channel/m-script-transaction.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            String error_message = obj.getString("Message");
                            int COUNT = Integer.parseInt(obj.getString("COUNT"));
                            if(COUNT == 1)
                            {
                                //show error essage
                                Toast.makeText(getApplicationContext(),"OTP verified, transaction is Processing",Toast.LENGTH_LONG).show();
                                //return;
                                Intent intent = new Intent(OtpActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                //startHomeActivity();
                                Toast.makeText(getApplicationContext(),error_message.toString(),Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        catch(Exception e)
                        {
                            // show error message
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }}
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("agent_acc_num",agent_acc_num);
                params.put("agent_mobile",agent_mobile);
                params.put("customer_acc_num",customer_acc_num);
                params.put("customer_ifsc_code",customer_ifsc_code);
                params.put("customer_mobile",customer_mobile);
                params.put("customer_amount",Integer.toString(customer_amount));
                params.put("customer_balance",Integer.toString(customer_balance));
                params.put("otp",otp);
                return params;
            }
        };
        queue.add(stringRequest);
        //startHomeActivity();
    }
}
