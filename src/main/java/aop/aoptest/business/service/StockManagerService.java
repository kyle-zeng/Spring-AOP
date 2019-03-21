package aop.aoptest.business.service;

import io.swagger.util.Json;

import org.springframework.stereotype.Service;

import aop.aoptest.aop.annotation.EventType;
import aop.aoptest.aop.annotation.InforUpdate;
import aop.aoptest.aop.annotation.InforUpdateEnable;
import aop.aoptest.aop.annotation.RelationType;
import aop.aoptest.business.AddCompanyStocksRequestDTO;
import aop.aoptest.business.StockManagerInterface;


@Service
@InforUpdateEnable
public class StockManagerService implements StockManagerInterface{
	/**
	 * 如果添加@InforUpdate
	 * 需要在类的顶部加@InforUpdateEnable开关量
	 */
	@Override
	@InforUpdate(relation = RelationType.STOCKHOLDERP_P,event = EventType.ADD, desc = "变更股权信息")
	public void modifyCompanyStocks(AddCompanyStocksRequestDTO requestDTO) {
		Json.prettyPrint("添加公司股东，信息如下: \n" + requestDTO);
	}
}
