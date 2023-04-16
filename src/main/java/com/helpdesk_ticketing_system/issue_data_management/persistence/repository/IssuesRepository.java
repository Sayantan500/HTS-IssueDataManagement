package com.helpdesk_ticketing_system.issue_data_management.persistence.repository;

import com.helpdesk_ticketing_system.issue_data_management.entities.Issue;
import com.helpdesk_ticketing_system.issue_data_management.entities.Page;
import com.helpdesk_ticketing_system.issue_data_management.entities.Status;
import com.helpdesk_ticketing_system.issue_data_management.persistence.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Repository
class IssuesRepository implements IssuesDao{
    private final Database<Issue> db;

    @Value("${issue.id.num_of_digits_to_extract.from.posted_on}")
    private String num_of_digits_to_extract_from_postedOn;

    @Value("${issue.id.random_number_range}")
    private String randomNumberRangeStr;

    @Value("${issue.id.prefix}")
    private String ISSUE_ID_PREFIX;

    @Autowired
    private Random random;

    @Autowired
    private Calendar calendar;
    @Autowired
    public IssuesRepository(Database<Issue> db) {
        this.db = db;
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

    private String generateIssueId(Long postedOn) {
        calendar.setTimeInMillis(postedOn);

        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        int numOfDigitsToExtractFromBack = Integer.parseInt(num_of_digits_to_extract_from_postedOn);
        int randomNumberRange = Integer.parseInt(randomNumberRangeStr);

        int extractedDigitsOfPostedOn = (int) (postedOn%Math.pow(10,numOfDigitsToExtractFromBack));
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
