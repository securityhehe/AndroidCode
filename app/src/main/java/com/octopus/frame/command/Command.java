package com.octopus.frame.command;


interface Receiver {
    void action();
}

abstract class Command{

    protected Receiver receiver;
    public Command(Receiver receiver) {
        this.receiver = receiver;
    }
    abstract public void execute();

}

class ConcreteCommand extends Command{

    public ConcreteCommand(Receiver receiver) {
        super(receiver);
    }

    @Override
    public void execute() {
        receiver.action();
    }

}



class Invoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }

    public static void main(String[] args) {
        Receiver r = new BReceive();
        Command c = new ConcreteCommand(r);

        Invoker i = new Invoker();
        i.setCommand(c);
        i.executeCommand();
    }
}


class AReceive implements Receiver{

    @Override
    public void action() {
        System.out.println("Arecevice") ;
    }

}



class BReceive implements Receiver{

    @Override
    public void action() {
        System.out.println("Brecevice") ;
    }

}

