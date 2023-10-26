package dbp.techcall.category;


import dbp.techcall.course.Course;
import dbp.techcall.course.CourseDTO;
import dbp.techcall.professor.dto.NewProfessorDto;
import dbp.techcall.professor.professor.infrastructure.models.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<CategoryDto> getLimitSixCategories() {
        Pageable limitedPageable = PageRequest.of(0, 6);
        Page<Category> categoryPage = categoryRepository.findAll(limitedPageable);

        return categoryPage.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CategoryDto convertToDTO(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(Long.valueOf(category.getId()));
        dto.setTitle(category.getTitle());

        return dto;
    }

}
