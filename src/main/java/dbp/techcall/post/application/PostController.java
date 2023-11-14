package dbp.techcall.post.application;


import dbp.techcall.like.domain.Likes;
import dbp.techcall.like.infrastructure.LikeRepository;
import dbp.techcall.post.domain.Post;
import dbp.techcall.post.domain.PostService;
import dbp.techcall.post.infrastructure.PostRepository;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.student.domain.Student;
import dbp.techcall.student.domain.StudentService;
import dbp.techcall.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
//    @Autowired
    private PostService postService;
    private ProfessorService professorService;

    private StudentService studentService;

    private StudentRepository studentRepository;

    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;


    @GetMapping() // Feed
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    @GetMapping("/{professor_id}")
    public List<Post> getPostsByProfessor(@PathVariable("professor_id") Long professor_id){
        return postRepository.findByProfessorId(professor_id);
    }


    @GetMapping("/{id}")
    public Post getPostById(@PathVariable("id") Long id){
        return postRepository.findById(id).orElse(null);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody Post post) {
        // find the post
        Post existingPost = postRepository.findById(id).orElse(null);

        if (existingPost == null) {
            // Return 404 Not Found if the post is not found
            return ResponseEntity.notFound().build();
        }

        // find the user in session
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Professor professor = professorService.findByEmail(username);

        // check if the user is the author of the post
        if (existingPost.getProfessor().equals(professor)) {
            // update the post
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            existingPost.setUpdatedAt(post.getUpdatedAt());

            // save the post
            Post savedPost = postRepository.save(existingPost);

            // return ok
            return ResponseEntity.ok(savedPost);
        } else {
            // return forbidden
            return ResponseEntity.status(403).build();
        }
    }



    @PostMapping
    public Post createPost(@RequestBody Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        Professor professor = professorService.findByEmail(username);
        post.setProfessor(professor);

        Post savedPost = postRepository.save(post);

        return ResponseEntity.ok(savedPost).getBody();
    }

    @PostMapping("like/{post_id}")
    public Post create(@PathVariable Long post_id){
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Student student = studentRepository.findByEmail(username);
        //if no student:
//        throw new ResourceNotFoundException("Student not found");

        Long studentId = student.getId();

        Likes like = new Likes(post_id,studentId);
        likeRepository.save(like);

        return postRepository.findById(post_id).orElse(null);

    }

}


