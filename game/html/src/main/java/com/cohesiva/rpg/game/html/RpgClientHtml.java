package com.cohesiva.rpg.game.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.cohesiva.rpg.game.core.GameClient;

public class RpgClientHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("game/");
    PlayN.run(new GameClient());
  }
}
