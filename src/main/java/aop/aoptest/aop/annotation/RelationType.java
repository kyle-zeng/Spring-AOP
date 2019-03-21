package aop.aoptest.aop.annotation;
public enum RelationType {
	/**
	 *  法人
	 */
	LEGALPERSON_P("{0}的法人是{1}"), 
	/**
	 * 股东
	 */
	STOCKHOLDERP_P("{0}的股东是{1}");
	private String relation;
	private RelationType(String index){
		this.relation = index;
	}
	public String getRelation(){
		return this.relation;
	}
}