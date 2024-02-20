package com.lic.epgs.gratuity.policy.helper;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;

public class DocumentStorageHelp {
	public static byte[] compressImage(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setLevel(Deflater.BEST_COMPRESSION);
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] tmp = new byte[4 * 1024];
		while (!deflater.finished()) {
			int size = deflater.deflate(tmp);
			outputStream.write(tmp, 0, size);
		}
		try {
			outputStream.close();
		} catch (Exception ignored) {
		}
		return outputStream.toByteArray();
	}

//	 public static DocumentStorageEntity dtoToEntity(DocumentStoragedto dto) {
//		 
//		 
//			return new ModelMapper().map(dto, DocumentStorageEntity.class);
//		}

	
	

}
