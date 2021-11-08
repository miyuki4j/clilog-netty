package com.gow.clilog.log;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public interface Parentable {
    /**
     * 返回所属
     * 
     * @return
     */
    Parentable parent();

    /**
     * 设置所属
     * 
     * @param p
     */
    void parent(Parentable p);

    /**
     * 取消所属关系
     */
    void unParent();
}
