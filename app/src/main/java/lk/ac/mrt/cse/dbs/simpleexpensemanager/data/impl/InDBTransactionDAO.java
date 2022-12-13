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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.DBHelper;

/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class InDBTransactionDAO implements TransactionDAO {
    private final DBHelper dbHelper;
    private final List<Transaction> transactions;

    public InDBTransactionDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        transactions = new LinkedList<>();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        dbHelper.insertLogdata(date, accountNo, expenseType.toString(), amount);
        /*
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        transactions.add(transaction);
         */
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> trans = new ArrayList<>();
        Cursor logdata = dbHelper.getLogdata();
        logdata.moveToFirst();
        while (!logdata.isAfterLast()){
            try {
                trans.add(new Transaction(new SimpleDateFormat("dd-MM-yyyy").parse(logdata.getString(0)), logdata.getString(1), ExpenseType.valueOf(logdata.getString(2)), Double.valueOf(logdata.getString(3))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return trans;
        //return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> trans = new ArrayList<>();
        Cursor logdata = dbHelper.getLogdata();
        logdata.moveToFirst();
        while (!logdata.isAfterLast()){
            try {
                trans.add(new Transaction(new SimpleDateFormat("dd-MM-yyyy").parse(logdata.getString(0)), logdata.getString(1), ExpenseType.valueOf(logdata.getString(2)), Double.valueOf(logdata.getString(3))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int size = trans.size();
        if (size <= limit) {
            return trans;
        }
        return trans.subList(size - limit, size);
        /*
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
         */
    }

}
