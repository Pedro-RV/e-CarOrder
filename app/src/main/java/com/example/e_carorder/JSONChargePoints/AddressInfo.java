package com.example.e_carorder.JSONChargePoints;

public class AddressInfo {
    String ID, Title, AddressLine1, AddressLine2, Town, StateOrProvince, Postcode, CountryID, Latitude, Longitude, ContactTelephone1, ContactTelephone2, ContactEmail,
            AccessComments, RelatedURL, Distance, DistanceUnit;

    Country country;

    public AddressInfo(String ID, String title, String addressLine1, String addressLine2, String town, String stateOrProvince, String postcode, String countryID, String latitude, String longitude, String contactTelephone1, String contactTelephone2, String contactEmail, String accessComments, String relatedURL, String distance, String distanceUnit, Country country) {
        this.ID = ID;
        Title = title;
        AddressLine1 = addressLine1;
        AddressLine2 = addressLine2;
        Town = town;
        StateOrProvince = stateOrProvince;
        Postcode = postcode;
        CountryID = countryID;
        Latitude = latitude;
        Longitude = longitude;
        ContactTelephone1 = contactTelephone1;
        ContactTelephone2 = contactTelephone2;
        ContactEmail = contactEmail;
        AccessComments = accessComments;
        RelatedURL = relatedURL;
        Distance = distance;
        DistanceUnit = distanceUnit;
        this.country = country;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getStateOrProvince() {
        return StateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        StateOrProvince = stateOrProvince;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public String getCountryID() {
        return CountryID;
    }

    public void setCountryID(String countryID) {
        CountryID = countryID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getContactTelephone1() {
        return ContactTelephone1;
    }

    public void setContactTelephone1(String contactTelephone1) {
        ContactTelephone1 = contactTelephone1;
    }

    public String getContactTelephone2() {
        return ContactTelephone2;
    }

    public void setContactTelephone2(String contactTelephone2) {
        ContactTelephone2 = contactTelephone2;
    }

    public String getContactEmail() {
        return ContactEmail;
    }

    public void setContactEmail(String contactEmail) {
        ContactEmail = contactEmail;
    }

    public String getAccessComments() {
        return AccessComments;
    }

    public void setAccessComments(String accessComments) {
        AccessComments = accessComments;
    }

    public String getRelatedURL() {
        return RelatedURL;
    }

    public void setRelatedURL(String relatedURL) {
        RelatedURL = relatedURL;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getDistanceUnit() {
        return DistanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        DistanceUnit = distanceUnit;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}

class Country{
    private String ISOCode, ContinentCode, ID, Title;

    public Country(String ISOCode, String continentCode, String ID, String title) {
        this.ISOCode = ISOCode;
        ContinentCode = continentCode;
        this.ID = ID;
        Title = title;
    }

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String ISOCode) {
        this.ISOCode = ISOCode;
    }

    public String getContinentCode() {
        return ContinentCode;
    }

    public void setContinentCode(String continentCode) {
        ContinentCode = continentCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
