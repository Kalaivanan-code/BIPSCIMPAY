package com.bornfire.qrcode.decoder.mpm;

import com.bornfire.qrcode.core.exception.PresentedModeException;
import com.bornfire.qrcode.core.model.mpm.TagLengthString;
import com.bornfire.qrcode.core.utils.TLVUtils;

// @formatter:off
public final class TagLengthStringDecoder extends DecoderMpm<TagLengthString> {

  public TagLengthStringDecoder(final String source) {
    super(source);
  }

  @Override
  protected TagLengthString decode() throws PresentedModeException {
    final TagLengthString result = new TagLengthString();

    while(iterator.hasNext()) {
      final String value = iterator.next();
      result.setTag(TLVUtils.valueOfTag(value));
      result.setValue(TLVUtils.valueOf(value));
    }

    return result;
  }

}

// @formatter:on
