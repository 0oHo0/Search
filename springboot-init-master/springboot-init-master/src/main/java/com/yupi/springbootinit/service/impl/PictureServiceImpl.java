package com.yupi.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class PictureServiceImpl implements PictureService {


    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize){
        long current=(pageNum-1)*pageSize;
        String url = String.format("https://www.bing.com/images/search?q=%sfirst=%s",searchText,current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据获取异常");
        }
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element : elements) {
            String m = element.select(".iusc").attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            String tittle = element.select(".inflnk").attr("aria-label");
            Picture picture = new Picture();
            picture.setTitle(tittle);
            picture.setUrl(murl);
            pictureList.add(picture);
            if(pictureList.size()>=pageSize){
                break;
            }
        }
        Page<Picture>  picturePage = new Page<Picture>(pageNum,pageSize);
        picturePage.setRecords(pictureList);
        return picturePage;
    }
}
