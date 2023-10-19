package com.yupi.springbootinit.esdao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.dto.post.PostEsDTO;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.service.PostService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import com.yupi.springbootinit.mapper.PostMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

/**
 * 帖子 ES 操作测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
public class PostEsDaoTest extends ServiceImpl<PostMapper, Post> {

    @Resource
    private PostEsDao postEsDao;

    @Resource
    private PostService postService;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Test
    void test() {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Post> page =
                postService.searchFromEs(postQueryRequest);
        System.out.println(page);
    }

    @Test
    void testSelect() {
        System.out.println(postEsDao.count());
        Page<PostEsDTO> PostPage = postEsDao.findAll(
                PageRequest.of(0, 5, Sort.by("createTime")));
        List<PostEsDTO> postList = PostPage.getContent();
        System.out.println(postList);
    }

    @Test
    void testAdd() {
        PostEsDTO postEsDTO = new PostEsDTO();
        postEsDTO.setId(1L);
        postEsDTO.setTitle("test");
        postEsDTO.setContent("test");
        postEsDTO.setTags(Arrays.asList("java", "python"));
        postEsDTO.setUserId(1L);
        postEsDTO.setCreatetime(new Date());
        postEsDTO.setUpdatetime(new Date());
        postEsDTO.setIsDelete(0);
        postEsDao.save(postEsDTO);
        System.out.println(postEsDTO.getId());
    }

    @Test
    void testFindById() {
        Optional<PostEsDTO> postEsDTO = postEsDao.findById(1L);
        System.out.println(postEsDTO);
    }

    @Test
    void testCount() {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete",0));
        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("title",""));
        boolQueryBuilder.should(QueryBuilders.matchQuery("title","小法师"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("content","小法师"));
        boolQueryBuilder.minimumShouldMatch(1);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .build();
        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
        System.out.println(searchHits);
       // System.out.println(postEsDao.count());
    }

    @Test
    void testFindByCategory() {
        List<PostEsDTO> postEsDaoTestList = postEsDao.findByUserId(1L);
        System.out.println(postEsDaoTestList);
    }

    @Test
    void  getByEs() {

        String searchText ="小法师";

        long current = 0;
        long pageSize = 5;
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete",0));
        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("title",""));
        boolQueryBuilder.should(QueryBuilders.matchQuery("title",searchText));
        boolQueryBuilder.should(QueryBuilders.matchQuery("content",searchText));
        boolQueryBuilder.minimumShouldMatch(1);
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest).build();
        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<PostVO> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<Post> postList=null;
        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
        if (searchHits.hasSearchHits()) {
            List<SearchHit<PostEsDTO>> searchHitList = searchHits.getSearchHits();
            List<Long> postIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
                    .collect(Collectors.toList());

          //  postList = postService.selectBatchIds(postIdList);
            postList = baseMapper.selectBatchIds(postIdList);
        }
        List<PostVO> postVOList = postList.stream().map(post -> PostVO.objToVo(post)).collect(Collectors.toList());
        page.setRecords(postVOList);
        System.out.println(page.getRecords());
        return ;
    }
}
