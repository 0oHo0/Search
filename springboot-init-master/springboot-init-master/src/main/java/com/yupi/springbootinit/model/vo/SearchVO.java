package com.yupi.springbootinit.model.vo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */

@Data
public class SearchVO implements Serializable {


   private List<PostVO> postList;
   private List<Picture> pictureList;
   private List<UserVO> userList;
   private List<Object> dataList;
   private static final long serialVersionUID = 12345678L;
}
