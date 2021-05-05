package nz.rausch.contact.configuration.models;

import java.util.List;

public class ClientListConfiguration {
    private List<ClientConfiguration> clients;

    public List<ClientConfiguration> getClients() {
        return clients;
    }

    public void setClients(List<ClientConfiguration> clients) {
        this.clients = clients;
    }
}
