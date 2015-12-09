<%-- 
    Document   : leftpage
    Created on : Jul 7, 2011, 12:27:21 PM
    Author     : Jagan Mohan. B
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
    <script>
        /**
         * Sub menu div's enabled and disabled.
         */
        function enabledSubMenuDiv(objId,submenuid){
            subMenuDivNone();
            subMenuClassNameNone();

            if(submenuid!=null && submenuid!="" && submenuid.length>0)
                document.getElementById(submenuid).style.display = '';
            document.getElementById(objId).className = 'selected';
        }

        /**
         * Sub menu div id's none.
         */
        function subMenuDivNone(){
            //document.getElementById("ADMIN_ID").style.display = 'none';
            document.getElementById("MASTER_ID").style.display = 'none';
            document.getElementById("TXN_ID").style.display = 'none';
            document.getElementById("ADMIN_ID").style.display = 'none';
            document.getElementById("REPORTS_ID").style.display = 'none';
        }

        /**
         * Removed the CSS from Sub Menu.
         */
        function subMenuClassNameNone(){
            document.getElementById("A_DIV_1").className = '';
            document.getElementById("A_DIV_2").className = '';
            document.getElementById("A_DIV_3").className = '';
            document.getElementById("A_DIV_4").className = '';
            document.getElementById("A_DIV_5").className = '';
        }
   </script>

<div class="menu">
    <ul>
        <li><a href="#" id="A_DIV_1" onclick="homePage();">Home</a></li>
        <li>
            <a href="#" id="A_DIV_2" onclick="enabledSubMenuDiv(this.id,'MASTER_ID')">Masters</a>
            <div id="MASTER_ID" style="display:none;">
                <ul>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TenderAction','tenderMasterPage','')">Tender Master</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TenderAction','tenderPhasePage','')">Tender Phase</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('InstitutionMasterAction','inistitutionMasterPage','')">Taluk Master</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('SupplierMasterAction','supplierMasterPage','')">Supplier</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('ReferenceCodeAction','showReferenceCode','')">Master Parameters</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('InspectionParameterMasterAction','InspectionDataParameterPage','')">Inspection Data Parameterization</a></span></li>
                    <!--li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','ProductMaster','')">Product Master</a></span></li-->
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','DeliverySchedule','')">Delivery Schedule</a></span></li>
                </ul>
            </div>
        </li>
        <li>
            <a href="#" id="A_DIV_3" onclick="enabledSubMenuDiv(this.id,'TXN_ID')">Transactions</a>
            <div id="TXN_ID" style="display:none;">
                <ul>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','TenderWiseLapAllocation','')">Tenderwise Supplier</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','DistrictwiseSupplier','')">Districtwise Allocation</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','PreDispatchInspection','')">Batch Submission</a></span></li>
                    <li><span class="submenu"><a href="#" >Serial Number Addition</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','ThirdPartyInspection','')">ThirdParty Inspection</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','SupplierDelivery','')">Supplier Delivery</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','DistrictLevelInspection','')">Field Inspection</a></span></li>
                    <li><span class="submenu"><a href="#" >Institute Demo</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','PaymentInitiation','')">1.Payment Initiation</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','PaymentDueComputation','')">3.Payment Approval</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','PaymentRelease','')">4.Payment Release</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','BeneficiaryDataCollection','')">Beneficiary Data Collection</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','BeneficiaryDataMapping','')">Beneficiary Data Mapping</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','','')">Serial Number Updation</a></span></li>
                </ul>
            </div>
        </li>
        <li>
            <a href="#" id="A_DIV_4" onclick="enabledSubMenuDiv(this.id,'ADMIN_ID')">Administration</a>
            <div id="ADMIN_ID" style="display:none;">
                <ul>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('UserAction','userCreationPage','')">User Creation</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('UserAction','changePasswordPage','')">Change Password</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('UserAction','userStatusModificationPage','')">Change Status Modification</a></span></li>
                    <!--li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','ProductMaster','')">Product Master</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','DeliverySchedule','')">Delivery Schedule</a></span></li-->
                </ul>
            </div>
        </li>        
        <li>
            <a href="#" id="A_DIV_5" onclick="enabledSubMenuDiv(this.id,'REPORTS_ID')">Reports</a>
            <div id="REPORTS_ID" style="display:none;">
                <ul>
                    <li><span class="submenu"><a href="#">Overall Progress</a></span></li>
                    <li><span class="submenu"><a href="#">Dist.wise progress</a></span></li>
                    <li><span class="submenu"><a href="#">A.C.wise progress</a></span></li>
                    <li><span class="submenu"><a href="#">List Payment Details</a></span></li>
                    <li><span class="submenu"><a href="#">Suppliers Payt Rpt</a></span></li>
                    <li><span class="submenu"><a href="#">Suppliers Alloc Rpts</a></span></li>
                    <li><span class="submenu"><a href="#">Indent Vs Supply</a></span></li>
                    <li><span class="submenu"><a href="#">Supplier wise progress</a></span></li>
                    <li><span class="submenu"><a href="#">District wise Report</a></span></li>
                    <li><span class="submenu"><a href="#">Supplier wise - Dt</a></span></li>
                    <li><span class="submenu"><a href="#">Summary of Supply</a></span></li>
                </ul>
            </div>
        </li>
        <%--li>
            <a href="#" id="A_DIV_4" onclick="enabledSubMenuDiv(this.id,'OPER_ID')">Operation</a>
            <div id="OPER_ID" style="display:none;">
                <ul>
                    <!--li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','SupplierwiseLapAllocation','')">Supplier wise Allocation</a></span></li-->
                    <!--li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','AcknowledgmentForDespatchedTVSets','')">Despatch Acknowledgment</a></span></li-->
                    <!--li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','AddNewHamlet','')">AddNew Village</a></span></li-->
                    <!--li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','SearchSerialNo','')">Search SerialNo</a></span></li-->
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','InvoiceEntry','')">Invoice Entry Details</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','DeliveryCompletion','')">Delivery Completion</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','GettingSignOff','')">Getting Sign Off</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','Indent','')">Indent</a></span></li>
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','PendingApproval','')">2.Pending Approval</a></span></li>
                    <!--li><span class="submenu"><a href="#" >4.Approved Indents</a></span></li-->
                    <li><span class="submenu"><a href="#" onclick="getMenuRequest('TempAction','ShortIndentClosing','')">Short Indent Close</a></span></li>
                </ul>
            </div>
        </li--%>
    </ul>
</div>