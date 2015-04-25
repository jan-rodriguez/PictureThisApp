package com.example.janrodriguez.picturethis.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.janrodriguez.picturethis.Helpers.Challenge;
import com.example.janrodriguez.picturethis.Helpers.ParseHelper;
import com.example.janrodriguez.picturethis.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChallengeFeedActivity extends AppCompatActivity implements ActionBar.TabListener {

    static private final String TAG = "ChallengeFeedActivity";


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    static ArrayList<Challenge> listOfReceivedChallenges = new ArrayList<>();
    static ArrayList<Challenge> listOfSentChallenges = new ArrayList<>();
    static CustomListAdapter adapter1;
    static CustomListAdapter adapter2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_feed);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        fetchData();
    }

    private void fetchData(){
        // TODO: change this function
        ParseHelper.GetAllChallengesTest(BaseGameActivity.currentUser, getFindCallback1());

        // TODO: change this function
        ParseHelper.GetAllChallengesTest(BaseGameActivity.currentUser, getFindCallback2());
    }

    private FindCallback<ParseObject> getFindCallback1() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    listOfReceivedChallenges.clear();

                    for (ParseObject parseObject : parseObjects) {

                        Challenge challenge = new Challenge(parseObject);
                        listOfReceivedChallenges.add(challenge);
                    }

                    adapter1.notifyDataSetChanged();
                    Log.e(TAG, listOfReceivedChallenges.size()+"");


                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        };
    }

    private FindCallback<ParseObject> getFindCallback2() {
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    listOfSentChallenges.clear();

                    for (ParseObject parseObject : parseObjects) {

                        Challenge challenge = new Challenge(parseObject);
                        listOfSentChallenges.add(challenge);
                    }

                    adapter2.notifyDataSetChanged();
                    Log.e(TAG, listOfSentChallenges.size()+"");


                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        };
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return ReceivedChallengeFeedFragment.newInstance();
                case 1:
                    return SentChallengeFeedFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    public static class ReceivedChallengeFeedFragment extends Fragment {


        static ListView listView;



        public static ReceivedChallengeFeedFragment newInstance() {
            ReceivedChallengeFeedFragment fragment = new ReceivedChallengeFeedFragment();
            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        public ReceivedChallengeFeedFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_received_challenge_feed, container, false);

            listView = (ListView)rootView.findViewById(R.id.listView2);
            adapter1 = new CustomListAdapter(getActivity(), listOfReceivedChallenges);
            listView.setAdapter(adapter1);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem= listOfReceivedChallenges.get(position).getTitle();
                    Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                }
            });

            return rootView;
        }
    }

    public static class SentChallengeFeedFragment extends Fragment {

        static ListView listView;


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SentChallengeFeedFragment newInstance() {
            SentChallengeFeedFragment fragment = new SentChallengeFeedFragment();
            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SentChallengeFeedFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sent_challenge_feed, container, false);

            listView = (ListView)rootView.findViewById(R.id.listView3);
            adapter2 = new CustomListAdapter(getActivity(), listOfSentChallenges);
            listView.setAdapter(adapter2);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem= listOfSentChallenges.get(position).getTitle();
                    Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                }
            });

            return rootView;
        }
    }

}