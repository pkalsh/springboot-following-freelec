package kr.co.springboot_following.service.posts;

import kr.co.springboot_following.domain.posts.Posts;
import kr.co.springboot_following.domain.posts.PostsRepository;
import kr.co.springboot_following.web.dto.PostsListResponseDto;
import kr.co.springboot_following.web.dto.PostsResponseDto;
import kr.co.springboot_following.web.dto.PostsSaveRequestDto;
import kr.co.springboot_following.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
 * 의존성 주입을 위한 @Autowired 어노테이션이 없는 이유는
 * 스프링에서 의존성을 주입하는 방식이 @Autowired, setter, 생성자 이러한 3가지가 존재하는데
 * 생성자를 권장한다. 이때 @RequiredArgsConstructor 어노테이션이 이미 기본 생성자를 자동으로 생성해줌으로써
 * 코드의 변경이 있을 때마다 생성자를 변경해야하는 수고를 덜 수 있어서 아주 편리하다.
 *
 **** 이 코드에서는 postsRepository 에 의존성이 주입되고 있다.
 */

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById (Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        
        postsRepository.delete(posts);
    }
}
