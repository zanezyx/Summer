package com.deter.TaxManager.model;

import java.io.Serializable;

public class SalaryInfo implements Serializable {

    private int salary;
    private int unemploymentInsurance;
    private int endowment;
    private int medicalInsurance;
    private int subsidy;
    private int fundCharges;

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getUnemploymentInsurance() {
        return unemploymentInsurance;
    }

    public void setUnemploymentInsurance(int unemploymentInsurance) {
        this.unemploymentInsurance = unemploymentInsurance;
    }

    public int getEndowment() {
        return endowment;
    }

    public void setEndowment(int endowment) {
        this.endowment = endowment;
    }

    public int getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(int medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public int getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(int subsidy) {
        this.subsidy = subsidy;
    }

    public int getFundCharges() {
        return fundCharges;
    }

    public void setFundCharges(int fundCharges) {
        this.fundCharges = fundCharges;
    }
}
