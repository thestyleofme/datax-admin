package com.github.thestyleofme.datax.server.infra.utils;

import com.github.thestyleofme.plugin.core.infra.utils.ApplicationContextHelper;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * Application
 * </p>
 *
 * @author thestyleofme 2020/12/21 11:39
 * @since 1.0.0
 */
public class ApplicationContextUtil {

    private ApplicationContextUtil() {
    }

    private static final ApplicationContext CONTEXT;

    static {
        CONTEXT = ApplicationContextHelper.getContext();
    }

    public static <T> T findBean(String name, Class<T> clazz) {
        return CONTEXT.getBean(name, clazz);
    }

}
