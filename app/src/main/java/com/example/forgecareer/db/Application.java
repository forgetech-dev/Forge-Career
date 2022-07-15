package com.example.forgecareer.db;

import com.example.forgecareer.utils.DateParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    String companyName;
    String jobType;
    String positionType;
    String startDate;
    String referer;
    String status;
    String applicationDate;
    String priority;
    String interviewDate;
    String notes;
    Boolean expanded;
    String createDate;
    String updateDate;


    /**
     * Empty constructor
     */
    public Application() {}

    /**
     * Partial constructor
     */
    public Application(String companyName, String jobType) {
        this.companyName = companyName;
        this.jobType = jobType;
    }

    /**
     * Full constructor
     */
    public Application(String companyName, String jobType, String positionType, String startDate, String referer, String status, String applicationDate, String priority, String interviewDate, String notes) {
        this.companyName = companyName;
        this.jobType = jobType;
        this.positionType = positionType;
        this.startDate = startDate;
        this.referer = referer;
        this.status = status;
        this.applicationDate = applicationDate;
        this.priority = priority;
        this.interviewDate = interviewDate;
        this.notes = notes;
        this.expanded = false;
        this.createDate = DateParser.getCurrentDateTimeString();
        this.updateDate = DateParser.getCurrentDateTimeString();
    }

    public Application(String companyName, String jobType, String positionType, String startDate, String referer, String status, String applicationDate, String priority, String interviewDate, String notes, String createDate, String updateDate) {
        this.companyName = companyName;
        this.jobType = jobType;
        this.positionType = positionType;
        this.startDate = startDate;
        this.referer = referer;
        this.status = status;
        this.applicationDate = applicationDate;
        this.priority = priority;
        this.interviewDate = interviewDate;
        this.notes = notes;
        this.expanded = false;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public LocalDate applicationDateToDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(applicationDate,formatter);
        return localDate;
    }

    public LocalDate interviewDateToDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(interviewDate,formatter);
        return localDate;
    }
}
