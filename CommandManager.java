package com.droopymantis.droopbot.commands;

import com.droopymantis.droopbot.DroopBot;
import com.droopymantis.droopbot.Queue;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommandManager extends ListenerAdapter {

    private HashMap<String, Queue> map = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        String command = event.getName();
        if(command.equalsIgnoreCase("start_queue")) {
            OptionMapping nameOption = event.getOption("name");
            String name = null;
            if (nameOption != null) {
                name = nameOption.getAsString();
            }
            OptionMapping playersOption = event.getOption("players");
            int players = 0;
            if (playersOption != null) {
                players = playersOption.getAsInt();
            }
            boolean is = false;
            for(String i : map.keySet()){
                if(name.equals(i)){
                    event.reply("Cannot create queue " + name + " because queue " + name + " already exists").queue();
                    is = true;
                }
            }
            if(is == false){
                event.reply("Queue " + name + " created for " + players + " players").queue();
                Queue obj = new Queue(players);
                map.put(name, obj);
                map.get(name).print();
            }

        }else if(command.equalsIgnoreCase("join_queue")) {
            String atUser = event.getUser().getAsMention();
            String userid = event.getUser().toString();
            String userid1 = userid.substring(5);
            int index = userid1.indexOf("(id=");
            String user = userid1.substring(0, index);
            OptionMapping nameOption = event.getOption("name");
            String name = null;
            if (nameOption != null) {
                name = nameOption.getAsString();
            }
            for(String i : map.keySet()){
                if(name.equals(i)){
                    if(map.get(i).checkFull() == true){
                        event.reply("You cannot join a full queue").queue();
                    }else if(map.get(i).checkUser(user) == true){
                        event.reply("You are already in the queue " + name).queue();
                    }else{
                        map.get(i).joinQueue(user, atUser);
                        if(map.get(i).checkFull() == true){
                            event.reply("The queue " + name + " has popped!" + map.get(name).getAts()).queue();

                        }else{
                            event.reply("You have joined queue " + name).queue();
                        }
                    }
                }
            }
            map.get(name).print();
            
        }else if(command.equalsIgnoreCase("queue_status")) {
            OptionMapping nameOption = event.getOption("name");
            String name = null;
            if (nameOption != null) {
                name = nameOption.getAsString();
            }
            String str = null;
            for (String i : map.keySet()) {
                if (name.equals(i)){
                    str = map.get(i).getStatus();
                }
            }
            event.reply("Players : " + str).queue();
        }else if(command.equalsIgnoreCase("leave_queue")){
            String atUser = event.getUser().getAsMention();
            String userid = event.getUser().toString();
            String userid1 = userid.substring(5);
            int index = userid1.indexOf("(id=");
            String user = userid1.substring(0, index);
            OptionMapping nameOption = event.getOption("name");
            String name = null;
            if (nameOption != null) {
                name = nameOption.getAsString();
            }
            for(String i : map.keySet()){
                if(name.equals(i)){
                    map.get(i).leaveQueue(user, atUser);
                }
            }
            event.reply("You have left queue " + name).queue();
            map.get(name).print();

        }else if(command.equalsIgnoreCase("stop_queue")){
            OptionMapping nameOption = event.getOption("name");
            String name = null;
            if (nameOption != null) {
                name = nameOption.getAsString();
            }
            for(String i : map.keySet()){
                if(name.equals(i)){
                    map.remove(i);
                }
            }
            event.reply("You stopped queue " + name).queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event){
        List<CommandData> commandData = new ArrayList<>();
        OptionData option1 = new OptionData(OptionType.STRING, "name", "name of queue", true);
        OptionData option2 = new OptionData(OptionType.INTEGER, "players", "number of players", true);
        commandData.add(Commands.slash("start_queue", "starts queue").addOptions(option1, option2));
        commandData.add(Commands.slash("join_queue", "joins a queue").addOptions(option1));
        commandData.add(Commands.slash("queue_status", "lists players in queue").addOptions(option1));
        commandData.add(Commands.slash("leave_queue", "leaves a queue").addOptions(option1));
        commandData.add(Commands.slash("stop_queue", "stops a queue").addOptions(option1));

        event.getGuild().updateCommands().addCommands(commandData).queue();

    }


}
