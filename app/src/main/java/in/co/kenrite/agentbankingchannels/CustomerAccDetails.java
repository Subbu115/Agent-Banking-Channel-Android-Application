package in.co.kenrite.agentbankingchannels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerAccDetails extends AppCompatActivity {
    private static final String MAIN_URL = "http://192.168.43.40/edited/agent-banking-channel/";
    private List<AccountList> accountListList = new ArrayList<AccountList>();
    private ListView listView;
    String customer_mobile,bank_name,ifsc_code,account_No,agent_account;
    //int agent_account;
    private CustomAccountAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_acc_details);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomAccountAdapter(CustomerAccDetails.this, accountListList);
        customer_mobile = getIntent().getExtras().getString("customer_mobile");
        agent_account = getIntent().getExtras().getString("agentAcc_no");
        //mbl.setText("" + store_acc);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountList accountList = (AccountList) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),"Selected :"+" "+ accountList.getAccount_no(),Toast.LENGTH_LONG).show();
                String account_no=accountList.getAccount_no();
                String ifsc=accountList.getIfsc_code();
                Intent intent = new Intent(CustomerAccDetails.this, PaymentProcess.class);
                intent.putExtra("account_no", account_no);
                intent.putExtra("ifsc_code", ifsc);
                intent.putExtra("cus_mobile", customer_mobile);
                intent.putExtra("agent_acNo",agent_account);
                startActivity(intent);
            }
        });
        //if(adapter.getCount()>0)
        //{
            getBankName();
        //}
        /*else {
            Toast.makeText(getApplicationContext(),"This Mobile No not have bank account" + customer_mobile + agent_account + ifsc_code + account_No,Toast.LENGTH_LONG).show();
        }*/
    }
    public void getBankName()
    {
        String url=MAIN_URL + "get-bank-details.php?mobile=" + customer_mobile;
        //mbl.setText(url);

        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching details...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray Message = jsonObj.getJSONArray("Message");
                            for (int i = 0; i < Message.length(); i++) {
                                JSONObject c = Message.getJSONObject(i);
                                account_No = c.getString("account_no");
                                ifsc_code = c.getString("ifsc_code");
                                bank_name = c.getString("bank_name");

                                AccountList accountList = new AccountList(account_No,bank_name,ifsc_code);
                                accountList.setAccount_no(account_No);
                                accountList.setIfsc_code(ifsc_code);
                                accountList.setBank_name(bank_name);
                                accountListList.add(accountList);
                            }
                        } catch( final JSONException e){
                            Toast.makeText(getApplicationContext(), "This Mobile No not have bank accounts :" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Busy Day. Cannot load the Bank list.", Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
