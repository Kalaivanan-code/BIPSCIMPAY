package com.bornfire.valid.builder;

import java.util.function.Function;

import com.bornfire.valid.exception.ValidationException;
import com.bornfire.valid.handler.HandlerInvalidField;

public interface When<T, P, W extends When<T, P, W, N>, N extends Whenever<T, P, W, N>> {

  /**
   *
   * @param code
   * @return
   */
  Code<T, P, W, N> withCode(final String code);

  /**
   *
   * @param code
   * @return
   */
  Code<T, P, W, N> withCode(final Function<T, String> code);

  /**
   *
   * @param message
   * @return
   */
  Message<T, P, W, N> withMessage(final String message);

  /**
   *
   * @param message
   * @return
   */
  Message<T, P, W, N> withMessage(final Function<T, String> message);

  /**
   *
   * @param fieldName
   * @return
   */
  FieldName<T, P, W, N> withFieldName(final String fieldName);

  /**
   *
   * @param fieldName
   * @return
   */
  FieldName<T, P, W, N> withFieldName(final Function<T, String> fieldName);

  /**
   *
   * @param fieldName
   * @return
   */
  AttemptedValue<T, P, W, N> withAttempedValue(final Object attemptedValue);

  /**
   *
   * @param fieldName
   * @return
   */
  AttemptedValue<T, P, W, N> withAttempedValue(final Function<T, Object> attemptedValue);

  /**
   *
   * @return
   */
  Critical<T, P, W, N> critical();

  /**
   *
   * @param clazz
   * @return
   */
  Critical<T, P, W, N> critical(final Class<? extends ValidationException> clazz);

  /**
   *
   * @param handlerInvalidField
   * @return
   */
  HandleInvalidField<T, P, W, N> handlerInvalidField(final HandlerInvalidField<P> handlerInvalidField);

}
