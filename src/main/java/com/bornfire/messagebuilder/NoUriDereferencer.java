package com.bornfire.messagebuilder;

import java.io.InputStream;

import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.XMLSignatureFactory;

public class NoUriDereferencer implements URIDereferencer{
		private InputStream inputStream;

		public NoUriDereferencer(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public Data dereference(URIReference uriRef, XMLCryptoContext ctx) throws URIReferenceException {
			if (uriRef.getURI() != null) {
				URIDereferencer defaultDereferencer = XMLSignatureFactory.getInstance("DOM").getURIDereferencer();
				return defaultDereferencer.dereference(uriRef, ctx);
			}
			Data data = new OctetStreamData(inputStream);
			return data;
		}
	
}
