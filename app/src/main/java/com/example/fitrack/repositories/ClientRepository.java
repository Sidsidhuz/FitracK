package com.example.fitrack.repositories;

import com.example.fitrack.local.LocalStore;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Client;
import com.example.fitrack.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClientRepository {
    public void saveClient(Client client, final ActionCallback callback) {
        String clientId = LocalStore.generateId(Constants.COLLECTION_CLIENTS);
        client.setId(clientId);
        LocalStore.save(Constants.COLLECTION_CLIENTS, clientId, client);
        callback.onSuccess();
    }

    public void updateClient(Client client, final ActionCallback callback) {
        if (client.getId() == null || client.getId().isEmpty()) {
            callback.onError("Client id is missing");
            return;
        }
        LocalStore.save(Constants.COLLECTION_CLIENTS, client.getId(), client);
        callback.onSuccess();
    }

    public void deleteClient(String clientId, final ActionCallback callback) {
        LocalStore.delete(Constants.COLLECTION_CLIENTS, clientId);
        callback.onSuccess();
    }

    public void getClients(final DataCallback<List<Client>> callback) {
        List<Client> clients = LocalStore.getAll(Constants.COLLECTION_CLIENTS);
        callback.onSuccess(clients);
    }

    public void searchClients(String query, final DataCallback<List<Client>> callback) {
        getClients(new DataCallback<List<Client>>() {
            @Override
            public void onSuccess(List<Client> clients) {
                if (query == null || query.trim().isEmpty()) {
                    callback.onSuccess(clients);
                    return;
                }
                String keyword = query.trim().toLowerCase();
                List<Client> filtered = new ArrayList<>();
                for (Client client : clients) {
                    String name = client.getFullName() == null ? "" : client.getFullName().toLowerCase();
                    String goal = client.getGoal() == null ? "" : client.getGoal().toLowerCase();
                    if (name.contains(keyword) || goal.contains(keyword)) {
                        filtered.add(client);
                    }
                }
                callback.onSuccess(filtered);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void sortByName(List<Client> clients) {
        Collections.sort(clients, new Comparator<Client>() {
            @Override
            public int compare(Client left, Client right) {
                String leftName = left.getFullName() == null ? "" : left.getFullName();
                String rightName = right.getFullName() == null ? "" : right.getFullName();
                return leftName.compareToIgnoreCase(rightName);
            }
        });
    }
}
