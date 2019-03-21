package aop.aoptest.business;

/**
 * 股权管理接口
 * @author kyle
 *
 */
public interface StockManagerInterface {
	/**
	 * 修改持股信息
	 * @param requestDTO
	 */
	public void modifyCompanyStocks(AddCompanyStocksRequestDTO requestDTO);
	
}
