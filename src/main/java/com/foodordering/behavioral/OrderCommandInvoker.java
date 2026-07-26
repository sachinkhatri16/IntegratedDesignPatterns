package com.foodordering.behavioral;

/**
 * Invoker for Command pattern
 */
public class OrderCommandInvoker {
    private java.util.Stack<OrderCommand> executedCommands = new java.util.Stack<>();

    public void executeCommand(OrderCommand command) {
        command.execute();
        executedCommands.push(command);
    }

    public void undoLastCommand() {
        if (!executedCommands.isEmpty()) {
            OrderCommand command = executedCommands.pop();
            command.undo();
        }
    }

    public java.util.Stack<OrderCommand> getCommandHistory() {
        return executedCommands;
    }
}
