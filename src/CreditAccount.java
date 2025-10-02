public class CreditAccount extends BankAccount {

    CreditAccount(String accHolder, String accNumber, int BIC) {
        super(accHolder, accNumber, BIC);
        setAccType(AccountType.CREDIT);
    }

    @Override
    public void Withdraw(float amount) {
        setBalance(getBalance() - amount);
    }

    @Override
    public void Deposit(float amount) {
        if (getBalance() + amount < 0) {
            setBalance(getBalance() + amount);
        } else {
            System.out.println("Error");
        }

    }
}
