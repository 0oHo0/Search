package com.yupi.springbootinit.datasource;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class PostDataSource implements DataSource{
    @Override
    public List<T> dosearch(String searchText, long pageNum, long pageSize) {
        return null;
    }
}
