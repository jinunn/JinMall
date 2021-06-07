package com.jinunn.mall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.product.dao.CategoryDao;
import com.jinunn.mall.product.entity.CategoryEntity;
import com.jinunn.mall.product.service.CategoryService;


/**
 * @author jindun
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> getListWallTree() {
        List<CategoryEntity> entityList = baseMapper.selectList(null);
        return entityList.stream()
                //集合中的元素父分类的id等于0，说明就是一级分类
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                //调用递归方法，获取当前菜单的子菜单分类数据
                .peek(menu -> menu.setChildren(this.getChildren(menu,entityList)))
                //给当前菜单排序
                .sorted(Comparator.comparingInt(item -> (item.getSort() == null ? 0 : item.getSort())))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(Long id) {
        //TODO 菜单是否被引用
        baseMapper.deleteById(id);
    }

    @Override
    public Long[] getCateLogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        this.findParentPath(catelogId,paths);
        //因为迭代的数据的先从子id查询，所以是逆序的，需要转换过来。
        Collections.reverse(paths);
        return paths.toArray(new Long[0]);
    }

    /**
     * 递归调用，参数：当前商品id和统一收集的容器 225/22/2一步一步循环
     */
    private void findParentPath(Long catelogId,List<Long> paths){
        //1、每递归一次，收集一次当前的id
        paths.add(catelogId);
        //2、查询出当前分类的信息的父id
        Long parentCid = baseMapper.selectById(catelogId).getParentCid();
        //3、如果父id等于0，就继续递归调用。
        if (parentCid !=0){
            findParentPath(this.getById(catelogId).getParentCid(),paths);
        }
    }

    /**
     *  递归查询所有菜单的子菜单, 传递参数：查询出的当前菜单和所有菜单数据。
     */
    private List<CategoryEntity> getChildren(CategoryEntity root,List<CategoryEntity> all){
        return all.stream()
                //1、过滤取出：如果当前菜单id和父类id相等，说明就是子类，就直接返回
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                //2、当前菜单可能还会有子菜单，所以继续递归遍历查询
                .peek(menu->menu.setChildren(this.getChildren(menu,all)))
                //排序
                .sorted(Comparator.comparingInt(item -> (item.getSort() == null ? 0 : item.getSort())))
                .collect(Collectors.toList());
    }
}