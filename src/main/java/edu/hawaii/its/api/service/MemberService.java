package edu.hawaii.its.api.service;

import edu.hawaii.its.api.type.GroupType;
import edu.hawaii.its.api.wrapper.HasMemberResult;
import edu.hawaii.its.api.wrapper.HasMembersResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Value("${groupings.api.grouping_admins}")
    private String GROUPING_ADMINS;

    @Value("${groupings.api.grouping_owners}")
    private String OWNERS_GROUP;

    @Autowired
    private GrouperApiService grouperApiService;

    public boolean isAdmin(String uhIdentifier) {
        return isMember(GROUPING_ADMINS, uhIdentifier);
    }

    public boolean isOwner(String uhIdentifier) {
        return isMember(OWNERS_GROUP, uhIdentifier);
    }

    public boolean isOwner(String groupingPath, String uhIdentifier) {
        return isMember(groupingPath + GroupType.OWNERS.value(), uhIdentifier);
    }

    public boolean isInclude(String groupingPath, String uhIdentifier) {
        return isMember(groupingPath + GroupType.INCLUDE.value(), uhIdentifier);
    }

    public boolean isExclude(String groupingPath, String uhIdentifier) {
        return isMember(groupingPath + GroupType.EXCLUDE.value(), uhIdentifier);
    }

    public boolean isMember(String groupPath, String uhIdentifier) {
        HasMembersResults hasMembersResults = grouperApiService.hasMemberResults(groupPath, uhIdentifier);
        List<HasMemberResult> results = hasMembersResults.getResults();
        if (results.isEmpty()) {
            return false;
        }
        return results.get(0).getResultCode().equals("IS_MEMBER");
    }
}
