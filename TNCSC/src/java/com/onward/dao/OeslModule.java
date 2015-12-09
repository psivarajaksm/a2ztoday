/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.onward.accounts.dao.*;
import com.onward.budget.dao.BudgetService;
import com.onward.budget.dao.BudgetServiceImpl;
import com.onward.budget.dao.FileUploadService;
import com.onward.budget.dao.FileUploadServiceImpl;
import com.onward.edli.dao.EDLIService;
import com.onward.edli.dao.EDLIServiceImpl;
import com.onward.epf.dao.EPFTransactionService;
import com.onward.epf.dao.EPFTransactionServiceImpl;

/**
 *
 * @author onward
 */
public class OeslModule extends AbstractModule {

    protected void configure() {
        GlobalDBOpenCloseAndUserPrivilagesImp GlobalDBOpenCloseAndUserPrivilagesImpObj = new GlobalDBOpenCloseAndUserPrivilagesImp();
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(GlobalDBOpenCloseAndUserPrivilages.class),
                GlobalDBOpenCloseAndUserPrivilagesImpObj);
        bind(EmployeePayBillService.class).to(EmployeePayBillServiceImpl.class);
        bind(EmployeeMasterService.class).to(EmployeeMasterServiceImpl.class);
        bind(EmployeePayBillProcess.class).to(EmployeePayBillProcessImpl.class);
        bind(EmployeeLoansAndAdvances.class).to(EmployeeLoansAndAdvancesImpl.class);
        bind(MasterService.class).to(MasterServiceImpl.class);
        bind(SupplementaryBillService.class).to(SupplementaryBillServiceImpl.class);
        bind(EmployeeTransferOutService.class).to(EmployeeTransferOutServiceImpl.class);
        bind(SalaryDeductionOthersService.class).to(SalaryDeductionOthersServiceImpl.class);
        bind(EmployeeFundSubService.class).to(EmployeeFundSubServiceImpl.class);
        bind(EarningSlapDetailsService.class).to(EarningSlapDetailsServiceImpl.class);
        bind(UserTypeService.class).to(UserTypeServiceImpl.class);
        bind(BonusBillService.class).to(BonusBillServiceImpl.class);
        bind(DAIncrementArrearService.class).to(DAIncrementArrearServiceImpl.class);
        bind(AccountsMasterService.class).to(AccountsMasterServiceImpl.class);
        bind(AccountVoucherService.class).to(AccountVoucherServiceImpl.class);
        bind(IncomeTaxService.class).to(IncomeTaxServiceImpl.class);
        bind(ReportNameService.class).to(ReportNameServiceImpl.class);
        bind(AccountReportService.class).to(AccountReportServiceImpl.class);
        bind(CashierEntryService.class).to(CashierEntryServiceImpl.class);
        bind(Tax.class).to(TaxImpl.class);
        bind(EPFTransactionService.class).to(EPFTransactionServiceImpl.class);
        bind(RegionwiseReportService.class).to(RegionwiseReportServiceImpl.class);
        bind(EDLIService.class).to(EDLIServiceImpl.class);
        bind(BudgetService.class).to(BudgetServiceImpl.class);
        bind(FileUploadService.class).to(FileUploadServiceImpl.class);
        bind(BankEntryService.class).to(BankEntryServiceImpl.class);
        bind(BRSService.class).to(BRSServiceImpl.class);
    }
}
