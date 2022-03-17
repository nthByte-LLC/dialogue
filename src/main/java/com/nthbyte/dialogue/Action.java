package com.nthbyte.dialogue;

import com.nthbyte.dialogue.action.context.ActionContext;
import com.nthbyte.dialogue.action.context.LocationContext;

import java.util.function.BiConsumer;

/**
 * Default actions that you can use for different stages of the prompt.
 * @author <a href="linktr.ee/c10_">Caleb Owens</a>
 * @version 1.3.0.0
 */
public final class Action {

    /**
     * Base prompt action. Can run when prompt ends, or on input receive.
     */
    public interface BasePromptAction<T extends ActionContext, U> extends BiConsumer<T, U> {}

    public interface ReceiveInputAction<T extends ActionContext> extends BasePromptAction<T, String> {}

    /**
     * Action that runs at the end of dialogue.
     */
    public interface EndAction<T extends ActionContext> extends BasePromptAction<T, DialogueEndCause> {}

    /**
     * Action that runs at the end of dialogue.
     */
    protected interface DefaultAction<T extends ActionContext> extends BasePromptAction<T, String> {}

    // DEFAULT ACTIONS

    /**
     * No action.
     */
    public static final DefaultAction<ActionContext> NO_ACTION = (context, input) -> {};

    /**
     * Teleports the player to a specific location.
     */
    public static final DefaultAction<LocationContext> TELEPORT = (context, input) -> {
        context.getResponder().teleport(context.getData());
    };

    /**
     * Stores the input in the list of stored inputs. Useful if you want to use previous input in a future prompt, end action, or validation action.
     */
    public static final DefaultAction<ActionContext<String>> STORE_INPUT = ( (context, input) -> {
        String key = context.getData();
        context.getInputStorage().put(key, input);
    });


}
