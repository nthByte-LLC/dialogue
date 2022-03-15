package com.nthbyte.dialogue;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Object that represents dialogue between the plugin and a player.
 *
 * @author <a href="linktr.ee/c10_">Caleb Owens</a>
 * @version 1.2.0.1
 */
public class Dialogue {

    /**
     * The sequence that you have to type in order to quit the dialogue.
     */
    private String escapeSequence;
    private List<Prompt> prompts;
    /**
     * Actions that are ran when the dialogue ends.
     */
    private BiConsumer<Player, DialogueEndCause> endAction;
    private int currentIndexPrompt = 0;
    /**
     * Repeats the prompt if the input was invalid.
     */
    private boolean repeatPrompt;

    private Dialogue(){}

    private Dialogue(Dialogue.Builder builder){
        this.prompts = builder.prompts;
        this.endAction = builder.endAction;
        this.escapeSequence = builder.escapeSequence;
        this.repeatPrompt = builder.repeatPrompt;
    }

    /**
     * Gets the current prompt this dialogue is on.
     *
     * @return The current prompt.
     */
    public Prompt getCurrentPrompt(){
        return prompts.get(currentIndexPrompt);
    }

    /**
     * Whether the dialogue has more prompts.
     *
     * @return If the dialogue has more prompts.
     */
    public boolean hasMorePrompts(){
        return currentIndexPrompt != prompts.size() - 1;
    }

//    /**
//     * Starts the dialogue with the first prompt.
//     * @param player The player we are conversing with.
//     */
//    public void start(Player player){
//        player.closeInventory();
//        getCurrentPrompt().prompt(player);
//    }

    /**
     * Prompts the next prompt to the player.
     *
     * @param player The player we are prompting.
     */
    public void nextPrompt(Player player){
        currentIndexPrompt++;
        getCurrentPrompt().prompt(player);
    }

    public BiConsumer<Player, DialogueEndCause> getEndAction() {
        return endAction;
    }

    public String getEscapeSequence() {
        return escapeSequence;
    }

    public int getCurrentIndexPrompt() {
        return currentIndexPrompt;
    }

    public boolean shouldRepeatPrompt() {
        return repeatPrompt;
    }

    public static class Builder{

        private boolean repeatPrompt = true;
        private String escapeSequence = "";
        private List<Prompt> prompts = new ArrayList<>();
        private BiConsumer<Player, DialogueEndCause> endAction = (player, dialogueEndCause) -> {};

        public Builder(){}

        public Builder addPrompt(Prompt.Builder prompt){
            this.prompts.add(prompt.build());
            return this;
        }

        public Builder setEscapeSequence(String escapeSequence){
            this.escapeSequence = escapeSequence;
            return this;
        }

        public Builder setEndAction(BiConsumer<Player, DialogueEndCause> action){
            this.endAction = action;
            return this;
        }

        public Builder setRepeatPrompt(boolean repeatPrompt){
            this.repeatPrompt = repeatPrompt;
            return this;
        }

        public Dialogue build(){
            return new Dialogue(this);
        }

    }

}
