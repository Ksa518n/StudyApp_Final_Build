package com.studyapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.studyapp.fragments.FilesFragment;
import com.studyapp.fragments.NotesFragment;

public class SubjectDetailPagerAdapter extends FragmentPagerAdapter {

    private final long subjectId;
    private final String[] tabTitles = new String[]{"الملاحظات", "الملفات", "الروابط", "الاختبارات"};

    public SubjectDetailPagerAdapter(@NonNull FragmentManager fm, long subjectId) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.subjectId = subjectId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NotesFragment.newInstance(subjectId);
            case 1:
                return FilesFragment.newInstance(subjectId);
            case 2:
                // TODO: Implement LinksFragment
                return new Fragment();
            case 3:
                // TODO: Implement ExamsFragment
                return new Fragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
