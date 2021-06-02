package com.jinunn.mall.member.dao;

import com.jinunn.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:27:51
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
