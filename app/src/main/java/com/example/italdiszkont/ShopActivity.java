package com.example.italdiszkont;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {


    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private ArrayList<ShoppingItem> itemList;
    private ShoppingItemAdapter adapter;
    private int gridNumber =1;
    private boolean viewRow = true;
    private boolean orderRow=true;
    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;
    private SharedPreferences preferences;
    private FirebaseFirestore firestore;
    private CollectionReference fireItems;
    private NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){

        }
        else{
            finish();
        }


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        itemList = new ArrayList<>();
        adapter = new ShoppingItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firestore = FirebaseFirestore.getInstance();
        fireItems = firestore.collection("Items");

        queryData(0);

        notificationHandler = new NotificationHandler(this);
    }

    private void queryData(int number){
        itemList.clear();
        if(number ==1){
            fireItems.orderBy("price", Query.Direction.DESCENDING).limit(12).get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                    ShoppingItem item = document.toObject(ShoppingItem.class);
                    item.setId(document.getId());
                    item.setCartedCount();
                    itemList.add(item);
                }
                if(itemList.size() ==0){

                    initializeData();
                    queryData(0);
                }
                adapter.notifyDataSetChanged();
            });
        }
        else if(number == 2){
            fireItems.orderBy("price", Query.Direction.ASCENDING).limit(12).get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                    ShoppingItem item = document.toObject(ShoppingItem.class);
                    item.setId(document.getId());
                    item.setCartedCount();
                    itemList.add(item);
                }
                if(itemList.size() ==0){

                    initializeData();
                    queryData(0);
                }
                adapter.notifyDataSetChanged();
            });
        }
        else{
            fireItems.orderBy("rated", Query.Direction.ASCENDING).limit(12).get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                    ShoppingItem item = document.toObject(ShoppingItem.class);
                    item.setId(document.getId());
                    item.setCartedCount();
                    itemList.add(item);
                }
                if(itemList.size() ==0){

                    initializeData();
                    queryData(0);
                }
                adapter.notifyDataSetChanged();
            });
        }

    }
    private void initializeData() {
        String[] itemsList = getResources().getStringArray(R.array.italdiszkont_item_names);
        String[] itemInfo = getResources().getStringArray(R.array.italdiszkont_item_desc);
        String[] itemPrice = getResources().getStringArray(R.array.italdiszkont_item_price);
        String[] itemAlcohol = getResources().getStringArray(R.array.italdiszkont_item_alcohol);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.italdiszkont_item_images);
        TypedArray itemsRate= getResources().obtainTypedArray(R.array.italdiszkont_item_rates);



        for (int i =0; i<itemsList.length; i++){
            fireItems.add(new ShoppingItem(itemsList[i],
                    itemInfo[i],
                    itemPrice[i],
                    itemsRate.getFloat(i,0),
                    itemAlcohol[i],
                    itemsImageResource.getResourceId(i,0)));
            //itemList.add();
        }

        itemsImageResource.recycle();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.log_out_button){
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        }
        else if(itemId == R.id.add_to_cart){
           //TODO
            notificationHandler.cancel();
            return true;
        }
        else if(itemId ==R.id.view_selector){
            if (viewRow) {
                changeSpanCount(item, R.drawable.view_2, 1);
            } else {
                changeSpanCount(item, R.drawable.view_stream, 2);
            }
            return true;
        }
        else if(itemId == R.id.order){
            if(orderRow){
                changeOrder(item,R.drawable.row_up);
                queryData(1);
            }
            else{
                changeOrder(item,R.drawable.row_down);
                queryData(2);
            }
        }
        else if(itemId == R.id.permission){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    private void changeOrder(MenuItem item, int drawableId) {
        orderRow = !orderRow;
        item.setIcon(drawableId);
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.add_to_cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(ShoppingItem item){
        cartItems = (cartItems+1);
        if(0==cartItems){
            countTextView.setText("");
            Log.d("ez a nullas ag", "Cart Items: " + cartItems);
        }
        else if(cartItems>0){
            countTextView.setText(String.valueOf(cartItems));
            Log.d("ez nem a nullas ag", "Cart Items: " + cartItems);
        }
        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);

        fireItems.document(item._getid()).update("cartedCount", item.getCartedCount()+1).addOnFailureListener(failure->{
            Toast.makeText(this, "Item "+item._getid() + " nem valtoztathato", Toast.LENGTH_LONG).show();
        });
        Log.d("itemnek a neve", "Nev" + item.getName());
        notificationHandler.send(item.getName());
        queryData(0);
    }
}