package aop.aoptest.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RelationParameter {
	/**
	 * 被该注解标注的字段是关联关系需要处理的字段
	 * 例如：@RelationParameter("keyMain")
	 * keyMain 关联关系主体
	 * keyRelt 关联关系相关体
	 * @return
	 */
   String value() default "";
}