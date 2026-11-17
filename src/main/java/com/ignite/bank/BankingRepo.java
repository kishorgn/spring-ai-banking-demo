package com.ignite.bank;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class BankingRepo {
    public record Transaction(String accountNo, BigDecimal amount, TxnType txnType, String remark){}
    enum TxnType {CR, DR};
    Map<String, Account> accounts = Map.of(
            "123", new Account("123", "Ravi Shankar", null),
            "124", new Account("124", "Kishor Kumar", null),
            "125", new Account("125", "Suresh Kumar", null),
            "126", new Account("126", "Sudheer Shetty", null),
            "127", new Account("127", "Ramesh Kumar", null),
            "129", new Account("128", "Rohit Kumar", null),
            "130", new Account("129", "Raghavendra Kumar", null),
            "131", new Account("130", "Ravindra Kumar", null)
    );

    List<Transaction> transactions = new CopyOnWriteArrayList<>( List.of(
            new Transaction("123", new BigDecimal("1000.00"), TxnType.CR, "Initial Deposit"),
            new Transaction("124", new BigDecimal("1000.00"), TxnType.CR, "Initial Deposit"),
            new Transaction("125", new BigDecimal("1200.00"), TxnType.CR, "Initial Deposit"),
            new Transaction("126", new BigDecimal("1500.00"), TxnType.CR, "Initial Deposit"),
            new Transaction("127", new BigDecimal("12000.00"), TxnType.CR, "Initial Deposit"),
            new Transaction("128", new BigDecimal("1800.00"), TxnType.CR, "Initial Deposit"),
            new Transaction("129", new BigDecimal("1100.00"), TxnType.CR, "Initial Deposit"),
            new Transaction("130", new BigDecimal("1400.00"), TxnType.CR, "Initial Deposit")
    ));

    public BigDecimal deposit(String accountNo, BigDecimal amount){
        if( accounts.get(accountNo) == null ){
            throw new IllegalArgumentException("Account number " + accountNo + " not found");
        }
        if( amount.compareTo(BigDecimal.ZERO) < 0 ){
            throw new IllegalArgumentException("Negative amounts can't be deposited");
        }
        transactions.add(new Transaction(accountNo, amount, TxnType.CR, "Deposit"));
        return amount;
    }

    public BigDecimal withdrawal(String accountNo, BigDecimal amount){
        if( accounts.get(accountNo) == null ){
            throw new IllegalArgumentException("Account number " + accountNo + " not found");
        }
        if( amount.compareTo(BigDecimal.ZERO) < 0 ){
            throw new IllegalArgumentException("Negative amounts can't be withdrawal");
        }
        if( getBalance(accountNo).subtract(amount).compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }
        transactions.add(new Transaction(accountNo, amount, TxnType.DR, "Withdrawal"));
        return amount;
    }

    public BigDecimal transfer(String from, String to, BigDecimal amount){
        if( accounts.get(from) == null ){
            throw new IllegalArgumentException("Account number " + from + " not found");
        }
        if( accounts.get(to) == null ){
            throw new IllegalArgumentException("Account number " + to + " not found");
        }
        if( getBalance(from).add(amount.negate()).compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }
        transactions.add(new Transaction(from, amount, TxnType.DR, "Transfer to " + to));
        transactions.add(new Transaction(to, amount, TxnType.CR, "Transfer from " + from));
        return amount;
    }

    public BigDecimal getBalance(String accountNo){
        if (accounts.get(accountNo) == null) {
            throw new IllegalArgumentException("Account number " + accountNo + " not found");
        }
        return transactions.stream()
                .filter(txn -> txn.accountNo().equals(accountNo))
                .map(txn -> txn.txnType() == TxnType.CR ? txn.amount() : txn.amount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Transaction> listTransactions(String accountNo){
        return transactions.stream()
                .filter(txn -> txn.accountNo().equals(accountNo))
                .toList();
    }

}
