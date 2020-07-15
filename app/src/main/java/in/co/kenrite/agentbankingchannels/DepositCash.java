package in.co.kenrite.agentbankingchannels;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DepositCash extends AppCompatActivity {
    EditText mobileEdit,accEdit,ifscEdit,amtEdit;
    Button button;
    String agentAcc_no,agentMobile;
    int agent_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_cash);
        mobileEdit=(EditText)findViewById(R.id.DepositMobile);
        accEdit=(EditText)findViewById(R.id.DepositAccNo);
        ifscEdit=(EditText)findViewById(R.id.DepositIfsc);
        amtEdit=(EditText)findViewById(R.id.DepositAmt);
        agentAcc_no = getIntent().getExtras().getString("agent_acno");
        agent_balance = getIntent().getExtras().getInt("agent_balance");
    }
    public void depositTrans(View view)
    {
        PrefManager pf5=new PrefManager(this);
        agentMobile = pf5.getMobile();
        final String account_no=accEdit.getText().toString();
        final String mobile_no=mobileEdit.getText().toString();
        final String ifsc_code=ifscEdit.getText().toString();
        final int amount=Integer.parseInt(amtEdit.getText().toString());
        if(agent_balance > amount) {

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = "http://192.168.43.40/edited/agent-banking-channel/m-script-deposit.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                String error_message = obj.getString("Message");
                                int COUNT = Integer.parseInt(obj.getString("COUNT"));
                                if (COUNT == 1) {
                                    //show error essage
                                    Toast.makeText(getApplicationContext(), "Deposit successful", Toast.LENGTH_LONG).show();
                                    //return;
                                    Intent intent = new Intent(DepositCash.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //startHomeActivity();
                                    Toast.makeText(getApplicationContext(), error_message.toString(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } catch (Exception e) {
                                // show error message
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("account_no", account_no);
                    params.put("mobile_no", mobile_no);
                    params.put("ifsc_code", ifsc_code);
                    params.put("amount", Integer.toString(amount));
                    params.put("agentAcc_no", agentAcc_no);
                    params.put("agentMobile", agentMobile);
                    return params;
                }
            };
            queue.add(stringRequest);
        }
        else
        {
            //errTxt.setText("Insufficient Balance");
            AlertDialog.Builder builder = new AlertDialog.Builder(DepositCash.this);
            builder.setTitle("Failed Transaction")
                    .setMessage("Insufficient Balance")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(Payment.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition( 0, 0);
                            startActivity(getIntent());
                            overridePendingTransition( 0, 0);
                        }
                    });

            //Creating dialog box
            AlertDialog dialog  = builder.create();
            dialog.show();
        }
    }
}


