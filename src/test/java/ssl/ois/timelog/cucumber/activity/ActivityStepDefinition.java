package ssl.ois.timelog.cucumber.activity;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import ssl.ois.timelog.adapter.repository.memory.MemoryActivityTypeListRepository;
import ssl.ois.timelog.adapter.repository.memory.MemoryUserRepository;
import ssl.ois.timelog.model.activity.type.ActivityType;
import ssl.ois.timelog.model.activity.type.ActivityTypeList;
import ssl.ois.timelog.model.user.User;
import ssl.ois.timelog.service.activity.type.add.AddActivityTypeUseCase;
import ssl.ois.timelog.service.activity.type.add.AddActivityTypeUseCaseInput;
import ssl.ois.timelog.service.activity.type.add.AddActivityTypeUseCaseOutput;
import ssl.ois.timelog.service.activity.type.edit.EditActivityTypeUseCase;
import ssl.ois.timelog.service.activity.type.edit.EditActivityTypeUseCaseInput;
import ssl.ois.timelog.service.activity.type.edit.EditActivityTypeUseCaseOutput;
import ssl.ois.timelog.service.activity.type.remove.RemoveActivityTypeUseCase;
import ssl.ois.timelog.service.activity.type.remove.RemoveActivityTypeUseCaseInput;
import ssl.ois.timelog.service.activity.type.remove.RemoveActivityTypeUseCaseOutput;
import ssl.ois.timelog.service.repository.ActivityTypeListRepository;
import ssl.ois.timelog.service.repository.UserRepository;
import ssl.ois.timelog.service.user.dto.UserDTOConverter;
import io.cucumber.java.en.Then;

public class ActivityStepDefinition {
    
    private UserRepository userRepository;
    private ActivityTypeListRepository activityTypeListRepository;
    private String userID;
    private String activityTypeName;
    private Boolean errorOccurred;


    @Before
    public void setup() {
        this.userRepository = new MemoryUserRepository();
        this.activityTypeListRepository = new MemoryActivityTypeListRepository();

        this.errorOccurred = false;
    }

    @Given("[Activity] I log in to Timelog with user ID {string}")
    public void activity_i_log_in_to_Timelog_with_user_ID(String userID) {
        if (this.userRepository.findByUserID(userID) == null) {
            this.userRepository.save(UserDTOConverter.toDTO(new User(UUID.fromString(userID))));

            ActivityTypeList activityTypeList = new ActivityTypeList(userID);
            activityTypeList.newType("Others");
            this.activityTypeListRepository.save(activityTypeList);
        }

        this.userID = userID;
    }

