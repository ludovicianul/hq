# hq
Small utility to parse and grep HTML files. It uses [CSS selectors](https://www.w3schools.com/cssref/css_selectors.asp) to extract HTML elements.

# Usage
```bash
Usage: hq [-htV] [-a=<attribute>] [-f=<FILE>] [-o=<FILE>] <selector>
      <selector>        The CSS selector
  -a, --attribute=<attribute>
                        Return only this attribute from the selected HTML
                          elements
  -f, --file=<FILE>     The HTML input file. If not supplied it will default to
                          stdin
  -h, --help            Show this help message and exit.
  -o, --output=<FILE>   The output file. If not supplied it will default to
                          stdout
  -t, --text            Display only the inner text of the selected HTML top
                          element
  -V, --version         Print version information and exit.

```

# Installation
`hq` is compiled to native code using GraalVM. Check the [release page](https://github.com/ludovicianul/hq/releases/tag/hq-1.0.0) for binaries (Linux, MacOS) or uberjar.

You can rename the downloaded version to `hq` (the `jar` version also) and execute it using `./hq` (or add it the `PATH` variable so that you can ditch the `./`).

The uberjar can be run using `java -jar hq`. Requires Java 11+.

# Examples
```
➜ curl -s https://www.w3schools.com/cssref/css_selectors.asp | ./hq "#mainLeaderboard"

<div id="mainLeaderboard" style="overflow:hidden;"> <!-- MainLeaderboard--> <!--<pre>main_leaderboard, all: [728,90][970,90][320,50][468,60]</pre>-->
 <div id="adngin-main_leaderboard-0"></div> <!-- adspace leaderboard -->
</div>

```