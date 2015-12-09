package com.onward.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DateParser implements java.io.Serializable {

    static Logger logger = Logger.getLogger("com.onward.welfare");

    public DateParser() {
    }

    public static java.sql.Date postgresDate(String date) {
        java.sql.Date sqlDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (date != null && date.length() > 0) {
                java.util.Date utilDate = dateFormat.parse(date);
                sqlDate = new java.sql.Date(utilDate.getTime());
            } else {
                sqlDate = null;
            }
        } catch (ParseException pe) {
            logger.info("Parsing Exception in date formatting..." + pe);
        }
        return sqlDate;
    }

    public static String ConvertFormate(String date) {
        String strDate = null;
        java.sql.Date sqlDate = null;
        try {
            DateFormat OriginalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat ConvertFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (date != null && date.length() > 0) {
                java.util.Date utilDate = OriginalFormat.parse(date);
                strDate = ConvertFormat.format(date);
            } else {
                sqlDate = null;
            }
        } catch (ParseException pe) {
            logger.info("Parsing Exception in date formatting..." + pe);
        }
        return strDate;
    }

    public HashMap getXmlDetails1(String xmlContent) {
        FileOutputStream fos = null;
        HashMap map = null;
        try {
            String xmlFilePath = "";
            String osname = System.getProperty("os.name");
            if (osname.equalsIgnoreCase("Linux") || osname.equalsIgnoreCase("Unix")) {
                xmlFilePath = "/root/sample" + ".xml";
            } else {
                xmlFilePath = "C:\\sample" + ".xml";
            }
            File xmlFileName = new File(xmlFilePath);
            fos = new FileOutputStream(xmlFileName);
            fos.write(xmlContent.getBytes());
            fos.close();

            HashMap detailsMap = new HashMap();
            Document dom = parseFile(xmlFilePath);
            Node node = dom.getDocumentElement();
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    detailsMap.put(child.getNodeName(), child.getNodeName());
                }
            }
            map = getXmlData(detailsMap, dom);
            //System.out.println("  map   " + map);

        } catch (IOException ex) {
            Logger.getLogger(DateParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(DateParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public HashMap getXmlDetails(String xmlContent) {
        //System.out.println(" &&&&&&&&&&&&&&&&&&&&&&& getXmlDetails &&&&&&&&&&&&&&&&&&&&&&&&& ");
        FileOutputStream fos = null;
        HashMap map = null;
        try {
            String xmlFilePath = "";
            String osname = System.getProperty("os.name");
            if (osname.equalsIgnoreCase("Linux") || osname.equalsIgnoreCase("Unix")) {
                xmlFilePath = "/root/sample" + ".xml";
            } else {
                xmlFilePath = "C:\\sample" + ".xml";
            }
            File xmlFileName = new File(xmlFilePath);
            fos = new FileOutputStream(xmlFileName);
            fos.write(xmlContent.getBytes());
            fos.close();

            HashMap detailsMap = new HashMap();
            Document dom = parseFile(xmlFilePath);
            Node node = dom.getDocumentElement();
            NodeList children = node.getChildNodes();
            //System.out.println(" children : " + children.getLength());
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                NodeList lineItemPropertyList = child.getChildNodes();
                //System.out.println(" lineItemPropertyList : "+lineItemPropertyList.getLength());
                for (int j = 0; j < lineItemPropertyList.getLength(); j++) {
                    Node child1 = lineItemPropertyList.item(j);
                    if (child1.getNodeType() == Node.ELEMENT_NODE) {
                        detailsMap.put(child1.getNodeName(), child1.getNodeName());
                        //System.out.println(" detailsMap : " + detailsMap);
                    }
                }
            }
            map = getXmlData(detailsMap, dom);
            //System.out.println("  map in getXmlDetails for Login " + map);

        } catch (IOException ex) {
            Logger.getLogger(DateParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(DateParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public HashMap getXmlDetails2(String xmlContent) {
        //System.out.println(" &&&&&&&&&&&&&&&&&&&&&&& getXmlDetails2 &&&&&&&&&&&&&&&&&&&&&&&&& ");
        FileOutputStream fos = null;
        HashMap map = null;
        try {
            String xmlFilePath = "";
            String osname = System.getProperty("os.name");
            if (osname.equalsIgnoreCase("Linux") || osname.equalsIgnoreCase("Unix")) {
                xmlFilePath = "/root/sample" + ".xml";
            } else {
                xmlFilePath = "C:\\sample" + ".xml";
            }
            File xmlFileName = new File(xmlFilePath);
            fos = new FileOutputStream(xmlFileName);
            fos.write(xmlContent.getBytes());
            fos.close();

            HashMap detailsMap = new HashMap();
            Document dom = parseFile(xmlFilePath);
            Node node = dom.getDocumentElement();
            NodeList children = node.getChildNodes();
            //System.out.println(" children : " + children.getLength());
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                NodeList lineItemPropertyList = child.getChildNodes();
                for (int j = 0; j < lineItemPropertyList.getLength(); j++) {
                    Node child1 = lineItemPropertyList.item(j);
                    NodeList lineItemPropertyList1 = child1.getChildNodes();
                    for (int k = 0; k < lineItemPropertyList1.getLength(); k++) {
                        Node child2 = lineItemPropertyList1.item(k);
                        NodeList lineItemPropertyList2 = child2.getChildNodes();
                        for (int l = 0; l < lineItemPropertyList2.getLength(); l++) {
                            Node child3 = lineItemPropertyList2.item(l);
                            NodeList lineItemPropertyList3 = child3.getChildNodes();
                            for (int m = 0; m < lineItemPropertyList3.getLength(); m++) {
                                Node child4 = lineItemPropertyList3.item(m);
                                if (child4.getNodeType() == Node.ELEMENT_NODE) {
                                    detailsMap.put(child4.getNodeName(), child4.getNodeName());
                                    //System.out.println(" detailsMap : " + detailsMap);
                                }
                            }
                        }
                    }
                }
            }
            map = getXmlData(detailsMap, dom);
            //System.out.println("  map   " + map);

        } catch (IOException ex) {
            Logger.getLogger(DateParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(DateParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public Document parseFile(String fileName) {
        DocumentBuilder docBuilder;
        Document dom = null;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringElementContentWhitespace(true);
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            //System.out.println("Wrong parser configuration: " + e.getMessage());
            return null;
        }
        try {
            dom = docBuilder.parse(fileName);
        } catch (SAXException e) {
            e.printStackTrace();
            //System.out.println("Wrong XML file structure: " + e.getMessage());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Could not read source file: " + e.getMessage());
        }
        return dom;
    }

    public HashMap getXmlData(Map detailsMap, Document dom) {
        //System.out.println(" &&&&&&&&&&&&&&&&&&&&&&& getXmlData &&&&&&&&&&&&&&&&&&&&&&&&& ");
        HashMap propertyMap = new HashMap();
        Iterator itr = detailsMap.keySet().iterator();
        String name = null, value = null;
        while (itr.hasNext()) {
            String nodeName = itr.next().toString();
            NodeList nodeLineItem = dom.getElementsByTagName(nodeName);
            for (int i = 0; i < nodeLineItem.getLength(); i++) {
                //System.out.println(" Inside the First FOR loop ");
                Node propertyNode = nodeLineItem.item(i);
                NodeList lineItemPropertyList = propertyNode.getChildNodes();
                for (int k = 0; k < lineItemPropertyList.getLength(); k++) {
                    //System.out.println(" Inside the Second FOR loop ");
                    Node propertyNode1 = lineItemPropertyList.item(k);
                    if (propertyNode1.getNodeType() == Node.ELEMENT_NODE) {
                        //System.out.println(" Inside the First IF loop ");
                        NamedNodeMap nodeMap = propertyNode1.getAttributes();
                        for (int j = 0; j < nodeMap.getLength(); j++) {
                            //System.out.println(" Inside the Third FOR loop ");
                            if (nodeMap.item(j).getNodeName().equalsIgnoreCase("name")) {
                                name = nodeMap.item(j).getNodeValue();
                            } else if (nodeMap.item(j).getNodeName().equalsIgnoreCase("value")) {
                                value = nodeMap.item(j).getNodeValue();
                            }
                        }
                        propertyMap.put(name, value);
                    }
                }
            }
        }
        //System.out.println(" &&&&&&&&&&&&&& propertyMap : "+propertyMap);
        return propertyMap;
    }

    public static String dateAsddMMyyyy(String stringyyyyMMdd){
        String dateAsddMMyyyy = dateToString(postgresDate1(stringyyyyMMdd));
        return dateAsddMMyyyy;
    }


    public static java.sql.Date postgresDate1(String date) {
        java.sql.Date sqlDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (date != null && date.length() > 0) {
                java.util.Date utilDate = dateFormat.parse(date);
                sqlDate = new java.sql.Date(utilDate.getTime());
            } else {
                sqlDate = null;
            }
        } catch (ParseException pe) {
            logger.info("Parsing Exception in date formatting..." + pe);
        }
        return sqlDate;
    }

    public static String dateToString(java.util.Date dt) {
        String strDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (dt != null) {
                strDate = sdf.format(dt);
            } else {
                strDate = "";
            }
        } catch (Exception e) {
            logger.info("Exception in parsing date to String..." + e);
        }
        return strDate;
    }

    public static java.util.Date utilDate(String date) {
        java.util.Date utilDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (date != null && date.length() > 0) {
                utilDate = dateFormat.parse(date);
            } else {
                utilDate = null;
            }
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return utilDate;
    }

//    public java.sql.Date getCurrentSqlDate() {
//        try {
//            String date = new TufidcoDAO().todaysDate();
//            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            if (date != null && date.length() > 0) {
//                java.util.Date utilDate = dateFormat.parse(date);
//                sqlDate = new java.sql.Date(utilDate.getTime());
//            } else {
//                sqlDate = null;
//            }
//            //java.util.Date date = new java.util.Date();
//            //sqlDate = new java.sql.Date(date.getTime());
//        } catch (Exception pe) {
//            logger.info("Parsing Exception in date formatting..." + pe);
//        }
//        return sqlDate;
//    }
    public static String convertTheDateIntoDisplayFormat(String dt) {
        String strDate = "";
        try {
            StringTokenizer st = new StringTokenizer(dt, "-");
            String s1 = st.nextToken();
            String s2 = st.nextToken();
            String s3 = st.nextToken();
            strDate = s3 + "/" + s2 + "/" + s1;

        } catch (Exception e) {
            logger.info("exception ----------->" + e);
        }
        return strDate;
    }

    /**
     * Added By Jagan Mohan.B (29/07/2011)
     * @param date
     * @return
     */
    public java.sql.Date excelCellTodbDate(String date) {
        java.sql.Date sqlDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
            if (date != null && date.length() > 0) {
                java.util.Date utilDate = dateFormat.parse(date);
                sqlDate = new java.sql.Date(utilDate.getTime());
            } else {
                sqlDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return sqlDate;
    }

    /**
     * Added By Jagan Mohan.B (29/07/2011)
     * @param date
     * @return
     */
    public String excelCellToNormalDate(String date) {
        java.sql.Date sqlDate = null;
        String strDate = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
            if (date != null && date.length() > 0) {
                java.util.Date utilDate = dateFormat.parse(date);
                sqlDate = new java.sql.Date(utilDate.getTime());
            } else {
                sqlDate = null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (sqlDate != null)
                strDate = sdf.format(sqlDate);
            else
                strDate = "";
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public java.sql.Date longtoPostgresDate(String date) {
        java.sql.Date sqlDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
            if (date != null && date.length() > 0) {
                java.util.Date utilDate = dateFormat.parse(date);
                sqlDate = new java.sql.Date(utilDate.getTime());
            } else {
                sqlDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return sqlDate;
    }
}


