# hq

Small utility to parse and grep HTML files. It
uses [CSS selectors](https://www.w3schools.com/cssref/css_selectors.asp) or [XPath Selectors](https://www.w3schools.com/xml/xpath_intro.asp) to extract HTML elements.

# Usage

```bash
hq - command line HTML elements finder; version 1.0.0

Usage: hq [-hptV] [-a=<attribute>] [-f=<FILE>] [-o=<FILE>] [-s=<POLICY>] [-x=<XPATH>] <selector>
          [COMMAND]
      <selector>            The CSS selector
  -a, --attribute=<attribute>
                            Return only this attribute from the selected HTML elements
  -f, --file=<FILE>         The HTML input file. If not supplied it will default to stdin
  -h, --help                Show this help message and exit.
  -o, --output=<FILE>       The output file. If not supplied it will default to stdout
  -p, --pretty              Force pretty printing the output
  -r, --remove=<SELECTOR>   Remove nodes matching given selector
  -s, --sanitize=<POLICY>   Sanitizes the html input according to the given policy
  -t, --text                Display only the inner text of the selected HTML top element
  -V, --version             Print version information and exit.
  -x, --xpath=<XPATH>       Supply an XPath selector instead of CSS
Commands:
  generate-completion  Generate bash/zsh completion script for hq.

```

# Installation

## Homebrew

```
> brew tap ludovicianul/tap
> brew install hq
```

## Manual

`hq` is compiled to native code using GraalVM. Check
the [release page](https://github.com/ludovicianul/hq/releases/) for binaries (Linux,
MacOS, uberjar).

After download, you can make `hq` globally available:

```bash
sudo cp hq-macos /usr/local/bin/hq
```

The uberjar can be run using `java -jar hq`. Requires Java 11+.

# Autocomplete
Run the following commands to get autocomplete:

```bash
hq generate-completion >> hq_autocomplete

source hq_autocomplete
```

# HTML Sanitizing
`hq` can sanitize html output. Supported modes are: `NONE, BASIC, SIMPLE_TEXT, BASIC_WITH_IMAGES, RELAXED`. 

This is how sanitization works: 

| Policy | Details |
| ------- | ------- | 
| `NONE` | Allows only text nodes: all HTML will be stripped. |
| `BASIC` | Allows a fuller range of text nodes: `a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, span, strike, strong, sub, sup, u, ul`, and appropriate attributes. Does not allow images.|
| `SIMPLE_TEXT` | Allows only simple text formatting: `b, em, i, strong, u`. All other HTML (tags and attributes) will be removed.| 
| `BASIC_WITH_IMAGES` | Allows the same text tags as `BASIC`, and also allows `img` tags, with appropriate attributes, with `src` pointing to `http` or `https`.
| `RELAXES` | Allows a full range of text and structural body HTML: `a, b, blockquote, br, caption, cite, code, col, colgroup, dd, div, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, span, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul`.|

# Examples

Get the `div` with id `mainLeaderboard`:

```
➜ curl -s https://www.w3schools.com/cssref/css_selectors.asp | hq "#mainLeaderboard"

<div id="mainLeaderboard" style="overflow:hidden;"> <!-- MainLeaderboard--> <!--<pre>main_leaderboard, all: [728,90][970,90][320,50][468,60]</pre>-->
 <div id="adngin-main_leaderboard-0"></div> <!-- adspace leaderboard -->
</div>

```

Get the text inside an article:

```
➜ curl -s https://ludovicianul.github.io/2021/07/16/unicode_language_version/ | hq '.post' -t

Make sure you know which Unicode version is supported by your programming language version 16 Jul 2021 While enhancing CATS I recently added a feature to send requests that include 
single and multi code point emojis. This is a single code point emoji: 🥶, which can be represented in Java as the \uD83E\uDD76 string. The test case is simple: inject emojis within 
strings and expect that the REST endpoint will sanitize the input and remove them entirely (I appreciate this might not be a valid case for all APIs, this is why the behaviour is 
configurable in CATS, but not the focus of this article). I usually recommend that any REST endpoint should sanitize input before validating it and remove special characters. 
A typical regex for this would be [\p{C}\p{Z}\p{So}]+ (although you should enhance it to allow spaces between words), which means: p{C} - match Unicode invisible Control 
Chars (\u000D - carriage return for example) ...
...
```

Sanitize the html according to the [specified policy](#html-sanitizing):
```
 ➜ curl -s https://ludovicianul.github.io/2021/07/16/unicode_language_version/ | ./hq html -s=BASIC -p

<html>
    <head></head>
    <body>
        <a href="https://ludovicianul.github.io/" rel="nofollow"> m's blog </a>
        <p>practical thoughts about software engineering</p>
        <a href="https://ludovicianul.github.io/" rel="nofollow">Home</a>
        <a rel="nofollow">About</a>
        <a href="https://github.com/ludovicianul" rel="nofollow">GitHub</a>
        <p>© 2021. All rights reserved.</p>
        Make sure you know which Unicode version is supported by your programming language version
        <span>16 Jul 2021</span>
        <p>
...
    </body>
</html>
```

Get all `href` attributes from a given page:

```shell
 ➜ curl -s https://ludovicianul.github.io | hq "*" -a "href"
http://gmpg.org/xfn/11
https://ludovicianul.github.io/public/css/poole.css
https://ludovicianul.github.io/public/css/syntax.css
https://ludovicianul.github.io/public/css/hyde.css
https://fonts.googleapis.com/css?family=PT+Sans:400,400italic,700|Abril+Fatface
https://ludovicianul.github.io/public/apple-touch-icon-144-precomposed.png
https://ludovicianul.github.io/public/favicon.ico
/atom.xml
https://ludovicianul.github.io/
https://ludovicianul.github.io/
/about/
...
```

# Resources

- [Universal selector in CSS](https://www.scaler.com/topics/universal-selector-in-css/)
- [HTML elements](https://developer.mozilla.org/en-US/docs/Web/HTML/Element)
