package agentdd.model.data;

import java.text.NumberFormat;
import java.util.Locale;

public class Contract {

    private String insatsuRenban;
    private String polNo;
    private Integer statusFlg;
    private boolean cancelFlg;
    private String inceptionDate;
    private String inceptionTime;
    private String conclusionDate;
    private String conclusionTime;
    private Integer paymentMethod;
    private Integer installment;
    private Integer insuredKbn;
    private String nameKana1;
    private String nameKana2;
    private String nameKanji1;
    private String nameKanji2;
    private String postcode;
    private String addressKana1;
    private String addressKana2;
    private String addressKanji1;
    private String addressKanji2;
    private String birthday;
    private Integer gender;
    private String telephoneNo;
    private String mobilephoneNo;
    private String faxNo;

    

    public String getInsatsuRenban() {
        return insatsuRenban;
    }



    public void setInsatsuRenban(String insatsuRenban) {
        this.insatsuRenban = insatsuRenban;
    }



    public String getPolNo() {
        return polNo;
    }



    public void setPolNo(String polNo) {
        this.polNo = polNo;
    }



    public Integer getStatusFlg() {
        return statusFlg;
    }



    public void setStatusFlg(Integer statusFlg) {
        this.statusFlg = statusFlg;
    }



    public boolean isCancelFlg() {
        return cancelFlg;
    }



    public void setCancelFlg(boolean cancelFlg) {
        this.cancelFlg = cancelFlg;
    }



    public String getInceptionDate() {
        return inceptionDate;
    }



    public void setInceptionDate(String inceptionDate) {
        this.inceptionDate = inceptionDate;
    }



    public String getInceptionTime() {
        return inceptionTime;
    }



    public void setInceptionTime(String inceptionTime) {
        this.inceptionTime = inceptionTime;
    }



    public String getConclusionDate() {
        return conclusionDate;
    }



    public void setConclusionDate(String conclusionDate) {
        this.conclusionDate = conclusionDate;
    }



    public String getConclusionTime() {
        return conclusionTime;
    }



    public void setConclusionTime(String conclusionTime) {
        this.conclusionTime = conclusionTime;
    }



    public Integer getPaymentMethod() {
        return paymentMethod;
    }



    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }



    public Integer getInstallment() {
        return installment;
    }



    public void setInstallment(Integer installment) {
        this.installment = installment;
    }



    public Integer getInsuredKbn() {
        return insuredKbn;
    }



    public void setInsuredKbn(Integer insuredKbn) {
        this.insuredKbn = insuredKbn;
    }



    public String getNameKana1() {
        return nameKana1;
    }



    public void setNameKana1(String nameKana1) {
        this.nameKana1 = nameKana1;
    }



    public String getNameKana2() {
        return nameKana2;
    }



    public void setNameKana2(String nameKana2) {
        this.nameKana2 = nameKana2;
    }



    public String getNameKanji1() {
        return nameKanji1;
    }



    public void setNameKanji1(String nameKanji1) {
        this.nameKanji1 = nameKanji1;
    }



    public String getNameKanji2() {
        return nameKanji2;
    }



    public void setNameKanji2(String nameKanji2) {
        this.nameKanji2 = nameKanji2;
    }



    public String getPostcode() {
        return postcode;
    }



    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }



    public String getAddressKana1() {
        return addressKana1;
    }



    public void setAddressKana1(String addressKana1) {
        this.addressKana1 = addressKana1;
    }



    public String getAddressKana2() {
        return addressKana2;
    }



    public void setAddressKana2(String addressKana2) {
        this.addressKana2 = addressKana2;
    }



    public String getAddressKanji1() {
        return addressKanji1;
    }



    public void setAddressKanji1(String addressKanji1) {
        this.addressKanji1 = addressKanji1;
    }



    public String getAddressKanji2() {
        return addressKanji2;
    }



    public void setAddressKanji2(String addressKanji2) {
        this.addressKanji2 = addressKanji2;
    }



    public String getBirthday() {
        return birthday;
    }



    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }



    public Integer getGender() {
        return gender;
    }



    public void setGender(Integer gender) {
        this.gender = gender;
    }



    public String getTelephoneNo() {
        return telephoneNo;
    }



    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }



    public String getMobilephoneNo() {
        return mobilephoneNo;
    }



    public void setMobilephoneNo(String mobilephoneNo) {
        this.mobilephoneNo = mobilephoneNo;
    }



    public String getFaxNo() {
        return faxNo;
    }



    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }



    public String payMaster(){
         if (Integer.valueOf(1).equals(paymentMethod)) {
            return "直接集金";
        }
        if (Integer.valueOf(2).equals(paymentMethod)) {
            return "口座振替";
        }
         if (Integer.valueOf(3).equals(paymentMethod)) {
            return "クレジットカード";
        }
        return "";
          

    }



     public String genderMaster(){
         if (Integer.valueOf(1).equals(gender)) {
            return "男性";
        }
        if (Integer.valueOf(2).equals(gender)) {
            return "女性";
        }
         
        return "";
    }


     public String insuredTypeMaster(){
         if (Integer.valueOf(1).equals(insuredKbn)) {
            return "個人";
        }
        if (Integer.valueOf(2).equals(insuredKbn)) {
            return "法人";
        }
         
        return "";
          

    }


    public String contractStatusMaster(){
         if (Integer.valueOf(0).equals(statusFlg)) {
            return "計上済み";
        }
        if (Integer.valueOf(1).equals(statusFlg)) {
            return "計上処理待ち状態（新規）";
        }
         if (Integer.valueOf(5).equals(statusFlg)) {
            return "計上処理待ち状態（変更）";
        }
         if (Integer.valueOf(9).equals(statusFlg)) {
            return "計上処理待ち状態（解約）";
        }
         
        return "";
          

    }




}
