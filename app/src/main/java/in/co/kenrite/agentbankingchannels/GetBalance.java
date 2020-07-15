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

public class GetBalance extends AppCompatActivity {
EditText BalanceAccEdit,BalanceIfscEdit;
Button balanceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_balance);
        BalanceAccEdit=(EditText)findViewById(R.id.BalanceAccNo);
        BalanceIfscEdit=(EditText)findViewById(R.id.BalanceIfsc);

        balanceBtn=(Button)findViewById(R.id.BalanceBtn);
        balanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBalanceDetails();
            }
        });
    }
    public void sendBalanceDetails()
    {
        final String AccNo=BalanceAccEdit.getText().toString();
        final String IFSC=BalanceIfscEdit.getText().toString();
        RequestQueue queue= Volley.newRequestQueue(this);

        String url="http://192.168.43.40/edited/agent-banking-channel/m-script-send-balance.php";
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
                                Toast.makeText(getApplicationContext(),"" +error_message,Toast.LENGTH_LONG).show();
                                //return;
                                Intent intent = new Intent(GetBalance.this,MainActivity.class);
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
                params.put("account_no",AccNo);
                params.put("ifsc_code",IFSC);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
