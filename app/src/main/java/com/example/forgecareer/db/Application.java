package com.example.forgecareer.db;

public class Application {
    String CompanyName;
    String JobType;

    public Application() {}
    public Application(String companyName, String jobType) {
        CompanyName = companyName;
        JobType = jobType;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }
}
