package com.kitsune.eduorder.mapper;

import com.kitsune.eduorder.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-21
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
