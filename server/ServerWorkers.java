package concurency.chat.server;

interface ServerWorkers {

    void add(Worker worker);

    void remove(Worker worker);

    Boolean contains(Worker worker);

    void broadcast(String text);

    Worker get(String name);

    Integer count();
}
