package com.example.rima.francais;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };


    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();


        words.add(new Word("one", "un", R.raw.un));
        words.add(new Word("two", "deux", R.raw.deux));
        words.add(new Word("three", "trois", R.raw.trois));
        words.add(new Word("four", "quatre", R.raw.quatre));
        words.add(new Word("five", "cinq", R.raw.cinq));
        words.add(new Word("six", "six", R.raw.six));
        words.add(new Word("seven", "sept", R.raw.sept));
        words.add(new Word("eight", "huit", R.raw.huit));
        words.add(new Word("nine", "neuf", R.raw.neuf));
        words.add(new Word("ten", "dix", R.raw.dix));
        words.add(new Word("eleven", "onze", R.raw.onze));
        words.add(new Word("twelve", "douze", R.raw.douze));
        words.add(new Word("thirteen", "treize", R.raw.treize));

        words.add(new Word("fourteen", "quatorze", R.raw.quatorze));
        words.add(new Word("fifteen", "quinze", R.raw.quinze));
        words.add(new Word("sixteen", "seize", R.raw.seize));
        words.add(new Word("seventeen", "dix sept", R.raw.dixsept));
        words.add(new Word("eighteen", "dix huit", R.raw.dixhuit));
        words.add(new Word("nineteen", "dix neuf", R.raw.dixneuf));
        words.add(new Word("twenty", "vingt", R.raw. vingt));
        words.add(new Word("twenty one", "vingt et un", R.raw.vingtetun));
        words.add(new Word("twenty two", "vingt deux", R.raw.vingtdeux));
        words.add(new Word("twenty three", "vingt trois", R.raw.vingttrois));

        words.add(new Word("twenty four", "vingt quatre", R.raw.vingtquatre));
        words.add(new Word("twenty five", "vingt cinq", R.raw.vingtcinq));
        words.add(new Word("twenty six", "vingt six", R.raw.vingtsix));
        words.add(new Word("twenty seven", "vingt sept", R.raw.vingtsept));
        words.add(new Word("twenty eight", "vingt huit", R.raw.vingthuit));
        words.add(new Word("twenty nine", "vingt neuf", R.raw.vingtneuf));
        words.add(new Word("thirty", "trente", R.raw.trente));










        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Word word = words.get(position);

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getAudio());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
