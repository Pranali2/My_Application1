package com.example.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.UserAdapter;
import com.example.myapplication.Model.Chat;
import com.example.myapplication.Model.User;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter_hope;
    private List<User> mUsers;

    private FirebaseUser fuser;
    private DatabaseReference reference;
    private List<String> userList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView= view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        userList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat_hope=snapshot.getValue(Chat.class);
                    if (chat_hope.getSender().equals(fuser.getUid())){
                        userList.add(chat_hope.getReceiver());
                    }
                    if (chat_hope.getReceiver().equals(fuser.getUid())){
                        userList.add(chat_hope.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
    private void readChats(){
        mUsers=new ArrayList<>();

        reference =FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user_hope=snapshot.getValue(User.class);
                    for (String id: userList){
                        try {
                            if (user_hope.getId().equals(id)) {
                                if (mUsers.size() != 0) {
                                    for (User user_hope1 : mUsers) {
                                        try {
                                            if (!user_hope.getId().equals(user_hope1.getId())) {
                                                mUsers.add(user_hope);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }

                                } else {
                                    mUsers.add(user_hope);
                                }
                            }
                        }catch(Exception e){}
                    }
                    userAdapter_hope=new UserAdapter(getContext(),mUsers);
                    recyclerView.setAdapter(userAdapter_hope);
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
