package in.co.kenrite.agentbankingchannels;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class AgentTransactionDetails extends AppCompatActivity {
    public final String MAIN_URL = "http://192.168.43.40/edited/agent-banking-channel/";
    TableLayout table_display1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_transaction_details);
        transactionInfo();
    }
    public void transactionInfo()
    {
        RequestQueue mRequestQueue;
        Cache cache=new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache,network);
        mRequestQueue.start();
        PrefManager pf5=new PrefManager(this);
        String mobileNo=pf5.getMobile();
        final String DATA_URL = MAIN_URL + "get-transaction-details.php?mobile=" + mobileNo;
        table_display1 = (TableLayout) findViewById(R.id.table_display1);
        //Creating a json array request to get the json from our api
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray Message = jsonObj.getJSONArray("Message");

                            for (int i = 0; i < Message.length(); i++) {
                                JSONObject transObj = Message.getJSONObject(i);

                                int customer_acc_num = transObj.getInt("customer_acc_num");
                                String transaction_date = transObj.getString("transaction_date");
                                int agent_main_bal = transObj.getInt("agent_main_bal");
                                int amount_transfer = transObj.getInt("amount_transfer");
                                int agent_current_bal = transObj.getInt("agent_current_bal");
                                TableRow row = new TableRow(AgentTransactionDetails.this);
                                row.setGravity(Gravity.CENTER);

                                //row.setBackground(ContextCompat.getDrawable(context, R.drawable.border));
                                //row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                row.setWeightSum(5);
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
                                row.setLayoutParams(lp);
                                //lp.gravity=Gravity.CENTER;
                                lp.weight = 1;
                                lp.setMargins(0, 2, 0, 2);
                                TextView tv1 = new TextView(AgentTransactionDetails.this);
                                tv1.setLayoutParams(lp);
                                tv1.setGravity(Gravity.LEFT);
                                tv1.setText(transaction_date);
                                TextView tv2 = new TextView(AgentTransactionDetails.this);
                                tv2.setLayoutParams(lp);
                                tv2.setGravity(Gravity.LEFT);
                                tv2.setText("" + customer_acc_num);
                                /*TextView tv3 = new TextView(Transactions.this);
                                tv3.setLayoutParams(lp);
                                tv3.setGravity(Gravity.LEFT);
                                tv3.setText("" + main_bal);*/
                                TextView tv4 = new TextView(AgentTransactionDetails.this);
                                tv4.setLayoutParams(lp);
                                tv4.setGravity(Gravity.LEFT);
                                tv4.setText("" + amount_transfer);
                                TextView tv5 = new TextView(AgentTransactionDetails.this);
                                tv5.setLayoutParams(lp);
                                tv5.setGravity(Gravity.CENTER);
                                tv5.setText("" + agent_current_bal);

                                row.addView(tv1);
                                row.addView(tv2);
                                //row.addView(tv3);
                                row.addView(tv4);
                                row.addView(tv5);
                                table_display1.addView(row, i);
                            }

                        } catch (final JSONException e) {
                            Toast.makeText(getApplicationContext(), "details parsing error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        //adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Busy Day. Cannot load the product list.", Toast.LENGTH_LONG).show();
            }
        });

        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        mRequestQueue.add(stringRequest);
    }
}
