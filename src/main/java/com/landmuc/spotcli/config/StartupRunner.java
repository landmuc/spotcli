package com.landmuc.spotcli.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.landmuc.spotcli.command.SpotifyCommands;

@Component
// Order ensures this runs before other ApplicationRunners
@Order(Integer.MIN_VALUE)
public class StartupRunner implements ApplicationRunner {

  private final SpotifyCommands spotifyCommands;

  @Autowired
  public StartupRunner(SpotifyCommands spotifyCommands) {
    this.spotifyCommands = spotifyCommands;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    System.out.println("""

        **********
        **********
        **********

            HELLO!
            IMPORTANT NOTE:
            YOU NEED TO AUTHORIZE VIA THE PROVIDED LINK SO YOU CAN USE ALL FUNCTIONALITIES!

          """);
    String result = spotifyCommands.authorize();
    System.out.println(result);

    System.out.println("""

        **********
        **********
        **********
          """);

  }

}
