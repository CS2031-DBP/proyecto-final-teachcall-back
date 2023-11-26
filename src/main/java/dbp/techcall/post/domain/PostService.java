package dbp.techcall.post.domain;

import com.amazonaws.AmazonServiceException;
import dbp.techcall.post.dto.BasicPost;
import dbp.techcall.post.dto.MyPostsResponse;
import dbp.techcall.post.dto.PostInfo;
import dbp.techcall.post.dto.PostInfoResponse;
import dbp.techcall.post.infrastructure.PostRepository;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.dto.ProfessorNames;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import dbp.techcall.s3.StorageService;
import dbp.techcall.student.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StorageService storageService;

    public Page<PostInfoResponse> getAllPostWithPagination(Pageable page, Student student) {
        Page<Post> posts = postRepository.findAll(page);

        return posts.map(post -> {
            PostInfoResponse postInfoResponse = new PostInfoResponse();

            ProfessorNames professorNames = professorRepository.findProfessorNamesById(post.getProfessor().getId());

            String postMediaKey = post.getMediaKey();

            String mediaUrl = postMediaKey != null
                    ? storageService.generatePresignedUrl(postMediaKey)
                    : "";

            String extension = postMediaKey != null
                    ? postMediaKey.substring(postMediaKey.lastIndexOf(".") + 1)
                    : "";

            postInfoResponse.setFirstName(professorNames.getFirstName());
            postInfoResponse.setLastName(professorNames.getLastName());
            postInfoResponse.setId(post.getId());
            postInfoResponse.setTitle(post.getTitle());
            postInfoResponse.setMediaUrl(mediaUrl);
            postInfoResponse.setMediaExtension(extension);
            postInfoResponse.setBody(post.getBody());
            postInfoResponse.setCreatedAt(post.getCreatedAt());
            postInfoResponse.setLiked(student.getLikedPosts().contains(post));
            postInfoResponse.setLikesQ(post.getLikes().size());
            return postInfoResponse;
        });
    }

    public Page<MyPostsResponse> getCurrentUserPost(Professor professor) {
        Pageable page = PageRequest.of(0, 10);
        Page<PostInfo> post = postRepository.getCurrentUserPostWithPagination(professor.getId(), page);

        List<MyPostsResponse> res = new ArrayList<>();

        for (PostInfo p : post.getContent()) {
            MyPostsResponse myPostsResponse = new MyPostsResponse();

            String postMediaKey = p.getMediaUrl();

            String mediaUrl = postMediaKey != null
                    ? storageService.generatePresignedUrl(p.getMediaUrl())
                    : "";
            String extension = postMediaKey != null
                    ? postMediaKey.substring(postMediaKey.lastIndexOf(".") + 1)
                    : "";

            myPostsResponse.setId(p.getId().intValue());
            myPostsResponse.setTitle(p.getTitle());
            myPostsResponse.setBody(p.getBody());
            myPostsResponse.setLikes(p.getLikes().intValue());
            myPostsResponse.setCreatedAt(p.getCreatedAt());
            myPostsResponse.setMediaUrl(mediaUrl);
            myPostsResponse.setMediaExtension(extension);
            res.add(myPostsResponse);
        }

        return new PageImpl<>(res, page, post.getTotalElements());


    }

    public void createPost(BasicPost newPost, Professor professor) {

        Post post = new Post();
        post.setTitle(newPost.getTitle());
        post.setBody(newPost.getBody());
        post.setCreatedAt(LocalDateTime.now());
        post.setProfessor(professor);

        if (newPost.getFile() != null) {
            MultipartFile file = newPost.getFile();
            String objectKey = professor.getId() + "/posts/" + file.getOriginalFilename();
            try {
                storageService.uploadFile(file, objectKey);
                post.setMediaKey(objectKey);
            } catch (Exception e) {
                throw new AmazonServiceException("Error uploading file", e);
            }
        }

        postRepository.save(post);

    }
}
