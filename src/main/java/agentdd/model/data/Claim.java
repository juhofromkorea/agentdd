package agentdd.model.data;

public class Claim {
    private Integer coverId;
    private String insatsuRenban;
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

    public String ratesMaster(Integer rates){
        if (rates==0) {
        return "0.0倍";

    } else if (rates==1) {
        return "1.0倍";

    } else if (rates==2) {
        return "1.2倍";

    } else if (rates==3) {
        return "1.4倍";

    } else if (rates==4) {
        return "1.6倍";

    } else {
        return "1.9倍";
    }

}

public String ageMaster(Integer ageLimit){
        if (ageLimit==1) {
        return "無制限";

    } else if (ageLimit==2) {
        return "21歳以上";

    
    } else {
        return "26歳以上";
    }

}
public String licenseColorMaster(Integer licenseColor){
        if (licenseColor==1) {
        return "ブルー";

    } else if (licenseColor==2) {
        return "グリーン";

    
    } else {
        return "ゴールド";
    }

}

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

}
    