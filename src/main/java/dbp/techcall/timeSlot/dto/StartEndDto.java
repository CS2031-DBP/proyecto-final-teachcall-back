package dbp.techcall.timeSlot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class StartEndDto {
    private Date timeStart;
    private Date timeEnd;
}
