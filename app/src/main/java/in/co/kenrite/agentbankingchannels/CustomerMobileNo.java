package in.co.kenrite.agentbankingchannels;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerMobileNo extends AppCompatActivity {
    EditText mobileEdit;
    Button btnSubmit;
    String agentAcc_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mobile_no);
        mobileEdit=(EditText)findViewById(R.id.MobileEdit);
        agentAcc_no = getIntent().getExtras().getString("agent_acno");
        btnSubmit=(Button)findViewById(R.id.mblNo_btn);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mobileEdit.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Mobile Number Field is required ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    String mobilePattern = "^[6789]\\d{9}$";
                    if (mobileEdit.getText().toString().trim().matches(mobilePattern))
                    {
                        String customer_mobile=mobileEdit.getText().toString();
                        Intent intent =new Intent(CustomerMobileNo.this,CustomerAccDetails.class);
                        intent.putExtra("customer_mobile",customer_mobile);
                        intent.putExtra("agentAcc_no",agentAcc_no);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Field Error :Invalid Mobile No", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
