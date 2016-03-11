package com.lft.latex.web;

import com.lft.latex.service.LaTeX2PNG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by two8g on 16-3-4.
 */
@RestController
public class LaTex2PNGController {
	@Autowired
	private LaTeX2PNG laTeX2PNG;

	@RequestMapping("/")
	public void tex2png(String tex, HttpServletResponse response) throws Exception {
		String png_path = laTeX2PNG.latex2png(tex);
		if (new File(png_path).exists()) {
			FileInputStream inputStream = new FileInputStream(png_path);
			byte[] png = new byte[inputStream.available()];
			int i = inputStream.read(png);
			if (i > 0) {
				response.setContentType("image/png");
				response.getOutputStream().write(png);
			} else {
				
			}
		}
	}
}
