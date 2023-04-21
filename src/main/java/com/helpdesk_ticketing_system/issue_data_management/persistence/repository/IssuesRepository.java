package com.helpdesk_ticketing_system.issue_data_management.persistence.repository;

import com.helpdesk_ticketing_system.issue_data_management.entities.Issue;
import com.helpdesk_ticketing_system.issue_data_management.entities.Page;
import com.helpdesk_ticketing_system.issue_data_management.entities.Status;
import com.helpdesk_ticketing_system.issue_data_management.persistence.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Repository
class IssuesRepository implements IssuesDao{
    private final Database<Issue> db;
    private final Integer num_of_digits_to_extract_from_postedOn;
    private final Integer randomNumberRange;
    private final String ISSUE_ID_PREFIX;

    @Autowired
    private Random random;

    @Autowired
    private Calendar calendar;
    @Autowired
    public IssuesRepository(
            @Qualifier("issueId.random_number_range") Integer randomNumberRange,
            @Qualifier("issueId.num_of_digits_to_extract.from.posted_on") Integer num_of_digits_to_extract_from_postedOn,
            @Qualifier("issueId.prefix") String ISSUE_ID_PREFIX,
            Database<Issue> db) {
        this.db = db;
        this.randomNumberRange = randomNumberRange;
        this.num_of_digits_to_extract_from_postedOn = num_of_digits_to_extract_from_postedOn;
        this.ISSUE_ID_PREFIX = ISSUE_ID_PREFIX;
    }

    @Override
    public String addNewIssue(Issue newIssue) throws Exception {
        // set the timestamp of saving the issue
        newIssue.setPostedOn(System.currentTimeMillis());

        // generate the id of the issue
        newIssue.set_id(generateIssueId(newIssue.getPostedOn()));

        // setting the status of the issue
        newIssue.setStatus(Status.DELIVERED.name());


        return db.saveToDb(newIssue);
    }

    @Override
    public Issue getIssueById(String issueId) throws Exception {
        return db.getIssueById(issueId,Issue.class);
    }

    @Override
    public List<Issue> getIssues(
            String username, Integer limit, Long postedOn, Page pageDirection) throws Exception {
        return db.getIssues(username,postedOn,limit,pageDirection, Issue.class);
    }

    @Override
    public Issue updateIssue(String issueId, Map<String, Object> updatedFieldValuePairs) throws Exception {
        return db.update(issueId,updatedFieldValuePairs, Issue.class);
    }

    @Override
    public List<Issue> getNewIssues(Status status, Integer limit, Long startRange, Long endRange) throws Exception {
        return db.getIssuesByStatus(status,startRange, endRange, limit, Issue.class);
    }

    private String generateIssueId(Long postedOn) {
        calendar.setTimeInMillis(postedOn);

        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        int extractedDigitsOfPostedOn = (int) (postedOn%Math.pow(10,num_of_digits_to_extract_from_postedOn));
        int randomNumberSuffix = random.nextInt(randomNumberRange);

        // pattern : ISS <yyyy> <mm> <dd> <last few digits of postedOn> <random integer>
        StringBuilder issueID = new StringBuilder(ISSUE_ID_PREFIX);
        issueID
                .append(year).append(month).append(date)
                .append(extractedDigitsOfPostedOn)
                .append(randomNumberSuffix);

        return issueID.toString();
    }
}
