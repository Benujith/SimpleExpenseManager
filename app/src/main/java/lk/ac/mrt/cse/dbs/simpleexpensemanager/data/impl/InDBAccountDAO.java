/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.DBHelper;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class InDBAccountDAO implements AccountDAO {
    private DBHelper dbHelper;
    private Map<String, Account> accountss;

    public InDBAccountDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.accountss = new HashMap<>();
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> account_numbers = new ArrayList<>();
        Cursor cursor = dbHelper.getAccountNumbers();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            account_numbers.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return account_numbers;
        //return new ArrayList<>(accounts.keySet());
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> accounts = new ArrayList<>();
        Cursor cursor = dbHelper.getAccountdata();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            accounts.add(new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.valueOf(cursor.getString(3))));
            cursor.moveToNext();
        }
        return accounts;
        //return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        ArrayList<String> account_numbers = new ArrayList<>();
        Cursor cursor = dbHelper.getAccountNumbers();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            account_numbers.add(cursor.getString(0));
            cursor.moveToNext();
        }
        if (account_numbers.contains(accountNo)){
            Cursor cursor1 = dbHelper.getAccountdata(accountNo);
            return new Account(cursor1.getString(0), cursor1.getString(1), cursor1.getString(2), Double.valueOf(cursor1.getString(3)));
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
        /*
        if (accountss.containsKey(accountNo)) {
            return accountss.get(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);*/
    }

    @Override
    public void addAccount(Account account) {
        dbHelper.insertAccountdata(account.getAccountNo(), account.getBankName(), account.getAccountHolderName(), account.getBalance());
        //accounts.put(account.getAccountNo(), account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbHelper.deleteAccountdata(accountNo);
        /*if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        accounts.remove(accountNo);*/
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        ArrayList<String> account_numbers = new ArrayList<>();
        Cursor cursor = dbHelper.getAccountNumbers();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            account_numbers.add(cursor.getString(0));
            cursor.moveToNext();
        }
        if (!account_numbers.contains(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Cursor acc = dbHelper.getAccountdata(accountNo);
        Account account = new Account(acc.getString(0), acc.getString(1), acc.getString(2), Double.valueOf(acc.getString(3)));
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        dbHelper.updateAccountdata(account.getAccountNo(), account.getBankName(), account.getAccountHolderName(), account.getBalance());
        /*
        if (!accountss.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = accountss.get(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        accountss.put(accountNo, account);
         */
    }
}