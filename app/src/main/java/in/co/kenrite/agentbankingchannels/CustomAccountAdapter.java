package in.co.kenrite.agentbankingchannels;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAccountAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<AccountList> accountLists;

    public CustomAccountAdapter(Activity activity,List<AccountList> accountLists)
    {
        this.activity=activity;
        this.accountLists=accountLists;
    }
    public CustomAccountAdapter(List<AccountList> ItemList) {
    }
    @Override
    public int getCount() {
        return accountLists.size();
    }

    @Override
    public Object getItem(int location) {
        return accountLists.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.customer_bank_list, null);


        TextView account_no = (TextView) convertView.findViewById(R.id.account_no);
        TextView ifsc_code = (TextView) convertView.findViewById(R.id.ifsc_code);
        TextView bank_name = (TextView) convertView.findViewById(R.id.bank_Name);

        // getting movie data for the row
        AccountList aList = accountLists.get(position);

        //account no
        account_no.setText(aList.getAccount_no());

        // bank
        bank_name.setText(aList.getBank_name());

        // ifsc
        ifsc_code.setText(aList.getIfsc_code());

        return convertView;
    }
}

