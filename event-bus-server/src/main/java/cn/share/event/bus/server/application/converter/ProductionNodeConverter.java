package cn.share.event.bus.server.application.converter;

import cn.share.event.bus.server.application.dto.ProductionNodeFlowDto;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeOutput;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionNodeConverter
 * @date 2023/6/23 20:06
 */
@Mapper(
//        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductionNodeConverter {
    ProductionNodeConverter INSTANCE = Mappers.getMapper(ProductionNodeConverter.class);

    ProductionNodeOutput flowDto2Pnil(ProductionNodeFlowDto nodeFlow);
}
