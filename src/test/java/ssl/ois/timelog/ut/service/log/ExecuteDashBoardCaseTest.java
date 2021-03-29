package ssl.ois.timelog.ut.service.log;

import org.junit.Before;
import org.junit.Test;
import ssl.ois.timelog.adapter.repository.memory.MemoryLogRepository;
import ssl.ois.timelog.model.unit.Unit;
import ssl.ois.timelog.model.user.User;
import ssl.ois.timelog.service.log.LogDTO;
import ssl.ois.timelog.service.repository.log.LogRepository;
import ssl.ois.timelog.service.exception.log.SaveLogErrorException;
import ssl.ois.timelog.service.exception.DatabaseErrorException;
import ssl.ois.timelog.service.log.add.*;
import ssl.ois.timelog.service.log.history.*;
import ssl.ois.timelog.adapter.presenter.log.history.LogHistoryPresenter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExecuteDashBoardCaseTest {
    private LogRepository logRepository;
    private String logID;
    private HistoryLogUseCaseOutput output;
    private String logTitle = "Study for Design Pattern";
    private String startTime = "2020/04/21 15:00";
    private String endTime = "2020/04/21 18:00";
    private String activityTypeName = "DP";



    @Before
    public void setup() {
        User user = new User(UUID.randomUUID());
        this.logRepository = new MemoryLogRepository();
        String description = "Composite Pattern";

        AddLogUseCaseInput inputData = new AddLogUseCaseInput(user.getID().toString(), user.getID().toString(), this.logTitle, this.startTime, this.endTime,
                description, this.activityTypeName);
        AddLogUseCaseOutput outputData = new AddLogUseCaseOutput();
        AddLogUseCase addLogUseCase = new AddLogUseCase(this.logRepository);
        try {
            addLogUseCase.execute(inputData, outputData);
        } catch (SaveLogErrorException | DatabaseErrorException e) {
            fail(e.getMessage());
        }
        this.logID = outputData.getLogID();
    }

    @Test
    public void executeDashBoard() {
        HistoryLogUseCaseInput input = new HistoryLogUseCaseInput();
        this.output =  new LogHistoryPresenter();
        HistoryLogUseCase service = new HistoryLogUseCase(this.logRepository);
        List<String> filterList = new ArrayList<>();
        filterList.add(this.activityTypeName);

        input.setStartDate(this.startTime);
        input.setEndDate(this.endTime);
        input.setFilterList(filterList);

        try {
            service.execute(input, this.output);
        } catch (ParseException | DatabaseErrorException e) {
            fail(e.getMessage());
        }

        for (LogDTO log: output.getLogDTOList()) {
            assertEquals(this.logID, log.getId().toString());
            assertEquals(this.logTitle, log.getTitle());
            assertEquals(this.startTime, log.getStartTime());
            assertEquals(this.endTime, log.getEndTime());
            assertEquals(this.activityTypeName, log.getActivityTypeName());
        }
    }
}