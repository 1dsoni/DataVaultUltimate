package com.projects.doctor.datavaultultimate.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.projects.doctor.datavaultultimate.fragments.Frag_Notes_subject;
import com.projects.doctor.datavaultultimate.fragments.Frag_Subject_main;

/**
 * Created by doctor on 06-07-2017.
 */

public class C_Adapter_TabPager extends FragmentStatePagerAdapter {
    public C_Adapter_TabPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Frag_Subject_main();
            case 1:
                return new Frag_Notes_subject();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
