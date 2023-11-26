package dbp.techcall.post.application;


import dbp.techcall.post.domain.Post;
import dbp.techcall.post.domain.PostService;
import dbp.techcall.post.dto.BasicPost;
import dbp.techcall.post.dto.PostInfo;
import dbp.techcall.post.dto.PostInfoResponse;
import dbp.techcall.post.infrastructure.PostRepository;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import dbp.techcall.student.domain.Student;
import dbp.techcall.student.domain.StudentService;
import dbp.techcall.student.repository.StudentRepository;
import dbp.techcall.user.domain.Users;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<PostInfoResponse> getAllPosts() {

        List<Post> post = postRepository.findAll();
        return post.stream()
                .map(currentPost -> modelMapper.map(currentPost, PostInfoResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/professor/{professor_id}")
    public ResponseEntity<Page<PostInfo>> getPostsByProfessor(@PathVariable("professor_id") Long professor_id) {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        return ResponseEntity.ok(postRepository.getCurrentUserPostWithPagination(professor_id, pageable));
    }

    @GetMapping("/myposts")
    public ResponseEntity<Page<PostInfo>> getCurrentUserPost() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isProfessor = authorities.stream().anyMatch(r -> r.getAuthority().equals("ROLE_teacher"));
        if (!isProfessor) {
            throw new IllegalArgumentException("You are not a professor");
        }

        String username = userDetails.getUsername();
        Professor professor = professorService.findByEmail(username);

        return ResponseEntity.ok(postService.getCurrentUserPost(professor));
    }

    @GetMapping("/{id}")
    public BasicPost getPostById(@PathVariable("id") Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        return modelMapper.map(post, BasicPost.class);
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody BasicPost post) {
//        // find the post
//        Post existingPost = postRepository.findById(id).orElse(null);
//
//        if (existingPost == null) {
//            // Return 404 Not Found if the post is not found
//            return ResponseEntity.notFound().build();
//        }
//
//        // find the user in session
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String username = userDetails.getUsername();
//        Professor professor = professorService.findByEmail(username);
//
//        // check if the user is the author of the post
//        if (existingPost.getProfessor().equals(professor)) {
//            // update the post
//            existingPost.setTitle(post.getTitle());
//            existingPost.setBody(post.getBody());
//            existingPost.setUpdatedAt(LocalDateTime.now());
//            // save the post
//            Post savedPost = postRepository.save(existingPost);
//
//            // return ok
//            return ResponseEntity.ok(savedPost);
//        } else {
//            // return forbidden
//            return ResponseEntity.status(403).build();
//        }
//    }

    @GetMapping("/feed")
    public ResponseEntity<Page<PostInfoResponse>> getAllPostWithPagination(@RequestParam("page") int page, @RequestParam("size") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Student student = studentRepository.findByEmail(username);

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<PostInfoResponse> posts = postService.getAllPostWithEPagination(pageable, student);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody BasicPost post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Professor professor = professorService.findByEmail(username);

        if (professor == null) {
            throw new ResourceNotFoundException("You are not a professor or not logged in");
        }

        Post newPost = new Post();
        LocalDateTime now = LocalDateTime.now();

        newPost.setTitle(post.getTitle());
        newPost.setBody(post.getBody());
        newPost.setProfessor(professor);
        newPost.setCreatedAt(now);

        postRepository.save(newPost);

        return ResponseEntity.ok("success");
    }

    @PostMapping("/like/{post_id}")
    public ResponseEntity<String> createLike(@PathVariable Long post_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        System.out.println("username: " + username);

        Student student = studentRepository.findByEmail(username);

        Post post = postRepository.findById(post_id).orElse(null);

        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        student.getLikedPosts().add(post);
        studentRepository.save(student);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/like/{post_id}")
    public ResponseEntity<String> deleteLike(@PathVariable Long post_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Student student = studentRepository.findByEmail(username);

        Post post = postRepository.findById(post_id).orElse(null);

        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        student.getLikedPosts().remove(post);
        studentRepository.save(student);
        return ResponseEntity.ok("success");
    }


    @PatchMapping("/{post_id}")
    public ResponseEntity<String> updatePost(@PathVariable Long post_id, @RequestBody BasicPost post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isProfessor = authorities.stream().anyMatch(r -> r.getAuthority().equals("ROLE_teacher"));
        if (!isProfessor) {
            throw new IllegalArgumentException("You are not a professor");
        }

        Post existingPost = postRepository.findById(post_id).orElse(null);

        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }

        existingPost.setTitle(!post.getTitle().isEmpty() ? post.getTitle() : existingPost.getTitle());
        existingPost.setBody(!post.getBody().isEmpty() ? post.getBody() : existingPost.getBody());
        existingPost.setUpdatedAt(LocalDateTime.now());

        postRepository.save(existingPost);

        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable Long post_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isProfessor = authorities.stream().anyMatch(r -> r.getAuthority().equals("ROLE_teacher"));
        if (!isProfessor) {
            throw new IllegalArgumentException("You are not a professor");
        }

        Post existingPost = postRepository.findById(post_id).orElse(null);

        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }

        postRepository.delete(existingPost);

        return ResponseEntity.ok("success");
    }



}


