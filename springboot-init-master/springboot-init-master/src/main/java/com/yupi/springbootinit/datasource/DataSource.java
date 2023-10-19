package com.yupi.springbootinit.datasource;

import com.yupi.springbootinit.model.vo.SearchVO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
@Component
public interface DataSource<T> {
    List<T> dosearch(String searchText, long pageNum, long pageSize) throws IOException;
}
