/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.valueobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Prince vijayakumar M
 */
@XmlRootElement(namespace = "com.onward.valueobjects")
public class EpfModelObject {

    @XmlElementWrapper(name = "epfModelList")
    @XmlElement(name = "epfModelObj")
    private ArrayList<EpfModel> epfModelList;

    /**
     * @return the epfModelList
     */
    public ArrayList<EpfModel> getEpfModelsList() {
        return epfModelList;
    }

    /**
     * @param epfModelList the epfModelList to set
     */
    public void setEpfModelList(ArrayList<EpfModel> epfModelList) {
        this.epfModelList = epfModelList;
    }
}
