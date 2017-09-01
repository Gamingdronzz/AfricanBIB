package biz.africanbib.Tabs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    Tab1 tab1;
    Tab2 tab2;
    Tab3 tab3;
    Tab4 tab4;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        tab1 = new Tab1();
        tab2 = new Tab2();
        tab3 = new Tab3();
        tab4 = new Tab4();
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                //Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                //Tab2 tab2 = new Tab2();
                return tab2;
            case 2:
                //Tab3 tab3 = new Tab3();
                return tab3;
            case 3:
                //Tab4 tab4 = new Tab4();
                return tab4;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}