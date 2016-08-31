package br.gui.cc.all;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

public class TestDynamicCompilerShortcut {

	public static void test(
			DynamicCompiler compiler,
			String programFile, 
			String[] vars) throws Exception {
		
		
		test(compiler, programFile, vars, null, 4);
	}
	
	public static void test(
			DynamicCompiler compiler,
			String programFile, 
			String[] vars,
			Map<String, Object> params,
			int level) throws Exception {
		
		final String expectedCode =
				new String(Files.readAllBytes(Paths.get(programFile+"_mips.s")));
		
		TestDynamicCompiler test = new TestDynamicCompiler(compiler);		
		test.setParam(TestDynamicCompiler.PARAM_ASSERT_SYMBOL_TABLE_VARS, vars);
		test.setParam(TestDynamicCompiler.PARAM_ASSERT_EXPECTED_CODE, expectedCode);
		setParams(test, params);
		test.run(programFile, level);
	}
	
	private static void setParams(TestDynamicCompiler test, Map<String, Object> params) {
		if (params != null) {
			for (Entry<String, Object> entry : params.entrySet()) {
				test.setParam(entry.getKey(), entry.getValue());
			}
		}
	}
	
}
