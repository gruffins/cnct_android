package com.ryanfung.cnct.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewPagerAdapterTests extends AdapterTests {

    @Mock
    private Fragment fragment;

    @Mock
    private FragmentManager manager;

    private ViewPagerAdapter adapter;

    @Before
    public void setup() {
        super.setup();

        ViewPagerAdapter.ViewPagerItem item = new ViewPagerAdapter.ViewPagerItem(fragment, "title");

        adapter = new ViewPagerAdapter(manager, Collections.singletonList(item));
    }

    @Test
    public void getItem() {
        assertThat(adapter.getItem(0)).isEqualTo(fragment);
    }

    @Test
    public void getCount() {
        assertThat(adapter.getCount()).isEqualTo(1);
    }

    @Test
    public void getPageTitle() {
        assertThat(adapter.getPageTitle(0)).isEqualTo("title");
    }

}
