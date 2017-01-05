package com.drunkbot.discord.Users;

import com.drunkbot.discord.WebRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.StatusChangeEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.util.Map;

/**
 * Created by Dylan on 1/4/2017.
 */
public class Watcher implements IListener<StatusChangeEvent> {
    @Override
    public void handle(StatusChangeEvent statusChangeEvent) {
       if ( statusChangeEvent.getUser().getID().equalsIgnoreCase("222105327217147904") && statusChangeEvent.getNewStatus().getStatusMessage().toLowerCase().contains("streaming") ) {
           try {
               ObjectMapper mapper = new ObjectMapper();
               WebRequest request = new WebRequest();
               IGuild guild = statusChangeEvent.getClient().getGuildByID("221334865155457025");

               System.out.println(statusChangeEvent.getNewStatus().getStatusMessage());
               for (IChannel channel : guild.getChannels()) {
                  String resp = request.doTwitchRequest("sodiumch1oride");
                   Map data = mapper.readValue(resp,Map.class);
                   if (data.containsKey("stream") && data.get("stream") != null && data.containsKey("channel") && !data.get("stream").equals("null")) {
                       Map stream =(Map) data.get("stream");
                       Map tcl =(Map) data.get("channel");
                       String msg = statusChangeEvent.getUser().getName() + " is currently streaming: " + stream.get("game") +
                               "\nGo to " + tcl.get("url") + " to watch!";
                       channel.sendMessage(msg).addReaction(guild.getEmojiByName("gitgud"));
                   }
               }
           }catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
}