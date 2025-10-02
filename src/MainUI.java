import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainUI {
    private JPanel panel1;
    private JButton createAccBtn;
    private JButton depositBtn;
    private JButton withdrawBtn;
    private JButton showAccBtn;
    private JButton delAccBtn;
    private JButton transferBtn;

    // Data storage
    private final List<BankAccount> accounts = new ArrayList<>();

    public MainUI() {
        createAccBtn.addActionListener(e -> createAccountDialog());
        depositBtn.addActionListener(e -> {
            int i = accSelect("Which Account would you like to deposit to?");
            if (i < 0) return;
            Float amount = amountDialog("Deposit amount:");
            if (amount == null || amount <= 0) return;
            accounts.get(i).Deposit(amount);
            info("New Account Balance: " + accounts.get(i).getBalance());
        });
        withdrawBtn.addActionListener(e -> {
            int i = accSelect("Select Account to withdraw from");
            if (i < 0) return;
            Float amount = amountDialog("Withdraw amount:");
            if (amount == null || amount <= 0) return;
            accounts.get(i).Withdraw(amount);
            info("New Balance: " + accounts.get(i).getBalance());
        });
        showAccBtn.addActionListener(e -> {
            int i = accSelect("Show account statement for:");
            if (i < 0) return;
            JOptionPane.showMessageDialog(panel1, accounts.get(i).toString(),
                    "Account Statement", JOptionPane.INFORMATION_MESSAGE);
        });
        delAccBtn.addActionListener(e -> {
            int i = accSelect("Which account to delete?");
            if (i < 0) return;
            BankAccount k = accounts.remove(i);
            info("Account deleted: " + k.getAccHolder() + " • " + k.getAccNumber());
        });
        transferBtn.addActionListener(e -> {
            if (accounts.size() < 2) { warn("At least two accounts required."); return; }
            int from = accSelect("Transfer from which account?");
            if (from < 0) return;
            int to = accSelect("Transfer to which account?");
            if (to < 0 || to == from) { warn("Invalid target account."); return; }
            Float amount = amountDialog("Transfer amount:");
            if (amount == null || amount <= 0) return;

            BankAccount source = accounts.get(from);
            BankAccount target = accounts.get(to);
            float oldSource = source.getBalance();
            source.Withdraw(amount);
            if (source.getBalance() == oldSource && !(source instanceof CreditAccount)) {
                warn("Debit not performed (limit/coverage?).");
                return;
            }
            target.Deposit(amount);
            info("Transfer successful.\nSource new balance: " + source.getBalance()
                    + "\nTarget new balance: " + target.getBalance());
        });
    }

    private void createAccountDialog() {
        String[] types = {"CheckingAccount", "SavingsAccount", "CreditAccount"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        JTextField holder = new JTextField();
        JTextField accNo = new JTextField();
        JTextField BIC = new JTextField();
        JTextField limit = new JTextField();

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel("Account type:")); p.add(typeBox);
        p.add(new JLabel("Holder:")); p.add(holder);
        p.add(new JLabel("Account number:")); p.add(accNo);
        p.add(new JLabel("BIC:")); p.add(BIC);
        p.add(new JLabel("Overdraft limit (only Checking):")); p.add(limit);

        typeBox.addActionListener(e -> {
            boolean checking = typeBox.getSelectedIndex() == 0;
            limit.setEnabled(checking); limit.setEditable(checking);
        });
        typeBox.setSelectedIndex(0);

        int res = JOptionPane.showConfirmDialog(panel1, p, "Create Account",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String accHolder = holder.getText().trim();
        String accNumber = accNo.getText().trim();
        if (accHolder.isEmpty() || accNumber.isEmpty()) { warn("Please fill out all required fields."); return; }

        int bankCode;
        try { bankCode = Integer.parseInt(BIC.getText().trim()); }
        catch (Exception ex) { warn("Bank code must be a number."); return; }

        BankAccount newAcc;
        switch ((String) typeBox.getSelectedItem()) {
            case "CheckingAccount":
                float overdraft;
                try { overdraft = Float.parseFloat(limit.getText().trim().replace(',', '.')); }
                catch (Exception ex) { warn("Please enter a valid overdraft limit"); return; }
                newAcc = new CheckingAccount(accHolder, accNumber, bankCode, overdraft);
                break;
            case "SavingsAccount":
                newAcc = new SavingsAccount(accHolder, accNumber, bankCode);
                break;
            default:
                newAcc = new CreditAccount(accHolder, accNumber, bankCode);
        }
        accounts.add(newAcc);
        info("Account created: " + accHolder + " • " + accNumber);
    }

    private int accSelect(String title) {
        if (accounts.isEmpty()) { warn("No Accounts Available."); return -1; }
        String[] display = new String[accounts.size()];
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount k = accounts.get(i);
            display[i] = i + ": " + k.getAccHolder() + " • " + k.getAccNumber()
                    + " • " + k.getClass().getSimpleName();
        }
        String s = (String) JOptionPane.showInputDialog(panel1, title, "Select Account",
                JOptionPane.PLAIN_MESSAGE, null, display, display[0]);
        if (s == null) return -1;
        try { return Integer.parseInt(s.substring(0, s.indexOf(':'))); }
        catch (Exception e) { return -1; }
    }

    private Float amountDialog(String title) {
        String s = JOptionPane.showInputDialog(panel1, title, "0.00");
        if (s == null) return null;
        try { return Float.parseFloat(s.replace(',', '.')); }
        catch (NumberFormatException e) { warn("Invalid amount."); return null; }
    }

    private void info(String msg) { JOptionPane.showMessageDialog(panel1, msg, "Info", JOptionPane.INFORMATION_MESSAGE); }
    private void warn(String msg) { JOptionPane.showMessageDialog(panel1, msg, "Notice", JOptionPane.WARNING_MESSAGE); }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Bank Account");
            f.setContentPane(new MainUI().panel1);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
