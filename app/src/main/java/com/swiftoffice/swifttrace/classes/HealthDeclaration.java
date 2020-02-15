package com.swiftoffice.swifttrace.classes;

public class HealthDeclaration {
    String CompanyName;
    String ContactNo;
    String DateTime;
    String FullName;

    String HealthQ1;
    String HealthQ2;
    String HealthQ3;
    String HealthQ4;
    String HealthQ5;
    String HealthQ6;

    String PersonID;

    public HealthDeclaration(String CompanyName, String ContactNo, String DateTime, String FullName,
                             String HealthQ1, String HealthQ2, String HealthQ3, String HealthQ4, String HealthQ5, String HealthQ6,
                             String PersonID) {
        this.CompanyName = CompanyName;
        this.ContactNo = ContactNo;
        this.DateTime = DateTime;
        this.FullName = FullName;

        this.HealthQ1 = HealthQ1;
        this.HealthQ2 = HealthQ2;
        this.HealthQ3 = HealthQ3;
        this.HealthQ4 = HealthQ4;
        this.HealthQ5 = HealthQ5;
        this.HealthQ6 = HealthQ6;

        this.PersonID = PersonID;
    }

    public String getCompanyName() {
        return CompanyName;
    }
    public String getContactNo() { return ContactNo;}
    public String getDateTime() { return DateTime;}
    public String getFullName() { return FullName;}

    public String getHealthQ1() { return HealthQ1;}
    public String getHealthQ2() { return HealthQ2;}
    public String getHealthQ3() { return HealthQ3;}
    public String getHealthQ4() { return HealthQ4;}
    public String getHealthQ5() { return HealthQ5;}
    public String getHealthQ6() { return HealthQ6;}

    public String getPersonID() { return PersonID;}
}
