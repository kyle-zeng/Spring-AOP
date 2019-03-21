Spring AOP + 注解实现公共操作的统一管理

#### 背景
做 CRM 系统的时候，有一个客户关联关系的模块（关联关系包括：企业与企业的关系，如担保；个人与企业的关系，如法人，个人与个人的关系，如父子）。
现在要实现这样一个功能：在做其他业务操作的时候往关联关系表插入一条说明该关联关系的数据（如给一个企业添加法人时，往关系表插入xxx是yyy公司法人），
要达到拿来即用，尽量不侵入业务代码的目的，类似于打印日志功能。

#### 实现思路
刚开始想到用装饰器，定义一个处理关联关系的被装饰类接口，具体被装饰类完成关联关系的入库及其他操作，具体的装饰器类则实现其他业务逻辑（如添加法人）
使用装饰器的话一个操作就要定义一个类，这样会产生很多小类。而且每种操作的参数都不一样，很难统一处理，达不到理想的效果。

后面经过资料查找和讨论，既然是类似日志的形式，就直接用 AOP 来做，拦截指定的包和类方法，在拦截函数中可以获取到拦截类的方法、注解，特定注解的方法，
方法上的注解，方法的参数，通过反射机制能拿到实体参数的字段等信息，应该可以达到指定的效果。

#### 具体实现

##### 定义相关的枚举类

首先定义关系类型的枚举类
```
public enum RelationType {
	/**
	 *  法人
	 */
	LEGALPERSON_P("{0}是{1}公司的法人"), 
	/**
	 * 股东
	 */
	STOCKHOLDERP_P("{0}是{1}公司的股东");
	private String relation;
	private RelationType(String index){
		this.relation = index;
	}
	public String getRelation(){
		return this.relation;
	}
}
```
使用枚举类定义操作的类型：EventType，如添加、删除、更新等。
```
public enum EventType {
	ADD("1", "add"), 
	UPDATE("2", "update"), 
	DELETE("3", "delete");
	 
	private String event;
	private String name;
	  
	private EventType(String index, String name){
		this.event = index;
		this.name = name;
	}
	public String getEvent(){
		return this.event;
	}
	public String getName() {
		return name;
	}
}
```
##### 定义更新关联关系相关的注解

1、定义一个是否需要更新关联关系的开关量，注解在类上，只有这个值为true，这个类的信息更新功能才开启。
```
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface InforUpdateEnable {
  /**
   * 如果为true，则类下面的InforUpdate启作用，否则忽略
   * @return
   */
  boolean inforUpdateEnable() default true;
}
```

2、这里定义关联关系，及操作类型的详细内容。如果此注解注解在类上，则这个参数做为类全部方法的默认值。如果注解在方法上，则只对该方法启作用。
```
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, ElementType.TYPE})
public @interface InforUpdate {
	/**
	 * 关系类型
	 * @return
	 */
	RelationType relation();
	/**
	 * 操作类型
	 * @return
	 */
	EventType event() default EventType.ADD;
	/**
	 * 描述信息
	 * @return
	 */
	String desc() default "";
}
```
3、定义需要处理参数的注解类
```
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RelationParameter {
	/**
	 * 被该注解标注的字段是关联关系需要处理的字段
	 * 例如：@RelationParameter("keyMain")
	 * keyMain 关联关系主体(如xxx公司)
	 * keyRelt 关联关系相关体(公司法人)
	 * @return
	 */
   String value() default "";
}
```

##### 定义关联关系处理类

1、保存关联关系的类
```
public class CrmCustRelation {
    private Integer codReltId; //关系序号
    private Integer codCustId;//客户序号
    private Integer codCustRelt;//关联客户ID
    private String codReltNature;//关系性质
    private String codReltType;//关系类型
    private String txtReltDesc;//关系描述
	//get 和 set 方法省略......
}
```
2、关联关系处理类，模拟入库操作
public class InforUpdateAspectBiz {
	public void dealWithRelation(CrmCustRelation crmCustRelation){
		//模拟插入关系表操作，这里只是打印
		System.out.println("将关联关系存入数据库,内容如下: ");
		Json.prettyPrint(crmCustRelation);
	}
}

