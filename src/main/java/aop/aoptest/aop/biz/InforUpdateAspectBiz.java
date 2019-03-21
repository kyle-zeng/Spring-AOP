package aop.aoptest.aop.biz;

import io.swagger.util.Json;

import org.springframework.stereotype.Component;

import aop.aoptest.aop.entity.CrmCustRelation;

@Component
public class InforUpdateAspectBiz {
	public void dealWithRelation(CrmCustRelation crmCustRelation){
		//模拟插入关系表操作，这里只是打印
		System.out.println("将关联关系存入数据库,内容如下: ");
		Json.prettyPrint(crmCustRelation);
	}
}


