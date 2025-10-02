public class CheckingAccount extends BankAccount {
    private float limit;

    CheckingAccount(String accHolder, String accNumber, int BIC, float overdraft) {
        super(accHolder, accNumber, BIC);
        this.limit = overdraft;
        setAccType(AccountType.CHECKING);
    }


    @Override
    public void Withdraw(float amount) {
        if (getBalance() - amount < -limit) {
            System.out.println("Overdraft hit!");
        } else {
            setBalance(getBalance() - amount);
            System.out.println("Withdrawal successful.");
        }
    }
}
