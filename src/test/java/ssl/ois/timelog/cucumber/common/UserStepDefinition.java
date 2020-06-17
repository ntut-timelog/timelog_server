package ssl.ois.timelog.cucumber.common;

import java.util.UUID;

import io.cucumber.java.en.Given;
import ssl.ois.timelog.adapter.repository.memory.MemoryActivityTypeListRepository;
import ssl.ois.timelog.adapter.repository.memory.MemoryUserRepository;
import ssl.ois.timelog.model.activity.type.ActivityTypeList;
import ssl.ois.timelog.model.user.User;
import ssl.ois.timelog.service.repository.ActivityTypeListRepository;
import ssl.ois.timelog.service.repository.UserRepository;
import ssl.ois.timelog.service.user.dto.UserDTOConverter;

public class UserStepDefinition {
    private String userID;
    private UserRepository userRepository;
    private ActivityTypeListRepository activityTypeListRepository;

    public UserStepDefinition() {
        this.userRepository = new MemoryUserRepository();
        this.activityTypeListRepository = new MemoryActivityTypeListRepository();
    }
    
    @Given("I log in to Timelog with user ID {string}")
    public void i_log_in_to_Timelog_with_user_ID(String userID) {
        if (this.userRepository.findByUserID(userID) == null) {
            this.userRepository.save(UserDTOConverter.toDTO(new User(UUID.fromString(userID))));

            ActivityTypeList activityTypeList = new ActivityTypeList(userID);
            activityTypeList.newType("Others");
            this.activityTypeListRepository.save(activityTypeList);
        }

        this.userID = userID;
    }

    public UserRepository getUserRepository() {
        return this.userRepository;
    }

    public ActivityTypeListRepository getAcivityTypeListRepository() {
        System.out.println(this.activityTypeListRepository);
        return this.activityTypeListRepository;
    }

    public String getUserID() {
        return userID;
    }
}