package agentdd.model.data;

public class Accident {
    // --- 既存のフィールド ---
    private String claimNo; 
    private int coverId;
    private int accidentFlag;

    private Contract contract;
    
    private String accidentLocationKana1;
    private String accidentLocationKana2;
    private String accidentLocationKanji1;
    private String accidentLocationKanji2;
    private String accidentSituation;
    
    private int paymentAmount; 
    
    // 【統一】occur -> accident に変更
    private String accidentPlaceKana1;
    private String accidentPlaceKana2;
    private String accidentPlaceKanji1;
    private String accidentPlaceKanji2;
    private String accidentDate;
    
    private String opponentStatus;
    private int negligenceInsured;
    private int negligenceOpponent;

    private int damageVehicle;
    private int damagePerson;
    private int damageObject;
    private int damageInjury;
    
    private String statusVehicle;
    private String statusPerson;
    private String statusObject;
    private String statusInjury;

    private String contractorName;

    // --- 画面引き継ぎ用（DB保存対象外） ---
    private String polNo; // 証券番号

    // --- コンストラクタ ---
    public Accident() {
    }

    // --- Getters and Setters ---
    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public int getCoverId() {
        return coverId;
    }

    public void setCoverId(int coverId) {
        this.coverId = coverId;
    }

    public int getAccidentFlag() {
        return accidentFlag;
    }

    public void setAccidentFlag(int accidentFlag) {
        this.accidentFlag = accidentFlag;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getAccidentPlaceKana1() {
        return accidentPlaceKana1;
    }

    public void setAccidentPlaceKana1(String accidentPlaceKana1) {
        this.accidentPlaceKana1 = accidentPlaceKana1;
    }

    public String getAccidentPlaceKana2() {
        return accidentPlaceKana2;
    }

    public void setAccidentPlaceKana2(String accidentPlaceKana2) {
        this.accidentPlaceKana2 = accidentPlaceKana2;
    }

    public String getAccidentPlaceKanji1() {
        return accidentPlaceKanji1;
    }

    public void setAccidentPlaceKanji1(String accidentPlaceKanji1) {
        this.accidentPlaceKanji1 = accidentPlaceKanji1;
    }

    public String getAccidentPlaceKanji2() {
        return accidentPlaceKanji2;
    }

    public void setAccidentPlaceKanji2(String accidentPlaceKanji2) {
        this.accidentPlaceKanji2 = accidentPlaceKanji2;
    }

    public String getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getOpponentStatus() {
        return opponentStatus;
    }

    public void setOpponentStatus(String opponentStatus) {
        this.opponentStatus = opponentStatus;
    }

    public int getNegligenceInsured() {
        return negligenceInsured;
    }

    public void setNegligenceInsured(int negligenceInsured) {
        this.negligenceInsured = negligenceInsured;
    }

    public int getNegligenceOpponent() {
        return negligenceOpponent;
    }

    public void setNegligenceOpponent(int negligenceOpponent) {
        this.negligenceOpponent = negligenceOpponent;
    }

    public int getDamageVehicle() {
        return damageVehicle;
    }

    public void setDamageVehicle(int damageVehicle) {
        this.damageVehicle = damageVehicle;
    }

    public int getDamagePerson() {
        return damagePerson;
    }

    public void setDamagePerson(int damagePerson) {
        this.damagePerson = damagePerson;
    }

    public int getDamageObject() {
        return damageObject;
    }

    public void setDamageObject(int damageObject) {
        this.damageObject = damageObject;
    }

    public int getDamageInjury() {
        return damageInjury;
    }

    public void setDamageInjury(int damageInjury) {
        this.damageInjury = damageInjury;
    }

    public String getStatusVehicle() {
        return statusVehicle;
    }

    public void setStatusVehicle(String statusVehicle) {
        this.statusVehicle = statusVehicle;
    }

    public String getStatusPerson() {
        return statusPerson;
    }

    public void setStatusPerson(String statusPerson) {
        this.statusPerson = statusPerson;
    }

    public String getStatusObject() {
        return statusObject;
    }

    public void setStatusObject(String statusObject) {
        this.statusObject = statusObject;
    }

    public String getStatusInjury() {
        return statusInjury;
    }

    public void setStatusInjury(String statusInjury) {
        this.statusInjury = statusInjury;
    }

    public String getPolNo() {
        return polNo;
    }

    public void setPolNo(String polNo) {
        this.polNo = polNo;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
    public String getAccidentLocationKana1() {
        return accidentLocationKana1;
    }

    public void setAccidentLocationKana1(String accidentLocationKana1) {
        this.accidentLocationKana1 = accidentLocationKana1;
    }

    public String getAccidentLocationKana2() {
        return accidentLocationKana2;
    }

    public void setAccidentLocationKana2(String accidentLocationKana2) {
        this.accidentLocationKana2 = accidentLocationKana2;
    }

    public String getAccidentLocationKanji1() {
        return accidentLocationKanji1;
    }

    public void setAccidentLocationKanji1(String accidentLocationKanji1) {
        this.accidentLocationKanji1 = accidentLocationKanji1;
    }

    public String getAccidentLocationKanji2() {
        return accidentLocationKanji2;
    }

    public void setAccidentLocationKanji2(String accidentLocationKanji2) {
        this.accidentLocationKanji2 = accidentLocationKanji2;
    }

    public String getAccidentSituation() {
        return accidentSituation;
    }

    public void setAccidentSituation(String accidentSituation) {
        this.accidentSituation = accidentSituation;
    }
    
}