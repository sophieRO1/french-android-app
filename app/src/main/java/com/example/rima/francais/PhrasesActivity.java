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

public class PhrasesActivity extends AppCompatActivity {

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

        words.add(new Word("Hello. (literally: good day)", "Bonjour", R.raw.bonjour));
        words.add(new Word("Where are you going?", "Où allez vous?", R.raw.ouallezvous));
        words.add(new Word("What is your name?", "Quel est votre nom?", R.raw.quelestvotrenom));
        words.add(new Word("My name is...", "Mon nom est...", R.raw.monnomest));
        words.add(new Word("How are you feeling?", "Comment allez-vous?", R.raw.commentallezvous));
        words.add(new Word("I’m feeling good", "Je me sens bien", R.raw.jemesensbien));
        words.add(new Word("Are you coming?", "Viens-tu?", R.raw.vienstu));
        words.add(new Word("Yes, I’m coming.", "Oui, je viens", R.raw.ouieviens));
        words.add(new Word("I’m coming.", "J'arrive.", R.raw.jarrive));
        words.add(new Word("Let’s go.", "Allons-y.", R.raw.allonsy));
        words.add(new Word("Come here.", "Venez ici.", R.raw.venezici));
        words.add(new Word("Excuse me", "Excusez-moi.", R.raw.excusezmoi));
        words.add(new Word("I would like...", "Je voudrais...", R.raw.jevoudrais));
        words.add(new Word("What did you say?", "Qu'est-ce que vous avez dit?", R.raw.questcequevousavezdit));
        words.add(new Word("I don't understand.", "Je ne comprends pas", R.raw.jenecomprendspas));
        words.add(new Word("Please speak slowly.", "Parlez lentement", R.raw.parlezlentement));
        words.add(new Word("Thank you.", "Merci", R.raw.merci));
        words.add(new Word("You' re welcome", "De rien.", R.raw.derien));
        words.add(new Word("Hi! / Bye! (informal)", "Salut!", R.raw.salut));
        words.add(new Word("How are you? (informal)", "Ça va?", R.raw.cava));
        words.add(new Word("Where is...?", "Où est-ce que se trouve...?", R.raw.ouestcequesetrouve));
        words.add(new Word("How much does it cost?", "Combien coûte-t-il?", R.raw.combiencoutetil));
        words.add(new Word("Please.", "S'il vous plaît.", R.raw.silvousplait));
        words.add(new Word("Don't worry. (informal)", "Ne t'en fais pas.", R.raw.netenfaispas));
        words.add(new Word("Don’t worry. (formal)", "Ne vous en faites pas.", R.raw.nevousenfaitespas));
        words.add(new Word("No.", "Non.", R.raw.non));
        words.add(new Word("OK", "D'accord", R.raw.daccord));
        words.add(new Word("Do you have..", "Avez-vous...", R.raw.avezvous));
        words.add(new Word("Do you speak English?", "Parlez-vous anglais?", R.raw.parlezvousanglais));
        words.add(new Word("Where are the bathrooms?", "Qu sont les toilettes?", R.raw.ousontlestoilettes));
        words.add(new Word("No problem.", "Ce n'est pas grave.", R.raw.cenestpasgrave));
        words.add(new Word("I’m fine.", "Je vais bien.", R.raw.jevaisbien));
        words.add(new Word("Do you speak English? (informal)", "Tu parles anglais?", R.raw.tuparlesanglais));
        words.add(new Word("Goodbye.", "Au revoir.", R.raw.aurevoir));





        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);

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
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudio());

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

//