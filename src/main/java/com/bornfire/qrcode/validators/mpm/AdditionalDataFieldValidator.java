package com.bornfire.qrcode.validators.mpm;

import static com.bornfire.valid.function.FunctionBuilder.of;
import static com.bornfire.valid.predicate.ComparablePredicate.betweenInclusive;
import static com.bornfire.valid.predicate.ComparablePredicate.greaterThan;
import static com.bornfire.valid.predicate.LogicalPredicate.not;
import static com.bornfire.valid.predicate.ObjectPredicate.nullValue;
import static com.bornfire.valid.predicate.StringPredicate.isNumeric;
import static com.bornfire.valid.predicate.StringPredicate.stringEmptyOrNull;
import static com.bornfire.valid.predicate.StringPredicate.stringEquals;
import static com.bornfire.valid.predicate.StringPredicate.stringSize;
import static com.bornfire.valid.predicate.StringPredicate.stringSizeLessThanOrEqual;

import java.util.Collection;
import java.util.Map;

import com.bornfire.qrcode.core.model.mpm.TagLengthString;
import com.bornfire.qrcode.model.mpm.AdditionalDataField;
import com.bornfire.qrcode.model.mpm.constants.AdditionalDataFieldCodes;

import com.bornfire.valid.AbstractValidator;

// @formatter:off
class AdditionalDataFieldValidator extends AbstractValidator<AdditionalDataField> {

