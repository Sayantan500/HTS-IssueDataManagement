package com.helpdesk_ticketing_system.issue_data_management.apis;

import com.helpdesk_ticketing_system.issue_data_management.entities.GetIssuesResponse;
import com.helpdesk_ticketing_system.issue_data_management.entities.Issue;
import com.helpdesk_ticketing_system.issue_data_management.entities.Response;
import com.helpdesk_ticketing_system.issue_data_management.entities.Status;
import com.helpdesk_ticketing_system.issue_data_management.exceptions.PostRequestBodyInvalid;
import com.helpdesk_ticketing_system.issue_data_management.persistence.repository.IssuesDao;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/issues")
public class ControllerAPI {

    private final IssuesDao issueRepository;
    private final Integer PAGINATION_LIMIT;

    @Autowired
    public ControllerAPI(
            IssuesDao issueRepository,
            @Qualifier("pagination-limit") Integer PAGINATION_LIMIT) {
        this.issueRepository = issueRepository;
        this.PAGINATION_LIMIT = PAGINATION_LIMIT;
    }

    @PostMapping
    public ResponseEntity<Object> saveNewIssueToDB(@RequestBody Issue newIssue){
        // Request body validation - submitted_by, subject and description must have value that is
        // not null and must not contain only whitespace.
        try{
            ValidateRequestBody(newIssue);
            String insertedIssueId = issueRepository.addNewIssue(newIssue);
            if (insertedIssueId!=null)
                return new ResponseEntity<>(
                        new Response(HttpStatus.OK.value(),insertedIssueId),
                        HttpStatus.OK
                );
            throw new Exception("Server returned null value for issue id.");
        }
        catch (PostRequestBodyInvalid e){
            return new ResponseEntity<>(
                    new Response(e.getStatus(),e.getMessage()),
                    HttpStatus.valueOf(e.getStatus())
            );
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getIssueByIdIssueIdPathParam(@PathVariable(name = "id") String issueId){
        try{
            Issue issue = issueRepository.getIssueById(issueId);
            if(issue!=null)
                return new ResponseEntity<>(issue, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(params = {"submitted_by","limit"})
    public ResponseEntity<Object> getIssues(
            @RequestParam(name = "submitted_by") String submittedBy,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "r_s",required = false) Long postedOnStartOfRange,
            @RequestParam(name = "r_e",required = false) Long postedOnEndOfRange
    ){
        try{
            // checking if the username is present and has valid value
            if(StringUtils.isEmpty(submittedBy) || StringUtils.isBlank(submittedBy))
                return new ResponseEntity<>(
                        new Response(HttpStatus.BAD_REQUEST.value(),"username not found in query params"),
                        HttpStatus.BAD_REQUEST
                );

            // cap the pagination limit to 50 number of records only.
            limit = Math.min(limit, PAGINATION_LIMIT);

            // setting default value of postedOn if contains null
            if(postedOnStartOfRange ==null)
                postedOnStartOfRange = System.currentTimeMillis();

            List<Issue> issues = issueRepository.getIssues(submittedBy,limit, postedOnStartOfRange,postedOnEndOfRange);
            GetIssuesResponse getIssuesResponse;
            if(issues.size()>0) {
                getIssuesResponse = new GetIssuesResponse(
                        issues.size(),
                        issues,
                        issues.get(0).getPostedOn(),
                        issues.get(issues.size() - 1).getPostedOn()
                );
            }
            else {
                getIssuesResponse = new GetIssuesResponse(
                        0, issues, null, null
                );
            }
            return new ResponseEntity<>(getIssuesResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(params = {"status","limit"})
    public ResponseEntity<Object> getNewIssues(
            @RequestParam(name = "status") String status,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "r_s",required = false) Long postedOnStartOfRange,
            @RequestParam(name = "r_e",required = false) Long postedOnEndOfRange
    ){
        if(postedOnStartOfRange==null) {
            postedOnStartOfRange = 0L;
        }

        // if query param contains value other than 'DELIVERED' (NOT case-sensitive)
        if(!Status.DELIVERED.name().equalsIgnoreCase(status) || limit<=0 || postedOnStartOfRange<0) {
            return new ResponseEntity<>(
                    new Response(
                            HttpStatus.BAD_REQUEST.value(),
                            "Invalid Query values."
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(limit>PAGINATION_LIMIT)
            limit = PAGINATION_LIMIT;

        // if query param contains expected value
        List<Issue> newIssuesList;
        try {
            newIssuesList =
                    issueRepository.getNewIssues(Status.DELIVERED,limit,postedOnStartOfRange,postedOnEndOfRange);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getClass().getName());
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int count = newIssuesList.size();
        if(count>0)
            return new ResponseEntity<>(
                    new GetIssuesResponse(
                            count,
                            newIssuesList,
                            newIssuesList.get(0).getPostedOn(),
                            newIssuesList.get(count-1).getPostedOn()
                    ),
                    HttpStatus.OK
            );
        return new ResponseEntity<>(newIssuesList,HttpStatus.OK);
    }

    private void ValidateRequestBody(Issue issue) throws PostRequestBodyInvalid {
        // submitted_by has username which must not contain any whitespace and must have some value
        String submittedBy = issue.getSubmitted_by();
        if(StringUtils.isBlank(submittedBy) || StringUtils.isEmpty(submittedBy) || submittedBy.contains(" "))
            throw new PostRequestBodyInvalid("Field 'submitted_by' is Blank, Empty or contains whitespace.");

        // subject and description must not contain only whitespaces or null
        String subject = issue.getSubject();
        if(StringUtils.isBlank(subject) || StringUtils.isEmpty(subject))
            throw new PostRequestBodyInvalid("Field 'subject' is Blank or Empty.");

        String description = issue.getDescription();
        if(StringUtils.isBlank(description) || StringUtils.isEmpty(description))
            throw new PostRequestBodyInvalid("Field 'description' is Blank or Empty.");
    }
}
