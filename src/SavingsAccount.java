public class SavingsAccount extends BankAccount {
    SavingsAccount(String accHolder, String accNumber, int BIC) {
        super(accHolder, accNumber, BIC);
        setAccType(AccountType.SAVINGS);
    }

}
