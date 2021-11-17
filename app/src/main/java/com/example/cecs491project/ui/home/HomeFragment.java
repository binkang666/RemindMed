package com.example.cecs491project.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.AddMedication;
import com.example.cecs491project.ui.medication.Medications;
import com.example.cecs491project.ui.reminder.Reminder;
import com.example.cecs491project.ui.reminder.ReminderAdapter;
import com.example.cecs491project.ui.reminder.ReminderDetails;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import android.os.Handler;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class HomeFragment extends Fragment {

    RecyclerView overview, scheduleView;
    overviewAdapter overviewAdapter;
    schedulesAdapter schedulesAdapter;
    FirebaseFirestore db;
    View view;
    TextSwitcher tip;
    private TextView textView, desc;
    int i = 0;
    String[] tipsList;

    private ArrayList<Integer> itemImages = new ArrayList<>();
    private ArrayList<String> itemName = new ArrayList<>();
    private ArrayList<String> itemDesc = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getImages();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        desc = view.findViewById(R.id.desc_med);
        tip = view.findViewById(R.id.tips);
        tipsList = new String[]{
                "Laughing is good for the heart and can increase blood flow by 20 percent.",
                "Writing in a journal can make you a happier person!",
                "Always look on the bright side: being an optimist can help you live longer.",
                "Exercise will give you more energy, even when you’re tired.",
                "Sitting and sleeping are great in moderation, but too much can increase your chances of an early death.",
                "A lack of exercise now causes as many deaths as smoking.",
                "39% of adults in the world are overweight.",
                "Between 2000 and 2015, the average global life expectancy increased by five years.",
                "Less than 1% of Americans ride their bike to work, while 50% of Copenhagen residents bike to work or school.",
                "Learning a new language or playing a musical instrument gives your brain a boost.",
                "Yoga can boost your cognitive function and lower stress.",
                "Chewing gum makes you more alert, relieves stress and reduces anxiety levels.",
                "Drinking coffee can reduce the risk of depression, especially in women.",
                "Swearing can make you feel better when you’re in pain.",
                "Chocolate is good for your skin; its antioxidants improve blood flow and protect against UV damage.",
                "Almonds, avocados and arugula (the three ‘A’s) can boost your sex drive and improve fertility.",
                "Eating oatmeal provides a serotonin boost to calm the brain and improve your mood.",
                "Women below the age of 50 need twice the amount of iron per day as men of the same age.",
                "The amino acid found in eggs can help improve your reflexes.",
                "Extra virgin olive oil is the healthiest fat on the planet.",
                "Vitamin D is as important as calcium in determining bone health, and most people don’t get enough of it.",
                "The body has more than 650 muscles.",
                "Sleeping naked can help you burn more calories.",
                "Even at rest, muscle is three times more efficient at burning calories than fat.",
                "Regular activity can ease the severity and reduce the frequency of lower back pain.",
                "Exercising regularly can increase your lifespan by keeping your DNA healthy and young.",
                "Stretching increases the blood flow to your muscles and helps avoid injuries.",
                "Drinking at least five glasses of water a day can reduce your chances of suffering from a heart attack by 40%.",
                "Consuming water helps the body maintain its natural pH balance.",
                "The spinal disc core is comprised of a large volume of water therefore dehydration could lead to back pain.",
                "Kidneys filter your blood up to 300 times a day and need water to function optimally.",
                "Hydration is key for a good complexion. Drinking enough water also makes you less prone to wrinkles.",
                "A lack of water can cause a range of problems, such as constipation, asthma, allergy and migraines.",
                "Your muscles and joints require water in order to stay energized, lubricated and healthy.",
                "Massage isn’t just for the muscles. It can help scars fade, and can be more beneficial than lotion or oil.",
                "Want to slow the aging process? Meditation is proven to help!",
                "Indoor air pollution can be even worse than outside.",
                "The blue light from your phone can mess with your circadian rhythm.",
                "Cardio exercise before breakfast can burn more fat.",
                "‘Gymnasium’ comes from the Greek word ‘gymnazein’, meaning ‘to exercise naked’.",
                "On average, there are more bacteria per square inch in a kitchen sink than the bathroom.",
                "The nose knows: it can remember 50,000 different scents.",
                "Humans have 46 chromosomes, while peas have 14 and crayfish have 200.",
                "During an allergic reaction your immune system is responding to a false alarm that it perceives as a threat.",
                "Left-handed people are more likely to suffer from ADHD.",
                "The eye muscles are the most active in the body, moving more than 100,000 times a day!",
                "Although bodies stop growing, noses and ears will not."
        };


        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                next(null);
            }
        };

        tip.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(getContext());
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });
        new Thread(){
            @Override
            public void run() {
                while (true){
                    Message message = handler.obtainMessage();
                    message.obj = 0;
                    handler.sendMessage(message);
                    try {
                        sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        EventChangeListener();
        return view;

    }
    private void next(View scource){
        Random rand = new Random();
        int rand_int1 = rand.nextInt(tipsList.length-1);
        if(rand_int1 == tipsList.length-1){
            rand_int1 = 0;
            tip.setText(tipsList[rand_int1]);
        }else{
            rand_int1+=1;
            tip.setText(tipsList[rand_int1]);
        }


    }

    private void getImages() {

        itemImages.add(R.drawable.tablet_overview);
        itemImages.add(R.drawable.capsule_overview);
        itemImages.add(R.drawable.drop_overview);
        itemImages.add(R.drawable.injection_overview);

        itemName.add("Tablet");
        itemName.add("Capsule");
        itemName.add("Drops");
        itemName.add("Injection");

        itemDesc.add("A tablet is a pharmaceutical oral dosage form or solid unit dosage form.");
        itemDesc.add("Drug-embedded empty capsule shells — A new class of capsule has been developed whereby drugs can be embedded in the capsule shell matrix.");
        itemDesc.add("Eyes drops sometimes do not have medications in them and are only lubricating and tear-replacing solutions.");
        itemDesc.add("An injection is the act of administering a liquid, especially a drug, into a person\\'s body using a needle (usually a hypodermic needle) and a syringe.");

    }

    private void EventChangeListener(){
        db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        Query query2;
        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){

            query2 = db.collection("Anonymous User Database")
                    .document(uid).collection("Reminder").whereArrayContains("days", today);
        }else{
            query2 = db.collection("User Database")
                    .document(uid).collection("Reminder").whereArrayContains("days", today);
        }

        overviewAdapter = new overviewAdapter(getContext(), itemName, itemImages, itemDesc);
        overview = view.findViewById(R.id.UpcomingView);

        overview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        overview.setAdapter(overviewAdapter);

        overview.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                overview, new ClickListener() {

            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                intent.putExtra("Title", itemName.get(position));
                intent.putExtra("Desc",itemDesc.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(), "Showing Position  (Long Press) : " + position,
                        Toast.LENGTH_SHORT).show();
            }
        }));


        FirestoreRecyclerOptions<Reminder> options2 = new FirestoreRecyclerOptions.Builder<Reminder>()
                .setQuery(query2,Reminder.class)
                .build();
        schedulesAdapter = new schedulesAdapter(getActivity(),options2);
        scheduleView = view.findViewById(R.id.recyclerView);

        scheduleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        scheduleView.setAdapter(schedulesAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Are you sure you want to delete this reminder?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        schedulesAdapter.deleteItem(viewHolder.getAdapterPosition());
                        Toast.makeText(getContext(), "Reminder Deleted", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        schedulesAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
                alertDialog.create().show();
            }
        }).attachToRecyclerView(scheduleView);
        schedulesAdapter.setOnItemClickListener(new schedulesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                String docID = documentSnapshot.getId();
                Map<String, Object> s = documentSnapshot.getData();

                String reminderName = (String) s.get("reminderName");
                String name = (String) s.get("medicationName");
                String start = (String) s.get("startDate");
                String end = (String) s.get("endDate");
                String time = (String) s.get("time");
                String note = (String) s.get("note");
                ArrayList<String> day = (ArrayList<String>) s.get("days");

                Bundle bundle = new Bundle();
                bundle.putString("remName", reminderName);
                bundle.putString("medName", name);
                bundle.putString("startD", start);
                bundle.putString("endD", end);
                bundle.putString("docID", docID);
                bundle.putString("note", note);
                bundle.putString("medTime",time);
                bundle.putStringArrayList("days", day);

                Intent i = new Intent(getContext(), ReminderDetails.class);
                i.putExtras(bundle);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private HomeFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView,
                                     final HomeFragment.ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    @Override
    public void onStart() {
        super.onStart();
//        overviewAdapter.startListening();
        schedulesAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
//        overviewAdapter.stopListening();
        schedulesAdapter.stopListening();
    }

}