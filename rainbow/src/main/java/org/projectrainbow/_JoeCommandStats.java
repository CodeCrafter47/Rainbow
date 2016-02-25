package org.projectrainbow;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class _JoeCommandStats {

    public int sameMessageCount = 0;
    public String lastMessage;
    public long startTime;
    public long lastTime;
    public int count;
    private static HashMap<String, Integer> kickCount = new HashMap();
    private static ConcurrentHashMap<String, _JoeCommandStats> cmdHist = new ConcurrentHashMap();

    public _JoeCommandStats(String msg, long curMS) {
        this.StartOver(msg, curMS);
    }

    public static String HandleNewCommand(String pName, String msg) {
        if (!_DiwUtils.DoSpamKick) {
            return null;
        } else {
            long curMS = System.currentTimeMillis();
            _JoeCommandStats stats = (_JoeCommandStats) cmdHist.get(pName);

            if (stats == null) {
                cmdHist.put(pName, new _JoeCommandStats(msg, curMS));
            } else {
                String res = stats.JoeNewCommand(msg, curMS, pName);

                if (res != null) {
                    return res;
                }

                cmdHist.put(pName, stats);
            }

            return null;
        }
    }

    public void StartOver(String msg, long curMS) {
        this.lastMessage = msg;
        this.startTime = curMS;
        this.lastTime = curMS;
        this.sameMessageCount = 1;
        this.count = 1;
    }

    public String JoeNewCommand(String msg, long now, String pName) {
        if (msg == null) {
            return null;
        } else {
            String[] skips = new String[]{
                    "help", "plot", "worth", "sell",
                    "buy", "ec", "bp", "worth"};
            String msgLower = msg.toLowerCase();

            for (int doKick = 0; doKick < skips.length; ++doKick) {
                if (msgLower.startsWith("/" + skips[doKick])) {
                    return null;
                }
            }

            if ((now - this.lastTime) / 1000L < 3L
                    && (now - this.lastTime) / 1000L > -100L) {
                String chatsPerSecond;

                if (this.lastMessage.equals(msg)) {
                    String var11 = _DiwUtils.JustLettersAndNumbersUpperCase(
                            this.lastMessage);

                    chatsPerSecond = _DiwUtils.JustLettersAndNumbersUpperCase(
                            msg);
                    if (this.lastMessage.equals(msg.toUpperCase())
                            && msg.length() > 4
                            && (msg.charAt(0) != 47 || msg.startsWith("/msg ")
                            || msg.startsWith("/r "))) {
                        String logmsg = String.format(
                                "*** SPAM KICK: Repeated CAPS by %s: %s",
                                new Object[]{pName, msg});

                        System.out.println(
                                "--------------------------------------------");
                        System.out.println(logmsg);
                        System.out.println(
                                "--------------------------------------------");
                        return "Repeated CAPS LOCK";
                    }

                    ++this.sameMessageCount;
                }

                boolean var12 = false;

                if (this.sameMessageCount >= 4) {
                    var12 = true;
                    chatsPerSecond = String.format(
                            "*** SPAM KICK: %s: 4 Repeats: %s",
                            new Object[]{pName, msg});
                    System.out.println(
                            "--------------------------------------------");
                    System.out.println(chatsPerSecond);
                    System.out.println(
                            "--------------------------------------------");
                    return "Kicked for spamming";
                } else {
                    this.lastTime = now;
                    ++this.count;
                    double var13 = (double) this.count
                            / (((double) (now - this.startTime) + 0.001D)
                            / 1000.0D);

                    if (this.count >= 4) {
                        if (this.count <= 5 && var13 >= 1.0D) {
                            var12 = true;
                        } else if (this.count > 5 && this.count <= 7
                                && var13 >= 0.66D) {
                            var12 = true;
                        } else if (this.count > 7 && var13 >= 0.33D) {
                            var12 = true;
                        }

                        if (var12) {
                            String logmsg1 = String.format(
                                    "*** SPAM KICK: %s: %.2f cmd/sec. n=%d : %s",
                                    new Object[]{
                                            pName, Double.valueOf(var13),
                                            Integer.valueOf(this.count), msg});

                            System.out.println(
                                    "--------------------------------------------");
                            System.out.println(logmsg1);
                            System.out.println(
                                    "--------------------------------------------");
                            return "Kicked for spamming";
                        }
                    }

                    return null;
                }
            } else {
                this.StartOver(msg, now);
                return null;
            }
        }
    }
}
