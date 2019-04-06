package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加品类方法
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类错误");
        }

        // 声明一个分类对象
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        // 表明分类可用
        category.setStatus(true);

        // 插入数据
        int rowCount = categoryMapper.insert(category);

        // 判断商品是否添加成功
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    /**
     * 更新品类名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }

        // 新建一个category对象
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        // 选择性的更新 通过主键更新
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);

        if (rowCount > 0 ) {
            return ServerResponse.createBySuccessMessage("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildenByParentId(categoryId);

        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }

        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点id及孩子节点id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChilderById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     * 递归算法，算出子节点
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);

        if (category != null) {
            categorySet.add(category);
        }

        // 查找子节点，递归算法一定要有一个退出条件
        List<Category> categoryList = categoryMapper.selectCategoryChildenByParentId(categoryId);
        // mybatis没有查到数据不会返回null
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
     }

}
