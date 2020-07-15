package in.co.kenrite.agentbankingchannels;

public class AccountList {
    private String bank_name,ifsc_code,account_no;

    public AccountList() {
    }
    public AccountList(String account_no, String bank_name, String ifsc_code) {
        this.account_no = account_no;
        this.bank_name = bank_name;
        this.ifsc_code = ifsc_code;
    }
    public String getAccount_no() {
        return account_no;
    }
    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }
    public String getIfsc_code() {
        return ifsc_code;
    }
    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }
    public String getBank_name() {
        return bank_name;
    }
    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
}
