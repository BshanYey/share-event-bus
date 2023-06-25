package cn.share.event.bus.server.application.converter;

import cn.share.event.bus.server.application.dto.ProductionNodeFlowDto;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeOutput;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionNodeConverter
 * @date 2023/6/23 20:06
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductionNodeConverter {
    ProductionNodeOutput flowDto2Pnil(ProductionNodeFlowDto nodeFlow);
}
