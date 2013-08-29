package siddur.common.miscellaneous;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ToolUtil {

	public static byte[] parse(String text){
		StringTokenizer st = new StringTokenizer(text, ",");
		List<Byte> list = new ArrayList<Byte>();
		while(st.hasMoreElements()){
			String s = st.nextToken().trim();
			byte b = (byte)Integer.parseInt(s);
			list.add(b);
		}
		
		byte[] bb = new byte[list.size()];
		for (int i = 0; i < bb.length; i++) {
			bb[i] = list.get(i);
		}
		return bb;
	}
	
	public static byte[] read(File f) throws IOException{
		int size = (int)f.length();
		ByteArrayOutputStream baos;
		ReadableByteChannel r = null;
		WritableByteChannel w = null;
		try {
			baos = new ByteArrayOutputStream(size);
			r = new FileInputStream(f).getChannel();
			w = Channels.newChannel(baos);
			
			ByteBuffer bb = ByteBuffer.allocate(size);
			while(r.read(bb) > 0){
				bb.flip();
				w.write(bb);
				bb.clear();
			}
		} finally{
			if(w != null)
				w.close();
			if(r != null)
				r.close();
		}
		
		return baos.toByteArray();
	}
}