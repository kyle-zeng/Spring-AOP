package aop.aoptest;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;

import aop.aoptest.business.AddCompanyStocksRequestDTO;
import aop.aoptest.business.StockManagerInterface;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"} ,loader = GenericXmlContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  
public class AopTest{
	
	@Autowired
	private StockManagerInterface stockManagerService;
	
	/**
	 * 变更股权信息
	 */
	@Test
	public void test001_modifyCompanyStocks(){
		AddCompanyStocksRequestDTO re = new AddCompanyStocksRequestDTO();
		re.setNamCustCompany("xxx有限公司");
		re.setNamCustGstk("张三");
		re.setDatGstk(new Date());
		re.setAmtCapitalRstk(new BigDecimal(50000));
		stockManagerService.modifyCompanyStocks(re);
	}
}
