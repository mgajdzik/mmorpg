package com.cohesiva.rpg.game.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.cohesiva.rpg.game.core.GameClient;

public class RpgClientActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/cohesiva/rpg/game/resources");
    PlayN.run(new GameClient());
  }
}
