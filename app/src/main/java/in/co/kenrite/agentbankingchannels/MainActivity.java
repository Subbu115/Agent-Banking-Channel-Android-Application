package in.co.kenrite.agentbankingchannels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView depositTxt,tranTxt,changeTxt,logoutTxt,payTxt,agentName,agentBalance,balanceTxt,serviceTxt;
    String mobile,agent_name,account_no;
    int agent_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agentName=(TextView)findViewById(R.id.agent_name);
        agentBalance=(TextView)findViewById(R.id.agent_bal);
        agentDetails();
        payTxt=(TextView)findViewById(R.id.pay_txt);
        payTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CustomerMobileNo.class);
                intent.putExtra("agent_acno",account_no);
                startActivity(intent);
            }
        });
        depositTxt=(TextView)findViewById(R.id.deposit_txt);
        depositTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DepositCash.class);
                intent.putExtra("agent_acno",account_no);
                intent.putExtra("agent_balance",agent_balance);
                startActivity(intent);
            }
        });
        tranTxt=(TextView)findViewById(R.id.transaction_txt);
        tranTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AgentTransactionDetails.class);
                startActivity(intent);
            }
        });
        balanceTxt=(TextView)findViewById(R.id.bal_txt);
        balanceTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,GetBalance.class);
                startActivity(intent);
            }
        });
        serviceTxt=(TextView)findViewById(R.id.service_txt);
        serviceTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,Services.class);
                startActivity(intent);
            }
        });
        logoutTxt=(TextView)findViewById(R.id.logout_txt);
        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("LoginDetails",0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent=new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });
        //PrefManager pf5=new PrefManager(this);
        //mobileNo = pf5.getMobile();
    }
    public void agentDetails()
    {
        PrefManager pf5=new PrefManager(this);
        mobile = pf5.getMobile();
        RequestQueue mRequestQueue;
        Cache cache=new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache,network);
        mRequestQueue.start();

        String url ="http://192.168.43.40/edited/agent-banking-channel/get-agent-details.php?mobile=" + mobile;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray Message = jsonObj.getJSONArray("Message");
                            for (int i = 0; i < Message.length(); i++) {
                                JSONObject c = Message.getJSONObject(i);

                                account_no = c.getString("account_no");
                                agent_name = c.getString("name");
                                agent_balance = c.getInt("balance");

                                //store_bal = productObj.getInt("balance");
                                agentName.setText(agent_name);
                                agentBalance.setText("\u20B9 " +agent_balance);
                            }
                        } catch (final JSONException e) {
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
