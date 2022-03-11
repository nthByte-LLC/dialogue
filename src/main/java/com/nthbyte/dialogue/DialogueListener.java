package com.nthbyte.dialogue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;

public class DialogueListener implements Listener {

    private JavaPlugin plugin;

    public DialogueListener(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){

        DialogueManager dialogueManager = DialogueAPI.getDialogueManager();
        Set<UUID> playersBeingPrompted = dialogueManager.getPlayersInPrompt().keySet();

        // Blocks all messages for recipients that are being prompted at the moment.
        // Players that are being prompted shouldn't receive any messages from other players.
        e.getRecipients().removeIf(recipient -> playersBeingPrompted.contains(recipient.getUniqueId()));

        Player player = e.getPlayer();
        if(dialogueManager.isConversing(player)){
            e.setCancelled(true);
            Dialogue dialogue = dialogueManager.getDialogue(player);
            fireReceiveInputEvent(player, dialogue, e.getMessage());
        }

    }

    /*
        Another way to deal with the receiving of input if you don't want to use the consumer.
     */

    @EventHandler
    public void onReceiveInput(ReceiveInputEvent e) {

//        Prompt prompt = e.getPrompt();
//
//        if (!prompt.getId().equalsIgnoreCase("creation-tier")) return;
//
//        int tier = (int) e.getInput();
//        Player player = e.getPlayer();
//        Toady.Builder currentSession = plugin.getToadyManager().getToadyCreationSessions().get(player.getUniqueId());
//        currentSession.setTier(tier);
//
//        player.sendMessage(StringUtils.colorString("&bThe tier has been set to "));

    }

    @EventHandler
    public void onReceiveInputOther(ReceiveInputEvent e){
        Prompt prompt = e.getPrompt();
        String input = e.getInput();
        prompt.getOnReceiveInputAction().accept(input);
    }

    private void fireReceiveInputEvent(Player player, Dialogue dialogue, String input){

        Prompt prompt = dialogue.getCurrentPrompt();
        PromptType promptType = prompt.getType();

        DialogueManager dialogueManager = DialogueAPI.getDialogueManager();
        if(input.equalsIgnoreCase(dialogue.getEscapeSequence())){
            dialogueManager.endDialogue(player);
            return;
        }

        if(!InputValidator.isValidInput(promptType, input)){
            player.sendMessage(Utils.tr("&cThis is not valid input. The input type should be " + promptType));
            prompt.prompt(player);
            return;
        }

        Bukkit.getPluginManager().callEvent(new ReceiveInputEvent(player, prompt, input));

        if(dialogue.hasMorePrompts()){
            dialogue.nextPrompt(player);
        }else{
            dialogueManager.endDialogue(player);
        }

    }

}