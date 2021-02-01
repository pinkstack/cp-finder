package com.pinkstack.sweetjava;

/**
 * In other to be compliant with the rules of competition and the
 * requirement that Java is used I've wrote this class here.
 * The rest of the codebase is written in modern Scala.
 *
 * @author otobrglez
 */
public class Banner {
    public static String showBanner(String hostString, Integer port) {
        return "🐇 Booting cp-finder server. 🐇 " +
                "Available on http://" + hostString + ":" + port;
    }
}
