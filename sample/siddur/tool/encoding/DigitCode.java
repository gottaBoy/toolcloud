package siddur.tool.encoding;

import java.util.Arrays;
import java.util.Map;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;

public class DigitCode implements ITool {

	/*
	 * Integer
	 * Integer
	 * Integer
	 */
	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		String s = inputs[0];
		int from = Integer.parseInt(inputs[1]);
		int to = Integer.parseInt(inputs[2]);
		
		int value = Integer.valueOf(s, from);
		String result = Integer.toString(value, to);
		
		return new String[]{result};
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) throws Exception {
		DigitCode d = new DigitCode();
		String [] inputs = new String[]{"ff", "16", "2"};
		System.out.println(Arrays.toString(inputs));
		String [] outputs = d.execute(inputs, null, null);
		System.out.println(outputs[0]);
	}
}
