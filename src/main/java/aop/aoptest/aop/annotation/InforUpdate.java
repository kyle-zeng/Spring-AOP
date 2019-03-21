package aop.aoptest.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


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
