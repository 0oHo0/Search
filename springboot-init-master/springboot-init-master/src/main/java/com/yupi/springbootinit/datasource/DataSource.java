package com.yupi.springbootinit.datasource;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface DataSource {
    List<T> dosearch(String searchText,long pageNum,long pageSize);
}
