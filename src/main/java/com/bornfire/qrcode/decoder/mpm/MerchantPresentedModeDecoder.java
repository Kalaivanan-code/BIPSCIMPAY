package com.bornfire.qrcode.decoder.mpm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import com.bornfire.qrcode.core.exception.DuplicateTagException;
import com.bornfire.qrcode.core.exception.InvalidTagException;
import com.bornfire.qrcode.core.exception.PresentedModeException;
import com.bornfire.qrcode.core.model.mpm.TagLengthString;
import com.bornfire.qrcode.core.utils.TLVUtils;
import com.bornfire.qrcode.model.mpm.AdditionalDataFieldTemplate;
import com.bornfire.qrcode.model.mpm.MerchantAccountInformationTemplate;
import com.bornfire.qrcode.model.mpm.MerchantInformationLanguageTemplate;
import com.bornfire.qrcode.model.mpm.MerchantPresentedMode;
import com.bornfire.qrcode.model.mpm.UnreservedTemplate;
import com.bornfire.qrcode.model.mpm.constants.MerchantPresentedModeCodes;

// @formatter:off
public final class MerchantPresentedModeDecoder extends DecoderMpm<MerchantPresentedMode> {

  private static final Map<String, Entry<Class<?>, BiConsumer<MerchantPresentedMode, ?>>> mapConsumers = new HashMap<>();

