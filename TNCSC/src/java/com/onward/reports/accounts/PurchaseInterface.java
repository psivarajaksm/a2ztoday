/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.accounts;

import com.onward.valueobjects.AccountsModel;

/**
 *
 * @author Prince vijayakumar.M
 */
public interface PurchaseInterface {

    public void getPrintWriter(AccountsModel am, String filePath);

    public void GrandTotal(AccountsModel am, String filePath);
}
