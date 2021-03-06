package com.bornfire.qrcode.validators.cpm;

import static com.bornfire.valid.function.FunctionBuilder.of;
import static com.bornfire.valid.predicate.ComparablePredicate.betweenInclusive;
import static com.bornfire.valid.predicate.ComparablePredicate.equalTo;
import static com.bornfire.valid.predicate.ComparablePredicate.lessThanOrEqual;
import static com.bornfire.valid.predicate.LogicalPredicate.not;
import static com.bornfire.valid.predicate.ObjectPredicate.nullValue;
import static com.bornfire.valid.predicate.StringPredicate.stringEmptyOrNull;

import com.bornfire.qrcode.core.model.cpm.BERTLV;
import com.bornfire.qrcode.model.cpm.ApplicationTemplate;

import com.bornfire.valid.AbstractValidator;

// @formatter:off
class ApplicationTemplateValidator extends AbstractValidator<ApplicationTemplate> {

	@Override
  public void rules() {

		ruleFor("ApplicationDefinitionFileName", ApplicationTemplate::getApplicationDefinitionFileName)
			.must(not(stringEmptyOrNull(BERTLV::getStringValue)))
  		  .when(not(nullValue()))
  			.withMessage("ApplicationDefinitionFileName value must be present")
  			.withAttempedValue(of(BERTLV::getStringValue))
  		.must(betweenInclusive(BERTLV::getLength, 5, 16))
  		  .when(not(nullValue()))
  			.withMessage("ApplicationDefinitionFileName value size must be between five and sixteen")
  			.withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("ApplicationLabel", ApplicationTemplate::getApplicationLabel)
      .must(betweenInclusive(BERTLV::getLength, 1, 16))
        .when(not(nullValue()))
        .withMessage("ApplicationLabel value size must be between five and sixteen")
        .withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("ApplicationPAN", ApplicationTemplate::getApplicationPAN)
      .must(lessThanOrEqual(BERTLV::getLength, 10))
        .when(not(nullValue()))
        .withMessage("ApplicationPAN value size must be less than or equal ten")
        .withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("ApplicationVersionNumber", ApplicationTemplate::getApplicationVersionNumber)
      .must(equalTo(BERTLV::getLength, 2))
        .when(not(nullValue()))
        .withMessage("ApplicationVersionNumber value size must be equal two")
        .withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("CardholderName", ApplicationTemplate::getCardholderName)
      .must(betweenInclusive(BERTLV::getLength, 2, 26))
        .when(not(nullValue()))
        .withMessage("CardholderName value size must be between two and twenty-six")
        .withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("Last4DigitsOfPAN", ApplicationTemplate::getLast4DigitsOfPAN)
      .must(equalTo(BERTLV::getLength, 2))
        .when(not(nullValue()))
        .withMessage("Last4DigitsOfPAN value size must be equal two")
        .withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("LanguagePreference", ApplicationTemplate::getLanguagePreference)
      .must(betweenInclusive(BERTLV::getLength, 2, 8))
        .when(not(nullValue()))
        .withMessage("LanguagePreference value size must be between two and eight")
        .withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("Track2EquivalentData", ApplicationTemplate::getTrack2EquivalentData)
      .must(lessThanOrEqual(BERTLV::getLength, 19))
        .when(not(nullValue()))
        .withMessage("Track2EquivalentData value size must be less than or equal nineteen")
        .withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("TokenRequestorID", ApplicationTemplate::getTokenRequestorID)
      .must(equalTo(BERTLV::getLength, 6))
        .when(not(nullValue()))
        .withMessage("TokenRequestorID value size must be equal six")
        .withAttempedValue(of(BERTLV::getStringValue));

    ruleFor("PaymentAccountReference", ApplicationTemplate::getPaymentAccountReference)
      .must(equalTo(BERTLV::getLength, 29))
        .when(not(nullValue()))
        .withMessage("PaymentAccountReference value size must be equal twenty-nine")
        .withAttempedValue(of(BERTLV::getStringValue));

	}
}
// @formatter:on