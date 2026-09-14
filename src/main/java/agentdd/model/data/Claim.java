package agentdd.model.data;

public class Claim {
    private Integer coverId;
    private String insatsuRenban;
    // DB項目定義書の金額18桁に対応するため Integer型で保持する。
    private Integer premiumAmount;
    private Integer premiumInstallment;
    private String maker;
    private String carName;
    private String licenseNo;
    private Integer vehiclePrice;
    private Integer vehicleRates;
    private Integer bodilyRates;
    private Integer propertyDamageRates;
    private Integer accidentRates;
    private Integer licenseColor;
    private Integer ageLimit;

    private String licenseColorStr;
    private String ageLimitStr;
    private Double vehicleRateValue;
    private Double bodilyRateValue;
    private Double propertyDamageRateValue;
    private Double accidentRateValue;

    public Integer getCoverId() {
        return coverId;
    }

    public void setCoverId(Integer coverId) {
        this.coverId = coverId;
    }

    public String getInsatsuRenban() {
        return insatsuRenban;
    }

    public void setInsatsuRenban(String insatsuRenban) {
        this.insatsuRenban = insatsuRenban;
    }

    public Integer getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(Integer premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public Integer getPremiumInstallment() {
        return premiumInstallment;
    }

    public void setPremiumInstallment(Integer premiumInstallment) {
        this.premiumInstallment = premiumInstallment;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public Integer getVehiclePrice() {
        return vehiclePrice;
    }

    public void setVehiclePrice(Integer vehiclePrice) {
        this.vehiclePrice = vehiclePrice;
    }

    public Integer getVehicleRates() {
        return vehicleRates;
    }

    public void setVehicleRates(Integer vehicleRates) {
        this.vehicleRates = vehicleRates;
    }

    public Integer getBodilyRates() {
        return bodilyRates;
    }

    public void setBodilyRates(Integer bodilyRates) {
        this.bodilyRates = bodilyRates;
    }

    public Integer getPropertyDamageRates() {
        return propertyDamageRates;
    }

    public void setPropertyDamageRates(Integer propertyDamageRates) {
        this.propertyDamageRates = propertyDamageRates;
    }

    public Integer getAccidentRates() {
        return accidentRates;
    }

    public void setAccidentRates(Integer accidentRates) {
        this.accidentRates = accidentRates;
    }

    public Integer getLicenseColor() {
        return licenseColor;
    }

    public void setLicenseColor(Integer licenseColor) {
        this.licenseColor = licenseColor;
    }

    public Integer getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(Integer ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getLicenseColorStr() {
        return licenseColorStr;
    }

    public void setLicenseColorStr(String licenseColorStr) {
        this.licenseColorStr = licenseColorStr;
    }

    public String getAgeLimitStr() {
        return ageLimitStr;
    }

    public void setAgeLimitStr(String ageLimitStr) {
        this.ageLimitStr = ageLimitStr;
    }

    public Double getVehicleRateValue() {
        return vehicleRateValue;
    }

    public void setVehicleRateValue(Double vehicleRateValue) {
        this.vehicleRateValue = vehicleRateValue;
    }

    public Double getBodilyRateValue() {
        return bodilyRateValue;
    }

    public void setBodilyRateValue(Double bodilyRateValue) {
        this.bodilyRateValue = bodilyRateValue;
    }

    public Double getPropertyDamageRateValue() {
        return propertyDamageRateValue;
    }

    public void setPropertyDamageRateValue(Double propertyDamageRateValue) {
        this.propertyDamageRateValue = propertyDamageRateValue;
    }

    public Double getAccidentRateValue() {
        return accidentRateValue;
    }

    public void setAccidentRateValue(Double accidentRateValue) {
        this.accidentRateValue = accidentRateValue;
    }
}