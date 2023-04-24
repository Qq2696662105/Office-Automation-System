package com.dataKing.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataKing.model.process.Process;
import com.dataKing.vo.process.ProcessQueryVo;
import com.dataKing.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * ClassName: OaProcessMapper
 * Package: com.dataKing.process.mapper
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 16:27
 * @Version 1.0
 */
@Repository
public interface OaProcessMapper extends BaseMapper<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> processPage,
                                @Param("vo") ProcessQueryVo processQueryVo);
}
