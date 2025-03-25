package com.oneflow.prm.core.utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * ValidationUtils 手动检验工具类
 * @author forest.liao
 * @date 2024/06/24
 */
public class ValidationUtils {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    public static Validator getValidator() {
        return VALIDATOR;
    }
}