package com.dreamteam.httprequest.GroupList.View;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dreamteam.httprequest.Group.Entity.GroupData.Group;
import com.dreamteam.httprequest.GroupList.Presenter.GroupsPresenter;
import com.dreamteam.httprequest.GroupList.Protocols.GroupsViewInterface;
import com.dreamteam.httprequest.GroupList.View.GroupAdapter;
import com.dreamteam.httprequest.GroupList.View.RecyclerItemClickListener;
import com.dreamteam.httprequest.Interfaces.OnBackPressedListener;
import com.dreamteam.httprequest.MainActivity;
import com.dreamteam.httprequest.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class GroupsListFragment extends Fragment implements GroupsViewInterface, OnBackPressedListener {

    //TODO: не забудь перенести обработчик клика в проект (class RecyclerItemClickListener и обработка outputGroupsView)

    private RecyclerView groupsRecyclerView;

    private GroupAdapter adapter;

    MainActivity activity;

    public GroupsPresenter groupsPresenter;
    MenuInflater inflater;
    Menu menu;

    SparseBooleanArray checkedArray = new SparseBooleanArray();

//    boolean deleteOn;

    ArrayList<Group> groups = new ArrayList<>();

    public GroupsListFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_list, container, false);
        groupsRecyclerView = view.findViewById(R.id.groups_recycler_view);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupsPresenter.getGroups("328d21d2-9797-4802-9f5d-0e0b3f204866");//здесь ID User'а
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activity = (MainActivity) getActivity();
        groupsPresenter = new GroupsPresenter(this, activity);
//        deleteOn = false;
        adapter = new GroupAdapter(groups);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.inflater = inflater;
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
//        if (!deleteOn) {
//            inflater.inflate(R.menu.group_list_controller, menu);
//        } else {
//            inflater.inflate(R.menu.delete_group_list_controller, menu);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//слушатель на нажатие кнопки edit
        switch (item.getItemId()){
            case R.id.add_item_edit:
                groupsPresenter.showAddGroup();
                break;

                //запрос списка с checkBox
            case R.id.delete_item_edit:
                String TYPE = "Delete";
                groupsPresenter.showSelectedList(groups,activity, TYPE);

//                adapter.animationOn();
//                menu.clear();
//                inflater.inflate(R.menu.delete_group_list_controller, menu);
//
//                deleteOn = true;
                break;

//            case R.id.remove_select_list_edit:
//                ArrayList<Group> deleteGroups = new ArrayList<>();
//               if (deleteOn){
//                   for (int i = 0; i < adapter.groupCollection.size()-1; i++){
//                       if (checkedArray.valueAt(i) == true){
//                           deleteGroups.add(adapter.groupCollection.get(i));
//                       }
//                   }
//                   groupsPresenter.deleteGroups (deleteGroups);
//               }
//               break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void outputGroupsView(final ArrayList<Group> groupCollection) {//отправка полученного списка групп на отображение в адаптере
        groups = groupCollection;
        adapter.groupCollection = groupCollection;
        groupsRecyclerView.setAdapter(adapter);
        final Context context = getContext();

        for (int i = 0; i < groupCollection.size(); i++){
            checkedArray.put(i, false);
        }

        //TODO: внедрить измененное состояние для флажка и синхронизировать недавно обновленное состояние с флагом isChecked текущего объекта. Когда вы связываете свой держатель вида, проверьте, является ли флаг истинным или ложным, и обновите макет в соответствии с флагом.
        groupsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), groupsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                MainActivity activityAction = (MainActivity) getActivity();
//                if(!deleteOn) {
                    activityAction = (MainActivity)context;
                    activityAction.getGroup(groupCollection.get(position).id);

//                    adapter.notifyItemChanged(position);
//                } else if (deleteOn){
//                    Log.i("Shhhhiiiit", String.valueOf(position));
//                    if (!checkedArray.valueAt(position)){
//                        checkedArray.append(position, true);
//                    } else {
//                        checkedArray.append(position, false);
//                    }
//
//                    adapter.checkArray = checkedArray;
//                    adapter.notifyItemChanged(position);


//                    if (adapter.groupHolders.get(position).checkBox.isChecked()) {
//                        adapter.groupHolders.get(position).checkBox.setChecked(false);
//                    } else {
//                        adapter.groupHolders.get(position).checkBox.setChecked(true);
//                    }
//                }

            }

//            public void checked (int position){
//                if (adapter.groupHolders.get(position).checkBox.isChecked()) {
//                    adapter.groupHolders.get(position).checkBox.setChecked(false);
//                } else {
//                    adapter.groupHolders.get(position).checkBox.setChecked(true);
//                }
//            }


            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    public void redrawAdapter(String groupID, Bitmap bitmap){//presenter отправляет bitmap/картинку в этот метод, он отправляет их на отображение в адаптере
        if (bitmap != null) {
            adapter.changeItem(groupID, bitmap);
        }
    }


    public void initAdapter(ArrayList<Group> groups){//инициализация адаптера
//        adapter  = new GroupAdapter(groups);
//        groupsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        Log.i("LISTLISTLIST", "DESTROOOOOOOOOOY");
        groupsPresenter = null;
        groups = null;
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
//        deleteOn = false;
        adapter.animationBack();
        menu.clear();
        inflater.inflate(R.menu.group_list_controller, menu);
    }
}