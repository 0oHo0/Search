package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.service.PictureService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
@Component
public class PictureDataSource implements DataSource<Picture>{
    @Resource
    PictureService pictureService;

    @Override
    public List<Picture> dosearch(String searchText, long pageNum, long pageSize) throws IOException {
        Page<Picture> picturePage =pictureService.searchPicture(searchText, pageNum, pageSize);
        return picturePage.getRecords();
    }
}
