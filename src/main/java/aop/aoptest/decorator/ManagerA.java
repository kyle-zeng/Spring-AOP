package aop.aoptest.decorator;

public class ManagerA extends Employee {
	private Person person;//给雇员升职
    
    public ManagerA(Person person) {
        super();
        this.person = person;
    }
    
    public void doCoding(RequestDTO request) {
        doEarlyWork(request.getDoing());
        person.doCoding(request.getName(),request.getOther());        
    }
    /**
     * 项目经理开始前期准备工作
     */
    public void doEarlyWork(int i) {
        System.out.println("项目经理A做需求分析"+i);
        System.out.println("项目经理A做架构设计");
        System.out.println("项目经理A做详细设计"); 
    }
    
    public void doCoding2(RequestDTO request) {
    	person.doCoding(request.getName(),request.getOther());        
    	doEndWork(request.getDoing());
    }
    /**
     * 项目经理开始前期准备工作
     */
    public void doEndWork(int i) {
        System.out.println("项目经理A善后"+i);

    }
}
