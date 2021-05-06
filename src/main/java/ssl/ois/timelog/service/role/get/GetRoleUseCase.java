package ssl.ois.timelog.service.role.get;
import ssl.ois.timelog.service.exception.role.GetRoleErrorException;
import ssl.ois.timelog.service.repository.user.UnitRepository;
import ssl.ois.timelog.model.team.Role;

public class GetRoleUseCase {
    private UnitRepository unitRepository;

    public GetRoleUseCase(UnitRepository unitRepository){
        this.unitRepository = unitRepository;
    }

    public void execute(GetRoleUseCaseInput input, GetRoleUseCaseOutput output) throws GetRoleErrorException{
        Role role = this.unitRepository.getRole(input.getUserID(), input.getTeamID());
        output.setRole(role);
    }
}