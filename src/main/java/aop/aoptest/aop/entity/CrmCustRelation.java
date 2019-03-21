package aop.aoptest.aop.entity;


public class CrmCustRelation {
    private Integer codReltId; //关系序号
    private Integer codCustId;//客户序号
    private Integer codCustRelt;//关联客户ID
    private String codReltNature;//关系性质
    private String codReltType;//关系类型
    private String txtReltDesc;//关系描述

	public Integer getCodReltId() {
		return codReltId;
	}

	public void setCodReltId(Integer codReltId) {
		this.codReltId = codReltId;
	}

	public Integer getCodCustId() {
		return codCustId;
	}

	public void setCodCustId(Integer codCustId) {
		this.codCustId = codCustId;
	}

	public Integer getCodCustRelt() {
		return codCustRelt;
	}

	public void setCodCustRelt(Integer codCustRelt) {
		this.codCustRelt = codCustRelt;
	}

	public String getCodReltNature() {
		return codReltNature;
	}

	public void setCodReltNature(String codReltNature) {
		this.codReltNature = codReltNature;
	}

	public String getCodReltType() {
		return codReltType;
	}

	public void setCodReltType(String codReltType) {
		this.codReltType = codReltType;
	}

	public String getTxtReltDesc() {
		return txtReltDesc;
	}

	public void setTxtReltDesc(String txtReltDesc) {
		this.txtReltDesc = txtReltDesc;
	}

}