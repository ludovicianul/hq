# hq

Small utility to parse and grep HTML files. It
uses [CSS selectors](https://www.w3schools.com/cssref/css_selectors.asp) to extract HTML elements.

# Usage

```bash
Usage: hq [-htV] [-a=<attribute>] [-f=<FILE>] [-o=<FILE>] <selector>
      <selector>        The CSS selector
  -a, --attribute=<attribute>
                        Return only this attribute from the selected HTML elements
  -f, --file=<FILE>     The HTML input file. If not supplied it will default to stdin
  -h, --help            Show this help message and exit.
  -o, --output=<FILE>   The output file. If not supplied it will default to stdout
  -t, --text            Display only the inner text of the selected HTML top element
  -V, --version         Print version information and exit.

```

# Installation

`hq` is compiled to native code using GraalVM. Check
the [release page](https://github.com/ludovicianul/hq/releases/tag/hq-1.0.0) for binaries (Linux,
MacOS, uberjar).

After download, you can make `hq` globally available:

```bash
sudo cp hq-macos /usr/local/bin/hq
```

The uberjar can be run using `java -jar hq`. Requires Java 11+.

# Examples

Get the div with id `mainLeaderboard`:

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