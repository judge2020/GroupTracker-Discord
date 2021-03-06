package bawbcat.gt.commands;

import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import bawbcat.gt.GroupType;
import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.Colors;

public class GroupInfoCommand extends Command {
    
    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        List<String> validUsers = new ArrayList<String>();
        for (Group g : bot.groups) {
            validUsers.add(g.owner);
        }
        List<String> s = bot.contains(message, validUsers.toArray());
        if (s != null) {
            sendInfo(sender, Group.getGroup(s.get(0)), bot);          
        } else {
            for (Group g : bot.groups) {
                if (g.nicksComing.contains(sender)) {
                    sendInfo(sender, g, bot);
                    break;
                }
            }
        }
        return true;
    }
    
    public void sendInfo(String sender, Group g, GroupTracker bot) {
        String nickList = "";
        int unnamedPlayers = g.players - g.nicksComing.size();
        String othersString = null;
        if (unnamedPlayers > 0) {
            othersString = unnamedPlayers + " " + (unnamedPlayers > 1 ? "others" : "other");
            g.nicksComing.add(othersString);
        }
        for (String s2 : g.nicksComing) {
            nickList = nickList + Colors.DARK_BLUE + s2 + Colors.NORMAL;
            if (g.nicksComing.get(g.nicksComing.size() - 1).equals(s2)) {
                nickList = nickList + ".";
            } else if (g.nicksComing.get(g.nicksComing.size() - 2).equals(s2)) {
                if (g.nicksComing.size() == 2) {
                    nickList = nickList + " and ";
                } else {
                    nickList = nickList + ", and ";
                }
            } else {
                nickList = nickList + ", ";
            }
        }
        if (othersString != null) {
            g.nicksComing.remove(othersString);
        }
        bot.sendNotice(sender, Colors.BROWN + Colors.BOLD + g.owner + Colors.NORMAL + " owns a " + g.getTypeString() + g.getLocationString() + " that has " + Colors.BOLD + g.players + "/" + g.maxPlayers + Colors.NORMAL + " players" + ". Want to join them? " + "Say '" + Colors.BOLD + "join group " + g.owner + Colors.NORMAL  + ".");
        if (g.players > 1) {
            bot.sendNotice(sender, "Who's coming: " + nickList);
        }
        if (g.extraInfo != null) {
            bot.sendNotice(sender, Colors.BROWN + "Owner-set info: " + Colors.NORMAL + g.extraInfo);
        }
    }
}
