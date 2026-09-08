package agentdd.model.data;

public class Claim {
    private Integer coverId;
    private String insatsuRenban;
    // DB項目定義書の金額18桁に対応するため、Long型で保持する。
    private Long premiumAmount;
    private Long premiumInstallment;
    private String maker;
    private String carName;
    private String licenseNo;
    private Long vehiclePrice;
    private Integer vehicleRates;
    private Integer bodilyRates;
    private Integer propertyDamageRates;
    private Integer accidentRates;
    private Integer licenseColor;
    private Integer ageLimit;

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

    public Long getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(Long premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public Long getPremiumInstallment() {
        return premiumInstallment;
    }

    public void setPremiumInstallment(Long premiumInstallment) {
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

    public Long getVehiclePrice() {
        return vehiclePrice;
    }

    public void setVehiclePrice(Long vehiclePrice) {
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

}