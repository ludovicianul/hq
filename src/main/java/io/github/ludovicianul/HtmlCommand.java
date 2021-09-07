package io.github.ludovicianul;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@QuarkusMain
@Command(
    name = "hq",
    mixinStandardHelpOptions = true,
    version = "hq 1.0.0",
    usageHelpWidth = 100,
    header = "hq - command line HTML elements finder; version 1.0.0\n")
public class HtmlCommand implements Runnable, QuarkusApplication {

  @Inject CommandLine.IFactory factory;

  @Parameters(
      index = "0",
      paramLabel = "<selector>",
      defaultValue = "empty",
      description = "The CSS selector")
  String selector;

  @CommandLine.Option(
      names = {"-a", "--attribute"},
      paramLabel = "<attribute>",
      description = "Return only this attribute from the selected HTML elements")
  String attribute;

  @CommandLine.Option(
      names = {"-f", "--file"},
      paramLabel = "<FILE>",
      description = "The HTML input file. If not supplied it will default to stdin")
  String file;

  @CommandLine.Option(
      names = {"-o", "--output"},
      paramLabel = "<FILE>",
      description = "The output file. If not supplied it will default to stdout")
  String output;

  @CommandLine.Option(
      names = {"-t", "--text"},
      description = "Display only the inner text of the selected HTML top element")
  boolean text;

  @Override
  public void run() {
    try {
      String html;
      if (file == null) {
        html = this.parseSystemIn();
      } else {
        html = this.parseFile();
      }
      this.processHtml(html);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void processHtml(String html) throws IOException {
    Document document = Jsoup.parse(html);
    Elements elements = document.select(selector);
    if (attribute != null) {
      List<String> elementsWithAttribute = elements.eachAttr(attribute);
      this.writeToOutput(
          elementsWithAttribute.stream().collect(Collectors.joining(System.lineSeparator())));
    } else if (text) {
      this.writeToOutput(elements.text());
    } else {
      this.writeToOutput(elements.outerHtml());
    }
  }

  private void writeToOutput(String string) throws IOException {
    if (output == null) {
      System.out.println(string);
    } else {
      Path path = Path.of(output);
      Files.writeString(path, string);
    }
  }

  private String parseInput(Reader reader) throws IOException {
    try (BufferedReader in = new BufferedReader(reader)) {
      return in.lines().collect(Collectors.joining(System.lineSeparator()));
    }
  }

  private String parseFile() throws IOException {
    return this.parseInput(new FileReader(file));
  }

  private String parseSystemIn() throws IOException {
    return this.parseInput(new InputStreamReader(System.in));
  }

  @Override
  public int run(String... args) {
    return new CommandLine(this, factory).execute(args);
  }
}
