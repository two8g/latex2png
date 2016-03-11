package com.lft.latex.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;

/**
 * Created by two8g on 16-3-4.
 */
@Service
public class LaTeX2PNG {

	private static String CRC32_TEX = "000000";
	@Value("${latex2png.dir}")
	private String DIR;
	@Value("${latex2png.png.dir}")
	private String PNG_DIR;
	@Value("${latex2png.tmp.dir}")
	private String TMP_DIR;
	@Value("${latex2png.template.file}")
	private String template_file = "tex/chinese_support_1.tex";
	@Value("${latex2png.tag:TEX}")
	private String CONTENT_TAG;

	/**
	 * tex png生成
	 *
	 * @param tex tex字符串
	 * @return png图片路径
	 */
	public String latex2png(String tex) throws Exception {
		if (!new File(DIR).exists())
			new File(DIR).mkdir();
		if (!new File(PNG_DIR).exists())
			new File(PNG_DIR).mkdir();
		if (!new File(TMP_DIR).exists())
			new File(TMP_DIR).mkdir();
		CRC32_TEX = texCode(tex);
		File dir = new File(TMP_DIR + CRC32_TEX);
		if (!dir.exists()) {
			dir.mkdir();
		}
		String fileName = TMP_DIR + CRC32_TEX + "/" + CRC32_TEX;
		tex(tex, fileName, template_file);
		latex(fileName);
		dvips(fileName);
		convertPNG(fileName);
		return finish(fileName);
	}

	//命名编码 CRC32
	private String texCode(String tex) {
		CRC32 crc32 = new CRC32();
		crc32.reset();
		crc32.update(tex.getBytes());
		return Long.toHexString(crc32.getValue());
	}

	//tex文件
	private void tex(String tex, String fileName, String template_file) throws IOException {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(template_file);
		StringBuffer buffer = new StringBuffer();
		String line; // 用来保存每行读取的内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		line = reader.readLine(); // 读取第一行
		while (line != null) { // 如果 line 为空说明读完了
			buffer.append(line); // 将读到的内容添加到 buffer 中
			buffer.append("\n"); // 添加换行符
			line = reader.readLine(); // 读取下一行
		}
		reader.close();
		stream.close();
		tex = buffer.toString().replace(CONTENT_TAG, tex);
		File file = new File(fileName + ".tex");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = tex.getBytes();
		fos.write(bytes);
		fos.close();
	}

	//latex编译
	private void latex(String fileName) throws Exception {
		File file = new File(fileName + ".tex");
		if (!file.exists())
			return;
		String parent = file.getParent();
		String name = file.getName();
		String[] cmd = {"/bin/sh", "-c", "cd " + parent + ";latex -halt-on-error " + name};
		shellProcess(cmd);
	}

	//dvips dvi转eps
	private void dvips(String fileName) throws Exception {
		File file = new File(fileName + ".dvi");
		if (!file.exists())
			return;
		String parent = file.getParent();
		String name = file.getName();
		String out_file = name.replaceAll("\\.dvi$", ".eps");
		String[] cmd = {"/bin/sh", "-c", "cd " + parent + ";dvips -R -E " + name + " -o " + out_file};
		shellProcess(cmd);
	}

	//convert png格式
	private void convertPNG(String fileName) throws Exception {
		File file = new File(fileName + ".eps");
		if (!file.exists())
			return;
		String parent = file.getParent();
		String name = file.getName();
		String out_file = name.replaceAll("\\.eps$", ".png");
		String[] cmd = {"/bin/sh", "-c", "cd " + parent + ";convert -quality 100 -density 150 ps:" + name + " " + out_file};
		shellProcess(cmd);
	}

	private String finish(String fileName) {
		File png_file = new File(fileName + ".png");
		File png_dir = new File(PNG_DIR);
		if (!png_dir.exists()) {
			png_dir.mkdir();
		}
		File target_png_file = new File(PNG_DIR + png_file.getName());
		if (png_file.exists()) {
			if (!target_png_file.exists()) {
				try {
					target_png_file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			nioTransferCopy(png_file, target_png_file);
		}
		return target_png_file.getPath();
	}

	//文件复制
	private void nioTransferCopy(File source, File target) {
		FileChannel in = null;
		FileChannel out = null;
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			inStream = new FileInputStream(source);
			outStream = new FileOutputStream(target);
			in = inStream.getChannel();
			out = outStream.getChannel();
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(inStream);
			close(in);
			close(outStream);
			close(out);
		}
	}

	private void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//shell子进程
	private int shellProcess(String[] cmd) throws Exception {
		Process ps = Runtime.getRuntime().exec(cmd);
		//读取标准输出流
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ps.getInputStream()));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}
		//读取标准错误流
		BufferedReader brError = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
		String err;
		while ((err = brError.readLine()) != null) {
			System.err.println(err);
		}
		//waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
		return ps.waitFor();
	}
}
