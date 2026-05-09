package com.example.fitrack.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitrack.activities.AddClientActivity;
import com.example.fitrack.activities.ClientProfileActivity;
import com.example.fitrack.adapters.ClientAdapter;
import com.example.fitrack.databinding.FragmentDashboardBinding;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.models.Client;
import com.example.fitrack.repositories.ClientRepository;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.viewmodels.DashboardViewModel;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private ClientAdapter adapter;
    private final ClientRepository clientRepository = new ClientRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new DashboardViewModel();
        adapter = new ClientAdapter(new ClientAdapter.OnClientActionListener() {
            @Override
            public void onClientClick(Client client) {
                Intent intent = new Intent(requireContext(), ClientProfileActivity.class);
                intent.putExtra(Constants.EXTRA_CLIENT_ID, client.getId());
                intent.putExtra(Constants.EXTRA_CLIENT_NAME, client.getFullName());
                startActivity(intent);
            }

            @Override
            public void onClientLongClick(Client client) {
                showClientActions(client);
            }
        });

        binding.clientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.clientsRecyclerView.setAdapter(adapter);

        binding.addClientFab
                .setOnClickListener(v -> startActivity(new Intent(requireContext(), AddClientActivity.class)));
        binding.clientSearch.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.filterClients(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.filterClients(newText);
                return true;
            }
        });

        viewModel.getClients().observe(getViewLifecycleOwner(), clients -> adapter.submitList(clients));
        viewModel.loadClients();
    }

    private void showClientActions(final Client client) {
        new AlertDialog.Builder(requireContext())
                .setTitle(client.getFullName())
                .setItems(new CharSequence[] { "Edit", "Delete" }, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(requireContext(), AddClientActivity.class);
                        intent.putExtra("client", client);
                        startActivity(intent);
                    } else {
                        confirmDelete(client);
                    }
                })
                .show();
    }

    private void confirmDelete(final Client client) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Client")
                .setMessage("Remove " + client.getFullName() + " from the system?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clientRepository.deleteClient(client.getId(), new ActionCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(requireContext(), "Client deleted", Toast.LENGTH_SHORT).show();
                                viewModel.loadClients();
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
