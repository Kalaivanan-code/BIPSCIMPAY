package com.bornfire.valid.rule;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import com.bornfire.valid.Validator;
import com.bornfire.valid.annotation.CleanValidationContextException;
import com.bornfire.valid.builder.AttemptedValue;
import com.bornfire.valid.builder.Code;
import com.bornfire.valid.builder.Critical;
import com.bornfire.valid.builder.FieldName;
import com.bornfire.valid.builder.HandleInvalidField;
import com.bornfire.valid.builder.Message;
import com.bornfire.valid.builder.Must;
import com.bornfire.valid.builder.RuleBuilderProperty;
import com.bornfire.valid.builder.WhenProperty;
import com.bornfire.valid.builder.WheneverProperty;
import com.bornfire.valid.builder.WithValidator;
import com.bornfire.valid.context.ValidationContext;
import com.bornfire.valid.exception.ValidationException;
import com.bornfire.valid.handler.HandlerInvalidField;

public class RuleBuilderPropertyImpl<T, P> extends AbstractRuleBuilder<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> implements RuleBuilderProperty<T, P>, WhenProperty<T, P>, WheneverProperty<T, P> {

  private final Collection<Rule<P>> rules = new LinkedList<>();

  private final RuleProcessorStrategy ruleProcessor = RuleProcessorStrategy.getFailFast();

  private ValidationRule<P, P> currentValidation;

  public RuleBuilderPropertyImpl(final String fieldName, final Function<T, P> function) {
    super(fieldName, function);
  }

  public RuleBuilderPropertyImpl(final Function<T, P> function) {
    super(function);
  }

  @Override
  public boolean apply(final T instance) {
    final P value = Objects.nonNull(instance) ? function.apply(instance) : null;
    return ruleProcessor.process(instance, value, rules);
  }

  @Override
  public WheneverProperty<T, P> whenever(final Predicate<P> whenever) {
    this.currentValidation = new ValidatorRuleInternal(fieldName, whenever);
    this.rules.add(this.currentValidation);
    return this;
  }

  @Override
  public Must<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> must(final Predicate<P> must) {
    this.currentValidation = new ValidationRuleInternal(fieldName, must);
    this.rules.add(this.currentValidation);
    return this;
  }

  @Override
  public Message<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withMessage(final String message) {
    this.currentValidation.withMessage(obj -> message);
    return this;
  }

  @Override
  public Message<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withMessage(final Function<T, String> message) {
    this.currentValidation.withMessage(message);
    return this;
  }

  @Override
  public Code<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withCode(final String code) {
    this.currentValidation.withCode(obj -> code);
    return this;
  }

  @Override
  public Code<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withCode(final Function<T, String> code) {
    this.currentValidation.withCode(code);
    return this;
  }

  @Override
  public FieldName<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withFieldName(final String fieldName) {
    this.currentValidation.withFieldName(obj -> fieldName);
    return this;
  }

  @Override
  public FieldName<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withFieldName(final Function<T, String> fieldName) {
    this.currentValidation.withFieldName(fieldName);
    return this;
  }

  @Override
  public AttemptedValue<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withAttempedValue(final Object attemptedValue) {
    this.currentValidation.withAttemptedValue(obj -> attemptedValue);
    return this;
  }

  @Override
  public AttemptedValue<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withAttempedValue(final Function<T, Object> attemptedValue) {
    this.currentValidation.withAttemptedValue(attemptedValue);
    return this;
  }

  @Override
  public Critical<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> critical() {
    this.currentValidation.critical();
    return this;
  }

  @Override
  public Critical<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> critical(final Class<? extends ValidationException> clazz) {
    this.currentValidation.critical(clazz);
    return this;
  }

  @Override
  public HandleInvalidField<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> handlerInvalidField(final HandlerInvalidField<P> handlerInvalidField) {
    this.currentValidation.withHandlerInvalidField(handlerInvalidField);
    return this;
  }

  @Override
  public WithValidator<T, P, WhenProperty<T, P>, WheneverProperty<T, P>> withValidator(final Validator<P> validator) {
    this.currentValidation.withValidator(validator);
    return this;
  }

  @Override
  public WhenProperty<T, P> when(final Predicate<P> predicate) {
    this.currentValidation.when(predicate);
    return this;
  }

  class ValidationRuleInternal extends AbstractValidationRule<P, P> {

    ValidationRuleInternal(final Function<T, String> fieldName, final Predicate<P> must) {
      super.must(must);
      super.withFieldName(fieldName);
    }

    @Override
    public boolean support(final P instance) {
      return Boolean.TRUE.equals(getWhen().test(instance));
    }

    @Override
    @CleanValidationContextException
    public boolean apply(final Object obj, final P instance) {

      final boolean apply = getMust().test(instance);

      if (Boolean.FALSE.equals(apply)) {
        ValidationContext.get().addErrors(getHandlerInvalid().handle(obj, instance));
      }

      if (Objects.nonNull(getCriticalException()) && Boolean.FALSE.equals(apply)) {
        throw ValidationException.create(getCriticalException());
      }

      return !(Boolean.TRUE.equals(isCritical()) && Boolean.FALSE.equals(apply));

    }

  }

  class ValidatorRuleInternal extends AbstractValidationRule<P, P> {

    ValidatorRuleInternal(final Function<T, String> fieldName, final Predicate<P> whenever) {
      super.whenever(whenever);
      super.withFieldName(fieldName);
    }

    @Override
    public boolean support(final P instance) {
      return Boolean.TRUE.equals(getWhenever().test(instance));
    }

    @Override
    @CleanValidationContextException
    public boolean apply(final Object obj, final P instance) {

      final boolean apply = ruleProcessor.process(obj, instance, getValidator());

      if (Objects.nonNull(getCriticalException()) && Boolean.FALSE.equals(apply)) {
        throw ValidationException.create(getCriticalException());
      }

      return !(Boolean.TRUE.equals(isCritical()) && Boolean.FALSE.equals(apply));
    }

  }

}
