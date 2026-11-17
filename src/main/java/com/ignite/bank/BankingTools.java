package com.ignite.bank;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BankingTools {
    private final BankingRepo bankingRepo;

    public BankingTools(BankingRepo bankingRepo) {
        this.bankingRepo = bankingRepo;
    }

    @Tool(description = "Get the balance of an account by the account number")
    public BigDecimal getBalance(String accountNo){
        return bankingRepo.getBalance(accountNo);
    }

    @Tool(description = "Deposit the given amount into an account by the account number")
    public BigDecimal deposit(String accountNo, BigDecimal amount){
        return bankingRepo.deposit(accountNo, amount);
    }

    @Tool(description = "Withdraw the given amount from an account by the account number")
    public BigDecimal withdrawal(String accountNo, BigDecimal amount){
        return bankingRepo.withdrawal(accountNo, amount);
    }

    @Tool(description = "Transfer the given amount from one account to another by their account numbers")
    public BigDecimal transfer(String from, String to, BigDecimal amount){
        return bankingRepo.transfer(from, to, amount);
    }

    @Tool(description = "List all transactions for a given account")
    public List<BankingRepo.Transaction> listTransactions(String accountNo) {
        return bankingRepo.listTransactions(accountNo);
    }

}
