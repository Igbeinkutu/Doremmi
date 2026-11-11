package com.ebisoft.doremmi.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

/** Stores game scores and lesson progress in a single SharedPreferences file. */
public final class ProgressRepository {
    private static final String PREFS = "doremmi_progress";
    private static final String KEY_MIGRATED = "migrated_v1";
    private static final String KEY_LAST_LESSON = "last_lesson";

    public static final class ScoreRecord {
        public final int best;
        public final int last;
        public final int max;
        public final long lastPlayedAt;

        ScoreRecord(int best, int last, int max, long lastPlayedAt) {
            this.best = best;
            this.last = last;
            this.max = max;
            this.lastPlayedAt = lastPlayedAt;
        }
    }

    private final SharedPreferences prefs;
    private final Context context;

    public ProgressRepository(Context context) {
        this.context = context.getApplicationContext();
        prefs = this.context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        migrateLegacyScores();
    }

    // region Quiz scores

    /** Records a finished round. Returns true if it is a new personal best. */
    public boolean recordScore(String quizId, int score, int max) {
        ScoreRecord previous = getScore(quizId);
        boolean newBest = previous == null || score > previous.best;
        prefs.edit()
                .putInt(quizId + "_last", score)
                .putInt(quizId + "_best", newBest ? score : previous.best)
                .putInt(quizId + "_max", max)
                .putLong(quizId + "_time", System.currentTimeMillis())
                .apply();
        return newBest && previous != null;
    }

    @Nullable
    public ScoreRecord getScore(String quizId) {
        if (!prefs.contains(quizId + "_last")) {
            return null;
        }
        return new ScoreRecord(
                prefs.getInt(quizId + "_best", 0),
                prefs.getInt(quizId + "_last", 0),
                prefs.getInt(quizId + "_max", 0),
                prefs.getLong(quizId + "_time", 0));
    }

    // endregion

    // region Lessons

    /** Saves the zero-based step the learner is on. */
    public void saveLessonStep(int lesson, int step) {
        prefs.edit()
                .putInt("lesson_" + lesson + "_step", step)
                .putInt(KEY_LAST_LESSON, lesson)
                .apply();
    }

    public int getLessonStep(int lesson) {
        return prefs.getInt("lesson_" + lesson + "_step", 0);
    }

    public boolean hasStartedLesson(int lesson) {
        return prefs.contains("lesson_" + lesson + "_step");
    }

    public void markLessonComplete(int lesson) {
        prefs.edit()
                .putBoolean("lesson_" + lesson + "_done", true)
                .remove("lesson_" + lesson + "_step")
                .apply();
    }

    public boolean isLessonComplete(int lesson) {
        return prefs.getBoolean("lesson_" + lesson + "_done", false);
    }

    /** The most recently opened lesson, or 0 if none. */
    public int getLastLesson() {
        return prefs.getInt(KEY_LAST_LESSON, 0);
    }

    public int completedLessonCount(int totalLessons) {
        int count = 0;
        for (int i = 1; i <= totalLessons; i++) {
            if (isLessonComplete(i)) {
                count++;
            }
        }
        return count;
    }

    // endregion

    public void reset() {
        prefs.edit().clear().putBoolean(KEY_MIGRATED, true).apply();
    }

    /** Imports "x/y" scores saved by the previous version of the games, once. */
    private void migrateLegacyScores() {
        if (prefs.getBoolean(KEY_MIGRATED, false)) {
            return;
        }
        importLegacy("nameNotePreference", "namescore", Quizzes.NAME_THAT_NOTE);
        importLegacy("identifyNotePreference_score", "identifyscore", Quizzes.IDENTIFY_NOTE);
        importLegacy("noteMathsPreference_score", "noteMathsScore", Quizzes.NOTE_MATHS);
        importLegacy("wordMatchPreference_score", "wordMatchScore", Quizzes.WORD_MATCH);
        prefs.edit().putBoolean(KEY_MIGRATED, true).apply();
    }

    private void importLegacy(String file, String key, String quizId) {
        String value = context.getSharedPreferences(file, Context.MODE_PRIVATE).getString(key, null);
        if (value == null) {
            return;
        }
        String[] parts = value.trim().split("/");
        if (parts.length != 2) {
            return;
        }
        try {
            int score = Integer.parseInt(parts[0].trim());
            int max = Integer.parseInt(parts[1].trim());
            prefs.edit()
                    .putInt(quizId + "_last", score)
                    .putInt(quizId + "_best", score)
                    .putInt(quizId + "_max", max)
                    .putLong(quizId + "_time", 0)
                    .apply();
        } catch (NumberFormatException ignored) {
            // Legacy value was not a score; skip it.
        }
    }
}
