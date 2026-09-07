package agentdd.model.data;

public class Accident {
    private String accidentNo;
    private int claim;
    private int accidentFlag;
    private int paymentAmount;
    private String occurPlaceKana1;
    private String occurPlaceKana2;
    private String occurPlaceKanji1;
    private String occurPlaceKanji2;
    private String occurDate;
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

    // --- コンストラクタ ---
    public Accident() {
    }

    // --- Getters and Setters ---
    public String getAccidentNo() {
        return accidentNo;
    }

    public void setAccidentNo(String accidentNo) {
        this.accidentNo = accidentNo;
    }

    public int getClaim() {
        return claim;
    }

    public void setClaim(int claim) {
        this.claim = claim;
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

    public String getOccurPlaceKana1() {
        return occurPlaceKana1;
    }

    public void setOccurPlaceKana1(String occurPlaceKana1) {
        this.occurPlaceKana1 = occurPlaceKana1;
    }

    public String getOccurPlaceKana2() {
        return occurPlaceKana2;
    }

    public void setOccurPlaceKana2(String occurPlaceKana2) {
        this.occurPlaceKana2 = occurPlaceKana2;
    }

    public String getOccurPlaceKanji1() {
        return occurPlaceKanji1;
    }

    public void setOccurPlaceKanji1(String occurPlaceKanji1) {
        this.occurPlaceKanji1 = occurPlaceKanji1;
    }

    public String getOccurPlaceKanji2() {
        return occurPlaceKanji2;
    }

    public void setOccurPlaceKanji2(String occurPlaceKanji2) {
        this.occurPlaceKanji2 = occurPlaceKanji2;
    }

    public String getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(String occurDate) {
        this.occurDate = occurDate;
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
}