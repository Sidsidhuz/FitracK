package com.example.fitrack.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Client;
import com.example.fitrack.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private final ClientRepository clientRepository = new ClientRepository();
    private final MutableLiveData<List<Client>> clients = new MutableLiveData<>(new ArrayList<Client>());
    private final List<Client> cache = new ArrayList<>();

    public LiveData<List<Client>> getClients() {
        return clients;
    }

    public void loadClients() {
        clientRepository.getClients(new DataCallback<List<Client>>() {
            @Override
            public void onSuccess(List<Client> data) {
                cache.clear();
                cache.addAll(data);
                clients.setValue(new ArrayList<Client>(cache));
            }

            @Override
            public void onError(String message) {
                clients.setValue(new ArrayList<Client>());
            }
        });
    }

    public void filterClients(String query) {
        if (query == null || query.trim().isEmpty()) {
            clients.setValue(new ArrayList<Client>(cache));
            return;
        }
        String keyword = query.trim().toLowerCase();
        List<Client> filtered = new ArrayList<>();
        for (Client client : cache) {
            String name = client.getFullName() == null ? "" : client.getFullName().toLowerCase();
            String goal = client.getGoal() == null ? "" : client.getGoal().toLowerCase();
            if (name.contains(keyword) || goal.contains(keyword)) {
                filtered.add(client);
            }
        }
        clients.setValue(filtered);
    }
}

