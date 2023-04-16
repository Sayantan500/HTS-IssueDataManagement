package com.helpdesk_ticketing_system.issue_data_management.apis;

import com.helpdesk_ticketing_system.issue_data_management.entities.Issue;
import com.helpdesk_ticketing_system.issue_data_management.entities.Response;
import com.helpdesk_ticketing_system.issue_data_management.exceptions.PostRequestBodyInvalid;
import com.helpdesk_ticketing_system.issue_data_management.persistence.repository.IssuesDao;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issues")
public class ControllerAPI {

    private final IssuesDao issueRepository;

    @Autowired
    public ControllerAPI(IssuesDao issueRepository) {
        this.issueRepository = issueRepository;
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
