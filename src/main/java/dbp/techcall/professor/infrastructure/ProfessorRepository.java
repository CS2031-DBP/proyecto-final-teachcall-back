package dbp.techcall.professor.infrastructure;

import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.dto.LastExperienceDto;
import dbp.techcall.professor.dto.ProfessorNames;
import dbp.techcall.user.infrastructure.BaseUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends BaseUserRepository<Professor> {
    Professor findByEmail(String email);

    @Query(value =
            """
                    SELECT p.id as id, p.price_per_hour as pricePerHour ,p.first_name as firstName , p.last_name as lastName, p.description as description, COALESCE(AVG(r.rating), 0) AS rating, count(r.id) as reviewCount\s
                    FROM professor p\s
                    LEFT JOIN review r\s
                    ON p.id = r.professor_id
                    GROUP BY p.first_name, p.last_name, p.description, p.id\s
                    ORDER BY COALESCE(AVG(r.rating), 0) DESC\s
                    """
            , nativeQuery = true)
    Page<BasicProfessorResponse> findAllProfessorsWithPagination(Pageable pageable);

    @Query(value = "SELECT p.id as id, p.first_name as firstName, p.last_name as lastName, " +
            "p.price_per_hour as pricePerHour, p.description as description, COALESCE(AVG(r.rating), 0) AS rating, " +
            "COUNT(r.id) as reviewCount \n" +
            "FROM professor p " +
            "LEFT JOIN review r ON p.id = r.professor_id " +
            "LEFT JOIN professor_category pc ON p.id = pc.professor_id " +
            "WHERE pc.category_id = (SELECT id FROM category " +
            "WHERE TRANSLATE(LOWER(title), 'áéíóú', 'aeiou') LIKE " +
            "TRANSLATE(LOWER(:category), 'áéíóú', 'aeiou') LIMIT 1) " +
            "GROUP BY p.first_name, p.last_name, p.description, p.id " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC NULLS LAST", nativeQuery = true)
    Page<BasicProfessorResponse> findAllProfessorsByCategoryWithPagination(Pageable pageable, @Param("category") String category);

    @Override
    Optional<Professor> findById(Long professorId);

    @Query(value = "SELECT COALESCE(AVG(r.rating), 0) AS rating FROM professor p LEFT JOIN review r ON p.id = r.professor_id WHERE p.email = :email", nativeQuery = true)
    Double getRating(@Param("email") String email);

    @Query(value =
            "select dg.firstName as firstName, dg.lastName as lastName, dg.degree as degree, dg.name as schoolName, we.employer as Employwe, we.title as position\n" +
                    "from work_experience as we\n" +
                    "         right join\n" +
                    "     (select p.first_name as firstName, p.last_name as lastName, degree.professor_id, degree.degree, sc.name\n" +
                    "      from professor as p,\n" +
                    "           (select professor_id, degree, school_id\n" +
                    "            from education\n" +
                    "            where professor_id = :id\n" +
                    "            order by start_date desc\n" +
                    "            limit 1) as degree,\n" +
                    "           (select id, name from school) as sc\n" +
                    "      where p.id = degree.professor_id\n" +
                    "        and degree.school_id = sc.id) as dg\n" +
                    "     on we.professor_id = dg.professor_id;",
            nativeQuery = true)
    LastExperienceDto getExperience(@Param("id") Long id);

    ProfessorNames findProfessorNamesById(Long id);

    @Query(value = """
            SELECT p.id as id, p.first_name as firstName, p.last_name as lastName,
            p.price_per_hour as pricePerHour, p.description as description, COALESCE(AVG(r.rating), 0) AS rating, 
            COUNT(r.id) as reviewCount 
            FROM professor p 
            LEFT JOIN review r ON p.id = r.professor_id 
            WHERE p.id = :id
            GROUP BY p.id 
            """
            , nativeQuery = true)
    BasicProfessorResponse findProfessor(@Param("id") Long id);

    void deleteById(Long id);
    void deleteByEmail(String email);

}