  static {
    mapConsumers.put(MerchantPresentedModeCodes.ID_PAYLOAD_FORMAT_INDICATOR, consumerTagLengthValue(String.class, MerchantPresentedMode::setPayloadFormatIndicator));
    mapConsumers.put(MerchantPresentedModeCodes.ID_POINT_OF_INITIATION_METHOD, consumerTagLengthValue(String.class, MerchantPresentedMode::setPointOfInitiationMethod));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_CATEGORY_CODE, consumerTagLengthValue(String.class, MerchantPresentedMode::setMerchantCategoryCode));
    mapConsumers.put(MerchantPresentedModeCodes.ID_TRANSACTION_CURRENCY, consumerTagLengthValue(String.class, MerchantPresentedMode::setTransactionCurrency));
    mapConsumers.put(MerchantPresentedModeCodes.ID_TRANSACTION_AMOUNT, consumerTagLengthValue(String.class, MerchantPresentedMode::setTransactionAmount));
    mapConsumers.put(MerchantPresentedModeCodes.ID_TIP_OR_CONVENIENCE_INDICATOR, consumerTagLengthValue(String.class, MerchantPresentedMode::setTipOrConvenienceIndicator));
    mapConsumers.put(MerchantPresentedModeCodes.ID_VALUE_OF_CONVENIENCE_FEE_FIXED, consumerTagLengthValue(String.class, MerchantPresentedMode::setValueOfConvenienceFeeFixed));
    mapConsumers.put(MerchantPresentedModeCodes.ID_VALUE_OF_CONVENIENCE_FEE_PERCENTAGE, consumerTagLengthValue(String.class, MerchantPresentedMode::setValueOfConvenienceFeePercentage));
    mapConsumers.put(MerchantPresentedModeCodes.ID_COUNTRY_CODE, consumerTagLengthValue(String.class, MerchantPresentedMode::setCountryCode));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_NAME, consumerTagLengthValue(String.class, MerchantPresentedMode::setMerchantName));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_CITY, consumerTagLengthValue(String.class, MerchantPresentedMode::setMerchantCity));
    mapConsumers.put(MerchantPresentedModeCodes.ID_POSTAL_CODE, consumerTagLengthValue(String.class, MerchantPresentedMode::setPostalCode));
    mapConsumers.put(MerchantPresentedModeCodes.ID_CRC, consumerTagLengthValue(String.class, MerchantPresentedMode::setCRC));
    mapConsumers.put(MerchantPresentedModeCodes.ID_ADDITIONAL_DATA_FIELD_TEMPLATE, consumerTagLengthValue(AdditionalDataFieldTemplate.class, MerchantPresentedMode::setAdditionalDataField));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_INFORMATION_LANGUAGE_TEMPLATE, consumerTagLengthValue(MerchantInformationLanguageTemplate.class, MerchantPresentedMode::setMerchantInformationLanguage));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RESERVED, consumerTagLengthValue(MerchantAccountInformationTemplate.class, MerchantPresentedMode::addMerchantAccountInformation));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RESERVED_ADDITIONAL, consumerTagLengthValue(MerchantAccountInformationTemplate.class, MerchantPresentedMode::addMerchantAccountInformation));
    mapConsumers.put(MerchantPresentedModeCodes.ID_RFU_FOR_EMVCO, consumerTagLengthValue(TagLengthString.class, MerchantPresentedMode::addRFUforEMVCo));
    mapConsumers.put(MerchantPresentedModeCodes.ID_UNRESERVED_TEMPLATES, consumerTagLengthValue(UnreservedTemplate.class, MerchantPresentedMode::addUnreserved));
    mapConsumers.put(MerchantPresentedModeCodes.ID_CRC, consumerTagLengthValue(String.class, MerchantPresentedMode::setCRC));
  }

  public MerchantPresentedModeDecoder(final String source) {
    super(source);
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected MerchantPresentedMode decode() throws PresentedModeException {

    final Set<String> tags = new HashSet<>();

    final MerchantPresentedMode result = new MerchantPresentedMode();

    result.setCRC("0000");

    while(iterator.hasNext()) {
      final String value = iterator.next();

      final String tag = TLVUtils.valueOfTag(value);

      final String derivateId = derivateId(tag);

      if (tags.contains(tag)) {
        throw new DuplicateTagException("MerchantPresentedMode", tag, value);
      }

      tags.add(tag);

      final Entry<Class<?>, BiConsumer<MerchantPresentedMode, ?>> entry = mapConsumers.get(derivateId);

      if (Objects.isNull(entry)) {
        throw new InvalidTagException("MerchantPresentedMode", tag, value);
      }

      final Class<?> clazz = entry.getKey();

      final BiConsumer consumer = entry.getValue();

      consumer.accept(result, DecoderMpm.decode(value, clazz));
    }

    return result;
  }

  private String derivateId(final String id) {

    if (betweenAccountInformationReservedRange(id)) {
      return MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RESERVED;
    }

    if (betweenAccountInformationaReservedAdditionalRange(id)) {
      return MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RESERVED_ADDITIONAL;
    }

    if (betweenRFUForEMVCORange(id)) {
      return MerchantPresentedModeCodes.ID_RFU_FOR_EMVCO;
    }

    if (betweenUnreservedTemplatesRange(id)) {
      return MerchantPresentedModeCodes.ID_UNRESERVED_TEMPLATES;
    }

    return id;
  }

  private boolean betweenAccountInformationReservedRange(final String value) {
    return value.compareTo(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RESERVED_RANGE_START) >= 0
        && value.compareTo(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RESERVED_RANGE_END) <= 0;
  }

  private boolean betweenAccountInformationaReservedAdditionalRange(final String value) {
    return value.compareTo(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RESERVED_ADDITIONAL_RANGE_START) >= 0
        && value.compareTo(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RESERVED_ADDITIONAL_RANGE_END) <= 0;
  }

  private boolean betweenRFUForEMVCORange(final String value) {
    return value.compareTo(MerchantPresentedModeCodes.ID_RFU_FOR_EMVCO_RANGE_START) >= 0
        && value.compareTo(MerchantPresentedModeCodes.ID_RFU_FOR_EMVCO_RANGE_END) <= 0;
  }

  private boolean betweenUnreservedTemplatesRange(final String value) {
    return value.compareTo(MerchantPresentedModeCodes.ID_UNRESERVED_TEMPLATES_RANGE_START) >= 0
        && value.compareTo(MerchantPresentedModeCodes.ID_UNRESERVED_TEMPLATES_RANGE_END) <= 0;
  }

}
// @formatter:on
