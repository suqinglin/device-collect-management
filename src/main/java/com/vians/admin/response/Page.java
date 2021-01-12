package com.vians.admin.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据
 * Created by wangkun23 on 2018/12/19.
 *
 * @param <T>
 */
public class Page<T> implements Serializable {
    /**
     * 内容
     */
    private final List<T> list = new ArrayList<T>();
    /**
     * 总记录数
     */
    private final long total;

    /**
     * 分页信息
     */
    private final Pageable pageable;

    /**
     * 初始化一个新创建的Page对象
     */
    public Page() {
        this.total = 0L;
        this.pageable = new Pageable();
    }

    /**
     * @param list     内容
     * @param total    总记录数
     * @param pageable 分页信息
     */
    public Page(List<T> list, long total, Pageable pageable) {
        this.list.addAll(list);
        this.total = total;
        this.pageable = pageable;
    }

    /**
     * 获取页码
     *
     * @return 页码
     */
    public int getPageNumber() {
        return pageable.getPageNumber();
    }

    /**
     * 获取每页记录数
     *
     * @return 每页记录数
     */
    public int getPageSize() {
        return pageable.getPageSize();
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public int getTotalPages() {
        return (int) Math.ceil((double) getTotal() / (double) getPageSize());
    }

    /**
     * 获取内容
     *
     * @return 内容
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    public long getTotal() {
        return total;
    }
}
