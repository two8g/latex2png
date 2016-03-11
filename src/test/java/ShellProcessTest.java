import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by two8g on 16-3-4.
 */
public class ShellProcessTest {
	@Test
	public void shellProcessTest() {
		try {
			//shell 命令
			String[] cmd = {"/bin/sh", "-c", "cd .;ls -l"};
			//运行 shell命令
			Process ps = Runtime.getRuntime().exec(cmd);

			StringBuffer sb = new StringBuffer();
			String line;

			//标准输出
			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			System.out.println(sb.toString());
			//标准错误输出
			br = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
			sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			System.out.println(sb.toString());
			ps.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
