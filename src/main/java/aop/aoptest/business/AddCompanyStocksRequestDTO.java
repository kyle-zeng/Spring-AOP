package aop.aoptest.business;

import java.math.BigDecimal;
import java.util.Date;

import aop.aoptest.aop.annotation.RelationParameter;


public class AddCompanyStocksRequestDTO {
	@RelationParameter("keyMain")
    private String namCustCompany; //公司客户
	@RelationParameter("keyRelt")
    private String namCustGstk;  //股东姓名
    private Date datGstk;   //交易日期
    private BigDecimal amtCapitalRstk; //受让（出资）金额
    
	public String getNamCustCompany() {
		return namCustCompany;
	}
	public void setNamCustCompany(String namCustCompany) {
		this.namCustCompany = namCustCompany;
	}
	public String getNamCustGstk() {
		return namCustGstk;
	}
	public void setNamCustGstk(String namCustGstk) {
		this.namCustGstk = namCustGstk;
	}
	public Date getDatGstk() {
		return datGstk;
	}
	public void setDatGstk(Date datGstk) {
		this.datGstk = datGstk;
	}
	public BigDecimal getAmtCapitalRstk() {
		return amtCapitalRstk;
	}
	public void setAmtCapitalRstk(BigDecimal amtCapitalRstk) {
		this.amtCapitalRstk = amtCapitalRstk;
	}

}