  @Override
  public void rules() {

    /**
     *
     */
    ruleFor(AdditionalDataField::getBillNumber)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("BillNumber tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getBillNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("BillNumber tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getBillNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("BillNumber tag must be number")
        .withAttempedValue(of(AdditionalDataField::getBillNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_BILL_NUMBER))
        .when(not(nullValue()))
        .withMessage(String.format("BillNumber tag must be '%s'", AdditionalDataFieldCodes.ID_BILL_NUMBER))
        .withAttempedValue(of(AdditionalDataField::getBillNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("BillNumber value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getBillNumber).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("BillNumber value must less then or equal size twenty-five")
        .withAttempedValue(of(AdditionalDataField::getBillNumber).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor(AdditionalDataField::getMobileNumber)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("MobileNumber tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getMobileNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("MobileNumber tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getMobileNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("MobileNumber tag must be number")
        .withAttempedValue(of(AdditionalDataField::getMobileNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_MOBILE_NUMBER))
        .when(not(nullValue()))
        .withMessage(String.format("MobileNumber tag must be '%s'", AdditionalDataFieldCodes.ID_MOBILE_NUMBER))
        .withAttempedValue(of(AdditionalDataField::getMobileNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("MobileNumber value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getMobileNumber).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("MobileNumber value must less then or equal size twenty-five")
        .withAttempedValue(of(AdditionalDataField::getMobileNumber).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor(AdditionalDataField::getStoreLabel)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("StoreLabel tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getStoreLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("StoreLabel tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getStoreLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("StoreLabel tag must be number")
        .withAttempedValue(of(AdditionalDataField::getStoreLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_STORE_LABEL))
        .when(not(nullValue()))
        .withMessage(String.format("StoreLabel tag must be '%s'", AdditionalDataFieldCodes.ID_STORE_LABEL))
        .withAttempedValue(of(AdditionalDataField::getStoreLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("StoreLabel value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getStoreLabel).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("StoreLabel value must less then or equal size twenty-five")
        .withAttempedValue(of(AdditionalDataField::getStoreLabel).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor(AdditionalDataField::getLoyaltyNumber)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("LoyaltyNumber tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getLoyaltyNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("LoyaltyNumber tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getLoyaltyNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("LoyaltyNumber tag must be number")
        .withAttempedValue(of(AdditionalDataField::getLoyaltyNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_LOYALTY_NUMBER))
        .when(not(nullValue()))
        .withMessage(String.format("LoyaltyNumber tag must be '%s'", AdditionalDataFieldCodes.ID_LOYALTY_NUMBER))
        .withAttempedValue(of(AdditionalDataField::getLoyaltyNumber).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("LoyaltyNumber value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getLoyaltyNumber).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("LoyaltyNumber value must less then or equal size twenty-five")
        .withAttempedValue(of(AdditionalDataField::getLoyaltyNumber).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor(AdditionalDataField::getReferenceLabel)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("ReferenceLabel tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getReferenceLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("ReferenceLabel tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getReferenceLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("ReferenceLabel tag must be number")
        .withAttempedValue(of(AdditionalDataField::getReferenceLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_REFERENCE_LABEL))
        .when(not(nullValue()))
        .withMessage(String.format("ReferenceLabel tag must be '%s'", AdditionalDataFieldCodes.ID_REFERENCE_LABEL))
        .withAttempedValue(of(AdditionalDataField::getReferenceLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("ReferenceLabel value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getReferenceLabel).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("ReferenceLabel value must less then or equal size twenty-five")
        .withAttempedValue(of(AdditionalDataField::getReferenceLabel).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor(AdditionalDataField::getCustomerLabel)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("CustomerLabel tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getCustomerLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("CustomerLabel tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getCustomerLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("CustomerLabel tag must be number")
        .withAttempedValue(of(AdditionalDataField::getCustomerLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_CUSTOMER_LABEL))
        .when(not(nullValue()))
        .withMessage(String.format("CustomerLabel tag must be '%s'", AdditionalDataFieldCodes.ID_CUSTOMER_LABEL))
        .withAttempedValue(of(AdditionalDataField::getCustomerLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("CustomerLabel value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getCustomerLabel).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("CustomerLabel value must less then or equal size twenty-five")
        .withAttempedValue(of(AdditionalDataField::getCustomerLabel).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor(AdditionalDataField::getTerminalLabel)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("TerminalLabel tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getTerminalLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("TerminalLabel tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getTerminalLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("TerminalLabel tag must be number")
        .withAttempedValue(of(AdditionalDataField::getTerminalLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_TERMINAL_LABEL))
        .when(not(nullValue()))
        .withMessage(String.format("TerminalLabel tag must be '%s'", AdditionalDataFieldCodes.ID_TERMINAL_LABEL))
        .withAttempedValue(of(AdditionalDataField::getTerminalLabel).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("TerminalLabel value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getTerminalLabel).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("TerminalLabel value must less then or equal size twenty-five")
        .withAttempedValue(of(AdditionalDataField::getTerminalLabel).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor(AdditionalDataField::getPurposeTransaction)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("PurposeTransaction tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getPurposeTransaction).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("PurposeTransaction tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getPurposeTransaction).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("PurposeTransaction tag must be number")
        .withAttempedValue(of(AdditionalDataField::getPurposeTransaction).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_PURPOSE_TRANSACTION))
        .when(not(nullValue()))
        .withMessage(String.format("PurposeTransaction tag must be '%s'", AdditionalDataFieldCodes.ID_PURPOSE_TRANSACTION))
        .withAttempedValue(of(AdditionalDataField::getPurposeTransaction).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("PurposeTransaction value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getPurposeTransaction).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("PurposeTransaction value must less then or equal size twenty-five")
        .withAttempedValue(of(AdditionalDataField::getPurposeTransaction).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor(AdditionalDataField::getAdditionalConsumerDataRequest)

      .must(not(stringEmptyOrNull(TagLengthString::getTag)))
        .when(not(nullValue()))
        .withMessage("AdditionalConsumerDataRequest tag is mandatory")
        .withAttempedValue(of(AdditionalDataField::getAdditionalConsumerDataRequest).andThen(TagLengthString::getTag))
        .critical()

      .must(stringSize(TagLengthString::getTag, 2))
        .when(not(nullValue()))
        .withMessage("AdditionalConsumerDataRequest tag must be size equal two")
        .withAttempedValue(of(AdditionalDataField::getAdditionalConsumerDataRequest).andThen(TagLengthString::getTag))
        .critical()

      .must(isNumeric(TagLengthString::getTag))
        .when(not(nullValue()))
        .withMessage("AdditionalConsumerDataRequest tag must be number")
        .withAttempedValue(of(AdditionalDataField::getAdditionalConsumerDataRequest).andThen(TagLengthString::getTag))
        .critical()

      .must(stringEquals(TagLengthString::getTag, AdditionalDataFieldCodes.ID_ADDITIONAL_CONSUMER_DATA_REQUEST))
        .when(not(nullValue()))
        .withMessage(String.format("AdditionalConsumerDataRequest tag must be '%s'", AdditionalDataFieldCodes.ID_ADDITIONAL_CONSUMER_DATA_REQUEST))
        .withAttempedValue(of(AdditionalDataField::getAdditionalConsumerDataRequest).andThen(TagLengthString::getTag))
        .critical()

      .must(not(stringEmptyOrNull(TagLengthString::getValue)))
        .when(not(nullValue()))
        .withMessage("AdditionalConsumerDataRequest value is mandatory")
        .withAttempedValue(of(AdditionalDataField::getAdditionalConsumerDataRequest).andThen(TagLengthString::getValue))
        .critical()

      .must(stringSizeLessThanOrEqual(TagLengthString::getValue, 25))
        .when(not(nullValue()))
        .withMessage("AdditionalConsumerDataRequest value must less then or equal size twenty five")
        .withAttempedValue(of(AdditionalDataField::getAdditionalConsumerDataRequest).andThen(TagLengthString::getValue))
        .critical();

    /**
     *
     */
    ruleFor("RFUforEMVCo", AdditionalDataField::getrFUforEMVCo)
      .must(betweenInclusive(Map::size, 1, 39))
        .when(greaterThan(Map::size, 0))
        .withMessage("RFUforEMVCo list size must be between one and thirty-nine")
        .withAttempedValue(of(AdditionalDataField::getrFUforEMVCo).andThen(Map::size));

    ruleForEach(of(AdditionalDataField::getrFUforEMVCo).andThen(Map::values))
      .whenever(greaterThan(Collection::size, 0))
        .withValidator(new TagLengthStringValidator("AdditionalDataField.RFUforEMVCo", "10", "49", 99));

    /**
     *
     */
    ruleFor("PaymentSystemSpecific", AdditionalDataField::getPaymentSystemSpecific)
      .must(betweenInclusive(Map::size, 1, 49))
        .when(greaterThan(Map::size, 0))
        .withMessage("PaymentSystemSpecific list size must be between one and forty-nine")
        .withAttempedValue(of(AdditionalDataField::getPaymentSystemSpecific).andThen(Map::size));

    ruleForEach(of(AdditionalDataField::getPaymentSystemSpecific).andThen(Map::values))
      .whenever(greaterThan(Collection::size, 0))
        .withValidator(new PaymentSystemSpecificTemplateValidator("50", "99", 99));

   }

}
// @formatter:on
