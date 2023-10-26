package dbp.techcall.professor.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class NewProfessorDto {
    String firstName;
    String lastName;
    String email;
    String password;
}
