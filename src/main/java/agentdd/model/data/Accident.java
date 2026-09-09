package agentdd.model.data;

public class Accident {

    private String claimNo;
    private int coverId;
    private int claimStatus;
    private long paymentPrice;
    
    private String accidentLocationKana1;
    private String accidentLocationKana2;
    private String accidentLocationKanji1;
    private String accidentLocationKanji2;
    private String accidentDate;
    
    private String accidentSituation;
    private int ratingBlameMyself;
    private int ratingBlameYourself;

    private long damageCarPrice;
    private long damageBodilyPrice;
    private long damagePropertyPrice;
    private long damageAccidentPrice;
    
    private String damageCarState;
    private String damageBodilyState;
    private String damagePropertyState;
    private String damageAccidentState;

    // 関連および画面引き継ぎ用
    private Claim claim;
    private String polNo;

    public Accident() {
    }

    // Getters and Setters

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

    public int getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(int claimStatus) {
        this.claimStatus = claimStatus;
    }

    public long getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(long paymentPrice) {
        this.paymentPrice = paymentPrice;
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

    public String getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getAccidentSituation() {
        return accidentSituation;
    }

    public void setAccidentSituation(String accidentSituation) {
        this.accidentSituation = accidentSituation;
    }

    public int getRatingBlameMyself() {
        return ratingBlameMyself;
    }

    public void setRatingBlameMyself(int ratingBlameMyself) {
        this.ratingBlameMyself = ratingBlameMyself;
    }

    public int getRatingBlameYourself() {
        return ratingBlameYourself;
    }

    public void setRatingBlameYourself(int ratingBlameYourself) {
        this.ratingBlameYourself = ratingBlameYourself;
    }

    public long getDamageCarPrice() {
        return damageCarPrice;
    }

    public void setDamageCarPrice(long damageCarPrice) {
        this.damageCarPrice = damageCarPrice;
    }

    public long getDamageBodilyPrice() {
        return damageBodilyPrice;
    }

    public void setDamageBodilyPrice(long damageBodilyPrice) {
        this.damageBodilyPrice = damageBodilyPrice;
    }

    public long getDamagePropertyPrice() {
        return damagePropertyPrice;
    }

    public void setDamagePropertyPrice(long damagePropertyPrice) {
        this.damagePropertyPrice = damagePropertyPrice;
    }

    public long getDamageAccidentPrice() {
        return damageAccidentPrice;
    }

    public void setDamageAccidentPrice(long damageAccidentPrice) {
        this.damageAccidentPrice = damageAccidentPrice;
    }

    public String getDamageCarState() {
        return damageCarState;
    }

    public void setDamageCarState(String damageCarState) {
        this.damageCarState = damageCarState;
    }

    public String getDamageBodilyState() {
        return damageBodilyState;
    }

    public void setDamageBodilyState(String damageBodilyState) {
        this.damageBodilyState = damageBodilyState;
    }

    public String getDamagePropertyState() {
        return damagePropertyState;
    }

    public void setDamagePropertyState(String damagePropertyState) {
        this.damagePropertyState = damagePropertyState;
    }

    public String getDamageAccidentState() {
        return damageAccidentState;
    }

    public void setDamageAccidentState(String damageAccidentState) {
        this.damageAccidentState = damageAccidentState;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getPolNo() {
        return polNo;
    }

    public void setPolNo(String polNo) {
        this.polNo = polNo;
    }
}