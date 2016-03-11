# LaTeX2PNG

	TeX(LaTeX)文本输出为PNG格式图片

## Examples

## 实现方法

1. *.tex文件	字符串拼接

```TeX
\documentclass{article}
\pagestyle{empty}
\usepackage{amsmath}
\usepackage{amssymb}
\usepackage{amsxtra}
\usepackage{CJK}
\begin{document}
\begin{CJK}{UTF8}{gbsn}
求解一元二次方程$y=ax^{2}+bx+c$$(a\neq0)$,答案如下.
$$
	x=\frac{-b\pm\sqrt{b^{2}-4ac}}{2a}
$$
\end{CJK}
\end{document}
```

- TeX模板

```TeX
\documentclass{article}
\pagestyle{empty}
\usepackage{amsmath}
\usepackage{amssymb}
\usepackage{amsxtra}
\usepackage{CJKutf8}
\begin{document}
\begin{CJK}{UTF8}{gbsn}
TeX
\end{CJK}
\end{document}
```

2. `*.tex` -> `*.dvi`	latex程序

```Bash
$ latex *.tex
```

3. `*.dvi` -> `*.eps`	dvips程序

```Bash
$ dvips -R -E *.dvi -o *.eps
```

4. `*.eps` -> `*.png` imagemagick程序

```Bash
$ convert -quality 100 -density 150 ps:*.eps *.png
```

## 依赖软件

1. texlive,latex-cjk-all

2. latex2rtf

3. imagemagick

## 参考资料

* https://en.wikibooks.org/wiki/LaTeX
* http://latex2rtf.sourceforge.net/
* http://homepages.inf.ed.ac.uk/imurray2/code/hacks/latex2png
* http://www.cnblogs.com/dezheng/p/3874434.html

* http://blog.csdn.net/caohaicheng/article/details/22928297