##### AOP 配置
1、InforUpdateAspect 类
使用@Aspect注解此类，并在 applicationContext.xml 文件中配置如下
```
<bean id="inforUpdateAspect" class="aop.aoptest.aop.service.InforUpdateAspect"/>
<aop:config proxy-target-class="true">
	<aop:aspect ref="inforUpdateAspect">
		<aop:pointcut id="around" expression="execution(public * aop.aoptest.**.service..*.*(..))"/>
		<aop:around pointcut-ref="around" method="aroundManagerInforPoint"/>
	</aop:aspect>
</aop:config>
```
pointcut 定义要拦截的包及类方法
around 定义切面入口方法

InforUpdateAspect 具体实现
```
@Component
@Aspect
public class InforUpdateAspect {

	@Autowired
	private InforUpdateAspectBiz inforUpdateAspectBiz;
	
	@Autowired
	private RelationEntityGeneration relationEntityGeneration;

	/**
	 * aop入口函数
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	public Object aroundManagerInforPoint(ProceedingJoinPoint jp) throws Throwable {
		Class<?> target = jp.getTarget().getClass();
		// 获取InforUpdateEnable，是否需要做关联关系的更新
		InforUpdateEnable inforUpdateEnable = (InforUpdateEnable) target.getAnnotation(InforUpdateEnable.class);
		if(inforUpdateEnable == null || !inforUpdateEnable.inforUpdateEnable()){
			return jp.proceed();
		}
		
		// 获取类上的InforUpdate做为默认值
		InforUpdate inforUpdateClass = (InforUpdate) target.getAnnotation(InforUpdate.class);
		//获取被注解的方法
		Method method = getInvokedMethod(jp);
		if(method == null){
			return jp.proceed();
		}
		
		// 获取方法上的InforUpdate
		InforUpdate inforUpdateMethod = method.getAnnotation(InforUpdate.class);
		if(inforUpdateMethod == null){
			return jp.proceed();
		}
		
		String optEvent = inforUpdateMethod.event().getEvent();
		String optRelation = inforUpdateMethod.relation().getRelation();
		
		if(inforUpdateClass != null){
			// 如果方法上的值为默认值，则使用全局的值进行替换
			optEvent = optEvent.equals(EventType.ADD.getEvent()) ? inforUpdateClass.event().getEvent() : optEvent;
			optRelation = optRelation.equals(RelationType.LEGALPERSON_P) ? inforUpdateClass.relation().getRelation() : optRelation;
		}
		//处理注解字段的值
		CrmCustRelation crmCustRelation = new CrmCustRelation();
		crmCustRelation.setTxtReltDesc(optRelation);
		crmCustRelation = relationEntityGeneration.processingManagerRelation(jp, method,crmCustRelation);
		//执行业务方法
		Object returnObj = jp.proceed();
		//返回成功才执行一下的操作：更新关联关系
		if(optEvent.equals(EventType.ADD.getEvent())){
			inforUpdateAspectBiz.dealWithRelation(crmCustRelation);
		}else {
			
		}
		return returnObj;
	}
}
```

##### 实际应用

1、业务接口
```
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
```

2、业务实现方法，在方法上添加注解，完成关联关系的插入
```
@Service
//开关量
@InforUpdateEnable
public class StockManagerService implements StockManagerInterface{
	/**
	 * 如果添加@InforUpdate
	 * 需要在类的顶部加@InforUpdateEnable开关量
	 */
	@Override
	//添加更新注解
	@InforUpdate(relation = RelationType.STOCKHOLDERP_P,event = EventType.ADD, desc = "变更股权信息")
	public void modifyCompanyStocks(AddCompanyStocksRequestDTO requestDTO) {
		//模拟添加操作
		System.out.println("添加公司股东，信息如下: ");
		Json.prettyPrint(requestDTO);
	}
}
```

执行该方法时，会首先进入前面定义的 aroundManagerInforPoint 方法做相关处理，然后返回业务方法执行业务；最后返回切面方法，执行关联关系的入库操作
```
@Component
public class InforUpdateAspectBiz {
	public void dealWithRelation(CrmCustRelation crmCustRelation){
		//模拟插入关系表操作，这里只是打印
		System.out.println("将关联关系存入数据库,内容如下: ");
		Json.prettyPrint(crmCustRelation);
	}
}
```
