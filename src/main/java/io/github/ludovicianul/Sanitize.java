package io.github.ludovicianul;

import org.jsoup.safety.Safelist;

public enum Sanitize {
  NONE(Safelist.none()),
  BASIC(Safelist.basic()),
  SIMPLE_TEXT(Safelist.simpleText()),
  BASIC_WITH_IMAGES(Safelist.basicWithImages()),
  RELAXED(Safelist.relaxed());

  private final Safelist safelist;

  Sanitize(Safelist safelist) {
    this.safelist = safelist;
  }

  public Safelist safelist() {
    return this.safelist;
  }
}
