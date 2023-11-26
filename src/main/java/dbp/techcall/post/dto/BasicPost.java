package dbp.techcall.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
public class BasicPost {
    private String title;
    private String body;
    private MultipartFile file;
}
