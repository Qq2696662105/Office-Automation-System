package com.dataKing.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dataKing.model.wechat.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ClassName: MenuMapper
 * Package: com.dataKing.wechat.controller.mapper
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/21 0021 16:29
 * @Version 1.0
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
}