    @Given("I have {string} course in this semester")
    public void i_have_course_in_this_semester(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    @When("I add it to my activity type list")
    public void i_add_it_to_my_activity_type_list() {
        AddActivityTypeUseCase addActivityTypeUseCase = new AddActivityTypeUseCase(this.activityTypeListRepository);
        AddActivityTypeUseCaseInput addActivityTypeUseCaseInput = new AddActivityTypeUseCaseInput();
        AddActivityTypeUseCaseOutput addActivityTypeUseCaseOutput = new AddActivityTypeUseCaseOutput();

        addActivityTypeUseCaseInput.setUserID(this.userID);
        addActivityTypeUseCaseInput.setActivityTypeName(this.activityTypeName);

        addActivityTypeUseCase.execute(addActivityTypeUseCaseInput, addActivityTypeUseCaseOutput);
    }

    @Then("{string} is in my activity type list")
    public void is_in_my_activity_type_list(String activityTypeName) {
        Boolean found = false;
        for(ActivityType activityType : this.activityTypeListRepository.findByUserID(this.userID).getTypeList()) {
            if(activityType.getName().equals(activityTypeName)) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Given("I have already had {string} in my activity type list")
    public void i_have_already_had_in_my_activity_type_lis(String activityTypeName) {
        AddActivityTypeUseCase addActivityTypeUseCase = new AddActivityTypeUseCase(this.activityTypeListRepository);
        AddActivityTypeUseCaseInput addActivityTypeUseCaseInput = new AddActivityTypeUseCaseInput();
        AddActivityTypeUseCaseOutput addActivityTypeUseCaseOutput = new AddActivityTypeUseCaseOutput();

        addActivityTypeUseCaseInput.setUserID(this.userID);
        addActivityTypeUseCaseInput.setActivityTypeName(activityTypeName);

        addActivityTypeUseCase.execute(addActivityTypeUseCaseInput, addActivityTypeUseCaseOutput);

        this.activityTypeName = addActivityTypeUseCaseOutput.getActivityTypeName();
    }

    @When("I add an activity type with same name")
    public void i_add_an_activity_type_with_same_name() {
        AddActivityTypeUseCase addActivityTypeUseCase = new AddActivityTypeUseCase(this.activityTypeListRepository);
        AddActivityTypeUseCaseInput addActivityTypeUseCaseInput = new AddActivityTypeUseCaseInput();
        AddActivityTypeUseCaseOutput addActivityTypeUseCaseOutput = new AddActivityTypeUseCaseOutput();

        addActivityTypeUseCaseInput.setUserID(this.userID);
        addActivityTypeUseCaseInput.setActivityTypeName(this.activityTypeName);

        try {
            addActivityTypeUseCase.execute(addActivityTypeUseCaseInput, addActivityTypeUseCaseOutput);
        } catch (RuntimeException e) {
            this.errorOccurred = true;
        }
    }

    @Then("Timelog should reject this command")
    public void timelog_should_reject_this_command() {
        assertTrue(this.errorOccurred);
    }

    @When("I remove it from my activity type list")
    public void i_remove_it_from_my_activity_type_list() {
        RemoveActivityTypeUseCase removeActivityTypeUseCase = new RemoveActivityTypeUseCase(this.activityTypeListRepository);
        RemoveActivityTypeUseCaseInput removeActivityTypeUseCaseInput = new RemoveActivityTypeUseCaseInput();
        RemoveActivityTypeUseCaseOutput removeActivityTypeUseCaseOutput = new RemoveActivityTypeUseCaseOutput();
        
        removeActivityTypeUseCaseInput.setActivityTypeName(this.activityTypeName);
        removeActivityTypeUseCaseInput.setUserID(this.userID);

        removeActivityTypeUseCase.execute(removeActivityTypeUseCaseInput, removeActivityTypeUseCaseOutput);
    }

    @Then("{string} is not in my activity type list")
    public void is_not_in_my_activity_type_list(String activityTypeName) {
         for(ActivityType activityType : this.activityTypeListRepository.findByUserID(this.userID).getTypeList()) {
             if(activityType.getName().equals(activityTypeName)) {
                 fail("Activity Type is not removed from the repository");
             }
         }
    }

    @When("I change the activity name to {string} and set its state to disabled and private")
    public void i_change_the_activity_name_to_and_set_its_state_to_disabled_and_private(String activityTypeName) {
        EditActivityTypeUseCase editActivityTypeUseCase = new EditActivityTypeUseCase(this.activityTypeListRepository);
        EditActivityTypeUseCaseInput editActivityTypeUseCaseInput = new EditActivityTypeUseCaseInput();
        EditActivityTypeUseCaseOutput editActivityTypeUseCaseOutput = new EditActivityTypeUseCaseOutput();

        editActivityTypeUseCaseInput.setActivtiyTypeName(activityTypeName);
        editActivityTypeUseCaseInput.setIsEnable(false);
        editActivityTypeUseCaseInput.setIsPrivate(true);

        editActivityTypeUseCase.execute(editActivityTypeUseCaseInput, editActivityTypeUseCaseOutput);
    }

    @Then("The activity type {string} should change its name to {string} and become disabled and private")
    public void the_activity_type_should_change_its_name_to(String oldActivityTypeName, String newActivityTypeName) {
        Boolean oldFound = false;
        Boolean newFound = false;
        for (ActivityType activityType: this.activityTypeListRepository.findByUserID(this.userID).getTypeList()) {
            if (activityType.getName().equals(oldActivityTypeName)) {
                oldFound = true;
            }
            if (activityType.getName().equals(newActivityTypeName) &&
                activityType.isEnable() == false &&
                activityType.isPrivate() == true) {
                    newFound = true;
            }
        }
        
        assertFalse(oldFound);
        assertTrue(newFound);
    }

    @Then("the activity type is disabled and private")
    public void the_activity_type_is_disabled_and_private() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}