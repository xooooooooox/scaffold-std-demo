package space.x9x.demo.types.common;

import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.asserts.ExtendedAssert;

/**
 * @author x9x
 * @since 2024-10-24 22:17
 */
public class AssertUtils extends ExtendedAssert {

    public static void notNull(Object object, ErrorCode errorCode) {
        notNull(object, errorCode.getMessage());
    }
}
