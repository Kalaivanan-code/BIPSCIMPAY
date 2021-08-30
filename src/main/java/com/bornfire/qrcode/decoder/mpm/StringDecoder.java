package com.bornfire.qrcode.decoder.mpm;

import com.bornfire.qrcode.core.exception.PresentedModeException;
import com.bornfire.qrcode.core.utils.TLVUtils;

// @formatter:off
public final class StringDecoder extends DecoderMpm<String> {

  public StringDecoder(final String source) {
    super(source);
  }

  @Override
  protected String decode() throws PresentedModeException {
    final StringBuilder result = new StringBuilder();

    while(iterator.hasNext()) {
      final String value = iterator.next();
      result.append(TLVUtils.valueOf(value));
    }

    return result.toString();
  }

}
// @formatter:on
