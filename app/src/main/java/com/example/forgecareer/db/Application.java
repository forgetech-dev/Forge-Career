package com.example.forgecareer.db;

import java.util.Date;

public class Application {

    /**
     * Sample Application data layout
     *  CompanyName: Amazon (mandatory)
     *  JobType: Full-Time (mandatory)
     *  PositionType: SWE (mandatory)
     *  StartDate: Summer23 (mandatory)
     *  AppliedDate: 2022-09-01 (mandatory)
     *  Priority: High (mandatory)
     *  Interview: False (mandatory)
     *  InterviewDate: Null (optional)
     *  InterviewRound: 0 (Optional)
     */
    String CompanyName;
    String JobType;
    String PositionType;
    String StartDate;
    String Status;
    Date ApplicationDate;
    String Priority;
    Boolean Interview;
    Date InterviewDate;


    /**
     * Empty constructor
     */
    public Application() {}

    /**
     * Full Constructor
     */
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
