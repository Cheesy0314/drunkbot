package com.drunkbot.discord.events;

import com.drunkbot.discord.DrunkBot;
import org.apache.commons.lang3.exception.ExceptionUtils;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.audio.AudioPlayer;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by Dylan on 11/25/2016.
 */
public class AdminListener implements IListener<MessageReceivedEvent> {
    public void handle(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
//        if message.getGuild().
        IGuild guild = message.getGuild();
        List<IRole> rolesForAuthor = message.getAuthor().getRolesForGuild(guild);
        boolean valid = false;

        for (IRole currentRole : rolesForAuthor) {
            if (valid == false) {
                if (currentRole.getID().equalsIgnoreCase("221379836776546304") || currentRole.getID().equalsIgnoreCase("231254104545034240")) {
                    valid = true;
                }
            }
        }
        try {
                if (message.getContent().contains("--kick" ) && valid) {
                    String user = message.getContent().substring(7);

                    message.reply("Kick: " + user);
                    try {

                        IUser userObj = guild.getUsersByName(user, true).get(0);
                        if (userObj.getConnectedVoiceChannels().size() == 0) {
                            message.getGuild().kickUser(guild.getUserByID(userObj.getID()));
                        } else {
                            userObj.getConnectedVoiceChannels().get(0).join();
                            AudioPlayer player = AudioPlayer.getAudioPlayerForGuild(guild);
                            player.clear();
                            player.queue(new File("src\\resources\\surprise.mp3"));
                            int i = 0;
                            while (player.getPlaylistSize() > 0) {
                                i++;
                            }
                            DrunkBot.logger.info("Took this long: " + i);
                            event.getClient().getOurUser().getConnectedVoiceChannels().get(0).leave();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        message.reply("Invalid user....");
                        message.addReaction(guild.getEmojiByName(":gitgud:"));
                    }

                } else if (message.getContent().contains("--ban") && valid) {
                    String user = message.getContent().substring(6);

                    message.reply("Ban: " + user);
                    try {
                        IUser userObj = guild.getUsersByName(user, true).get(0);
                        message.getGuild().kickUser(guild.getUserByID(userObj.getID()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.getContent().contains("--soft") && valid) {
                    String user = message.getContent().substring(7);
                    message.reply("Soft Ban: " + user);
                    try {
                        IUser userObj = guild.getUsersByName(user, true).get(0);
                        message.getGuild().kickUser(guild.getUserByID(userObj.getID()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message.getContent().contains("--prune") && valid) {
                    try {
                        int amount = Integer.parseInt(message.getContent().substring(8));
                        IChannel channel = message.getChannel();
                        if (amount > channel.getMessages().size()) {
                            amount = channel.getMessages().size();
                        }
                        int messageIndex = channel.getMessages().indexOf(message);
                        channel.getMessages().deleteAfter(messageIndex, amount);
                    } catch (Exception e) {
//                        message.reply("I'm sorry... I cannot do that right now.");
                        e.printStackTrace();
                    }
                } else if (valid == false && message.getContent().startsWith("--")) {
                    String content = message.getContent().toLowerCase();
                    if (content.contains("--prune") || content.contains("--kick") || content.contains("--ban") || content.contains("--soft")) {
                        message.reply("You can't do that shit stain");
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
