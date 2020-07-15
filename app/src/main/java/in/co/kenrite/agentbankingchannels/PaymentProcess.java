package in.co.kenrite.agentbankingchannels;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentProcess extends AppCompatActivity {
    String customer_mbl,ifsc_code,customer_acc,agent_acc,agent_mobile;
    EditText amountEdit;
    Button paymentBtn;
    TextView errorTxt;
    int customer_balance,customer_amount,agent_balance;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_process);
        errorTxt = (TextView) findViewById(R.id.errorTxt);
        amountEdit=(EditText) findViewById(R.id.amountEdit);
        paymentBtn=(Button) findViewById(R.id.payment_button);
        customer_acc=getIntent().getExtras().getString("account_no");
        customer_mbl=getIntent().getExtras().getString("cus_mobile");
        ifsc_code=getIntent().getExtras().getString("ifsc_code");
        agent_acc = getIntent().getExtras().getString("agent_acNo");
        //textView=(TextView)findViewById(R.id.text);
        //textView.setText("" + customer_acc + customer_mbl + ifsc_code + agent_acc);
    }
    public void balanceInfo(View v)
    {
        PrefManager pf5=new PrefManager(this);
        agent_mobile = pf5.getMobile();

        if (TextUtils.isEmpty(amountEdit.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Field is required ", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            customer_amount = Integer.parseInt(amountEdit.getText().toString());
            RequestQueue mRequestQueue;
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();

            String url = "http://192.168.43.40/edited/agent-banking-channel/get-balance.php?ifsc=" + ifsc_code + "&accNo=" + customer_acc;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                JSONObject productObj = jsonObj.getJSONObject("Message");

                                customer_balance = productObj.getInt("balance");

                                //checkBalance(customer_acc, ifsc_code, agent_mobile, customer_amount, customer_balance, customer_mbl);
                                checkBalance(customer_amount,customer_balance,agent_mobile);
                            } catch (final JSONException e) {
                                Toast.makeText(getApplicationContext(), "Not Valid Account no or Mobile No", Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "Details parsing error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Busy Day. Cannot load the Seller Details.", Toast.LENGTH_LONG).show();
                }
            });

            mRequestQueue.add(stringRequest);
        }
    }
    //public void checkBalance(String customer_acc,String ifsc_code, String agent_mobile, int customer_amount, int customer_balance, String customer_mbl)
    public void checkBalance(int customer_amount, int customer_balance, String agent_mobile)
    {

        if(customer_balance > customer_amount)
        {
            //errTxt.setText("");

            Intent intent=new Intent(PaymentProcess.this,OtpActivity.class);

            intent.putExtra("agent_acc_num", agent_acc);
            intent.putExtra("agent_mobile", agent_mobile);
            intent.putExtra("customer_amount", customer_amount);
            intent.putExtra("customer_balance", customer_balance);
            intent.putExtra("customer_acc_num", customer_acc);
            intent.putExtra("customer_ifsc_code", ifsc_code);
            intent.putExtra("customer_mobile", customer_mbl);


            startActivity(intent);
            finish();
        }
        else
        {
            //errTxt.setText("Insufficient Balance");
            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentProcess.this);
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
