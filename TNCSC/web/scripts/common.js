/*
 * Document   : common.js
 * Created on : April 25, 2011, 12:15:39 PM
 * Author     : Jagan Mohan. B
 */


/** Below code for Password Strenght **/
var minpwlength = 4;
var fairpwlength = 7;

var STRENGTH_SHORT = 0;  // less than minpwlength
var STRENGTH_WEAK = 1;  // less than fairpwlength
var STRENGTH_FAIR = 2;  // fairpwlength or over, no numbers
var STRENGTH_STRONG = 3; // fairpwlength or over with at least one number

img0 = new Image();
img1 = new Image();
img2 = new Image();
img3 = new Image();

img0.src = "images/tooshort.png";
img1.src = "images/fair.png";
img2.src = "images/medium.png";
img3.src = "images/strong.png";

var strengthlevel = 0;
var strengthimages = Array(img0.src,img1.src,img2.src,img3.src );

function istoosmall(pw) {
    if(pw.length < minpwlength) {
        return true;
    } else {
        return false;
    }
}
function isfair(pw) {
    if(pw.length < fairpwlength) {
        return false;
    } else {
        return true;
    }
}
function hasnum(pw) {
    var hasnum = false;
    for( var counter = 0; counter < pw.length; counter ++ ) {
        if( !isNaN( pw.charAt( counter ) ) ) {
            hasnum = true;
        }
    }
    return hasnum;
}
function updatestrength( pw ) {
    if( istoosmall( pw ) ) {
        strengthlevel = STRENGTH_SHORT;
    } else if( !isfair( pw ) ) {
        strengthlevel = STRENGTH_WEAK;
    } else if( hasnum( pw ) ) {
        strengthlevel = STRENGTH_STRONG;
    } else {
        strengthlevel = STRENGTH_FAIR;
    }
    document.getElementById("strength").src = strengthimages[ strengthlevel ];
}
/** Above code for Password Strenght **/

/**
 * Below function used for check the special caharacters in User id field
 **/
