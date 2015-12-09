/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.onward.dao.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import net.sf.jasperreports.engine.JRException;
import org.apache.struts.action.*;

import com.google.inject.Guice;
import com.google.inject.Injector;
//import com.itextpdf.text.Rectangle;
import com.onward.dao.OeslModule;

import java.io.*;
import java.util.*;
import org.apache.struts.validator.DynaValidatorForm;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Rectangle;
import com.onward.common.ApplicationConstants;
import com.onward.util.AppProps;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Prince vijayakumar M
 */
public class AccountsReportsAction extends OnwardAction {

    OeslModule oeslModule = new OeslModule();
    Injector injector = Guice.createInjector(oeslModule);
    AccountReportService accountReportService = (AccountReportService) injector.getInstance(AccountReportService.class);
    AccountVoucherService accountVoucherService = (AccountVoucherService) injector.getInstance(AccountVoucherService.class);
    ReportNameService reportNameService = (ReportNameService) injector.getInstance(ReportNameService.class);
    RegionwiseReportService regionwiseReportService = injector.getInstance(RegionwiseReportService.class);

    public ActionForward ledgerViewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("ledgerViewPage");
    }

    public ActionForward consolidatedLedgerViewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("consolidatedLedgerViewPage");
    }

    public ActionForward ledgerViewRegionPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String regionlist = regionwiseReportService.getRegionList(null, request, response, null, null);
        request.getSession().setAttribute("regionlist", regionlist);
        return mapping.findForward("ledgerViewRegionPage");
    }

    public ActionForward trailBalanceViewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("trailBalanceViewPage");
    }

    public ActionForward consolidatedTrailBalanceViewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("consolidatedTrailBalanceViewPage");
    }

    public ActionForward trailBalanceRegionViewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String regionlist = regionwiseReportService.getRegionList(null, request, response, null, null);
        request.getSession().setAttribute("regionlist", regionlist);
        return mapping.findForward("trailBalanceRegionViewPage");
    }

    public ActionForward progressivetrailBalanceViewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("progressivetrailBalanceViewPage");
    }

    public ActionForward ledgerSingleAccountHeadReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("ledgerSingleAccountHeadReportPage");
    }

    public ActionForward receiptAccHeadWiseAbstractPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("receiptAccHeadWiseAbstractPage");
    }

    public ActionForward cashBookAbstractPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("cashBookAbstractPage");
    }

    public ActionForward accountsVouchersDashboard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("accountsVouchersDashboard");
    }

    public ActionForward partyLedgerReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("partyLedgerReportPage");
    }

    public ActionForward cashBookReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************************** AccountsReportsAction class cashBookReportPage method ******************************");
        request.getSession().setAttribute("bookname", getBookType(request, response));
        return mapping.findForward("cashBookReportPage");
    }

    public ActionForward cashBookReportRegionPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************************** AccountsReportsAction class cashBookReportPage method ******************************");
        request.getSession().setAttribute("bookname", getBookType(request, response));
        String regionlist = regionwiseReportService.getRegionList(null, request, response, null, null);
        request.getSession().setAttribute("regionlist", regionlist);
        return mapping.findForward("cashBookReportRegionPage");
    }

    public Map cashBookPrintout(String startingdate, String enddate, String cashbookname, String cashbooktype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class cashBookPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
            String booktype = null;
            if (cashbooktype.equals("1")) {
                booktype = "C1";
            } else if (cashbooktype.equals("2")) {
                booktype = "C2";
            } else if (cashbooktype.equals("3")) {
                booktype = "NC";
            }
            String fname = null;
            if (cashbookname.equals("P")) {
                fname = "P" + booktype;
            } else if (cashbookname.equals("R")) {
                fname = "R" + booktype;
            } else if (cashbookname.equals("B")) {
                fname = "B";
            } else if (cashbookname.equals("J")) {
                fname = "J";
            }

            String fileName = reportNameService.getFileName(null, request, response, null, null, fname, enddate.substring(3, 5), enddate.substring(6, 10));
            System.out.println("fileName = " + fileName);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            if (cashbookname.equals("P")) {
                map = accountReportService.paymentcashBookPrintout(null, request, response, null, null, startingdate, enddate, "PAYMENT", cashbooktype, filePathwithName);
            } else if (cashbookname.equals("R")) {
                map = accountReportService.receiptcashBookPrintout(null, request, response, null, null, startingdate, enddate, "RECEIPT", cashbooktype, filePathwithName);
            } else if (cashbookname.equals("B")) {
                map = accountReportService.bankcashBookPrintout(null, request, response, null, null, startingdate, enddate, "BANK", cashbooktype, filePathwithName);
            } else if (cashbookname.equals("J")) {
                map = accountReportService.journalcashBookPrintout(null, request, response, null, null, startingdate, enddate, "JOURNAL", cashbooktype, filePathwithName);
            }
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map cashBookRegionPrintout(String startingdate, String enddate, String cashbookname, String cashbooktype, String selectedRegion, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class cashBookPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
            String booktype = null;
            if (cashbooktype.equals("1")) {
                booktype = "C1";
            } else if (cashbooktype.equals("2")) {
                booktype = "C2";
            } else if (cashbooktype.equals("3")) {
                booktype = "NC";
            }
            String fname = null;
            if (cashbookname.equals("P")) {
                fname = "P" + booktype;
            } else if (cashbookname.equals("R")) {
                fname = "R" + booktype;
            } else if (cashbookname.equals("B")) {
                fname = "B";
            } else if (cashbookname.equals("J")) {
                fname = "J";
            }

            String fileName = reportNameService.getFileNameByRegion(null, request, response, null, null, fname, enddate.substring(3, 5), enddate.substring(6, 10), selectedRegion);
            System.out.println("fileName = " + fileName);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            if (cashbookname.equals("P")) {
                map = accountReportService.paymentcashBookPrintoutByRegion(null, request, response, null, null, startingdate, enddate, "PAYMENT", cashbooktype, filePathwithName, selectedRegion);
            } else if (cashbookname.equals("R")) {
                map = accountReportService.receiptcashBookPrintoutByRegion(null, request, response, null, null, startingdate, enddate, "RECEIPT", cashbooktype, filePathwithName, selectedRegion);
            } else if (cashbookname.equals("B")) {
                map = accountReportService.bankcashBookPrintoutByRegion(null, request, response, null, null, startingdate, enddate, "BANK", cashbooktype, filePathwithName, selectedRegion);
            } else if (cashbookname.equals("J")) {
                map = accountReportService.journalcashBookPrintoutByRegion(null, request, response, null, null, startingdate, enddate, "JOURNAL", cashbooktype, filePathwithName, selectedRegion);
            }
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward voucherreportpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************************** AccountsReportsAction class voucherreportpage method ******************************");
        request.getSession().setAttribute("accountbook", accountReportService.getAccountBook(null, request, response, null, null));
        return mapping.findForward("voucherreportpage");
    }

    public ActionForward voucherreportRegionpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************************** AccountsReportsAction class voucherreportpage method ******************************");
        request.getSession().setAttribute("accountbook", accountReportService.getAccountBook(null, request, response, null, null));
        String regionlist = regionwiseReportService.getRegionList(null, request, response, null, null);
        request.getSession().setAttribute("regionlist", regionlist);
        return mapping.findForward("voucherreportRegionpage");
    }

    public Map getVoucherDetails(String startingdate, String enddate, String cashbooktype, String voucherno, HttpServletRequest request, HttpServletResponse response) {
        return accountReportService.getVoucherDetails(null, request, response, null, null, startingdate, enddate, cashbooktype, voucherno);
    }

    public Map getVoucherDetailsByRegion(String startingdate, String enddate, String cashbooktype, String voucherno, String selectedRegion, HttpServletRequest request, HttpServletResponse response) {
        return accountReportService.getVoucherDetailsByRegion(null, request, response, null, null, startingdate, enddate, cashbooktype, voucherno, selectedRegion);
    }

    public Map getVoucherReceiptDetailDetails(String startingdate, String enddate, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getVoucherReceiptDetailDetails(null, request, response, null, null, startingdate, enddate);
    }

    public Map getVoucherReceiptDetailDetailsPrintOut(String startingdate, String enddate, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Bon" + months[Integer.valueOf(endmonth) - 1] + endyear.substring(2, 4) + ".txt";
            String fileName = "voucherreciptdetails.txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            BonusBillService bonusBillService = (BonusBillService) injector.getInstance(BonusBillService.class);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountVoucherService.getVoucherReceiptDetailDetailsPrint(null, request, response, null, null, startingdate, enddate, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map voucherPrintout(String voucherno, String vdate, String booktype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class voucherPrintout Method is calling ************************");
        System.out.println("voucherno = " + voucherno);
        Map map = new HashMap();
        try {
            System.out.println("voucherno = " + voucherno);

            String bookname[] = {"", "CL1", "CL2", "NCL", "BA"};

//            String fileName = reportNameService.getFileName(null, request, response, null, null, bookname[Integer.valueOf(booktype)], vdate.substring(3, 5), vdate.substring(6, 10));
            String fileName = voucherno + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            System.out.println("filePathwithName = > " + filePathwithName);

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                String pdffileName = fileName.replace(".txt", ".pdf");
                File ex_txt_file = new File(filePath + "/" + fileName);
                File ex_pdf_file = new File(filePath + "/" + pdffileName);
                if (ex_txt_file.exists()) {
                    ex_txt_file.delete();
                }
                if (ex_pdf_file.exists()) {
                    ex_pdf_file.delete();
                }
            }

            map = accountReportService.voucherPrintout(null, request, response, null, null, voucherno, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public String TxtToPdf(String filePath) {
        System.out.println("********************** AccountsReportsAction.class TxtToPdf Method is calling ************************");
        String path = filePath.replace(".txt", ".pdf");
        try {
//            float width = mmToPt(95);
//            float height = mmToPt(50);
            Rectangle rectanglePage = new Rectangle(740, 960);
            char[] FORM_FEED = {(char) 12};
            char[] BOLD = {(char) 14, (char) 15};

            CharSequence BOLDNORMAL = "";
            CharSequence BOLDWITHLARGEFONT = "";

            FileInputStream fstream = new FileInputStream(filePath);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            Document document = new Document(PageSize.A4, 30, 0, 30, 0);
            Document document = new Document(rectanglePage, 30, 0, 30, 0);

            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            FontSelector fontselector;
            String strLine;
            int j = 1;
            while ((strLine = br.readLine()) != null) {
                fontselector = new FontSelector();
                char[] charArray = strLine.toCharArray();
                boolean check = false;
                if (strLine.length() == 0) {
                    document.add(Chunk.NEWLINE);
                } else if (Arrays.equals(charArray, FORM_FEED)) {
                    document.newPage();
                } else {
                    strLine = strLine.replace(" ", "\u00a0");
                    if (strLine.contains(BOLDNORMAL)) {
                        char ch[] = strLine.toCharArray();
                        StringBuffer buf = new StringBuffer();
                        for (int i = 0; i < ch.length; i++) {
                            if ((int) ch[i] == 14 || (int) ch[i] == 15 || (int) ch[i] == 18) {
                            } else {
                                buf.append(ch[i]);
                            }
                        }
                        strLine = buf.toString();
                        fontselector.addFont(new Font(Font.COURIER, 14, 1));
                    } else if (strLine.contains(BOLDWITHLARGEFONT)) {
                        char ch[] = strLine.toCharArray();
                        StringBuffer buf = new StringBuffer();
                        for (int i = 0; i < ch.length; i++) {
                            if ((int) ch[i] == 14 || (int) ch[i] == 18) {
                            } else {
                                buf.append(ch[i]);
                            }
                        }
                        strLine = buf.toString();
                        check = true;
                        fontselector.addFont(new Font(Font.COURIER, 20, 1));
                    } else {
                        fontselector.addFont(new Font(Font.COURIER, 14, 0));
                    }

                    Phrase ph = fontselector.process(strLine);
                    Paragraph para1 = new Paragraph(10);
//                    if (check) {
//                        para1.setSpacingBefore(8.0f);
//                    }
                    para1.setSpacingBefore(4.0f);
                    para1.add(ph);
                    para1.setSpacingAfter(4.0f);
//                    if (check) {
//                        para1.setSpacingAfter(8.0f);
//                    }
                    document.add(para1);
                }
                j++;
            }
            document.close();
            in.close();
        } catch (java.io.IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return path;
    }

    public ActionForward PopupPDFReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class PopupPDFReport Method is calling ************************");
        try {
            DynaValidatorForm dvf = (DynaValidatorForm) form;
            String fileName = (String) dvf.get("fileName");
            String pdffileName = fileName.replace(".txt", ".pdf");
            String filePath = (String) dvf.get("filePath");

            System.out.println("fileName = " + fileName);
            System.out.println("filePath = " + filePath);

//            response.setContentType("text/plain");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + pdffileName);
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("TEXT File NOt FOund");
            } else {
                File Pdffile = new File(TxtToPdf(filePath));
                if (!Pdffile.exists()) {
                    System.out.println("PDF File Not Found");
                } else {
//                    file.delete();
                    FileInputStream in = new FileInputStream(Pdffile);
                    int fileSize = (int) file.length();
                    ServletOutputStream out = response.getOutputStream();
                    byte[] outputByte = new byte[fileSize];
                    while (in.read(outputByte, 0, fileSize) != -1) {
                        out.write(outputByte, 0, fileSize);
                    }
                    in.close();
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ActionForward journalreportpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************************** AccountsReportsAction class journalreportpage method ******************************");
        request.getSession().setAttribute("accountbook", accountReportService.getAccountBook(null, request, response, null, null));
        return mapping.findForward("journalreportpage");
    }

    public Map getJournalDetails(String startingdate, String enddate, String cashbooktype, String journalno, HttpServletRequest request, HttpServletResponse response) {
        return accountReportService.getJournalDetails(null, request, response, null, null, startingdate, enddate, cashbooktype, journalno);
    }

    public Map JournalPrintout(String journalno, String vdate, String booktype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class JournalPrintout Method is calling ************************");
        System.out.println("journalno = " + journalno);
        Map map = new HashMap();
        try {
            System.out.println("journalno = " + journalno);

            String bookname[] = {"", "CL1", "CL2", "NCL", "BA"};

//            String fileName = reportNameService.getFileName(null, request, response, null, null, bookname[Integer.valueOf(booktype)], vdate.substring(3, 5), vdate.substring(6, 10));
            String fileName = journalno + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            System.out.println("filePathwithName = > " + filePathwithName);

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                String pdffileName = fileName.replace(".txt", ".pdf");
                File ex_txt_file = new File(filePath + "/" + fileName);
                File ex_pdf_file = new File(filePath + "/" + pdffileName);
                if (ex_txt_file.exists()) {
                    ex_txt_file.delete();
                }
                if (ex_pdf_file.exists()) {
                    ex_pdf_file.delete();
                }
            }

            map = accountReportService.journalPrintout(null, request, response, null, null, journalno, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward bankreportpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************************** AccountsReportsAction class bankreportpage method ******************************");
        request.getSession().setAttribute("accountbook", accountReportService.getAccountBook(null, request, response, null, null));
        return mapping.findForward("bankreportpage");
    }

    public Map getBankDetails(String startingdate, String enddate, String cashbooktype, String bankno, HttpServletRequest request, HttpServletResponse response) {
        return accountReportService.getBankDetails(null, request, response, null, null, startingdate, enddate, cashbooktype, bankno);
    }

    public Map BankPrintout(String bankno, String vdate, String booktype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class BankPrintout Method is calling ************************");
        System.out.println("bankno = " + bankno);
        Map map = new HashMap();
        try {
            System.out.println("bankno = " + bankno);

            String bookname[] = {"", "CL1", "CL2", "NCL", "BA"};

//            String fileName = reportNameService.getFileName(null, request, response, null, null, bookname[Integer.valueOf(booktype)], vdate.substring(3, 5), vdate.substring(6, 10));
            String fileName = bankno + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            System.out.println("filePathwithName = > " + filePathwithName);

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                String pdffileName = fileName.replace(".txt", ".pdf");
                File ex_txt_file = new File(filePath + "/" + fileName);
                File ex_pdf_file = new File(filePath + "/" + pdffileName);
                if (ex_txt_file.exists()) {
                    ex_txt_file.delete();
                }
                if (ex_pdf_file.exists()) {
                    ex_pdf_file.delete();
                }
            }

            map = accountReportService.bankPrintout(null, request, response, null, null, bankno, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward receiptreportpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************************** AccountsReportsAction class receiptreportpage method ******************************");
        request.getSession().setAttribute("accountbook", accountReportService.getAccountBook(null, request, response, null, null));
        return mapping.findForward("receiptreportpage");
    }

    public Map getReceiptDetails(String startingdate, String enddate, String cashbooktype, String receiptno, HttpServletRequest request, HttpServletResponse response) {
        return accountReportService.getReceiptDetails(null, request, response, null, null, startingdate, enddate, cashbooktype, receiptno);
    }

    public Map ReceiptPrintout(String receiptno, String vdate, String booktype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class ReceiptPrintout Method is calling ************************");
        System.out.println("receiptno = " + receiptno);
        Map map = new HashMap();
        try {
            System.out.println("receiptno = " + receiptno);

            String bookname[] = {"", "CL1", "CL2", "NCL", "BA"};

//            String fileName = reportNameService.getFileName(null, request, response, null, null, bookname[Integer.valueOf(booktype)], vdate.substring(3, 5), vdate.substring(6, 10));
            String fileName = receiptno + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            System.out.println("filePathwithName = > " + filePathwithName);

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                String pdffileName = fileName.replace(".txt", ".pdf");
                File ex_txt_file = new File(filePath + "/" + fileName);
                File ex_pdf_file = new File(filePath + "/" + pdffileName);
                if (ex_txt_file.exists()) {
                    ex_txt_file.delete();
                }
                if (ex_pdf_file.exists()) {
                    ex_pdf_file.delete();
                }
            }

            map = accountReportService.ReceiptPrintout(null, request, response, null, null, receiptno, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public String getBookType(HttpServletRequest request, HttpServletResponse response) {
        return accountReportService.getBookType(null, request, response, null, null);
    }

    public ActionForward purchasereportpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("purchasereportpage");
    }

    public Map PurchasePrintOut(String month, String year, String breakuptype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class PurchasePrintOut Method is calling ************************");
        Map map = new HashMap();
        month = String.valueOf((Integer.valueOf(month)) + 1);
        try {
            String fileName = reportNameService.getFileName(null, request, response, null, null, "PUR", month, year);
            System.out.println("year = " + year);
            System.out.println("month = " + month);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }

            map = accountReportService.PurchasePrintOut(null, request, response, null, null, month, year, breakuptype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward purchaseAbstractreportpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************************** AccountsReportsAction class purchaseAbstractreportpage method ******************************");
        return mapping.findForward("purchaseAbstractreportpage");
    }

    public Map PurchaseAbstractPrintOut(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class PurchaseAbstractPrintOut Method is calling ************************");
        Map map = new HashMap();
        month = String.valueOf((Integer.valueOf(month)) + 1);
        try {
            String fileName = reportNameService.getFileName(null, request, response, null, null, "PABS", month, year);
            System.out.println("year = " + year);
            System.out.println("month = " + month);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }

            map = accountReportService.PurchaseAbstractPrintOut(null, request, response, null, null, month, year, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward salesTaxreportpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("salesTaxreportpage");
    }

    public Map SalesTaxPrintOut(String month, String year, String breakuptype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class SalesTaxPrintOut Method is calling ************************");
        Map map = new HashMap();
        month = String.valueOf((Integer.valueOf(month)) + 1);
        try {
            String fileName = reportNameService.getFileName(null, request, response, null, null, "SAL", month, year);
            System.out.println("year = " + year);
            System.out.println("month = " + month);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }

            map = accountReportService.SalesTaxPrintOut(null, request, response, null, null, month, year, breakuptype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward BankChallanreportpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("BankChallanreportpage");
    }

    public Map BankChallanPrintOut(String challanno, String challantype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class BankChallanPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = reportNameService.getFileName(null, request, response, null, null, challanno);
//            fileName = challanno + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            System.out.println("filePathwithName = > " + filePathwithName);
//            System.out.println("request.getSession().getServletContext().getRealPath(\"/\")+\"PayBillPrint\" = "+request.getSession().getServletContext().getRealPath("/")+"PayBillPrint");

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }

            if (challantype.equals("cash")) {
                map = accountReportService.BankChallanCashPrintOut(null, request, response, null, null, challanno, filePathwithName);
            } else {
                map = accountReportService.BankChallanChequePrintOut(null, request, response, null, null, challanno, filePathwithName);
            }

            StringBuilder urladdress = new StringBuilder();
            urladdress.append(AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH));
            urladdress.append("PayBillPrint").append("/").append(fileName);
            System.out.println("urladdress = " + urladdress.toString());

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
            map.put("urladdress", urladdress.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map LedgerPrintOut(String accountbookno, String period, String fromdate, String todate, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class cashBookPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
//            String fileName = accountbookno + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.LedgerPrintOut(null, request, response, null, null, accountbookno, period, fromdate, todate, reporttype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
    
    public Map consolidatedLedgerPrintOut(String accountbookno, String period, String fromdate, String todate, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class cashBookPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
//            String fileName = accountbookno + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.consolidatedLedgerPrintOut(null, request, response, null, null, accountbookno, period, fromdate, todate, reporttype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }    

    public Map LedgerPrintOutByRegion(String accountbookno, String period, String fromdate, String todate, String reporttype, String selectedRegion, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class cashBookPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = reportNameService.getFileNameByRegion(null, request, response, null, null, accountbookno, selectedRegion);
//            String fileName = accountbookno + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.LedgerPrintOutByRegion(null, request, response, null, null, accountbookno, period, fromdate, todate, reporttype, filePathwithName, selectedRegion);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward trialBalancePrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
        System.out.println("*************************** AccountsReportsAction class trialBalancePrint method ******************************");
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Logger logger = Logger.getLogger("com.elcot");
        String fromDate = (String) request.getParameter("fromDate");
        String toDate = (String) request.getParameter("toDate");
//        System.out.println("fromDate==" + fromDate);
//        System.out.println("toDate==" + toDate);
        try {
            JRMapCollectionDataSource trialBalanceDS = new JRMapCollectionDataSource(accountReportService.trialBalancePrintOut(null, request, response, null, null, fromDate, toDate, periodcode));

            String reportsPath = getReportsPath();
            String reportName = "trialbalanceprint";

            Map parameters = new HashMap();
            parameters.put("fromdate", fromDate);
            parameters.put("todate", toDate);
//            parameters.put("regionname", "Head office");


            //String jrxml = "C:\\reports\\" + re   portName + ".jrxml";
            String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";
            //String jrxml = "/root/NetBeansProjects/onlinefamilycard/web/WEB-INF/report/jrxml/"+ reportName +".jrxml";
//                logger.info(" @@@@@@@@ JRXML PATH @@@@@@@@@@@@ " + jrxml);

            //Specify the jasper path
            //String jasper = "/root/NetBeansProjects/onlinefamilycard/web/WEB-INF/report/jasper/"+ reportName +".jasper";
            String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";
//                logger.info(" @@@@@@@@@@ JASPER PATH @@@@@@@@@ " + jasper);

            JasperReport jasperReport = null;
            if (new File(jrxml).exists()) {
                jasperReport = JasperCompileManager.compileReport(jrxml);
            } else if (new File(jasper).exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasper);
            } else {
//                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, trialBalanceDS);
            //logger.info(" [ Report Action : getAcknowledgementDetails ] Jasper Print Object : " + jasperPrint);
            JRExporter exporter = null;
            if (jasperPrint != null) {
                OutputStream outStream = null;
//            try {
                outStream = response.getOutputStream();
//                            response.setHeader("Content-disposition", "inline;filename="+"Online FamilyCard Application_"+reportForm.getOnlineForm().getApplicationnumber()+".pdf");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=TrialBalance.pdf");
                exporter = new JRPdfExporter();


//              Text file
//                response.setContentType( "text/plain;charset=UTF-8" );
//                response.setHeader( "Content-Disposition", "attachment;filename=TrialBalance.txt" );
//                exporter = new JRTextExporter();
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 6);//6.55 //6
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 11); //11//10
//                exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT, ""); //11//10
                //                    logger.info(" @@@ [ Online Family Card Action ] generate Report : [ @@ Ready To Print @@ ] " + jasperPrint);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                exporter.exportReport();
                return null;
            } else {
                logger.info(" [ Accounts Report Action : trialBalancePrint ] No Records Available ");

            }
        } catch (IOException ex) {
            Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ActionForward consolidatedTrialBalancePrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
        System.out.println("*************************** AccountsReportsAction class trialBalancePrint method ******************************");
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Logger logger = Logger.getLogger("com.elcot");
        String fromDate = (String) request.getParameter("fromDate");
        String toDate = (String) request.getParameter("toDate");
//        System.out.println("fromDate==" + fromDate);
//        System.out.println("toDate==" + toDate);
        try {
            JRMapCollectionDataSource trialBalanceDS = new JRMapCollectionDataSource(accountReportService.consolidatedTrialBalancePrintOut(null, request, response, null, null, fromDate, toDate, periodcode));

            String reportsPath = getReportsPath();
            String reportName = "trialbalanceprint";

            Map parameters = new HashMap();
            parameters.put("fromdate", fromDate);
            parameters.put("todate", toDate);
//            parameters.put("regionname", "Head office");


            //String jrxml = "C:\\reports\\" + re   portName + ".jrxml";
            String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";
            //String jrxml = "/root/NetBeansProjects/onlinefamilycard/web/WEB-INF/report/jrxml/"+ reportName +".jrxml";
//                logger.info(" @@@@@@@@ JRXML PATH @@@@@@@@@@@@ " + jrxml);

            //Specify the jasper path
            //String jasper = "/root/NetBeansProjects/onlinefamilycard/web/WEB-INF/report/jasper/"+ reportName +".jasper";
            String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";
//                logger.info(" @@@@@@@@@@ JASPER PATH @@@@@@@@@ " + jasper);

            JasperReport jasperReport = null;
            if (new File(jrxml).exists()) {
                jasperReport = JasperCompileManager.compileReport(jrxml);
            } else if (new File(jasper).exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasper);
            } else {
//                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, trialBalanceDS);
            //logger.info(" [ Report Action : getAcknowledgementDetails ] Jasper Print Object : " + jasperPrint);
            JRExporter exporter = null;
            if (jasperPrint != null) {
                OutputStream outStream = null;
//            try {
                outStream = response.getOutputStream();
//                            response.setHeader("Content-disposition", "inline;filename="+"Online FamilyCard Application_"+reportForm.getOnlineForm().getApplicationnumber()+".pdf");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=TrialBalance.pdf");
                exporter = new JRPdfExporter();


//              Text file
//                response.setContentType( "text/plain;charset=UTF-8" );
//                response.setHeader( "Content-Disposition", "attachment;filename=TrialBalance.txt" );
//                exporter = new JRTextExporter();
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 6);//6.55 //6
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 11); //11//10
//                exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT, ""); //11//10
                //                    logger.info(" @@@ [ Online Family Card Action ] generate Report : [ @@ Ready To Print @@ ] " + jasperPrint);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                exporter.exportReport();
                return null;
            } else {
                logger.info(" [ Accounts Report Action : trialBalancePrint ] No Records Available ");

            }
        } catch (IOException ex) {
            Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ActionForward trialBalancePrintByRegion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
        System.out.println("*************************** AccountsReportsAction class trialBalancePrint method ******************************");
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Logger logger = Logger.getLogger("com.elcot");
        String fromDate = (String) request.getParameter("fromDate");
        String toDate = (String) request.getParameter("toDate");
        String selectedRegion = (String) request.getParameter("selectedRegion");
//        System.out.println("fromDate==" + fromDate);
//        System.out.println("toDate==" + toDate);
        try {
            JRMapCollectionDataSource trialBalanceDS = new JRMapCollectionDataSource(accountReportService.trialBalancePrintOutByRegion(null, request, response, null, null, fromDate, toDate, periodcode, selectedRegion));

            String reportsPath = getReportsPath();
            String reportName = "trialbalanceprint";

            Map parameters = new HashMap();
            parameters.put("fromdate", fromDate);
            parameters.put("todate", toDate);
//            parameters.put("regionname", "Head office");


            //String jrxml = "C:\\reports\\" + re   portName + ".jrxml";
            String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";
            //String jrxml = "/root/NetBeansProjects/onlinefamilycard/web/WEB-INF/report/jrxml/"+ reportName +".jrxml";
//                logger.info(" @@@@@@@@ JRXML PATH @@@@@@@@@@@@ " + jrxml);

            //Specify the jasper path
            //String jasper = "/root/NetBeansProjects/onlinefamilycard/web/WEB-INF/report/jasper/"+ reportName +".jasper";
            String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";
//                logger.info(" @@@@@@@@@@ JASPER PATH @@@@@@@@@ " + jasper);

            JasperReport jasperReport = null;
            if (new File(jrxml).exists()) {
                jasperReport = JasperCompileManager.compileReport(jrxml);
            } else if (new File(jasper).exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasper);
            } else {
//                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, trialBalanceDS);
            //logger.info(" [ Report Action : getAcknowledgementDetails ] Jasper Print Object : " + jasperPrint);
            JRExporter exporter = null;
            if (jasperPrint != null) {
                OutputStream outStream = null;
//            try {
                outStream = response.getOutputStream();
//                            response.setHeader("Content-disposition", "inline;filename="+"Online FamilyCard Application_"+reportForm.getOnlineForm().getApplicationnumber()+".pdf");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=TrialBalance.pdf");
                exporter = new JRPdfExporter();


//              Text file
//                response.setContentType( "text/plain;charset=UTF-8" );
//                response.setHeader( "Content-Disposition", "attachment;filename=TrialBalance.txt" );
//                exporter = new JRTextExporter();
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 6);//6.55 //6
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 11); //11//10
//                exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT, ""); //11//10
                //                    logger.info(" @@@ [ Online Family Card Action ] generate Report : [ @@ Ready To Print @@ ] " + jasperPrint);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                exporter.exportReport();
                return null;
            } else {
                logger.info(" [ Accounts Report Action : trialBalancePrint ] No Records Available ");

            }
        } catch (IOException ex) {
            Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ActionForward progressiveTrialBalancePrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
        System.out.println("*************************** AccountsReportsAction class trialBalancePrint method ******************************");
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Logger logger = Logger.getLogger("com.elcot");
        String fromDate = (String) request.getParameter("fromDate");
        String toDate = (String) request.getParameter("toDate");
//        System.out.println("fromDate==" + fromDate);
//        System.out.println("toDate==" + toDate);
        try {
            JRMapCollectionDataSource trialBalanceDS = new JRMapCollectionDataSource(accountReportService.progressivetrialBalancePrintOut(null, request, response, null, null, fromDate, toDate, periodcode));

            String reportsPath = getReportsPath();
            String reportName = "trialbalanceprint";

            Map parameters = new HashMap();
            parameters.put("fromdate", fromDate);
            parameters.put("todate", toDate);
//            parameters.put("regionname", "Head office");


            //String jrxml = "C:\\reports\\" + re   portName + ".jrxml";
            String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";
            //String jrxml = "/root/NetBeansProjects/onlinefamilycard/web/WEB-INF/report/jrxml/"+ reportName +".jrxml";
//                logger.info(" @@@@@@@@ JRXML PATH @@@@@@@@@@@@ " + jrxml);

            //Specify the jasper path
            //String jasper = "/root/NetBeansProjects/onlinefamilycard/web/WEB-INF/report/jasper/"+ reportName +".jasper";
            String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";
//                logger.info(" @@@@@@@@@@ JASPER PATH @@@@@@@@@ " + jasper);

            JasperReport jasperReport = null;
            if (new File(jrxml).exists()) {
                jasperReport = JasperCompileManager.compileReport(jrxml);
            } else if (new File(jasper).exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasper);
            } else {
//                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, trialBalanceDS);
            //logger.info(" [ Report Action : getAcknowledgementDetails ] Jasper Print Object : " + jasperPrint);
            JRExporter exporter = null;
            if (jasperPrint != null) {
                OutputStream outStream = null;
//            try {
                outStream = response.getOutputStream();
//                            response.setHeader("Content-disposition", "inline;filename="+"Online FamilyCard Application_"+reportForm.getOnlineForm().getApplicationnumber()+".pdf");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=TrialBalance.pdf");
                exporter = new JRPdfExporter();


//              Text file
//                response.setContentType( "text/plain;charset=UTF-8" );
//                response.setHeader( "Content-Disposition", "attachment;filename=TrialBalance.txt" );
//                exporter = new JRTextExporter();
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 6);//6.55 //6
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 11); //11//10
//                exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT, ""); //11//10
                //                    logger.info(" @@@ [ Online Family Card Action ] generate Report : [ @@ Ready To Print @@ ] " + jasperPrint);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                exporter.exportReport();
                return null;
            } else {
                logger.info(" [ Accounts Report Action : trialBalancePrint ] No Records Available ");

            }
        } catch (IOException ex) {
            Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ActionForward ledgerPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
        System.out.println("*************************** AccountsReportsAction class ledgerPrint method ******************************");
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Logger logger = Logger.getLogger("com.onward.action");
        String fromDate = (String) request.getParameter("fromDate");
        String toDate = (String) request.getParameter("toDate");
        String acccode = (String) request.getParameter("acccode");
        String accountcode[] = acccode.split("-");
//        System.out.println("fromDate==" + fromDate);
//        System.out.println("toDate==" + toDate);
        try {
            JRMapCollectionDataSource trialBalanceDS = new JRMapCollectionDataSource(accountReportService.ledgerPrint(null, request, response, null, null, fromDate, toDate, periodcode, accountcode[0]));

            String reportsPath = getReportsPath();
            String reportName = "ledgeraccountprint";

            Map parameters = new HashMap();
            parameters.put("fromdate", fromDate);
            parameters.put("todate", toDate);
            parameters.put("acccode", acccode);



            String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";

            String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";


            JasperReport jasperReport = null;
            if (new File(jrxml).exists()) {
                jasperReport = JasperCompileManager.compileReport(jrxml);
            } else if (new File(jasper).exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasper);
            } else {
                logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, trialBalanceDS);

            JRExporter exporter = null;
//            JRTextExporter exporter = null;
            if (jasperPrint != null) {
                OutputStream outStream = null;
//            try {
                outStream = response.getOutputStream();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=" + accountcode[0] + ".pdf");
                exporter = new JRPdfExporter();


//              Text file
//                response.setContentType( "text/plain;charset=UTF-8" );
//                response.setHeader( "Content-Disposition", "attachment;filename="+accountcode[0]+".txt" );
//                exporter = new JRTextExporter();
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 6);//6.55 //6
//                exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 11); //11//10
//                exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT, ""); //11//10
                //                    logger.info(" @@@ [ Online Family Card Action ] generate Report : [ @@ Ready To Print @@ ] " + jasperPrint);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                exporter.exportReport();
                return null;
            } else {
                logger.info(" [ Accounts Report Action : ledgerPrint ] No Records Available ");

            }
        } catch (IOException ex) {
            Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ActionForward receiptHeadWiseAbstractPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
//        System.out.println("*************************** AccountsReportsAction class receiptHeadWiseAbstractPrint method ******************************");
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Logger logger = Logger.getLogger("com.onward.action");
        String fromDate = (String) request.getParameter("fromDate");
        String toDate = (String) request.getParameter("toDate");
        String regionname;
        String error;
        LinkedList receiptAbstractlist = new LinkedList();
        Map abstractmap = new HashMap();
        abstractmap = accountReportService.getReceiptHeadWiseAbstractPrint(null, request, response, toDate, toDate, fromDate, toDate, periodcode);
        error = (String) abstractmap.get("ERROR");
        receiptAbstractlist = (LinkedList) abstractmap.get("receiptAbstractlist");
        regionname = (String) abstractmap.get("regionname");


        if (error.equalsIgnoreCase("")) {
            try {
//                System.out.println("regionname==" + regionname);
//                System.out.println("receiptAbstractlist==" + receiptAbstractlist.size());
                JRMapCollectionDataSource trialBalanceDS = new JRMapCollectionDataSource(receiptAbstractlist);

                String reportsPath = getReportsPath();
                String reportName = "receiptAbstract";

                Map parameters = new HashMap();
                parameters.put("fromdate", fromDate);
                parameters.put("todate", toDate);
                parameters.put("regionname", regionname);
                parameters.put("heading", " Receipt Ac.HeadWise Abstract for the Period From " + fromDate + " - " + toDate);

                String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";

                String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";


                JasperReport jasperReport = null;
                if (new File(jrxml).exists()) {
                    jasperReport = JasperCompileManager.compileReport(jrxml);
                } else if (new File(jasper).exists()) {
                    jasperReport = (JasperReport) JRLoader.loadObject(jasper);
                } else {
                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
                }

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, trialBalanceDS);

                JRExporter exporter = null;

                if (jasperPrint != null) {
                    OutputStream outStream = null;

                    outStream = response.getOutputStream();
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment;filename=ReceiptAbstract.pdf");
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                    exporter.exportReport();
                    return null;
                } else {
                    logger.info(" [ Accounts Report Action : receiptHeadWiseAbstractPrint ] No Records Available ");
                    return mapping.findForward("failure");

                }
            } catch (IOException ex) {
                Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;

        } else {
            request.setAttribute("message", error.toUpperCase());
            return mapping.findForward("failure");
        }
    }

    public ActionForward cashBookAbstractPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
//        System.out.println("*************************** AccountsReportsAction class receiptHeadWiseAbstractPrint method ******************************");
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Logger logger = Logger.getLogger("com.onward.action");
        String fromDate = (String) request.getParameter("fromDate");
        String toDate = (String) request.getParameter("toDate");
        String ctype = (String) request.getParameter("ctype");
        String accbook = (String) request.getParameter("accbook");
        String regionname;
        String error = "@";
        String headername = "";
        String reportName = "";
        LinkedList cashbookAbstractlist = new LinkedList();
        Map abstractmap = new HashMap();
        abstractmap = accountReportService.getCashBookAbstractPrint(null, request, response, toDate, toDate, fromDate, toDate, periodcode, ctype, accbook);
        error = (String) abstractmap.get("ERROR");
        cashbookAbstractlist = (LinkedList) abstractmap.get("cashbookAbstractlist");
        regionname = (String) abstractmap.get("regionname");


        if (error.equalsIgnoreCase("")) {
            try {
                System.out.println("regionname==" + regionname);
                System.out.println("cashbookAbstractlist==" + cashbookAbstractlist.size());
                JRMapCollectionDataSource cashBookAbstractDS = new JRMapCollectionDataSource(cashbookAbstractlist);

                String reportsPath = getReportsPath();

                Map parameters = new HashMap();
                parameters.put("fromdate", fromDate);
                parameters.put("todate", toDate);
                parameters.put("regionname", regionname);
                if (ctype.equalsIgnoreCase("P")) {
                    headername = "Payment";
                    reportName = "cashbookDailyAbstract";
                } else if (ctype.equalsIgnoreCase("R")) {
                    headername = "Receipt";
                    reportName = "receiptcashbookDailyAbstract";
                } else if (ctype.equalsIgnoreCase("J")) {
                    reportName = "cashbookDailyAbstract";
                    headername = "Journal";
                } else if (ctype.equalsIgnoreCase("B")) {
                    reportName = "cashbookDailyAbstract";
                    headername = "Bank";
                }
                parameters.put("heading", headername + " Cash Book Daily Abstract from " + fromDate + " - " + toDate);

                String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";

                String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";


                JasperReport jasperReport = null;
                if (new File(jrxml).exists()) {
                    jasperReport = JasperCompileManager.compileReport(jrxml);
                } else if (new File(jasper).exists()) {
                    jasperReport = (JasperReport) JRLoader.loadObject(jasper);
                } else {
                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
                }

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, cashBookAbstractDS);

                JRExporter exporter = null;

                if (jasperPrint != null) {
                    OutputStream outStream = null;

                    outStream = response.getOutputStream();
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment;filename=" + headername + "CashBookAbstract.pdf");
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                    exporter.exportReport();
                    return null;
                } else {
                    logger.info(" [ Accounts Report Action : cashBookAbstractPrint ] No Records Available ");
                    return mapping.findForward("failure");

                }
            } catch (IOException ex) {
                Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;

        } else {
            request.setAttribute("message", error.toUpperCase());
            return mapping.findForward("failure");
        }
    }

    private String getReportsPath() {
        String webPath = "WEB-INF/report";
        String jrxmlPath = this.getServlet().getServletContext().getRealPath("/");
        AppProps app = AppProps.getInstance();
        if (app.getProperty("appContext") == null) {
            app.setProperty("appContext", jrxmlPath);
            app.loadProps();
        }
        return app.getProperty("appContext").concat(webPath);
    }

    public ActionForward purchasevatBreakupPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("purchasevatBreakupPage");
    }

    public ActionForward purchasevatAbstractPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("purchasevatAbstractPage");
    }

    public ActionForward salesvatAbstractPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("salesvatAbstractPage");
    }

    public ActionForward salesvatbreakupPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("salesvatbreakupPage");
    }

    public Map PurchaseVatBreakupPrintOut(String month, String year, String percentage, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class PurchaseVatBreakupPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
            String fileName = "Purchase" + percentage + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.PurchaseVatBreakupPrintOut(null, request, response, null, null, month, year, percentage, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward PopupXMLReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaValidatorForm dvf = (DynaValidatorForm) form;
            String fileName = (String) dvf.get("fileName");
            String filePath = (String) dvf.get("filePath");

//            response.setContentType("text/plain");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File NOt FOund");
            } else {
                FileInputStream in = new FileInputStream(file);
                int fileSize = (int) file.length();
                ServletOutputStream out = response.getOutputStream();
                byte[] outputByte = new byte[fileSize];
                while (in.read(outputByte, 0, fileSize) != -1) {
                    out.write(outputByte, 0, fileSize);
                }
                in.close();
                out.flush();
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Map SalesVatBreakupPrintOut(String month, String year, String percentage, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class SalesVatBreakupPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
            String fileName = "Sales" + percentage + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.SalesVatBreakupPrintOut(null, request, response, null, null, month, year, percentage, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map PurchaseVatAbstractPrintOut(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class PurchaseVatAbstractPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
            String fileName = "PURabs" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.PurchaseVatAbstractPrintOut(null, request, response, null, null, month, year, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map SalesVatAbstractPrintOut(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class SalesVatAbstractPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
            String fileName = "SALabs" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.SalesVatAbstractPrintOut(null, request, response, null, null, month, year, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map getChallanDetails(String startingdate, String enddate, String challantype, String challanno, HttpServletRequest request, HttpServletResponse response) {
        return accountReportService.getChallanDetails(null, request, response, null, null, startingdate, enddate, challantype, challanno);
    }

    public ActionForward purchasevatExportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("purchasevatExportPage");
    }

    public ActionForward salesvatExportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("salesvatExportPage");
    }

    public Map PurchaseVatExportPrintOut(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class PurchaseVatExportPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
            String fileName = "Purchase" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.PurchaseVatExportPrintOut(null, request, response, null, null, month, year, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map SalesVatExportPrintOut(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class SalesVatExportPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
            String fileName = "Sales" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.SalesVatExportPrintOut(null, request, response, null, null, month, year, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map PaymentRealizationPrintOut(String periodsele, String voucherdatefrom, String voucherdateto, String book, String realizationstatus, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class PaymentRealizationPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
//            String fileName = "Sales" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
            String fileName = "PaymentRealization" + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.PaymentRealizationPrintOut(null, request, response, null, null, periodsele, voucherdatefrom, voucherdateto, book, realizationstatus, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map ReceiptRealizationPrintOut(String realizationstatus, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class ReceiptRealizationPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
//            String fileName = "Sales" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
            String fileName = "ReceiptRealization" + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.ReceiptRealizationPrintOut(null, request, response, null, null, realizationstatus, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map ChequeRegisterPrintOut(String periodsele, String voucherdatefrom, String voucherdateto, String book, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class ChequeRegisterPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
//            String fileName = "Sales" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
            String fileName = "ChequeRegister" + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.ChequeRegisterPrintOut(null, request, response, null, null, periodsele, voucherdatefrom, voucherdateto, book, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward receiptRealizationReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("receiptRealizationReportPage");
    }

    public Map ReceiptRealizationReportPrintOut(String startingdate, String enddate, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** AccountsReportsAction.class ReceiptRealizationReportPrintOut Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
//            String fileName = "Sales" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
            String fileName = "RRreport" + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = accountReportService.ReceiptRealizationReportPrintOut(null, request, response, null, null, startingdate, enddate, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward PaymentRealizationAbstractPrintOut(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
        System.out.println("*************************** AccountsReportsAction class PaymentRealizationAbstractPrintOut method ******************************");

        DynaValidatorForm dvf = (DynaValidatorForm) form;
        String periodsele = (String) dvf.get("periodsele");
        String book = (String) dvf.get("acbook");
        String voucherdatefrom = (String) dvf.get("realfromdate");
        String voucherdateto = (String) dvf.get("realtodate");
        String realizationstatus = (String) dvf.get("realized");

        System.out.println("periodsele = " + periodsele);
        System.out.println("book = " + book);
        System.out.println("voucherdatefrom = " + voucherdatefrom);
        System.out.println("voucherdateto = " + voucherdateto);
        System.out.println("realizationstatus = " + realizationstatus);


        try {
            Map map = accountReportService.PaymentRealizationAbstractPrintOut(null, request, response, null, null, periodsele, voucherdatefrom, voucherdateto, book, realizationstatus);

            String reportsPath = getReportsPath();
            String reportName = "PaymentRealization";

//            if (map.get("ERROR") != null) {
//                System.out.println("Report Generated Error");
//            } else {
            String region = (String) map.get("regionname");
            String fromdate = (String) map.get("fromdate");
            String todate = (String) map.get("todate");
            System.out.println(fromdate + " " + todate);
            List paymentlist = (List) map.get("realizationlist");
            JRMapCollectionDataSource jrmcds = new JRMapCollectionDataSource(paymentlist);
//                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(paymentlist);

            Map parameters = new HashMap();
            parameters.put("region", region);
            parameters.put("fromdate", fromdate);
            parameters.put("todate", todate);

            String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";
            String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";

            JasperReport jasperReport = null;
            if (new File(jrxml).exists()) {
                jasperReport = JasperCompileManager.compileReport(jrxml);
            } else if (new File(jasper).exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasper);
            } else {
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrmcds);
            JRExporter exporter = null;
            if (jasperPrint != null) {
                OutputStream outStream = null;
                outStream = response.getOutputStream();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=PaymentRealization.pdf");
                exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                exporter.exportReport();
                return null;
            } else {
                System.out.println(" [ Accounts Report Action : trialBalancePrint ] No Records Available ");
            }
//            }

        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public ActionForward RealizationAbstractPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("RealizationAbstractPage");
    }

    public ActionForward RealizationAbstractPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
        System.out.println("*************************** AccountsReportsAction class RealizationAbstractPrint method ******************************");
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Logger logger = Logger.getLogger("com.onward.action");
        String fromDate = (String) request.getParameter("fromDate");
        String toDate = (String) request.getParameter("toDate");
        String ctype = (String) request.getParameter("ctype");
        String accbook = (String) request.getParameter("accbook");
        String regionname;
        String error = "@";
        String headername = "";
        String reportName = "";
        LinkedList cashbookAbstractlist = new LinkedList();
        Map abstractmap = new HashMap();
        abstractmap = accountReportService.getRealizationAbstractPrint(null, request, response, toDate, toDate, fromDate, toDate, periodcode, ctype, accbook);
        error = (String) abstractmap.get("ERROR");
        cashbookAbstractlist = (LinkedList) abstractmap.get("cashbookAbstractlist");
        regionname = (String) abstractmap.get("regionname");


        if (error.equalsIgnoreCase("")) {
            try {
                System.out.println("regionname==" + regionname);
                System.out.println("cashbookAbstractlist==" + cashbookAbstractlist.size());
                JRMapCollectionDataSource cashBookAbstractDS = new JRMapCollectionDataSource(cashbookAbstractlist);

                String reportsPath = getReportsPath();

                Map parameters = new HashMap();
                parameters.put("fromdate", fromDate);
                parameters.put("todate", toDate);
                parameters.put("regionname", regionname);
                reportName = "RealizationAbstract";
                if (ctype.equalsIgnoreCase("P")) {
                    headername = "Payment";
                } else if (ctype.equalsIgnoreCase("R")) {
                    headername = "Receipt";
                } else if (ctype.equalsIgnoreCase("J")) {
                    headername = "Journal";
                } else if (ctype.equalsIgnoreCase("B")) {
                    headername = "Bank";
                }
                parameters.put("heading", headername + " Realization Abstract from " + fromDate + " - " + toDate);

                String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";

                String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";


                JasperReport jasperReport = null;
                if (new File(jrxml).exists()) {
                    jasperReport = JasperCompileManager.compileReport(jrxml);
                } else if (new File(jasper).exists()) {
                    jasperReport = (JasperReport) JRLoader.loadObject(jasper);
                } else {
                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
                }

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, cashBookAbstractDS);

                JRExporter exporter = null;

                if (jasperPrint != null) {
                    OutputStream outStream = null;

                    outStream = response.getOutputStream();
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment;filename=" + headername + "RealizationAbstract.pdf");
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                    exporter.exportReport();
                    return null;
                } else {
                    logger.info(" [ Accounts Report Action : RealizationAbstractPrint ] No Records Available ");
                    return mapping.findForward("failure");

                }
            } catch (IOException ex) {
                Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;

        } else {
            request.setAttribute("message", error.toUpperCase());
            return mapping.findForward("failure");
        }
    }

    public Map trialBalanceCsvPrepare(String accountingperiod, String accountperiodfrom, String accountperiodto, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeePayBillPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = "trialbalancecsv.txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
            String filePath = request.getRealPath("/") + "TrialBalance";
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
                ex_file.setReadOnly();
            }
            map = accountVoucherService.prepareTrialBalanceCSV(null, request, response, null, null, accountingperiod, accountperiodfrom, accountperiodto, filePathwithName);
            System.out.println("fileName " + fileName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
}
