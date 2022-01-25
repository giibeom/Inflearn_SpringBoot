package hello.core.scan.filter;

import java.lang.annotation.*;

/**
 * @author Created by 명기범 on 2022-01-25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {
}