function userIdClearText(textBoxObj) {
    textBoxObj.value = filterNum(textBoxObj.value)
//var iChars = "~`!%^&*()+={}[]\\\';|\":<>?";
    function filterNum(str) {
        re = /\s|\~|\`|\!|\#|\@|\$|\%|\^|\&|\*|\(|\)|\+|\=|\{|\}|\[|\]|\'|\"|\;|\:|\,|\.|\\|\||\?|\>|\<|\./g;

//        re = /\s|\~|\`|\!|\#|\%|\^|\&|\*|\(|\)|\+|\=|\{|\}|\[|\]|\'|\"|\;|\:|\,|\.|\\|\||\?|\/|\>|\<|\./g;
        return str.replace(re, "");
    }
}


/*
 * This method used for Password checking and Password should cantain minimum 5 Characters
 * Karthikeyan.S
 */
function passwordCheck(txtBoxObj){
    x = txtBoxObj.value
    if(x!=""){
        if(txtBoxObj.value.length < 8) {
            alert("Your password must be atleast 8 Characters in length");
            txtBoxObj.value="";
            return false;
        }
    //isAlphaNumeric(obj);
    }
    return true;
}


/*
 * This function used for give value is proper mail id or not -- 
 */
function validateEmailid(txtBoxObj) {
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    var address = txtBoxObj.value;
    if(address!=null && address!="" && reg.test(address) == false) {
        alert('Invalid Email Address');
        txtBoxObj.value="";
        return false;
    }
    return true;
}

//var iChars = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?";
var iChars = "~`!%^&*()+={}[]\\\';|\":<>?";

function isValidMonth(obj)
{
    var val = parseFloat(obj.value);
    if(val<1 || val>12)
    {
        alert('Please enter the valid month');
        obj.value='';
        obj.focus();
        return false;
    }
    return true;
}
/*
 * This Function used for given values is number or not -- Jagan Mohan. B
 */
function isNumber(txtBoxObj){
    x = txtBoxObj.value;
    nos=new Array('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E',
        'F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','!','@','#','$','%','^','&','(',')','|','?','>','<','[',']','{','}','*','"','`','~','.','-','_','+','=','*');
    for(i=0;i<txtBoxObj.value.length;i++){
        for(j=0;j<=nos.length;j++){
            if(x.charAt(i) == nos[j]){
                alert("Only Numbers Are Allowed")
                txtBoxObj.value="";
                return false;
            }
        }
    }
    return true;
}

/*
 * This Function used for given values is not number or not
 */
function numberNotAllowed(txtBoxObj){
    x = txtBoxObj.value;
    nos=new Array('0','1','2','3','4','5','6','7','8','9');
    for(i=0;i<txtBoxObj.value.length;i++){
        for(j=0;j<=nos.length;j++){
            if(x.charAt(i) == nos[j]){
                alert("Numbers Are not Allowed")
                txtBoxObj.value="";
                return false;
            }
        }
    }
    return true;
}


/*
 * This function used for Special Characters -- Jagan Mohan. B
 */
function specialCharNotAllowed(txtBoxObj){
    var fieldValue = txtBoxObj.value;
    var fieldName = txtBoxObj.name;
    for (var i = 0; i < fieldValue.length; i++) {
        if (iChars.indexOf(fieldValue.charAt(i)) >= 0) {
            alert (fieldName+" has special characters. \nSpecial Caharacters are not allowed.\n");
            txtBoxObj.value = "";
            return false;
        }
    }
    return true;
}

function specialCharNotAllowedFOREPFNO(txtBoxObj){
    var avoidChars = "!@#$%^&*()+=[]\\\';,.{}|\":<>?";// Allow /-
    var fieldValue = txtBoxObj.value;
    var fieldName = txtBoxObj.name;
    for (var i = 0; i < fieldValue.length; i++) {
        if (avoidChars.indexOf(fieldValue.charAt(i)) >= 0) {
            alert (fieldName+" has special characters. \nSpecial Caharacters are not allowed.\n");
            txtBoxObj.value = "";
            return false;
        }
    }
    return true;
}

/*
 * This function used for check enter value having any special character onkeyup
 * This method not allowed ~`!#%^&*()+={}[]'";:,./|\?<>
 * Jagan Mohan. B
 */
function specialCharactersCheck(textBoxObj) {
    textBoxObj.value = filterNum(textBoxObj.value)

    function filterNum(str) {
        //re = /\s|\~|\`|\!|\#|\%|\^|\&|\*|\(|\)|\+|\=|\{|\}|\[|\]|\'|\"|\;|\:|\,|\.|\\|\||\?|\/|\>|\<|\./g;
        re = /\~|\`|\!|\#|\%|\^|\&|\*|\(|\)|\+|\=|\{|\}|\[|\]|\'|\"|\;|\:|\\|\||\?|\>|\<|/g;
        return str.replace(re, "");
    }
}

/*
 * This function used Exit the Screen -- Jagan Mohan. B
 */
function exitScreen(){
    document.forms[0].action="LoginAction.htm";
    document.forms[0].method.value="loginPage";
    document.forms[0].submit();
}

/*
 * This function used for check the value most contain Alpha and Number
 * Jagan Mohan. B
 */
function isAlphaNumeric(txtBoxObj){
    var textString = txtBoxObj.value;
    var re = /[\D]/g
    var re1 = /[^a-zA-Z]/g;
    if( (!textString.match(re)) || (!textString.match(re1)) ){
        alert(txtBoxObj.name+' Should be AlphaNumeric');
        txtBoxObj.value="";
        return ;
    }
}


/**
 * This function check the special characters in particular Form
 * This method not allowed !#$%^&*()+=-[]\\\';,./{}|\":<>?
 * Jagan Mohan. B
 */
function checkTotalFormSpecialCharacters(doc){
    var fields=doc.getElementsByTagName("input");
    for(var i=0;i<fields.length;i++) {
        if(!(fields[i].type=="button") && !(fields[i].type=="hidden")) {
            for (var j = 0; j < fields[i].value.length; j++) {
                //if (iChars.indexOf(fields[i].value.charAt(j)) != -1) {
                if (iChars.indexOf(fields[i].value.charAt(j)) >= 0) {
                    alert(fields[i].name+" Containts special characters. \n These are not allowed.\n Please remove and try again.");
                    return false;
                }
            }
        }
    }
    return true;
}

/**
 * This function used validate the pincode.
 */
function pincodeValidate(objval){
    var val = objval.value;
    if(val.substring(0,1)!='6'){
        alert("Please Enter Proper Pincode");
        objval.value = "";
        return false;
    }else if(val.length !=6){
        alert("Please Enter Proper Pincode");
        objval.value = "";
        return false;
    }
    return true;
}

/**
 * This function is used to check mandatory fields in the Form
 */
function checkMandatoryFormFields(elementsToSkip,formId){
    var formObj=document.getElementById(formId);
    var formElementsArray=formObj.getElementsByTagName("input");
    var length=formElementsArray.length;
    var fieldObject=null,fieldObject1=null,fieldObject2=null;
    for(var i=0;i<length;i++){
        fieldObject=formElementsArray[i];
        fieldObject.value=fieldObject.value.replace(/^\s*|\s*$/g,'');
        if(fieldObject.type=='text' && fieldObject.value=="" && elementsToSkip.indexOf(fieldObject.name)==-1){
            alert("please enter "+fieldObject.name);
            return false;
        }
    }
    var formElementsArray1=formObj.getElementsByTagName("select");
    var length1=formElementsArray1.length;
    for(var j=0;j<length1;j++){
        fieldObject1=formElementsArray1[j];
        fieldObject1.value=fieldObject1.value.replace(/^\s*|\s*$/g,'');
        if(fieldObject1.type=='select-one' && fieldObject1.selectedIndex==0 && elementsToSkip.indexOf(fieldObject1.name)==-1){
            alert("please select "+fieldObject1.name);
            return false;
        }
    }
    var formElementsArray2=formObj.getElementsByTagName("textarea");
    var length2=formElementsArray2.length;
    for(var k=0;k<length2;k++){
        fieldObject2=formElementsArray2[k];
        fieldObject2.value=fieldObject2.value.replace(/^\s*|\s*$/g,'');
        if(fieldObject2.value=="" && elementsToSkip.indexOf(fieldObject2.name)==-1){
            alert("please enter "+fieldObject2.name);
            return false;
        }

    }
    return true;
}

function clearAllFormFields(elementsToSkip,formId){
    var formObj=document.getElementById(formId);
    var formElementsArray=formObj.getElementsByTagName("input");
    var length=formElementsArray.length;
    var fieldObject=null,fieldObject1=null,fieldObject2=null;
    for(var i=0;i<length;i++){
        fieldObject=formElementsArray[i];
        fieldObject.value=fieldObject.value.replace(/^\s*|\s*$/g,'');
        if(fieldObject.type=='text' && fieldObject.value=="" && elementsToSkip.indexOf(fieldObject.name)==-1){
            fieldObject.value="";
        }
    }
    var formElementsArray1=formObj.getElementsByTagName("select");
    var length1=formElementsArray1.length;
    for(var j=0;j<length1;j++){
        fieldObject1=formElementsArray1[j];
        fieldObject1.value=fieldObject1.value.replace(/^\s*|\s*$/g,'');
        if(fieldObject1.type=='select-one' && fieldObject1.selectedIndex==0 && elementsToSkip.indexOf(fieldObject1.name)==-1){
            fieldObject1.selectedIndex=0;
        }
    }
    var formElementsArray2=formObj.getElementsByTagName("textarea");
    var length2=formElementsArray2.length;
    for(var k=0;k<length2;k++){
        fieldObject2=formElementsArray2[k];
        fieldObject2.value=fieldObject2.value.replace(/^\s*|\s*$/g,'');
        if(fieldObject2.value=="" && elementsToSkip.indexOf(fieldObject2.name)==-1){
            fieldObject2.value="";
        }
    }
}

function clearFormFields(fieldIdArray){
    var fieldObject=null;
    for(var i=0;i<fieldIdArray.length;i++){
        fieldObject=document.getElementById(fieldIdArray[i]);
        if(fieldObject.type=='select-one')
            fieldObject.selectedIndex=0;
        else
            fieldObject.value='';
    }
}

function mantatoryFieldsValidation(mantoryFields,mantoryFieldsLabels){
    for (i=0;i<mantoryFields.length;i++)   {
        var id = mantoryFields[i];
        document.getElementById(id).value=(document.getElementById(id).value).replace(/^\s*|\s*$/g,'');
        if(document.getElementById(id).value == "") {
            alert(mantoryFieldsLabels[i] +" should not be empty ");
            return false;
        }
    }
    return true;
}

function compareDates(date1, date2, flag) {
    var element1 = document.getElementById(date1);
    var element2 = document.getElementById(date2);

    var reviewdate = element1.value
    strArray = reviewdate.split("/");
    var arrDay= strArray[0];
    if(arrDay.substring(0,1)=="0")
        arrDay = parseInt(arrDay.substring(1));
    else
        arrDay = parseInt(arrDay);
    var arrMonth=strArray[1];
    if(arrMonth.substring(0,1)=="0")
        arrMonth = parseInt(arrMonth.substring(1));
    else
        arrMonth = parseInt(arrMonth);
    var arrYear=parseInt(strArray[2]);

    var validupto = element2.value
    strArray = validupto.split("/");
    var day= strArray[0];
    if(day.substring(0,1)=="0")
        day = parseInt(day.substring(1));
    else
        day = parseInt(day);
    var month=strArray[1];
    if(month.substring(0,1)=="0")
        month = parseInt(month.substring(1));
    else
        month = parseInt(month);
    var year = parseInt(strArray[2]);

    if(flag.toLowerCase()=="less") {
        if(arrYear<year || (arrYear==year && arrMonth<month) || (arrYear==year && arrMonth==month && arrDay<day)) {
            return true;
        }
    }
    if(flag.toLowerCase()=="lesseq") {
        if(arrYear<year || (arrYear==year && arrMonth<month) || (arrYear==year && arrMonth==month && arrDay<=day)) {
            return true;
        }
    }
    if(flag.toLowerCase()=="eq") {
        if(arrYear==year && arrMonth==month && arrDay==day) {
            return true;
        }
    }
    if(flag.toLowerCase()=="greater") {
        if(arrYear>year || (arrYear==year && arrMonth>month) || (arrYear==year && arrMonth==month && arrDay>day)) {
            return true;
        }
    }

    if(flag.toLowerCase()=="greatereq") {
        if(arrYear>year || (arrYear==year && arrMonth>month) || (arrYear==year && arrMonth==month && arrDay>=day)) {
            return true;
        }
    }

    return false;
}

function clear_AllFormFields(elementIdName, elementsToSkip) {
    try{
        var formObj=document.getElementById(elementIdName);
        var formElementsArray=formObj.getElementsByTagName("input");
        var length=formElementsArray.length;
        var fieldObject=null;
        for(var i=0;i<length;i++){
            fieldObject=formElementsArray[i];
            if(fieldObject.type=='text' && elementsToSkip.indexOf(fieldObject.name)==-1)
                fieldObject.value='';
            else if(fieldObject.type=='select-one' && elementsToSkip.indexOf(fieldObject.name)==-1)
                fieldObject.selectedIndex=0;
        }
        formElementsArray=formObj.getElementsByTagName("select");
        length=formElementsArray.length;
        for( i=0;i<length;i++){
            fieldObject=formElementsArray[i];
            if(fieldObject.type=='select-one' && elementsToSkip.indexOf(fieldObject.name)==-1)
                fieldObject.selectedIndex=0;
        }
        formElementsArray=formObj.getElementsByTagName("textarea");
        length=formElementsArray.length;
        for( i=0;i<length;i++){
            fieldObject=formElementsArray[i];
            if(fieldObject.type=='textarea' && elementsToSkip.indexOf(fieldObject.name)==-1)
                fieldObject.value='';
        }
    }catch(err){
        alert(err);
    }
}

/**
 * This function used for comapre the two dates.
 * For that u need to pass firstdate, compare date and flag.
 * date formate should be dd/MM/yyyy.
 * flag are =, >=, >, <=, <.
 *
 * Created on : July 22, 2011, 2:40: PM
 * Author     : Jagan Mohan. B
 */
function CompareTwoDates(obj, compareDateObj, flag) {
    var firstDateValue  = obj.value;
    var secondDateValue  = compareDateObj.value;
    var firstDateName = obj.name;
    var compareDateName = compareDateObj.name;

    if(firstDateValue==null || firstDateValue=="" || firstDateValue.length<=0){
        return false;
    }
    if(secondDateValue==null || secondDateValue=="" || secondDateValue.length<=0){
        alert(compareDateName+" should not be null or empty");
        obj.value = "";
        return false;
    }

    var dt1   = parseInt(firstDateValue.substring(0,2),10);
    var mon1  = parseInt(firstDateValue.substring(3,5),10);
    var yr1   = parseInt(firstDateValue.substring(6,10),10);
    var dt2   = parseInt(secondDateValue.substring(0,2),10);
    var mon2  = parseInt(secondDateValue.substring(3,5),10);
    var yr2   = parseInt(secondDateValue.substring(6,10),10);
    var date1 = new Date(yr1, mon1, dt1);
    var date2 = new Date(yr2, mon2, dt2);

    if(flag == "="){
        if(date1 == date2){
            return true;
        } else {
            alert(firstDateName+" not Equal to "+compareDateName);
            obj.value = "";
            return false;
        }
    } else if(flag == ">="){
        if(date1 >= date2){
            return true;
        } else {
            alert(firstDateName+" should be greater than or equal "+compareDateName);
            obj.value = "";
            return false;
        }
    } else if(flag == ">"){
        if(date1 > date2){
            return true;
        } else{
            alert(firstDateName+" should be greater than "+compareDateName);
            obj.value = "";
            return false;
        }
    } else if(flag == "<="){
        if(date1 <= date2){
            return true;
        } else {
            alert(firstDateName+" should be less than or equal "+compareDateName);
            obj.value = "";
            return false;
        }
    } else if(flag == "<"){
        if(date1 < date2){
            return true;
        } else {
            alert(firstDateName+" should be less than "+compareDateName);
            obj.value = "";
            return false;
        }
    }
    return false;
}

function CompareTwoDateswithMsg(obj, compareDateObj, flag, msg) {
    var firstDateValue  = obj.value;
    var secondDateValue  = compareDateObj.value;
    var firstDateName = obj.name;
    var compareDateName = compareDateObj.name;

    if(firstDateValue==null || firstDateValue=="" || firstDateValue.length<=0){
        return false;
    }
    if(secondDateValue==null || secondDateValue=="" || secondDateValue.length<=0){
        alert(compareDateName+" should not be null or empty");
        obj.value = "";
        return false;
    }

    var dt1   = parseInt(firstDateValue.substring(0,2),10);
    var mon1  = parseInt(firstDateValue.substring(3,5),10);
    var yr1   = parseInt(firstDateValue.substring(6,10),10);
    var dt2   = parseInt(secondDateValue.substring(0,2),10);
    var mon2  = parseInt(secondDateValue.substring(3,5),10);
    var yr2   = parseInt(secondDateValue.substring(6,10),10);
    var date1 = new Date(yr1, mon1, dt1);
    var date2 = new Date(yr2, mon2, dt2);

    if(flag == "="){
        if(date1 == date2){
            return true;
        } else {
            alert(msg);
            obj.value = "";
            return false;
        }
    } else if(flag == ">="){
        if(date1 >= date2){
            return true;
        } else {
            alert(msg);
            obj.value = "";
            return false;
        }
    } else if(flag == ">"){
        if(date1 > date2){
            return true;
        } else{
            alert(msg);
            obj.value = "";
            return false;
        }
    } else if(flag == "<="){
        if(date1 <= date2){
            return true;
        } else {
            alert(msg);
            obj.value = "";
            return false;
        }
    } else if(flag == "<"){
        if(date1 < date2){
            return true;
        } else {
            alert(msg);
            obj.value = "";
            return false;
        }
    }
    return false;
}


/**
 * This function used for add the comma to ammount fields array.
 * Jagan Mohan. B (25/07/2011)
 */
function AddOrRemoveCommaToAmountArray(fieldIdsArray,flag){
    var length=fieldIdsArray.length;
    if(flag=="add"){
        for(var i=0; i<length; i++)
            addCommaToAmount(document.getElementById(fieldIdsArray[i]));
    } else if(flag=="remove"){
        for(var j=0; j<length; j++)
            removeCommaFromAmount(document.getElementById(fieldIdsArray[j]));
    }
}

/**
 * This function used for add the comma to ammount
 * Jagan Mohan. B (25/07/2011)
 */
function addCommaToAmount(obj){
    var inD = '.';
    var sep = ',';

    //finalAmount += '';
    var finalAmount = obj.value;
    var dpos = finalAmount.indexOf(inD);
    if (dpos != -1) {
        finalAmount = finalAmount.substring(0, dpos);
    }
    var rgx = /(\d+)(\d{3})/;
    var rgx1 = /(\d+)(\d{2})/;
    var minusFlag = false;
    if(parseFloat(finalAmount)<0){
        minusFlag = true;
        if (dpos == -1)
            finalAmount = finalAmount.substring(1, finalAmount.length);
        else
            finalAmount = finalAmount.substring(1, dpos);
    }
    var lenght = finalAmount.length;

    if (finalAmount > 999) {
        for (var i = lenght; i > 0; i = i - 2) {
            if (i == lenght) {
                i = i - 3;
                finalAmount = finalAmount.replace(rgx, '$1' + sep + '$2');
            } else {
                finalAmount = finalAmount.replace(rgx1, '$1' + sep + '$2');
            }
        }
    }
    if(finalAmount==null || finalAmount=="" || finalAmount.length<=0)   finalAmount = "0";

    finalAmount = finalAmount+'.00';
    if(minusFlag && finalAmount!=null && finalAmount!="" && parseFloat(finalAmount) != "0"){
        finalAmount = '-'+finalAmount;
    }

    if(finalAmount==null || finalAmount=="" || parseFloat(finalAmount) == "0")   finalAmount = "0.00";
    obj.value = finalAmount;
}

/**
 * This function used for remove the comma from ammount
 * Jagan Mohan. B (25/07/2011)
 */
function removeCommaFromAmount(obj){
    //finalAmount = finalAmount.replace(/,/g,'');
    var finalAmount = obj.value;
    obj.value = finalAmount.replace(/\$|\,/g,'');
}

/**
 * This function used for enterd amount is numeric or not.
 * Jagan Mohan. B (25/07/2011)
 */
function isNumeric(obj){
    var strValidChars = "0123456789,.-";
    var strChar;
    var blnResult = true;
    var strString = obj.value;

    if (strString.length == 0) return false;

    for (var i = 0; i < strString.length && blnResult == true; i++){
        strChar = strString.charAt(i);
        if (strValidChars.indexOf(strChar) == -1){
            blnResult = false;
        }
    }
    if(!blnResult){
        alert("Only Numbers Are Allowed")
        obj.value="";
        return false;
    }
    return blnResult;
}
function isNumericCheck(obj){
    var strValidChars = "0123456789,.-";
    var strChar;
    var blnResult = true;
    var strString = obj.value;

    if (strString.length == 0) return false;

    for (var i = 0; i < strString.length && blnResult == true; i++){
        strChar = strString.charAt(i);
        if (strValidChars.indexOf(strChar) == -1){
            blnResult = false;
        }
    }
    if(!blnResult){
        alert("Only Numbers Are Allowed")
        obj.value="";
        //return false;
    }
    return blnResult;
}

function checkPercentage(id,msg){
    var numVal = document.getElementById(id);
    var numPatt = /^[0-9.]*$/;
    if(numVal.value.length > 0){
        if(numVal.value == '.'){
            alert(msg + " is invalid.");
            numVal.value = "";
            numVal.focus();
            return false;
        }

        if(!numPatt.test(numVal.value)){
            alert(msg + " is invalid.");
            numVal.value = "";
            numVal.focus();
            return false;
        } else{
            var arval = numVal.value.split(".");
            if (arval.length > 2) {
                alert(msg + " is invalid.");
                numVal.value = "";
                numVal.focus();
                return false;
            }
            if(arval[0] != null && arval[0]!= "" && arval[0] != "undefined"){
                if(arval[0] > 99){
                    alert(msg + " is invalid.");
                    numVal.value = "";
                    numVal.focus();
                    return false;
                }
                if(numVal.value >= 100) {
                    alert(msg + " is invalid.");
                    numVal.value = "";
                    numVal.focus();
                    return false;
                }
                if (numVal.value.indexOf(".") == -1) {
                    numVal.value = arval[0]+".00";
                } else{
                    numVal.value = arval[0]+"."+arval[1];
                }
            }
            numVal.style.align = "right";
            return true;
        }
    } else{
        return true;
    }
}

function isPercentage(id,msg){
    var numVal = document.getElementById(id);
    var numPatt = /^[0-9.]*$/;
    if(numVal.value.length > 0){
        if(numVal.value == '.'){
            alert(msg + " is invalid.");
            numVal.value = "";
            numVal.focus();
            return false;
        }

        if(!numPatt.test(numVal.value)){
            alert(msg + " is invalid.");
            numVal.value = "";
            numVal.focus();
            return false;
        } else{
            var arval = numVal.value.split(".");
            if (arval.length > 3) {
                alert(msg + " is invalid.");
                numVal.value = "";
                numVal.focus();
                return false;
            }
            if(arval[0] != null && arval[0]!= "" && arval[0] != "undefined"){
                if(numVal.value > 100) {
                    alert(msg + " is invalid.");
                    numVal.value = "";
                    numVal.focus();
                    return false;
                }
                if (numVal.value.indexOf(".") == -1) {
                    numVal.value = arval[0]+".00";
                } else{
                    numVal.value = arval[0]+"."+arval[1];
                }
            }
            numVal.style.align = "right";
            return true;
        }
    } else{
        return true;
    }
}

function makeAllFieldsEnabled(forMId) {

    var formId = document.getElementById(forMId);
    var inputTags = formId.getElementsByTagName("input");
    for(var i = 0; i < inputTags.length; i++)
        inputTags[i].disabled=false;
    var selectTags = formId.getElementsByTagName("select");
    for(i = 0; i < selectTags.length; i++)
        selectTags[i].disabled=false;
}
function checkFloat(id,msg){
    var numVal = document.getElementById(id);
    var numPatt = /^[0-9.]*$/;
    if(numVal.value.length > 0){
        if(numVal.value == '.'){
            alert(msg + " is invalid.");
            numVal.value = "";
            numVal.focus();
            return false;
        }
        if(!numPatt.test(numVal.value)){
            alert(msg + " is invalid.");
            numVal.value = "";
            numVal.focus();
            return false;
        }else{
            var arval = numVal.value.split(".");

            if(arval[0] != null && arval[0]!= "" && arval[0] != "undefined")
            {
                if (numVal.value.indexOf(".") == -1) {
                    numVal.value = arval[0]+".00";
                }else{
                    numVal.value = arval[0]+"."+arval[1];
                }
            }
            numVal.style.align = "right";
            return true;
        }
    }else{
        return true;
    }
}
function isPhone(obj){
    var strValue = obj.value;
    var startPhoneNumber = strValue.charAt(0);
    if(strValue.length<10)
    {
        alert("Driver Mobile No should be 10 numbers.");
        obj.value = '';
        return false;
    }
    if(startPhoneNumber =='7' || startPhoneNumber =='8' || startPhoneNumber =='9' ){
        return true;
    }else{
        alert("Driver Mobile No should start with 7 or 8 or 9");
        obj.value = '';
        return false;
    }   
}
function sairam(){
    alert("Hi Siva How are you?");
}
