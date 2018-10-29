package de.karmell.discord.bot.core;

/**
 * Simple command which responds with a predefined text to an invoke.
 */
public class SimpleCommand {
    private String invoke;
    private String response;

    public SimpleCommand() {}

    public SimpleCommand(String invoke, String response) {
        this.invoke = invoke;
        this.response = response;
    }

    public String getInvoke() {
        return invoke;
    }

    public void setInvoke(String invoke) {
        this.invoke = invoke;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
