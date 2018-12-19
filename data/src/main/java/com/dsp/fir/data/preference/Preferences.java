package com.dsp.fir.data.preference;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import javax.inject.Inject;

public class Preferences {
    private static final String FILTER_BY_DEBIT_ID = "FILTER_BY_DEBIT_ID";
    private static final String JOURNAL_OUTDATED = "JOURNAL_OUTDATED";


    private final RxSharedPreferences preferences;

    @Inject
    public Preferences(RxSharedPreferences rxSharedPreferences) {
        preferences = rxSharedPreferences;
    }

    public Preference<Long> getFilterByDebitIDPreference() {
        return preferences.getLong(FILTER_BY_DEBIT_ID);
    }

    public Preference<Boolean> getJournalOutdatedPreference() {
        return preferences.getBoolean(JOURNAL_OUTDATED);
    }
}
