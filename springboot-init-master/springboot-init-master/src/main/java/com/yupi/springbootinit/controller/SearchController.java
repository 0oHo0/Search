package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.datasource.DataSource;
import com.yupi.springbootinit.datasource.PictureDataSource;
import com.yupi.springbootinit.datasource.PostDataSource;
import com.yupi.springbootinit.datasource.UserDataSource;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.dto.Search.SearchQueryRequest;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.enums.SearchEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private ElasticsearchRestTemplate elasticSearchRestTemplate;

    @PostMapping("/all")
    public SearchVO searchAll(@RequestBody SearchQueryRequest searchQueryRequest,
                               HttpServletRequest request) throws IOException {

        String type = searchQueryRequest.getType();
        SearchEnum searchEnum = SearchEnum.getEnumByValue(type);
        String searchText = searchQueryRequest.getSearchText();
        long current = searchQueryRequest.getCurrent();
        long pageSize = searchQueryRequest.getPageSize();
        SearchVO searchVO = new SearchVO();
        if(searchEnum==null) {
            CompletableFuture<List<UserVO>> userTask = CompletableFuture.supplyAsync(()->{
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                List<UserVO> userVOPage = userDataSource.dosearch(searchText,current,pageSize);
                return userVOPage;
            });
            CompletableFuture<List<PostVO>> postTask = CompletableFuture.supplyAsync(()->{
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                List<PostVO> postVOPage = postDataSource.dosearch(searchText,current,pageSize);
                return postVOPage;
            });
            CompletableFuture<List<Picture>> pictureTask = CompletableFuture.supplyAsync(()->{
                List<Picture> pictureList = null;
                try {
                    pictureList = pictureDataSource.dosearch(searchText,current,pageSize);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return pictureList;
            });
            CompletableFuture.allOf(userTask,pictureTask,postTask).join();
            try {
                List<UserVO> userVOList = userTask.get();
                List<PostVO> postVOList = postTask.get();
                List<Picture> pictureList = pictureTask.get();
                searchVO.setPostList(postVOList);
                searchVO.setPictureList(pictureList);
                searchVO.setUserList(userVOList);
                return searchVO;
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
            }
        }else {
            Map<String,DataSource> typeDataSourceMap = new HashMap(){{
                put(searchEnum.POST.getValue(),postDataSource);
                put(searchEnum.PICTURE.getValue(),pictureDataSource);
                put(searchEnum.USER.getValue(),userDataSource);
            }};
            DataSource dataSource = typeDataSourceMap.get(type);
            List dataList = dataSource.dosearch(searchText, current, pageSize);
            searchVO.setDataList(dataList);
            return searchVO;
        }
    }
}