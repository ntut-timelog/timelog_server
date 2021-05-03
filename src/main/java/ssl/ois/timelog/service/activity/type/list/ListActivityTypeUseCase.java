package ssl.ois.timelog.service.activity.type.list;

import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import ssl.ois.timelog.model.connect.UnitInterface;
import ssl.ois.timelog.model.unit.Unit;
import ssl.ois.timelog.model.user.User;
import ssl.ois.timelog.service.exception.DatabaseErrorException;
import ssl.ois.timelog.service.repository.user.UserRepository;

import java.util.List;

@Service
public class ListActivityTypeUseCase {

    private UserRepository userRepository;

    public ListActivityTypeUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(ListActivityTypeUseCaseInput input, ListActivityTypeUseCaseOutput output)
            throws DatabaseErrorException {

        final String urlName = "http://localhost:8080/team/get/name";


        for(int i = 0 ; i < input.getUnitIdList().size(); i++){
            UnitInterface user = this.userRepository.findByUserID(input.getUnitIdList().get(i));
            RestTemplate restTemplate = new RestTemplate();
            String userName = restTemplate.postForObject(urlName, input.getUnitIdList().get(i), String.class);
            userName = userName.replaceAll("\"","");

            output.addUnitDTOtoList(user.getID().toString(), userName, user.getActivityTypeList());
        }
    }
}