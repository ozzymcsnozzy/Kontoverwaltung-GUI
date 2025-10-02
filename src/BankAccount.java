public class BankAccount {
    private String accHolder;
    private String accNumber;
    private int BIC;
    private float balance;
    private AccountType accType;

    BankAccount(String accHolder, String accNumber, int BIC) {
        this.accHolder = accHolder;
        this.accNumber = accNumber;
        this.BIC = BIC;
        this.accNumber = accNumber;
    }
    public void setAccType(AccountType accType) {
        this.accType = accType;
    }

    public void Deposit(float amount) {
        balance += amount;
    }

    public void Withdraw(float amount) {
        if (balance > amount) {
            balance -= amount;
        } else {
            System.out.println("Your balance is too low to withdraw that amount");
        }
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public float getBIC() {
        return BIC;
    }

    public String getAccHolder() {
        return accHolder;
    }

    public void setAccHolder(String accHolder) {
        this.accHolder = accHolder;
    }



    @Override
    public String toString() {
        return ("-------Account-------" + "\n" +
                "Account Holder: " + accHolder + "\n" +
                "Account Type: " + accType + "\n" +
                "Account Number: " + accNumber + "\n" +
                "BIC: " + BIC + "\n" +
                "Balance: " + balance);
    }
}